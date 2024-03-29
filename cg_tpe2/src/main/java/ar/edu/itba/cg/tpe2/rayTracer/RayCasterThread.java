package ar.edu.itba.cg.tpe2.rayTracer;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import ar.edu.itba.cg.tpe2.core.camera.Camera;
import ar.edu.itba.cg.tpe2.core.geometry.Primitive;
import ar.edu.itba.cg.tpe2.core.geometry.Ray;
import ar.edu.itba.cg.tpe2.core.geometry.Vector3;
import ar.edu.itba.cg.tpe2.core.light.Light;
import ar.edu.itba.cg.tpe2.core.light.PointLight;
import ar.edu.itba.cg.tpe2.core.scene.Scene;

/**
 * RayCasterThread is an optimization using threads 
 */
class RayCasterThread extends Thread {

	private Scene scene;
	private List<Light> lights;
	private Camera camera;
	private int fromX;
	private int toX;
	private int fromY;
	private int toY;
	private static int width;
	private static int height;
	private RayCaster rayCaster;
	private IColorProvider colorMode;
	private int colorVariation = RayCaster.COLOR_VARIATION_LINEAR;
	private static final int MAX_REBOUNDS = 4;
	private static final Color INITIAL_COLOR = Color.BLACK;
	private static boolean LIGHTS_ON = true;

	private static final float AMBIENT_LIGHT = 0.1f;

	private static List<Task> tasks = null;
	private static Iterator<Task> taskIterator;
	private static int finishedTasks;
	
	private static int i=0;
	private static BufferedImage image;
	private int id;
	private float totalLight;
	
	private boolean progressBar;
	private int totalTasks;
	
	/**
	 * Constructor for RayCasterThread class
	 * 
	 * @param scene Scene representation to work with
	 * @param camera Actual camera where the viewer is 
	 * @param rayCaster RayCaster class parent
	 * @param cb CyclicBarrier for the threads
	 */
	public RayCasterThread(Scene scene, Camera camera, RayCaster rayCaster, boolean progressBar) {
		this.scene = scene;
		this.lights = scene.getLights();
		this.camera = camera;
		this.rayCaster = rayCaster;
		this.totalLight = calculateTotalLight();
		this.progressBar = progressBar;
		id = i;
		i++;
	}

	private float calculateTotalLight() {
		float t = 0;
		for(Light l:lights)
			t += l.getPower();
		return t;
	}

	/**
	 * Get the color mode for the ray caster
	 * 
	 * @return Color mode
	 */
	public IColorProvider getColorMode() {
		return colorMode;
	}
	
	/**
	 * Set the color mode for the ray caster
	 * 
	 * @param colorProvider Color mode to be set to the ray caster
	 */
	public void setColorMode(IColorProvider colorProvider) {
		this.colorMode = colorProvider;
	}
	
	/**
	 * Get color variation set to the ray caster
	 * 
	 * @return Color variation type
	 */
	public int getColorVariation() {
		return colorVariation;
	}
	
	/**
	 * Set the color variation used by the ray caster
	 * 
	 * @param colorVariation Color variation type
	 */
	public void setColorVariation(int colorVariation) {
		this.colorVariation = colorVariation;
	}
	
	/**
	 * Set image portion to work with
	 * 
	 * @param fromX First column
	 * @param toX Last column
	 * @param fromY First row
	 * @param toY Last row
	 * @param width Viewport width
	 * @param height Viewport height
	 */
	public void setPortion(Rectangle portion, int width, int height) {
		this.fromX = portion.x;
		this.toX = portion.x+portion.width;
		this.fromY = portion.y;
		this.toY = portion.y+portion.height;
	}

	/**
	 * Get last column of the image to work with
	 * 
	 * @return last column
	 */
	public int getToX() {
		return toX;
	}
	
	/**
	 * Get first column of the image to work with
	 * 
	 * @return first column
	 */
	public int getFromX() {
		return fromX;
	}

	/**
	 * Get first row of the image to work with
	 * 
	 * @return first row
	 */
	public int getFromY() {
		return fromY;
	}
	
	/**
	 * Get last row of the image to work with
	 * 
	 * @return last row
	 */
	public int getToY() {
		return toY;
	}
	
	synchronized public static void setTasks(List<Task> tasks) {
		RayCasterThread.tasks = tasks;
		RayCasterThread.taskIterator = tasks.iterator();
		finishedTasks = 0;
	}

	public boolean allTasksFinished(){
		return tasks.size() == finishedTasks;
	}
	
	private Task getTask() {
		Task r;
		synchronized (this.getClass()) {
			if ( tasks == null || !taskIterator.hasNext() )
				return null;
			r = taskIterator.next();
			totalTasks = tasks.size();
		}
		return r;
	}

	public static void setImage(BufferedImage img) {
		image = img;
		width = img.getWidth();
		height = img.getHeight();
	}
	
	public void setProgressBar(boolean progressBar) {
		
	}
	
	/**
	 * Start the rayCaster
	 * 
	 */
	public void run() {
		while ( true ){
			Task task = getTask();
			while ( task == null ){
				task = getTask();
			}

			setPortion(task.getRegion(), width, height);
			BufferedImage img = task.getImage();
			Point3f origin = camera.getEye();

			Color color;
			float[] tmp = new float[3];
			int side = (int)Math.pow(2,scene.getImage().getAa_max());
			int samples = scene.getImage().getSamples();

			Ray ray;
			for (int j = fromY; j < toY; j++) {
				for (int i = fromX; i < toX; i++) {
					// Set infinite color
					color = Color.BLACK;

					Point3f[] po = camera.getPointFromXY(width, height, i, j, side, samples);

					float[] colorAA = new float[]{0,0,0};
					for (int aa = 0; aa < po.length; aa++) {
						// Create a new Ray from camera, i, j
						ray = new Ray(origin, po[aa]);
					
						// Get pixel color
						tmp = getColor(ray, MAX_REBOUNDS).getRGBColorComponents(null);
						
						colorAA[0] += (tmp[0] / po.length);
						colorAA[1] += (tmp[1] / po.length);
						colorAA[2] += (tmp[2] / po.length);
					}

					color = clamp(colorAA);
					
					img.setRGB(i-fromX, j-fromY, color.getRGB());
				}
			}
			synchronized( this.getClass() ){
				finishedTasks++;
				if (this.progressBar)
					System.out.println("Progress done: " + (int)((finishedTasks* 100f) / totalTasks) + '%');
				if ( allTasksFinished() )
					synchronized (rayCaster) {
						rayCaster.notify();	
					}
			}
		}
	}

	private Color getColor(Ray ray, int maxRebounds){
		return getColor(ray, maxRebounds, null);
	}
	
    private Color getColor(Ray ray, int maxRebounds, Primitive primitiveToIgnore) {
    	Point3f intersectionPoint = new Point3f();
    	Primitive impactedFigure;
		Color refractColor, reflectColor, ilumColor;
    	
		impactedFigure = scene.getFirstIntersection(ray, intersectionPoint,primitiveToIgnore);
		
		if (impactedFigure == null ) {
			return INITIAL_COLOR;
		}

    	float reflectK = impactedFigure.getReflectionK();
    	float refractK = impactedFigure.getRefractionK();
		
    	/*
    	 * Calculate reflection & refraction rays
    	 */
		Ray refractRay = null;
		Ray reflectRay = null;
		if ( refractK != 0 ) 
			refractRay = getRefractRay(ray, impactedFigure, intersectionPoint);
		if ( reflectK != 0 )
			reflectRay = getReflectRay(ray, impactedFigure, intersectionPoint);    	
    	
		float [] refractRGBArray = {0.0f, 0.0f, 0.0f};
		float [] reflectRGBArray = {0.0f, 0.0f, 0.0f};
		
		// Stopping recursive calls and gets only ilumination color
		if (maxRebounds-- != 0) {
			if ( reflectRay != null ){
				reflectColor = getColor(reflectRay, maxRebounds, impactedFigure);
				reflectRGBArray = reflectColor.getRGBColorComponents(null);
			}
			if ( refractRay != null ){
				refractColor = getColor(refractRay, maxRebounds, impactedFigure);
				refractRGBArray = refractColor.getRGBColorComponents(null);
			}

		}
    	
		ilumColor = ilumination(ray, impactedFigure, intersectionPoint, INITIAL_COLOR);
    	float [] ilumRGBArray = ilumColor.getRGBColorComponents(null);
    	
    	float [] resultingRGBArray = new float [3];
    	
    	float sumKs = 1 + reflectK + refractK;

    	resultingRGBArray[0] = (0.01f + ilumRGBArray[0] + refractK*refractRGBArray[0] + reflectK*reflectRGBArray[0])/sumKs;
    	resultingRGBArray[1] = (0.01f + ilumRGBArray[1] + refractK*refractRGBArray[1] + reflectK*reflectRGBArray[1])/sumKs;
    	resultingRGBArray[2] = (0.01f + ilumRGBArray[2] + refractK*refractRGBArray[2] + reflectK*reflectRGBArray[2])/sumKs;

    	return clamp(resultingRGBArray);
	}

	private Color ilumination(Ray ray, Primitive impactedFigure, Point3f intersectionPoint, Color initialColor) {
		Vector3 figureNormal = impactedFigure.getNormalAt(intersectionPoint, ray.getOrigin());
		Color figureColor = impactedFigure.getColorAt(intersectionPoint,lights,ray, scene);
		float [] figureRGBComponents = figureColor.getRGBColorComponents(null);
		float [] rgbs = initialColor.getRGBColorComponents(null);

		if ( impactedFigure.getShader().hasIlumination() )
			return figureColor;

		if ( LIGHTS_ON && !lights.isEmpty()) {
			for(Light l:lights){
				if ( l instanceof PointLight ){
					PointLight pl = (PointLight) l;
					Primitive p = null;
					Point3f intersectionP = new Point3f(intersectionPoint);
					Point3f newIntersectionP = new Point3f();
					
					Vector3f vectorToLight = new Vector3f(pl.getP());
					vectorToLight.sub(intersectionP);
					
					Ray rayFromLight = new Ray(intersectionP,vectorToLight);
					float distanceToLight = intersectionP.distance(pl.getP());
					
					p = scene.getFirstIntersection(rayFromLight, newIntersectionP, impactedFigure);
					float distanceToNewPrimitive = intersectionP.distance(newIntersectionP);
					// No object between light and impactedFigure :D
					if ( p == null || ( p != null && distanceToLight < distanceToNewPrimitive ) ){
						Vector3 dirToLight = new Vector3(vectorToLight);
						float [] lightContribution = getLightContribution(dirToLight,figureNormal,pl,figureRGBComponents,distanceToLight);
						rgbs[0] += lightContribution[0];
						rgbs[1] += lightContribution[1];
						rgbs[2] += lightContribution[2];
					} else {
						// This light does not illuminate this object, let's put some ambient light
//						rgbs[0] += figureRGBComponents[0] * AMBIENT_LIGHT;
//						rgbs[1] += figureRGBComponents[1] * AMBIENT_LIGHT;
//						rgbs[2] += figureRGBComponents[2] * AMBIENT_LIGHT;
					}
				}
			}
		} else {
			// There is no light in the scene, put some ambient light
			rgbs = figureRGBComponents;
			rgbs[0] *= AMBIENT_LIGHT * 0.5f;
			rgbs[1] *= AMBIENT_LIGHT * 0.5f;
			rgbs[2] *= AMBIENT_LIGHT * 0.5f;
		}

		return clamp(rgbs);
		
	}

	private Color clamp(float [] rgbs){
		return new Color(clamp(rgbs[0]),clamp(rgbs[1]),clamp(rgbs[2]));
	}
	
	private float clamp(float channel){
		if ( channel > 1.0 )
			return 1;
		if ( channel < 0 )
			return 0;
		return channel;
	}
	
	private float[] getLightContribution(Vector3 dirToLight, Vector3 figureNormal, PointLight pl, float[] figureColor, float distanceToLight){
		float [] lightContribution = {0,0,0};
		float angleToLight = (float) figureNormal.dot(dirToLight);
		if ( angleToLight >= 0 ){
			float[] lightColor = pl.getASpec().getColor().getRGBColorComponents(null);
			float fallOff = pl.getFallOff(distanceToLight);
			
			lightContribution[0] = angleToLight * lightColor[0] * figureColor[0] * fallOff;
			lightContribution[1] = angleToLight * lightColor[1] * figureColor[1] * fallOff;
			lightContribution[2] = angleToLight * lightColor[2] * figureColor[2] * fallOff;
			
		}
		return lightContribution;
	}
	
	private Ray getReflectRay(Ray ray, Primitive p, Point3f intersectionPoint) {
		return ray.reflectFrom(p.getNormalAt(intersectionPoint, ray.getOrigin()), intersectionPoint);
	}

	private Ray getRefractRay(Ray ray, Primitive p, Point3f intersectionPoint) {
		return ray.refractFrom(p.getNormalAt(intersectionPoint, ray.getOrigin()), intersectionPoint, p.getShader().getEta());
	}

	

}
