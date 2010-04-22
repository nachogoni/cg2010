package ar.edu.itba.cg.tpe2.rayCaster;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.geometry.Primitive;
import ar.edu.itba.cg.tpe2.geometry.Ray;

/**
 * RayCasterThread is an optimization using threads 
 */
class RayCasterThread extends Thread {

	private Scene scene;
	private Camera camera;
	private int fromX;
	private int toX;
	private int fromY;
	private int toY;
	private int width;
	private int height;
	private RayCaster rayCaster;
	private CyclicBarrier cb;
	private IColorProvider colorMode;
	private int colorVariation = RayCaster.COLOR_VARIATION_LINEAR;
	static private double farthestDistance = 20.0;
	private static final int MAX_REBOUNDS=3;

	/**
	 * Constructor for RayCasterThread class
	 * 
	 * @param scene Scene representation to work with
	 * @param camera Actual camera where the viewer is 
	 * @param rayCaster RayCaster class parent
	 * @param cb CyclicBarrier for the threads
	 */
	public RayCasterThread(Scene scene, Camera camera, RayCaster rayCaster, CyclicBarrier cb) {
		this.cb = cb;
		this.scene = scene;
		this.camera = camera;
		this.rayCaster = rayCaster;
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
		this.width = width;
		this.height = height;
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
	
	/**
	 * Start the rayCaster
	 * 
	 */
	public void run() {
		while ( true ){
			//System.out.println(cb.getNumberWaiting()+" threads are waiting");
			try { cb.await(); } catch (InterruptedException e) { 
				//System.out.println("Someone told me to die.");
				return;
			} catch (BrokenBarrierException e) { 
				//System.out.println("Someone broke the barrier.");
				return;
			}

			Point3d origin = camera.getOrigin();

			Color color;
			Ray ray;
			for (int i = fromX; i < toX; i++) {
				for (int j = fromY; j < toY; j++) {
					// Set infinite color
					color = Color.BLACK;
					
					Point3d po = camera.getPointFromXY(width, height, i, j);
					
					// Create a new Ray from camera, i, j
					ray = new Ray(origin, po);
					
					// Get pixel color
					color = getColor(ray, MAX_REBOUNDS);
					
					// Set color to image(i, j)
					synchronized (rayCaster.image) {
						rayCaster.getImage().setRGB(i, j, color.getRGB());
					}
				}
			}

			try { cb.await(); } catch (InterruptedException e) { e.printStackTrace(); } catch (BrokenBarrierException e) { e.printStackTrace(); }
		}
	}
		
    private Color getColor(Ray ray, int maxRebounds) {
    	
    	Point3d intersectionPoint=new Point3d();
    	Primitive impactedFigure;
		Color refractColor, reflectColor, ilumColor;
    	
		impactedFigure = scene.getFirstIntersection(ray, intersectionPoint);
		
		if (impactedFigure == null) {
			return new Color(0.0f, 0.0f, 0.0f);
		}
		
    	/*
    	 * Calcula los rayos de refleccion y refraccion (los que atraviesan y se reflejan)
    	 */
		Ray refractRay = getRefractRay(ray, impactedFigure);
		Ray reflectRay = getReflectRay(ray, impactedFigure);    	
		
		float [] reflactRGBArray = {0.0f, 0.0f, 0.0f};
		float [] reflectRGBArray = {0.0f, 0.0f, 0.0f};
		
		// Stopping recursive calls and gets only ilumination color
		if (maxRebounds-- != 0) {
			refractColor = getColor(refractRay, maxRebounds);
			reflectColor = getColor(reflectRay, maxRebounds);
			reflactRGBArray = refractColor.getRGBColorComponents(null);
			reflectRGBArray = reflectColor.getRGBColorComponents(null);
		}
    	
    	ilumColor = ilumination(ray, impactedFigure, intersectionPoint);
    	float [] ilumRGBArray = ilumColor.getRGBColorComponents(null);
    	
    	float [] resultingRGBArray = new float [3];
    	
    	//TODO cambiar esto por los valores posta
    	float refleccion=0.0f;
    	float refraccion=0.0f;
    	
    	resultingRGBArray[0] = ilumRGBArray[0] + refraccion*reflactRGBArray[0] + refleccion*reflectRGBArray[0];
    	resultingRGBArray[1] = ilumRGBArray[1] + refraccion*reflactRGBArray[1] + refleccion*reflectRGBArray[1];
    	resultingRGBArray[2] = ilumRGBArray[2] + refraccion*reflactRGBArray[2] + refleccion*reflectRGBArray[2];
    	
		
    	
    	return new Color(resultingRGBArray[0],resultingRGBArray[1], resultingRGBArray[2]);
	}

	private Color ilumination(Ray ray, Primitive impactedFigure, Point3d intersectionPoint) {
		// the color of the impacted figure
		//TODO hacer bien, deberia usar el rayo!!!
		return impactedFigure.getColor(intersectionPoint);
	}

	private Ray getReflectRay(Ray ray, Primitive p) {
		// TODO Auto-generated method stub
		return ray;
	}

	private Ray getRefractRay(Ray ray, Primitive p) {
		// TODO Auto-generated method stub
		return ray;
	}

}
