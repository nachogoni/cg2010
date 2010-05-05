package ar.edu.itba.cg.tpe2.core.geometry;

import javax.vecmath.Point3d;

/**
 * Ray representation
 *
 */
public class Ray {
	
	private Point3d origin;
	
	private Point3d direction;
	
	/**
	 * Constructor for Ray class
	 * 
	 * @param origin Position where from the ray starts 
	 * @param direction Direction for the ray
	 */
	public Ray(Point3d origin, Point3d direction) {
		this.origin = origin;
		this.direction = direction;
	}
	
	/**
	 * Position where from the ray starts
	 * 
	 * @return Origin point
	 */
	public Point3d getOrigin() {
		return origin;
	}

	/**
	 * Set position where from the ray starts
	 * 
	 * @param origin New position
	 */
	public void setOrigin(Point3d origin) {
		this.origin = origin;
	}

	/**
	 * Get ray direction
	 * 
	 * @return Ray direction
	 */
	public Point3d getDirection() {
		return direction;
	}

	/**
	 * Set ray direction
	 * 
	 * @param direction New ray direction
	 */
	public void setDirection(Point3d direction) {
		this.direction = direction;
	}

}
