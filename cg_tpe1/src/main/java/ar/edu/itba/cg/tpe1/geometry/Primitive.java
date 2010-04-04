package ar.edu.itba.cg.tpe1.geometry;

import java.awt.Color;

import javax.vecmath.Point3d;

public interface Primitive {

	/**
	 * Get the intersection between the figure and a ray
	 */
	Point3d intersect(Ray ray);
	
	/**
	 * Get color of the figure
	 */
	Color getColor(Point3d point);
	
	/**
	 * Set color of the figure
	 */
	void setColor(Color color);
	
	
	
}
