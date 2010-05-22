package ar.edu.itba.cg.tpe2.core.camera;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

public class Pinhole extends Camera {

	private Point2f shift;

	public Pinhole(String type, Point3f eye, Point3f target, Vector3f up,
			float fov, float aspect, Point2f shift) {
		super(type, eye, target, up, fov, aspect);
		this.shift = shift;
	}

	public Pinhole(String type, Point3f eye, Point3f target2, Vector3f up,
			float fov, float aspect) {
		super(type, eye, target2, up, fov, aspect);
	}

	public Point2f getShift() {
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
