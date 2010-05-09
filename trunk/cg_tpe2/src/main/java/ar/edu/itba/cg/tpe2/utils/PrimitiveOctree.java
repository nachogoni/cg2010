package ar.edu.itba.cg.tpe2.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import ar.edu.itba.cg.tpe2.core.geometry.Plane;
import ar.edu.itba.cg.tpe2.core.geometry.Primitive;
import ar.edu.itba.cg.tpe2.core.geometry.Ray;

public class PrimitiveOctree {

	static final int MAX_PRIMITIVES = 1000;
	
	List<PrimitiveOctree> nodes = new ArrayList<PrimitiveOctree>();
	
	Plane xPlane=null;
	Plane yPlane=null;
	Plane zPlane=null;
	
	List<Primitive> primitives = new ArrayList<Primitive>();
	
	private double xMin;
	private double xMax;
	private double yMin;
	private double yMax;
	private double zMin;
	private double zMax;
	
	private final static double EPSILON = 0.00001;
	
	public PrimitiveOctree(double xMinimun, double xMaximun, double yMinimun, double yMaximun, double zMinimun, double zMaximun) {
 
		// We are giving the chance to the intersect functions in primitives to have a representation error
		// and they still be in the correct octree node
		this.xMax = xMaximun+EPSILON;
		this.xMin = xMinimun-EPSILON;
		this.yMax = yMaximun+EPSILON;
		this.yMin = yMinimun-EPSILON;
		this.zMax = zMaximun+EPSILON;
		this.zMin = zMinimun-EPSILON;
		
		Point3d xPoint = new Point3d((xMinimun+xMaximun)/2,0,0);
		this.xPlane = new Plane(null, null, xPoint, new Vector3d(1.0, 0.0, 0.0), null );

		Point3d yPoint = new Point3d((yMinimun+yMaximun)/2,0,0);
		this.yPlane = new Plane(null, null, yPoint, new Vector3d(0.0, 1.0, 0.0), null );

		Point3d zPoint = new Point3d((zMinimun+zMaximun)/2,0,0);
		this.zPlane = new Plane(null, null, zPoint, new Vector3d(0.0, 0.0, 1.0), null );

	}
	
	public boolean contains(Point3d point) {
		// A point can be in two cubes with very low probability.
		if ((point.x >= this.xMin) && (point.x <= this.xMax) &&
			(point.y >= this.yMin) && (point.y <= this.yMax) &&
			(point.z >= this.zMin) && (point.z <= this.zMax))
			return true;
		else
			return false;
	}
	
	public void add(Primitive p) {

		// If it is a leaf node
		if (isLeaf()) {
			// add the primitive to the primitives list
			primitives.add(p);
			// check if the capacity is enought, otherwise, expands the node
			if (primitives.size() > MAX_PRIMITIVES) {
				expand();
			}
		} else {
			// Soy nodo? si-> chequeo si lo tengo que meter en que hijos y se los doy
			for (Point3d point: p.getBoundaryPoints()) {
				for (PrimitiveOctree child: nodes) {
						if (child.contains(point)) {
							child.add(p);
						}
				}
			}
		}
		
		return;
	}
	
	public void add(List<Primitive> list) {
		for (Primitive p : list) {
			add(p);
		}
	}
	
	private void expand() {
		
		double xMed = (xMin + xMax)/2;
		double yMed = (yMin + yMax)/2;
		double zMed = (zMin + zMax)/2;
		
		nodes.add(new PrimitiveOctree(xMin, xMed, yMin, yMed, zMin, zMed));
		nodes.add(new PrimitiveOctree(xMed, xMax, yMin, yMed, zMin, zMed));
		nodes.add(new PrimitiveOctree(xMin, xMed, yMed, yMax, zMin, zMed));
		nodes.add(new PrimitiveOctree(xMed, xMax, yMed, yMax, zMin, zMed));
		nodes.add(new PrimitiveOctree(xMin, xMed, yMin, yMed, zMed, zMax));
		nodes.add(new PrimitiveOctree(xMed, xMax, yMin, yMed, zMed, zMax));
		nodes.add(new PrimitiveOctree(xMin, xMed, yMed, yMax, zMed, zMax));
		nodes.add(new PrimitiveOctree(xMed, xMax, yMed, yMax, zMed, zMax));

		for (PrimitiveOctree child : nodes) {
			for (Primitive p : primitives) {
				for (Point3d point: p.getBoundaryPoints()) {
					if (child.contains(point)) {
						child.add(p);
					}
				}
			}
		}
		
		// This is not a leaf anymore
		primitives.clear();
		return;
	}
	
	public boolean isLeaf() {
		return nodes.isEmpty();
	}
	
	public boolean isEmpty() {
		return (primitives.isEmpty()) && nodes.isEmpty();
	}
	
	/**
	 * Get the possible primitive list
	 */
	@SuppressWarnings("serial")
	public List<Primitive> intersect(Ray ray) {
		
		List<Primitive> ret = null;
 		
		if (isLeaf()) {
			// returns the list of primitives
			ret = Collections.unmodifiableList(primitives);
		} else {
			
			// Generate the empty list of primitives for the function return
			ret = new ArrayList<Primitive>();
			
			//Override the add to avoid null insertions
			ArrayList<Point3d> intersectPts = new ArrayList<Point3d>() {
				@Override
				public boolean add(Point3d e) {
					if (e == null)
						return false;
					return super.add(e);
				}
			};
			
			// check intersections with middle planes in the octree to search in its childs
			intersectPts.add(xPlane.intersect(ray));
			intersectPts.add(yPlane.intersect(ray));
			intersectPts.add(zPlane.intersect(ray));
			for (Point3d point: intersectPts) {
				for (PrimitiveOctree child: nodes) {
					if (child.contains(point)) {
						System.out.println("hola");
						ret.addAll(child.intersect(ray));
					}
				}
			}
		}
		
		return ret;
	}

	
}
