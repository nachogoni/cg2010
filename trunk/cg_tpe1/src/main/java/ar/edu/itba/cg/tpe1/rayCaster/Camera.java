package ar.edu.itba.cg.tpe1.rayCaster;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe1.geometry.Vector3;

/**
 * Create a camera representation
 */
public class Camera {

	private Point3d origin;
	private Point3d direction;
	private double apertureAngle;
	private double zoom;
	
	/**
	 * Constructor for the Camera representation
	 *  
	 * @param origin Origin point for the camera, where the viewer is
	 * @param direction Direction for the viewer
	 * @param apertureAngle Aperture angle for the camera
	 * @param zoom Zoom for the camera
	 */
	public Camera(Point3d origin, Point3d direction, double apertureAngle, double zoom) throws IllegalArgumentException {
		if (apertureAngle <= 0 || apertureAngle > 360)
			throw new IllegalArgumentException();
		
		this.origin = origin;
		this.direction = direction;
		this.apertureAngle = apertureAngle;
		this.zoom = zoom;
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
	 * Get aperture angle for the camera
	 * 
	 * @return Aperture angle
	 */
	public double getApertureAngle() {
		return apertureAngle;
	}

	/**
	 * Set the aperture angle for the camera
	 * 
	 * @param apertureAngle New aperture angle
	 */
	public void setApertureAngle(int apertureAngle) throws IllegalArgumentException {
		if (apertureAngle <= 0 || apertureAngle > 360)
			throw new IllegalArgumentException();
		
		this.apertureAngle = apertureAngle;
	}
	
	/**
	 * Set the zoom for the camera
	 * 
	 * @param zoom New zoom
	 */
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}
	
	/**
	 * Get zoom for the camera
	 * 
	 * @return Camera's zoom
	 */
	public double getZoom() {
		return zoom;
	}
	
	/**
	 * Get a point at a certain distance from a pixel in the viewport
	 *  
	 * @param width Width for the viewport
	 * @param height Height for the viewport
	 * @param i Column
	 * @param j Row
	 * 
	 * @return Point at certain distance
	 */
	public Point3d getPointFromXY(int width, int height, int i, int j, double distance) {
		
		Point3d point = null;
		
		Vector3 v = new Vector3(origin, direction);
		
		//TODO: cleanup
		//double x = v.x + distance * Math.tan((((i * apertureAngle) / width) - (apertureAngle / 2)) / (apertureAngle / 2));
		//double y = v.y + distance * Math.tan((((j * apertureAngle) / height) - (apertureAngle / 2)) / (apertureAngle / 2));
		double x =  v.x - i + width / 2;
		double y =  v.y - j + height /2;
		
		point = new Point3d(x, y, v.z * this.zoom);
		
		return point;
	}

	/**
	 * Get a point from a pixel in the viewport
	 *  
	 * @param width Width for the viewport
	 * @param height Height for the viewport
	 * @param i Column
	 * @param j Row
	 * 
	 * @return Point at distance of 1 unit 
	 */
	public Point3d getPointFromXY(int width, int height, int i, int j) {
		return this.getPointFromXY(width, height, i, j, Math.abs(origin.distance(direction)));
	}
}
