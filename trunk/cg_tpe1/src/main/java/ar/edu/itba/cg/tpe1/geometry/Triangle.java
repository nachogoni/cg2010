package ar.edu.itba.cg.tpe1.geometry;

import java.awt.Color;

import javax.vecmath.Point3d;

public class Triangle implements Primitive {

	Point3d p1;
	Point3d p2;
	Point3d p3;
	Color color;
	
	public Triangle(Point3d p1, Point3d p2, Point3d p3, Color color) throws IllegalArgumentException {
	
		// TODO: check if p1 p2 p3 are in the same line
		
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.color = color;
	}
	
	public Point3d intersect(Ray ray) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color; 
	}
	
}
