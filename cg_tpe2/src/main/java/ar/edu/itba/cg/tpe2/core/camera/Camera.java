package ar.edu.itba.cg.tpe2.core.camera;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public abstract class Camera {

	// Type
	// Eye
	// Target
	// Up
	// FOV
	// Aspect

	private String type;
	private Point3d eye, target;
	private Vector3d up;
	double fov;
	double aspect;
	
	public Camera(String type, Point3d eye, Point3d target2, Vector3d up,
			double fov, double aspect) {
		super();
		this.type = type;
		this.eye = eye;
		this.target = target2;
		this.up = up;
		this.fov = fov;
		this.aspect = aspect;
	}

	public String getType() {
		return type;
	}

	public Point3d getEye() {
		return eye;
	}

	public Point3d getTarget() {
		return target;
	}

	public Vector3d getUp() {
		return up;
	}

	public double getFov() {
		return fov;
	}

	public double getAspect() {
		return aspect;
	}

	@Override
	public String toString() {
		return "Camera [aspect=" + aspect + ", eye=" + eye + ", fov=" + fov
				+ ", target=" + target + ", type=" + type + ", up=" + up + "]";
	}
	
	
	
	
	// Implementations
	
	// Type: Thinlens
		// fdist
		// lensr
	
	// Type: Pinhole
		// Shift
	
}
