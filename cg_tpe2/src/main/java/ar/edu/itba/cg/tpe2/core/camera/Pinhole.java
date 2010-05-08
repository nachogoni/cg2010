package ar.edu.itba.cg.tpe2.core.camera;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Pinhole extends Camera {

	private Point2d shift;

	public Pinhole(String type, Point3d eye, Point3d target, Vector3d up,
			double fov, double aspect, Point2d shift) {
		super(type, eye, target, up, fov, aspect);
		this.shift = shift;
	}

	public Pinhole(String type, Point3d eye, Point3d target2, Vector3d up,
			double fov, double aspect) {
		super(type, eye, target2, up, fov, aspect);
	}

	public Point2d getShift() {
		return shift;
	}

	@Override
	public String toString() {
		return "Pinhole [shift=" + shift + ", getAspect()=" + getAspect()
				+ ", getEye()=" + getEye() + ", getFov()=" + getFov()
				+ ", getTarget()=" + getTarget() + ", getType()=" + getType()
				+ ", getUp()=" + getUp() + "]";
	}
	
	
	
}
