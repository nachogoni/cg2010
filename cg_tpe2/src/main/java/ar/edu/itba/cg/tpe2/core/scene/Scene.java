package ar.edu.itba.cg.tpe2.core.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.core.camera.Camera;
import ar.edu.itba.cg.tpe2.core.geometry.Primitive;
import ar.edu.itba.cg.tpe2.core.geometry.Ray;
import ar.edu.itba.cg.tpe2.core.light.Light;

/**
 * Create a scene representation
 */
public class Scene {

	private List<Primitive> primitives = Collections.synchronizedList(new ArrayList<Primitive>());
	
	private List<Light> lights = Collections.synchronizedList(new ArrayList<Light>());
	
	private PrimitiveOctree octree = null;
	
	final static boolean USE_NOT_WORKING_OCTREE=false;
	
	private Camera aCamera = null;
	private Image anImage = null;
	
	public Scene(List<Primitive> primitives, PrimitiveOctree octree, Camera aCamera, Image anImage) {
		super();
		this.primitives = primitives;
		this.octree = octree;
		this.aCamera = aCamera;
		this.anImage = anImage;
	}

	public Camera getCamera() {
		return aCamera;
	}

	public void setCamera(Camera aCamera) {
		this.aCamera = aCamera;
	}

	public Image getImage() {
		return anImage;
	}

	public void setImage(Image anImage) {
		this.anImage = anImage;
	}

	@Override
	public String toString() {
		return "Scene [aCamera=" + aCamera + ", anImage=" + anImage + ", list="
				+ primitives + ", octree=" + octree + "]";
	}


	/**
	 * Constructor for the scene from a file name
	 * 
	 * @param scene Scene file name
	 */
	/*
	public Scene(String scene) {
		this.list = Collections.synchronizedList(read(scene));
		this.optimize();
	}
*/
	/**
	 * Constructor for the scene from a primitives list
	 * 
	 * @param list List of primitives in the scene
	 */
	public Scene(List<Primitive> list) {
		this.primitives = list;
	}
	
	/**
	 * Constructor for the scene
	 * 
	 */
	public Scene() {
	}

	/**
	 * Get the list of primitives in the scene
	 * 
	 * @return List of primitives
	 */
	public List<Primitive> getPrimitives() {
		return primitives;
	}
	
	/**
	 * Add a list of primitives in the scene
	 * 
	 * @param primitives List of primitives to be added
	 */
	public void add(List<Primitive> primitives) {
		for (Primitive p : primitives) {
			this.primitives.add(p);
		}
	}
	
	public void optimize() {
		
		double xMin = Double.MAX_VALUE, xMax = Double.MIN_VALUE, 
			   yMin = Double.MAX_VALUE, yMax = Double.MIN_VALUE, 
			   zMin = Double.MAX_VALUE, zMax = Double.MIN_VALUE;
		
		// Search for the maximun an minimun points for the scene
		for (Primitive p : primitives) {
			double[] extremes= p.getBoundaryPoints();
			if (extremes[0] < xMin) {
				xMin = extremes[0];
			}
			if ( extremes[1]> xMax) {
				xMax = extremes[1];
			} 
			if (extremes[2] < yMin) {
				yMin = extremes[2];
			}
			if (extremes[3] > yMax) {
				yMax = extremes[3];
			} 
			if (extremes[4] < zMin) {
				zMin = extremes[4];
			}
			if (extremes[5] > zMax) {
				zMax = extremes[5];
			} 
		}
		// Create the octree
		octree = new PrimitiveOctree(xMin, xMax, yMin, xMax, zMin, zMax);

		// Add primitives to the octree
		for (Primitive p : primitives) {
			octree.add(p);
		}
		System.out.flush();
		octree.printOctree();
		
		return;
	}
	
	/**
	 * Return a list of primitives into a file
	 * 
	 * @param scene File name of the scene
	 * 
	 * @return List of primitives
	 */
	
	public Primitive getFirstIntersection(Ray ray, Point3d intersectionPoint) {
		Primitive ret=null;
		if (!USE_NOT_WORKING_OCTREE) {
			ret = getFirstIntersectionOld(ray, intersectionPoint);
		} else {
			ret = getFirstIntersectionNew(ray, intersectionPoint);
		}
		return ret;
	}
	
	
	public Primitive getFirstIntersectionOld(Ray ray, Point3d intersectionPoint) {
		Primitive nearestPrimitive = null;
		Point3d currIntersection = null, nearestIntersection=null;
		Point3d origin = ray.getOrigin();
		
		// Find intersection in scene with ray
		for (Primitive p : this.primitives) {
		
			currIntersection = p.intersect(ray);
			if (currIntersection != null && currIntersection.distance(origin) > 0.0000001 && (nearestIntersection == null || (nearestIntersection != null &&
				currIntersection.distance(origin) < nearestIntersection.distance(origin) ))) {
				nearestIntersection = currIntersection;
				nearestPrimitive = p;
			}
		}
		
		if (nearestIntersection !=null) {
			intersectionPoint.set(nearestIntersection);
		}
		
		return nearestPrimitive;
	}

	
	public Primitive getFirstIntersectionNew(final Ray ray, Point3d intersectionPoint) {
		Primitive nearestPrimitive = null;
		Point3d currIntersection = null, nearestIntersection=null;
		final Point3d origin = ray.getOrigin();
		// Check if the octree is created
		synchronized(this) {
			if (octree == null) {
				this.optimize();
			}		
		}
		
		// Find intersection in scene with ray
		// Los nodos del octree me vienen ordenados por cercania.
		// Si intersecta con el primer nodo, no se sigue procesando
		boolean found=false;
		//&& !found
		for (Iterator<OctreeNode> iter = octree.intersectedNodes(ray).iterator(); iter.hasNext() ;) {
			OctreeNode currNode = iter.next();
			
			//System.out.println("intersected node!" + currNode.primitives.size());
			
			for (Primitive p : currNode.primitives ) {
				currIntersection = p.intersect(ray);
				if (currIntersection != null && (nearestIntersection == null || (nearestIntersection != null &&
						currIntersection.distance(origin) < nearestIntersection.distance(origin)
						//&& currIntersection.distance(origin) > 0.1
						))) {
					nearestIntersection = currIntersection;
					nearestPrimitive = p;
				}
			}
			if (nearestIntersection!=null) {
				found=true;
			}
		}
		
		
		if (found) {
			intersectionPoint.set(nearestIntersection);
		}
		
		return nearestPrimitive;
	}

	public void addLight(Light light) {
		if ( light != null )
			lights.add(light);
	}
	
	public List<Light> getLights(){
		return lights;
	}
}
