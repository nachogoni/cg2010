package ar.edu.itba.cg.tpe1.rayCaster;

import javax.vecmath.Point3d;

public class Camera {

	private Point3d origin;
	
	private Point3d direction;
	
	public Camera(Point3d point3d, Point3d point3d2) {
		this.origin = point3d;
		this.direction = point3d2;
	}
	
	public Point3d getOrigin() {
		return origin;
	}

	public void setOrigin(Point3d origin) {
		this.origin = origin;
	}

	public Point3d getDirection() {
		return direction;
	}

	public void setDirection(Point3d direction) {
		this.direction = direction;
	}

	
}
