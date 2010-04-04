package ar.edu.itba.cg.tpe1.geometry;

import java.awt.Color;

import javax.vecmath.Point3d;

public class Fog implements Primitive {

	Color color;
	
	public Fog(Color color) {
		this.color = color;
	}
	
	public Color getColor(Point3d point) {
		return this.color;
	}
	
	public Point3d intersect(Ray ray) {
		
		double x = (int)(ray.getDirection().x); 
		double y = (int)(ray.getDirection().y); 
		
		return ((((x % 2) == 0) && ((y % 2) == 0)) ? ray.getOrigin(): null);
		//return null;
		//return ray.getOrigin();
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	
	
}
