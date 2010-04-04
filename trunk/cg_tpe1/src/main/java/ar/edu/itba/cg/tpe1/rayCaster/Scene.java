package ar.edu.itba.cg.tpe1.rayCaster;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe1.geometry.Primitive;
import ar.edu.itba.cg.tpe1.geometry.Quadrilateral;
import ar.edu.itba.cg.tpe1.geometry.Sphere;
import ar.edu.itba.cg.tpe1.geometry.Triangle;

/**
 * Create a scene representation
 */
public class Scene {

	private List<Primitive> list = new ArrayList<Primitive>();
	
	/**
	 * Contructor for the scene from a file name
	 * 
	 * @param scene Scene file name
	 */
	public Scene(String scene) {
		this.list = read(scene);
	}

	/**
	 * Contructor for the scene from a primitives list
	 * 
	 * @param list List of primitives in the scene
	 */
	public Scene(List<Primitive> list) {
		this.list = list;
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
	 * Add a primitive in the scene
	 * 
	 * @param p New primitive
	 */
	public void add(Primitive p) {
    	list.add(p);
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
	
	/**
	 * Return a list of primitives into a file
	 * 
	 * @param scene File name of the scene
	 * 
	 * @return List of primitives
	 */
	public static List<Primitive> read(String scene) {
		
		List<Primitive> list = new ArrayList<Primitive>();
		
		if (scene.equals("scene1.sc")) {
			// Pyramid of side 5
			list.add(new Triangle(new Point3d(-5, 0, 0),  new Point3d( 0, 5, 0), new Point3d( 0, 0, 5),  Color.RED));
			list.add(new Triangle(new Point3d(-5, 0, 0),  new Point3d( 0, 5, 0), new Point3d( 0, 0,-5),  Color.YELLOW));
			list.add(new Triangle(new Point3d( 0, 0,-5),  new Point3d( 0, 5, 0), new Point3d( 5, 0, 0),  Color.GREEN));
			list.add(new Triangle(new Point3d( 0, 0, 5),  new Point3d( 0, 5, 0), new Point3d( 5, 0, 0),  Color.BLUE));
			// Sphere of radius 1 at the top of the pyramid
			list.add(new Sphere(new Point3d( 0, 6, 0), 1,  Color.ORANGE));
			// Spheres of radius 0.5 at pyramid vertexes
			list.add(new Sphere(new Point3d( 8, 0, 0), 0.5f,  Color.CYAN));
			list.add(new Sphere(new Point3d(-8, 0, 0), 0.5f,  Color.GRAY));
			list.add(new Sphere(new Point3d( 0, 0, 8), 0.5f,  Color.MAGENTA));
			list.add(new Sphere(new Point3d( 0, 0,-8), 0.5f,  Color.PINK));
		} else if (scene.equals("scene2.sc")) {
			// 64 spheres of radius 1 in a 4 x 4 x 4 cube distribution with a separation of 0.5
			for (double x = -2.25d; x <= 2.25; x += 1.5) {
				for (double y = -2.25d; y <= 2.25; y += 1.5) {
					for (double z = -2.25d; z <= 2.25; z += 1.5) {
						list.add(new Sphere(new Point3d( x, y, z), 1,  new Color((int)(((x + 2.25)/4.5) * 255), (int)(((y + 2.25)/4.5) * 255), (int)(((z + 2.25)/4.5) * 255))));
					}
				}
			}
		} else if (scene.equals("scene3.sc")) {
			// 3 cubes of side 2 and 2 spheres of radius 1 distributed and aligned to X axis
			for (double x = -4d; x <= 4; x += 4) {
				for (Primitive p : createCube(new Point3d(x,0,0), 2, Color.DARK_GRAY)) {
					list.add(p);
				}
			}
			// 2 spheres of radius 1 between the cubes
			list.add(new Sphere(new Point3d( -2, 0, 0), 1f,  Color.MAGENTA));
			list.add(new Sphere(new Point3d(  2, 0, 0), 1f,  Color.MAGENTA));
		} else {
			list = null;
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
		
		list.add(new Quadrilateral( a, b, c, d,  color));
		list.add(new Quadrilateral( a, b, f, e,  color));
		list.add(new Quadrilateral( a, d, h, e,  color));
		list.add(new Quadrilateral( c, b, f, g,  color));
		list.add(new Quadrilateral( d, c, g, h,  color));
		list.add(new Quadrilateral( e, f, g, h,  color));
		
		return list;
	}
	
}
