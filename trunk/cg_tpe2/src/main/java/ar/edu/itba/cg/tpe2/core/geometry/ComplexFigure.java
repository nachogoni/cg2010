package ar.edu.itba.cg.tpe2.core.geometry;

import java.util.List;

public abstract class ComplexFigure {

	// Name
	String name;
	
	public ComplexFigure(String name) {
		
		this.name = name;
	}
	
	/**
	 * Rotate object in X axis 
	 * 
	 */
	public abstract void rotatex(double angle);
	
	/**
	 * Rotate object in Y axis 
	 * 
	 */
	public abstract void rotatey(double angle); 
	
	/**
	 * Rotate object in Z axis 
	 * 
	 */
	public abstract void rotatez(double angle); 
	
	/**
	 * Scale primitive in X axis
	 * 
	 */
	public abstract void scalex(double scale); 
	
	/**
	 * Scale primitive in Y axis
	 * 
	 */
	public abstract void scaley(double scale); 
	
	/**
	 * Scale primitive in Z axis
	 * 
	 */
	public abstract void scalez(double scale); 
	
	/**
	 * Scale primitive in U
	 * 
	 */
	public abstract void scaleu(double scale); 
	
	/**
	 * Get the intersection between the figure and a ray
	 */
	public abstract List<Primitive> intersect(Ray ray);
	
}
