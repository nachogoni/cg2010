package ar.edu.itba.cg.tpe1.geometry;

import java.awt.Color;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

public class Quadrilateral implements Primitive {
	
	private Point3d p1, p2, p3, p4;
	private Color aColor;
	private Point3d normal;
	
	public Quadrilateral(Point3d p1, Point3d p2, Point3d p3, Point3d p4){
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
	}
	
	public Quadrilateral(Point3d p1, Point3d p2, Point3d p3, Point3d p4, Color aColor){
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
		this.aColor = aColor;
	}

	
	public Point3d intersect(Ray ray) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Color getColor(Point3d point) {
		// TODO Auto-generated method stub
		return this.aColor;
	}
	
	public void setColor(Color color) {
		// TODO Auto-generated method stub
		this.aColor = color;
		
	}
	
}
