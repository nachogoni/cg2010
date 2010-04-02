package ar.edu.itba.cg.tpe1.geometry;

import javax.vecmath.Point3f;

public class Ray {
	
	private Point3f origin;
	
	private Point3f direction;
	
	public Ray(Point3f origin, Point3f direction) {
		this.origin = origin;
		this.direction = direction;
	}
	
	public Point3f getOrigin() {
		return origin;
	}

	public void setOrigin(Point3f origin) {
		this.origin = origin;
	}

	public Point3f getDirection() {
		return direction;
	}

	public void setDirection(Point3f direction) {
		this.direction = direction;
	}

}
