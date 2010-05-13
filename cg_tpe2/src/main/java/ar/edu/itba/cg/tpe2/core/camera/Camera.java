package ar.edu.itba.cg.tpe2.core.camera;

import java.util.Random;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import ar.edu.itba.cg.tpe2.core.geometry.Vector3;

public abstract class Camera {

	private String type;
	private Point3d eye, target;
	private Vector3d up;
	double fov;
	double aspect;
	private Vector3d FieldCenter;
	private Vector3 dv;
	private Vector3 du;
	private int width;
	
	public Camera(String type, Point3d eye, Point3d target2, Vector3d up,
			double fov, double aspect) {
		super();
		this.type = type;
		this.eye = eye;
		this.target = target2;
		this.up = up;
		this.fov = fov;
		this.aspect = aspect;
		this.FieldCenter = new Vector3d();
		this.FieldCenter.sub(this.target, this.eye);
		this.FieldCenter.normalize();
//		this.FieldCenter.scale((width / 2) / Math.tan(Math.toRadians((this.fov / 2))));
		// Y unit vector
		this.dv = new Vector3(-this.up.x, -this.up.y, -this.up.z);
		this.dv.normalize();
		// X unit vector
		this.du = new Vector3();
		Vector3 dir = new Vector3();
		dir.sub(this.target, this.eye);
		this.du.cross(this.dv, dir);
		this.du.normalize();
	}
	
	public void setWidth(int width){
		this.width = width;
		this.FieldCenter.scale((width / 2) / Math.tan(Math.toRadians((this.fov / 2))));
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

	/**
	 * Get a point from a pixel in the viewport
	 *  
	 * @param width Width for the viewport
	 * @param height Height for the viewport
	 * @param i Column
	 * @param j Row
	 * 
	 * @return Point at certain distance
	 */
	public Point3d getPointFromXY(int width, int height, int i, int j) {
		
		// Get the center for the image
		Vector3d v = new Vector3d(this.FieldCenter);
		
		// v1 = i * this.du + v
		v.scaleAdd(i - width / 2, this.du, v);

		// v = j * this.dv + v
		v.scaleAdd(j - height /2, this.dv, v);
		
		return new Point3d(v.x, v.y, v.z);
	}

	public Point3d[] getPointFromXY(int width, int height, int i, int j, int aaCount, int size) {

		Point3d[] points = new Point3d[aaCount];

		Random rand = new Random(0);
		float rnd;
		
		for (int idx = 0; idx < aaCount; idx++) {
			Vector3d v = new Vector3d(this.FieldCenter);
			rnd = (rand.nextFloat() - 1) * size;
			v.scaleAdd((i - width / 2) + rnd, this.du, v);
			rnd = (rand.nextFloat() - 1) * size;
			v.scaleAdd((j - height /2) + rnd, this.dv, v);
			points[idx] = new Point3d(v.x, v.y, v.z);
		}
		
		return points;
	}


	
	@Override
	public String toString() {
		return "Camera [aspect=" + aspect + ", eye=" + eye + ", fov=" + fov
				+ ", target=" + target + ", type=" + type + ", up=" + up + "]";
	}
	
}
