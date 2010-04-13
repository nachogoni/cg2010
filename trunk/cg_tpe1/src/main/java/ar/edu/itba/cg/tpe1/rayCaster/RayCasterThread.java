package ar.edu.itba.cg.tpe1.rayCaster;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
	private int colorMode = RayCaster.COLOR_MODE_ORDERED;
	private int colorVariation = RayCaster.COLOR_VARIATION_LINEAR;

	//TODO: hacer el enum...
	static private Color nextColor = null;
	static private int nextOne = 0;
	
	static Random rnd = new Random();
	
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
	public int getColorMode() {
		return colorMode;
	}
	
	/**
	 * Set the color mode for the ray caster
	 * 
	 * @param colorMode Color mode to be set to the ray caster
	 */
	public void setColorMode(int colorMode) {
		this.colorMode = colorMode;
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

			List<Primitive> viewedObjects = new ArrayList<Primitive>();
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
						// Check if this is the first time we see this object
						if (viewedObjects.contains(primitive) == false) {
							// Add it to the list and set the color
							viewedObjects.add(primitive);
							// ColorMode?
							if (colorMode == RayCaster.COLOR_MODE_ORDERED) {
								// ColorMode -> ORDERED
								primitive.setColor(getNextColor());
							} else { 
								// ColorMode -> RANDOM
								primitive.setColor(getRandomColor());
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

        //TODO: ver como queda con los 3 canales

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
	
    private Color getNextColor() {
    	//TODO: crear un iterador o un enum para los colores thread safety ;)
    	switch (nextOne) {
    	case 0:
    		// Violet
    		nextColor = Color.CYAN;
    		break;
    	case 1:
    		// Blue
    		nextColor = Color.BLUE;
    		break;
    	case 2:
    		// Green
    		nextColor = Color.GREEN;
    		break;
    	case 3:
    		// Yellow
    		nextColor = Color.YELLOW;
    		break;
    	case 4:
    		// Orange
    		nextColor = Color.ORANGE;
    		break;
    	case 5:
    		// Red
    		nextColor = Color.RED;
    		break;
    	}
    	
    	nextOne++;
    	
    	if (nextOne == 6)
    		nextOne = 0;
    	
    	return nextColor;
    }
    
    private Color getRandomColor() {
    	return new Color(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat());
    }
    
}
