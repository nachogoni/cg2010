package ar.edu.itba.cg.tpe2.core.geometry;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Ray representation
 *
 */
public class Ray {
	
	private Point3d origin;
	
	private Vector3d direction;
	
	/**
	 * Constructor for Ray class
	 * 
	 * @param origin Position where from the ray starts 
	 * @param direction Direction vector for the ray
	 */
	
	public Ray(Point3d origin, Vector3d direction) {
		this.origin = origin;
		direction.normalize();
		//System.out.println("rayo x:"+dirNormalized.x+", y:"+dirNormalized.y+", z:"+dirNormalized.z);
		this.direction = direction;
	}
	
	/**
	 * Constructor for Ray class
	 * 
	 * @param origin Position where from the ray starts 
	 * @param direction Position where from the ray ends
	 */	
	public Ray(Point3d origin, Point3d end) {
		this.origin = origin;		
		this.direction = new Vector3d(end.x-origin.x,end.y-origin.y,end.z-origin.z);
	}
	
	/**
	 * Constructs a ray at (0,0,0) with the given direction
	 * 
	 * 
	 * @param direction Direction for the ray
	 */
	public Ray(Vector3 direction){
		this.origin = new Point3d();
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
	public Vector3d getDirection() {
		return direction;
	}

	/**
	 * Set ray direction
	 * 
	 * @param direction New ray direction
	 */
	public void setDirection(Vector3d direction) {
		this.direction = direction;
	}

//	http://www.siggraph.org/education/materials/HyperGraph/raytrace/rtreflec.htm
	
	public Ray reflectFrom(Vector3 primitiveNormal, Point3d primitivePoint){
		Vector3 rNormal = new Vector3(primitiveNormal);
		double dotProduct = rNormal.dot(this.direction);
		rNormal.scale(-2*dotProduct);
		rNormal.add(this.direction);
		rNormal.normalize();
		
		Point3d rEnd = new Point3d(primitivePoint);
		rEnd.add(rNormal);
		Ray newRay = new Ray(primitivePoint,rEnd);
		return newRay;
	}

//	http://www.cse.ohio-state.edu/~kerwin/refraction.html
//	http://www.yaldex.com/open-gl/ch14lev1sec1.html
	public Ray refractFrom(Vector3 primitiveNormal, Point3d primitivePoint, double refractivity){
		Vector3d normalToUse = new Vector3d(primitiveNormal);
		double dotProduct = normalToUse.dot(this.direction);
		
		if ( dotProduct < 0 ){
			// Ray enters
			refractivity = 1 / refractivity;
		}else{
			// Ray goes out
			normalToUse.scale(-1.0);
		}
		Vector3d rDir = calculateRefractedDir(normalToUse,this.direction,dotProduct,refractivity);
		
		if ( rDir == null ){
			// No ray is refracted
			return null;
		}
		Point3d rEnd = new Point3d(primitivePoint);
		rEnd.add(rDir);
		Ray newRay = new Ray(primitivePoint,rEnd);
		return newRay;
	}
	
	private Vector3d calculateRefractedDir(Vector3d normalToUse, Vector3d thisDir, double angle, double refractivity) {
		Vector3d direction = new Vector3d();
		
		double dotProduct = -angle;
		double squareProduct = 1 - ( 1 - dotProduct * dotProduct ) * refractivity * refractivity;
		if ( squareProduct <= 0 )
			// Not refracted
			return null;
		
		double scale = refractivity * dotProduct - Math.sqrt(squareProduct);
		normalToUse.scale(scale);
		
		direction.add(thisDir);
		direction.scale(refractivity);
		normalToUse.add(direction);
		
		normalToUse.normalize();
		return normalToUse;
	}

}
