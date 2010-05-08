package ar.edu.itba.cg.tpe2.core.scene;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.core.camera.Camera;
import ar.edu.itba.cg.tpe2.core.geometry.Plane;
import ar.edu.itba.cg.tpe2.core.geometry.Primitive;
import ar.edu.itba.cg.tpe2.core.geometry.Quadrilateral;
import ar.edu.itba.cg.tpe2.core.geometry.Ray;
import ar.edu.itba.cg.tpe2.core.geometry.Sphere;
import ar.edu.itba.cg.tpe2.core.geometry.Triangle;
import ar.edu.itba.cg.tpe2.core.geometry.Vector3;
import ar.edu.itba.cg.tpe2.utils.PrimitiveOctree;

/**
 * Create a scene representation
 */
public class Scene {

	private List<Primitive> list = Collections.synchronizedList(new ArrayList<Primitive>());
	
	private PrimitiveOctree octree = null;
	
	private Camera aCamera = null;
	private Image anImage = null;
	
	public Scene(List<Primitive> list, PrimitiveOctree octree, Camera aCamera,
			Image anImage) {
		super();
		this.list = list;
		this.octree = octree;
		this.aCamera = aCamera;
		this.anImage = anImage;
		this.optimize();
	}

	public Camera getaCamera() {
		return aCamera;
	}

	public void setaCamera(Camera aCamera) {
		this.aCamera = aCamera;
	}

	public Image getAnImage() {
		return anImage;
	}

	public void setAnImage(Image anImage) {
		this.anImage = anImage;
	}

	@Override
	public String toString() {
		return "Scene [aCamera=" + aCamera + ", anImage=" + anImage + ", list="
				+ list + ", octree=" + octree + "]";
	}

	/**
	 * Constructor for the scene from a file name
	 * 
	 * @param scene Scene file name
	 */
	public Scene(String scene) {
		this.list = read(scene);
		this.optimize();
	}

	/**
	 * Constructor for the scene from a primitives list
	 * 
	 * @param list List of primitives in the scene
	 */
	public Scene(List<Primitive> list) {
		this.list = list;
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
	public List<Primitive> getList() {
		return list;
	}
	
	/**
	 * Add a list of primitives in the scene
	 * 
	 * @param primitives List of primitives to be added
	 */
	public void add(List<Primitive> primitives) {
		for (Primitive p : primitives) {
			list.add(p);
		}
	}
	
	public void optimize() {
		
		double xMin = Double.MAX_VALUE, xMax = Double.MIN_VALUE, 
			   yMin = Double.MAX_VALUE, yMax = Double.MIN_VALUE, 
			   zMin = Double.MAX_VALUE, zMax = Double.MIN_VALUE;
		
		// Search for the maximun an minimun points for the scene
		for (Primitive p : list) {
			for (Point3d point : p.getBoundaryPoints()) {
				if (point.x > xMax) {
					xMax = point.x;
				} else if (point.x < xMin) {
					xMin = point.x;
				}
				if (point.y > yMax) {
					yMax = point.y;
				} else if (point.y < yMin) {
					yMin = point.y;
				}
				if (point.z > zMax) {
					zMax = point.z;
				} else if (point.z < zMin) {
					zMin = point.z;
				}
			}
			
		}
		
		// Create the octree
		octree = new PrimitiveOctree(xMin, xMax, yMin, xMax, zMin, zMax);
		
		// Add primitives to the octree
		for (Primitive p : list) {
			octree.add(p);
		}
		
		return;
	}
	
//	public List<Primitive> intersect
	
	/**
	 * Return a list of primitives into a file
	 * 
	 * @param scene File name of the scene
	 * 
	 * @return List of primitives
	 */
	@Deprecated
public static List<Primitive> read(String scene) {
		
		List<Primitive> list = Collections.synchronizedList(new ArrayList<Primitive>());
		
		if (scene.equals("scene1.sc")) {
			
			double a = Math.sqrt(Math.pow(2.5d, 2) + Math.pow(2.5d, 2));
			
			// Pyramid of side 5
			list.add(new Triangle("",null,new Point3d(-a, 0, 0),  new Point3d( 0, 5, 0), new Point3d( 0, 0, a),  Color.RED));
			list.add(new Triangle("",null,new Point3d( 0, 0, a),  new Point3d( 0, 5, 0), new Point3d( a, 0, 0),  Color.BLUE));
			list.add(new Triangle("",null,new Point3d(-a+0.01, 0, 0),  new Point3d( 0, 5, 0), new Point3d( 0, 0,-a),  Color.YELLOW));
			list.add(new Triangle("",null,new Point3d( 0, 0,-a),  new Point3d( 0, 5, 0), new Point3d( a-0.01, 0, 0),  Color.GREEN));
			// Sphere of radius 1 at the top of the pyramid
			list.add(new Sphere("",null,new Point3d( 0, 6, 0), 1,  Color.ORANGE));
			// Spheres of radius 0.5 at pyramid vertexes
			list.add(new Sphere("",null,new Point3d( a + 3.5, 0, 0), 0.5f,  Color.CYAN));
			list.add(new Sphere("",null,new Point3d(-(a + 3.5), 0, 0), 0.5f,  Color.GRAY));
			list.add(new Sphere("",null,new Point3d( 0, 0, a + 3.5), 0.5f,  Color.MAGENTA));
			list.add(new Sphere("",null,new Point3d( 0, 0,-(a + 3.5)), 0.5f,  Color.PINK));
		} else if (scene.equals("scene2.sc")) {
			// 64 spheres of radius 1 in a 4 x 4 x 4 cube distribution with a separation of 0.5
			//list.add ( new Sphere(new Point3d( 0, 0, 0), 0.5,  Color.CYAN ));
			double spheresPerFace=4.0;
			double distance=0.5;
			double radius=0.5;
			double interval=spheresPerFace*radius + (spheresPerFace/2-1)*distance + distance/2 - radius; 
			for (double x = -interval ; x <= interval; x += 2*radius + distance) {
				for (double y = -interval; y <= interval; y += 2*radius + distance) {
					for (double z = -interval; z <= interval; z += 2*radius + distance) {
						list.add(new Sphere("",null,new Point3d( x, y, z), radius,  new Color((int)(((x + interval)/(2*interval)) * 255), (int)(((y + interval)/(2*interval)) * 255), (int)(((z + interval)/(2*interval)) * 255))));
					}
				}
			}
		} else if (scene.equals("scene3.sc")) {
			// 3 cubes of side 2 and 2 spheres of radius 1 distributed and aligned to X axis over the y=0 surface.
			// Ambiguous scene:
			/*Escena 3 (scene3.sc): deberá contener de forma intercalada 3 cubos de lado 2 y
			dos esferas de radio 1 alineados de manera centrada sobre el eje x. Las bases de
			los cubos y las esferas deben estar “apoyadas” sobre el plano y = 0. La separación
			entre las figuras, esto es, el “hueco de aire” entre ellas es de 0.5. La escena debe
			estar centrada a lo largo del eje x. Los centros de las figuras deben coincidir con el
			plano z = 0.
			*/
			for (double x = -5d; x <= 5; x += 5) {
				for (Primitive p : createCube(new Point3d(x,1,0), 2, Color.BLUE)) {
					list.add(p);
				}
			}
			// 2 spheres of radius 1 between the cubes
			list.add(new Sphere("",null,new Point3d( -2.5, 1, 0), 1f,  Color.MAGENTA));
			list.add(new Sphere("",null,new Point3d(  2.5, 1, 0), 1f,  Color.MAGENTA));
		} else if (scene.equals("scene4.sc")) {
			// 3 cubes of side 2 and 2 spheres of radius 1 distributed and aligned to X axis
			for (double x = -5d; x <= 5; x += 5) {
				for (Primitive p : createCubeT(new Point3d(x,0,0), 2, Color.DARK_GRAY)) {
					list.add(p);
				}
			}
			// 2 spheres of radius 1 between the cubes
			list.add(new Sphere("",null,new Point3d( -2.5, 0, 0), 1f,  Color.MAGENTA));
			list.add(new Sphere("",null,new Point3d(  2.5, 0, 0), 1f,  Color.MAGENTA));
		} else if (scene.equals("scene5.sc")) {
			double spheres=11.0;
			double distance=0.5;
			double radius=0.5;
			double interval=spheres*radius + (spheres/2-1)*distance + distance/2 - radius; 
			for (double x = -interval ; x <= interval; x += 2*radius + distance) {
				list.add(new Sphere("",null,new Point3d( x, 0, 0), radius,  new Color(0,0,255)));
			}
		} else if (scene.equals("scene6.sc")){
			list.add(new Triangle("",null,new Point3d( 0, 0, 9),  new Point3d( -5, 5, -4), 
					new Point3d( 5, 5, -10),  Color.WHITE));
		} else if (scene.equals("scene7.sc")){
			Triangle triangle = new Triangle("",null,new Point3d( 0, 0, 2),  new Point3d( 0, 1, 2), new Point3d( 1, 0, 2),  Color.YELLOW);
			Triangle triangle2 = new Triangle("",null,new Point3d( 1, 1, 2),  new Point3d( 0, 1, 2), new Point3d( 1, 0, 2),  Color.BLUE);
			triangle.scalex(0.5d);
			triangle2.scaley(0.5d);
			triangle.scalez(-10d);
			list.add(triangle);
			list.add(triangle2);
		} else if (scene.equals("scene8.sc")){
			Plane plane = new Plane(null, null, new Point3d(0,0,0),new Vector3(new Point3d(),new Point3d(1, 0, 0)));
			plane.setColor(Color.BLUE);
			list.add(plane);
			plane = new Plane(null, null, new Point3d(0,0,0),new Vector3(new Point3d(),new Point3d(0, 1, 0)));
			plane.setColor(Color.YELLOW);
			list.add(plane);
			plane = new Plane(null, null, new Point3d(0,0,0),new Vector3(new Point3d(),new Point3d(0, 0, 1)));
			plane.setColor(Color.WHITE);
			list.add(plane);
//			list.add(new Sphere(new Point3d( 0, 10, 1), 5f,  Color.CYAN));
//			list.add(new Sphere(new Point3d( 0 , -10, 1), 5f,  Color.GRAY));
		} else {
			// Another options?
		}
    	
    	return list;
	}

	/**
	 * Create a cube from a center point, a side and a color
	 * 
	 * @param center Center for the cube
	 * @param side Side of the cube
	 * @param color Color for the cube
	 * 
	 * @return List of primitives creating the cube
	 */
	@Deprecated
	private static List<Primitive> createCube(Point3d center, double side, Color color) {

		List<Primitive> list = new ArrayList<Primitive>();

		Point3d a = new Point3d( center.x - side / 2, center.y + side / 2, center.z - side / 2);
		Point3d b = new Point3d( center.x + side / 2, center.y + side / 2, center.z - side / 2);
		Point3d c = new Point3d( center.x + side / 2, center.y + side / 2, center.z + side / 2);
		Point3d d = new Point3d( center.x - side / 2, center.y + side / 2, center.z + side / 2);
		Point3d e = new Point3d( center.x - side / 2, center.y - side / 2, center.z - side / 2);
		Point3d f = new Point3d( center.x + side / 2, center.y - side / 2, center.z - side / 2);
		Point3d g = new Point3d( center.x + side / 2, center.y - side / 2, center.z + side / 2);
		Point3d h = new Point3d( center.x - side / 2, center.y - side / 2, center.z + side / 2);
		
		list.add(new Quadrilateral("",null, a, b, f, e,  Color.CYAN));
		list.add(new Quadrilateral("",null, a, d, h, e,  Color.GREEN));
		list.add(new Quadrilateral("",null, c, b, f, g,  Color.PINK));
		list.add(new Quadrilateral("",null, d, c, g, h,  Color.ORANGE));
		list.add(new Quadrilateral("",null, e, f, g, h,  Color.ORANGE));
		list.add(new Quadrilateral("",null, a, b, c, d,  Color.BLUE));
		
		return list;
	}
	
	/**
	 * Create a cube from a center point, a side and a color with triangles
	 * 
	 * @param center Center for the cube
	 * @param side Side of the cube
	 * @param color Color for the cube
	 * 
	 * @return List of primitives creating the cube
	 */
	@Deprecated
	private static List<Primitive> createCubeT(Point3d center, double side, Color color) {
		
		List<Primitive> list = new ArrayList<Primitive>();
		
		Point3d a = new Point3d( center.x - side / 2, center.y + side / 2, center.z - side / 2);
		Point3d b = new Point3d( center.x + side / 2, center.y + side / 2, center.z - side / 2);
		Point3d c = new Point3d( center.x + side / 2, center.y + side / 2, center.z + side / 2);
		Point3d d = new Point3d( center.x - side / 2, center.y + side / 2, center.z + side / 2);
		Point3d e = new Point3d( center.x - side / 2, center.y - side / 2, center.z - side / 2);
		Point3d f = new Point3d( center.x + side / 2, center.y - side / 2, center.z - side / 2);
		Point3d g = new Point3d( center.x + side / 2, center.y - side / 2, center.z + side / 2);
		Point3d h = new Point3d( center.x - side / 2, center.y - side / 2, center.z + side / 2);
		
		list.add(new Triangle("",null, a, b, f,  new Color(  0,  0,255)));
		list.add(new Triangle("",null, b, a, e,  new Color(  0,128,  0)));
		list.add(new Triangle("",null, e, d, h,  new Color(  0,128,128)));
		list.add(new Triangle("",null, d, a, e,  new Color(  0,128,255)));
		list.add(new Triangle("",null, c, b, f,  new Color(128,  0,  0)));
		list.add(new Triangle("",null, c, f, g,  new Color(128,  0,128)));
		list.add(new Triangle("",null, d, c, g,  new Color(128,  0,255)));
		list.add(new Triangle("",null, d, g, h,  new Color(128,128,  0)));
		list.add(new Triangle("",null, e, f, g,  new Color(128,128,128)));
		list.add(new Triangle("",null, e, g, h,  new Color(128,128,255)));
		list.add(new Triangle("",null, a, b, c,  new Color(255,255,255)));
		list.add(new Triangle("",null, a, c, d,  new Color(  0,  0,128)));
		
		return list;
	}

	public Primitive getFirstIntersection(Ray ray, Point3d intersectionPoint) {
		Primitive nearestPrimitive = null;
		Point3d currIntersection = null, nearestIntersection=null;
		Point3d origin = ray.getOrigin();
		
		// Find intersection in scene with ray
		for (Primitive p : octree.intersect(ray)) {
			currIntersection = p.intersect(ray);
			if (currIntersection != null && (nearestIntersection == null || (nearestIntersection != null &&
				currIntersection.distance(origin) < nearestIntersection.distance(origin)))) {
				nearestIntersection = currIntersection;
				nearestPrimitive = p;
			}
		}
		
		if (nearestIntersection !=null) {
			intersectionPoint.set(nearestIntersection);
		}
		
		return nearestPrimitive;
	}
	
}
