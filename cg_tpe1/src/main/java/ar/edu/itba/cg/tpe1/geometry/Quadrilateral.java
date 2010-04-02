package ar.edu.itba.cg.tpe1.geometry;

import java.awt.Color;

import javax.vecmath.Point3d;

public class Quadrilateral implements Primitive {

	Point3d p1;
	Point3d p2;
	Point3d p3;
	Point3d p4;
	Color color;
	
	public Quadrilateral(Point3d p1, Point3d p2, Point3d p3, Point3d p4, Color color) throws IllegalArgumentException {
	
		// TODO: check if p1 p2 p3 p4 do not define a quadrilateral
		
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
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
