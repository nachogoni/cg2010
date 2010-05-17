package ar.edu.itba.cg.tpe2.core.camera;

import java.util.Random;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import ar.edu.itba.cg.tpe2.core.geometry.Vector3;

public abstract class Camera {

	private String type;
	private Point3f eye, target;
	private Vector3f up;
	float fov;
	float aspect;
	private Vector3f FieldCenter;
	private Vector3 dv;
	private Vector3 du;
	private int width;
	private Random rand = new Random(0);
	
	public Camera(String type, Point3f eye, Point3f target2, Vector3f up,
			float fov, float aspect) {
		super();
		this.type = type;
		this.eye = eye;
		this.target = target2;
		this.up = up;
		this.fov = fov;
		this.aspect = aspect;
		this.FieldCenter = new Vector3f();
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
		this.FieldCenter.scale((width / 2) / (float)Math.tan(Math.toRadians((this.fov / 2))));
	}

	public String getType() {
		return type;
	}

	public Point3f getEye() {
		return eye;
	}

	public Point3f getTarget() {
		return target;
	}

	public Vector3f getUp() {
		return up;
	}

	public float getFov() {
		return fov;
	}

	public float getAspect() {
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
	public Point3f getPointFromXY(int width, int height, int i, int j) {
		
		// Get the center for the image
		Vector3f v = new Vector3f(this.FieldCenter);
		
		// v1 = i * this.du + v
		v.scaleAdd(i - width / 2, this.du, v);

		// v = j * this.dv + v
		v.scaleAdd(j - height /2, this.dv, v);
		
		return new Point3f(v.x, v.y, v.z);
	}

	public Point3f[] getPointFromXY(int width, int height, int i, int j, int side, int samples) {

		float rnd;
		int p = 0;
		int aa = 1;
		float portion = 1f / side /2;
		Point3f[] points = new Point3f[side*side*samples];
		
		if ((side * samples) == 1)
			aa = 0;
		
		for (int ii = 1; ii <= side; ii++) {
			for (int jj = 1; jj <= side; jj++) {
				for (int idx = 0; idx < samples; idx++) {
					Vector3f v = new Vector3f(this.FieldCenter);
					rnd = (rand.nextFloat() - 0.5f);
					v.scaleAdd((i - width / 2) + portion * ii + rnd * aa, this.du, v);
					rnd = (rand.nextFloat() - 0.5f);
					v.scaleAdd((j - height /2) + portion * jj + rnd * aa, this.dv, v);
					points[p++] = new Point3f(v.x, v.y, v.z);
				}
			}
		}
		
		return points;
	}

	// BLUR
	public Point3f[] getBlurPointFromXY(int width, int height, int i, int j, int side, int samples) {
		
		Point3f[] points = new Point3f[side];
		
		Random rand = new Random(0);
		float rnd;
		
		for (int idx = 0; idx < side; idx++) {
			Vector3f v = new Vector3f(this.FieldCenter);
			rnd = (rand.nextFloat() - 1) * samples;
			v.scaleAdd((i - width / 2) + rnd, this.du, v);
			rnd = (rand.nextFloat() - 1) * samples;
			v.scaleAdd((j - height /2) + rnd, this.dv, v);
			points[idx] = new Point3f(v.x, v.y, v.z);
		}
		
		return points;
	}

	@Override
	public String toString() {
		return "Camera [aspect=" + aspect + ", eye=" + eye + ", fov=" + fov
				+ ", target=" + target + ", type=" + type + ", up=" + up + "]";
	}
	
}
