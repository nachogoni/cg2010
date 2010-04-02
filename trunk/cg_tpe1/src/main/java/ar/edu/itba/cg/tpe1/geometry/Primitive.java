package ar.edu.itba.cg.tpe1.geometry;

import java.awt.Color;

import javax.vecmath.Point3f;

public interface Primitive {

	/**
	 * Get the intersection between the figure and a ray
	 */
	Point3f intersect(Ray ray);
	
	/**
	 * Get color of the figure
	 */
	Color getColor();
	
	/**
	 * Set color of the figure
	 */
	void setColor(Color color);
	
	
	
}
