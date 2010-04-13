package ar.edu.itba.cg.tpe1.rayCaster;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe1.geometry.Primitive;
import ar.edu.itba.cg.tpe1.geometry.Ray;

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
	static private double farthestDistance=20.0;
	static private List<Primitive> viewedObjects = new ArrayList<Primitive>();

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

			Primitive primitive = null;
			Point3d origin = camera.getOrigin();
			Point3d intersection = null, aux = null;
			Color color;
			Ray ray;
			for (int i = fromX; i < toX; i++) {
				for (int j = fromY; j < toY; j++) {
					// Set infinite color
					color = Color.BLACK;
					
					Point3d po = camera.getPointFromXY(width, height, i, j);
					
					// Create a new Ray from camera, i, j
					ray = new Ray(origin, po);
					
					// There is no intersection yet
					intersection = null;
					
					// There is no primitive intersection
					primitive = null;
					
					// Find intersection in scene with ray
					for (Primitive p : scene.getList()) {
						aux = p.intersect(ray);
						if (aux != null && (intersection == null || (intersection != null &&
							aux.distance(origin) < intersection.distance(origin)))) {
							intersection = aux;
							primitive = p;
						}
					}

					if (primitive != null) {
						synchronized (viewedObjects) {
							// Check if this is the first time we see this object
							if ( ! viewedObjects.contains(primitive) ) {
								// Add it to the list and set the color
								viewedObjects.add(primitive);
								primitive.setColor(colorMode.getNextColor());
							}
						}
						// Get the color to be painted
						color = adjustColor(primitive.getColor(intersection), intersection.distance(origin));
					}
					
					// Set color to image(i, j)
					synchronized (rayCaster.image) {
						rayCaster.getImage().setRGB(i, j, color.getRGB());
					}
				}
			}

			try { cb.await(); } catch (InterruptedException e) { e.printStackTrace(); } catch (BrokenBarrierException e) { e.printStackTrace(); }
		}
	}
	
    Color adjustColor(Color color, double distance) {
        //float[] hsb = new float[3];
    	Color ret=null;
        //TODO: ver como queda con los 3 canales
		
/*    	distance /= 10;
		
		if (distance < 1)
			distance = 1;
*/
		if (distance >= farthestDistance) {
    		ret = new Color(0,0,0);    		
    	} else if (colorVariation==RayCaster.COLOR_VARIATION_LINEAR) {
			ret= new Color((int)(-color.getRed()/farthestDistance + color.getRed()),
					(int)(-color.getGreen()/farthestDistance + color.getGreen()),
					(int)(-color.getBlue()/farthestDistance + color.getBlue()));    			
    	} else {
    		ret= new Color((color.getRed()/255) * (float)(1f / distance),
                    (color.getGreen()/255) * (float)(1f / distance),
                    (color.getBlue()/255) * (float)(1f / distance));    		
    	}
    	
/*    	return new Color((color.getRed()/255) * (float)(1f / distance),
                        (color.getGreen()/255) * (float)(1f / distance),
                        (color.getBlue()/255) * (float)(1f / distance));*/
/*        return new Color((color.getRed()/255) * (1 / (float)Math.log(Math.E + distance)),
        		(color.getGreen()/255) * (1 / (float)Math.log(Math.E + distance)),
        		(color.getBlue()/255) * (1 / (float)Math.log(Math.E + distance)));*/
        
        /*hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb); 
        
        //hsb[2] /= (float)Math.log(Math.E + distance);
        hsb[0] *= 1 / (float)Math.log(Math.E + distance);
        hsb[1] *= 1 / (float)Math.log(Math.E + distance);
        hsb[2] *= 1 / (float)Math.log(Math.E + distance);
        //hsb[2] *= 5/distance;
        
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));*/
    	return color;
    }
	
}
