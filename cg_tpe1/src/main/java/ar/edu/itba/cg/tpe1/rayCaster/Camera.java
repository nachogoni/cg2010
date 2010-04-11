package ar.edu.itba.cg.tpe1.rayCaster;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import ar.edu.itba.cg.tpe1.geometry.Vector3;

/**
 * Create a camera representation
 */
public class Camera {

	private Point3d origin;
	private Vector3d FieldCenter;
	private Vector3d direction;
	private Vector3d upVector;
	private double fov;
	private Vector3d du;
	private Vector3d dv;
	private int width = 0;
	private int height = 0;
	private int xTranslation = 0;
	private int yTranslation = 0;
	
	/**
	 * Constructor for the Camera representation
	 *  
	 * @param origin Origin point for the camera, where the viewer is
	 * @param direction Direction for the viewer
	 * @param upVector Up vector for the camera
	 * @param fov Aperture angle for the camera
	 * @param height Height for the image
	 * @param width Width for the image
	 */
	public Camera(Point3d origin, Point3d direction, Point3d upVector, double fov, int height, int width) {
		this.origin = (Point3d) origin.clone();
		this.direction = new Vector3(origin, direction);
		this.direction.normalize();
		this.upVector = new Vector3(upVector, origin);
		this.fov = fov;
		this.width = width;
		this.height = height;
		this.FieldCenter = new Vector3d();
		this.FieldCenter.sub(this.direction, this.origin);
		this.FieldCenter.normalize();
		this.FieldCenter.scale((width / 2) / Math.tan(Math.toRadians((this.fov / 2))));
		// Y unit vector
		this.dv = (Vector3) this.upVector.clone();
		this.dv.normalize();
		// X unit vector
		this.du = new Vector3();
		this.du.cross(this.dv, this.direction);
		this.du.normalize();
		this.xTranslation = 0;
		this.yTranslation = 0;
	}
	
	/**
	 * Position where the viewer is
	 * 
	 * @return Origin point
	 */
	public Point3d getOrigin() {
		return (Point3d) origin.clone();
	}

	/**
	 * Set position where the viewer is
	 * 
	 * @param origin New position
	 */
	public void setOrigin(Point3d origin) {
		this.origin = (Point3d) origin.clone();
	}

	/**
	 * Get the viewre's direction
	 * 
	 * @return Camera direction
	 */
	public Vector3d getDirection() {
		return (Vector3d) direction.clone();
	}

	/**
	 * Set the viewre's direction
	 * 
	 * @param direction New camera direction
	 */
	public void setDirection(Vector3d direction) {
		this.direction = (Vector3d) direction.clone();;
	}

	/**
	 * Set the aperture angle for the camera
	 * 
	 * @param fov New aperture angle
	 */
	public void setFov(double fov) {
		this.fov = fov;
	}
	
	/**
	 * Get focus aperture angle the camera
	 * 
	 * @return Camera's aperture angle
	 */
	public double getFov() {
		return fov;
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

}
