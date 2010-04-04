package ar.edu.itba.cg.tpe1.geometry;

import java.awt.Color;

import javax.vecmath.Point3d;

public class Sphere implements Primitive {

	Point3d origin;
	float radius;
	Color color;
	
	public Sphere(Point3d origin, float radius, Color color) throws IllegalArgumentException {
		if (radius <= 0f)
			throw new IllegalArgumentException("Invalid radius");
		this.radius = radius;
		this.origin = origin;
		this.color = color;
	}
	
	public Point3d intersect(Ray ray) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Color getColor(Point3d point) {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color; 
	}
	
}
