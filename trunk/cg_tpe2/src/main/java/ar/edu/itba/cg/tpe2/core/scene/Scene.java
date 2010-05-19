package ar.edu.itba.cg.tpe2.core.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Point3f;

import ar.edu.itba.cg.tpe2.core.camera.Camera;
import ar.edu.itba.cg.tpe2.core.geometry.Plane;
import ar.edu.itba.cg.tpe2.core.geometry.Primitive;
import ar.edu.itba.cg.tpe2.core.geometry.Ray;
import ar.edu.itba.cg.tpe2.core.light.Light;

/**
 * Create a scene representation
 */
public class Scene {

	private List<Primitive> primitives = Collections.synchronizedList(new ArrayList<Primitive>());
	
	private List<Primitive> planes = Collections.synchronizedList(new ArrayList<Primitive>());
	
	private List<Light> lights = Collections.synchronizedList(new ArrayList<Light>());
	
	private PrimitiveOctree octree = null;
	
	private Camera aCamera = null;
	private Image anImage = null;

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
				+ primitives + ", listPLanes="
				+ planes + ", octree=" + octree + "]";
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
	 * Get the list of planes in the scene
	 * 
	 * @return List of planes
	 */
	public List<Primitive> getPlanes() {
		return planes;
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
	
	public void addPlanes(List<Primitive> planes) {
		for (Primitive p : planes) {
			this.planes.add(p);
		}
	}
	
	public void optimize() {
		
		float xMin = PrimitiveOctree.DEFAULT_OCTREE_SIZE, xMax = -PrimitiveOctree.DEFAULT_OCTREE_SIZE, 
			   yMin = PrimitiveOctree.DEFAULT_OCTREE_SIZE, yMax = -PrimitiveOctree.DEFAULT_OCTREE_SIZE, 
			   zMin = PrimitiveOctree.DEFAULT_OCTREE_SIZE, zMax = -PrimitiveOctree.DEFAULT_OCTREE_SIZE;
		
		System.out.println("Creating Octree...");
		
		// Search for the maximun an minimun points for the scene
		for (Primitive p : primitives) {
			if (!(p instanceof Plane)) {
				float[] extremes= p.getBoundaryPoints();
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
		}

		// Create the octree
		octree = new PrimitiveOctree(xMin, xMax, yMin, xMax, zMin, zMax);

		// Add primitives to the octree
		for (Primitive p : primitives) {
			octree.add(p);
		}
		
		return;
	}
	
	public Primitive getFirstIntersection(final Ray ray, Point3f intersectionPoint){
		return getFirstIntersection(ray, intersectionPoint, null);
	}
	
	public Primitive getFirstIntersection(final Ray ray, Point3f intersectionPoint, Primitive primitiveToIgnore) {
		Primitive nearestPrimitive = null;
		Point3f currIntersection = null, nearestIntersection=null;
		final Point3f origin = ray.getOrigin();
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
		for (Iterator<OctreeNode> iter = octree.intersectedNodes(ray).iterator(); iter.hasNext() ;) {
			OctreeNode currNode = iter.next();
			/*System.out.println("Rayo: "+ ray.getDirection().toString());
			System.out.println("Intersecto con nodo: " + currNode.xMin + ", " + currNode.xMax + ", " 
					+ currNode.yMin + ", " + currNode.yMax + ", " + currNode.zMin + ", " + currNode.zMax );*/
			//System.out.println("intersected node!" + currNode.primitives.size());*/
			
			for (Primitive p : currNode.primitives ) {
				if ( !p.equals(primitiveToIgnore) ){
					currIntersection = p.intersect(ray);
					if (currIntersection != null  && currIntersection.distance(origin) > 0.00001 && 
							(nearestIntersection == null || (nearestIntersection != null &&
							currIntersection.distance(origin) < nearestIntersection.distance(origin)
							//&& currIntersection.distance(origin) > 0.1
							))) {
						nearestIntersection = currIntersection;
						nearestPrimitive = p;
					}
				}
			}
		}
		for (Primitive p : this.planes) {
			if ( !p.equals(primitiveToIgnore) ){
				currIntersection = p.intersect(ray);
				if (currIntersection != null  && currIntersection.distance(origin) > 0.00001 && 
						(nearestIntersection == null || (nearestIntersection != null &&
						currIntersection.distance(origin) < nearestIntersection.distance(origin)
						//&& currIntersection.distance(origin) > 0.1
						))) {
					nearestIntersection = currIntersection;
					nearestPrimitive = p;
				}
			}
		}
		if (nearestIntersection!=null) {
			found=true;
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
