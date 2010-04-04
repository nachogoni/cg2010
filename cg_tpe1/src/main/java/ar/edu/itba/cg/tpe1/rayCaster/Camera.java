package ar.edu.itba.cg.tpe1.rayCaster;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe1.geometry.Vector3;

/**
 * Create a camera representation
 */
public class Camera {

	private Point3d origin;
	private Point3d direction;
	private double focus;
	
	/**
	 * Constructor for the Camera representation
	 *  
	 * @param origin Origin point for the camera, where the viewer is
	 * @param direction Direction for the viewer
	 * @param focus Focus where the camera place the viewport and take the picture
	 */
	public Camera(Point3d origin, Point3d direction, double focus) {
		this.origin = origin;
		this.direction = direction;
		this.focus = focus;
	}
	
	/**
	 * Position where the viewer is
	 * 
	 * @return Origin point
	 */
	public Point3d getOrigin() {
		return origin;
	}

	/**
	 * Set position where the viewer is
	 * 
	 * @param origin New position
	 */
	public void setOrigin(Point3d origin) {
		this.origin = origin;
	}

	/**
	 * Get the viewre's direction
	 * 
	 * @return Camera direction
	 */
	public Point3d getDirection() {
		return direction;
	}

	/**
	 * Set the viewre's direction
	 * 
	 * @param direction New camera direction
	 */
	public void setDirection(Point3d direction) {
		this.direction = direction;
	}

	/**
	 * Set the focus for the camera
	 * 
	 * @param focus New focus
	 */
	public void setFocus(double focus) {
		this.focus = focus;
	}
	
	/**
	 * Get focus for the camera
	 * 
	 * @return Camera's focus
	 */
	public double getFocus() {
		return focus;
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
		
		Point3d point = null;
		
		Vector3 v = new Vector3(origin, direction);
		
		//double x = v.x + distance * Math.tan((((i * apertureAngle) / width) - (apertureAngle / 2)) / (apertureAngle / 2));
		//double y = v.y + distance * Math.tan((((j * apertureAngle) / height) - (apertureAngle / 2)) / (apertureAngle / 2));
		double x =  v.x + i - width / 2;
		double y =  v.y - j + height /2;
		
		point = new Point3d(x, y, v.z * this.focus);
		
		return point;
	}

}
