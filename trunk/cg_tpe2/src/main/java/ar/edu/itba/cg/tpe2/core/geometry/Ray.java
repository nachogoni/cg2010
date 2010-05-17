package ar.edu.itba.cg.tpe2.core.geometry;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * Ray representation
 *
 */
public class Ray {
	
	private Point3f origin;
	
	private Vector3f direction;
	
	/**
	 * Constructor for Ray class
	 * 
	 * @param origin Position where from the ray starts 
	 * @param direction Direction vector for the ray
	 */
	
	public Ray(Point3f origin, Vector3f direction) {
		this.origin = origin;
		//System.out.println("rayo x:"+dirNormalized.x+", y:"+dirNormalized.y+", z:"+dirNormalized.z);
		this.direction = direction;
		this.direction.normalize();
	}
	
	/**
	 * Constructor for Ray class
	 * 
	 * @param origin Position where from the ray starts 
	 * @param direction Position where from the ray ends
	 */	
	public Ray(Point3f origin, Point3f end) {
		this.origin = origin;		
		this.direction = new Vector3f(end.x-origin.x,end.y-origin.y,end.z-origin.z);
		this.direction.normalize();
	}
	
	/**
	 * Constructs a ray at (0,0,0) with the given direction
	 * 
	 * 
	 * @param direction Direction for the ray
	 */
	public Ray(Vector3 direction){
		this.origin = new Point3f();
		this.direction = direction;
		this.direction.normalize();
	}
	
	/**
	 * Position where from the ray starts
	 * 
	 * @return Origin point
	 */
	public Point3f getOrigin() {
		return this.origin;
	}

	/**
	 * Set position where from the ray starts
	 * 
	 * @param origin New position
	 */
	public void setOrigin(Point3f origin) {
		this.origin = origin;
	}

	/**
	 * Get ray direction
	 * 
	 * @return Ray direction
	 */
	public Vector3f getDirection() {
		return this.direction;
	}

	/**
	 * Set ray direction
	 * 
	 * @param direction New ray direction
	 */
	public void setDirection(Vector3f direction) {
		this.direction = direction;
		this.direction.normalize();
	}

//	http://www.siggraph.org/education/materials/HyperGraph/raytrace/rtreflec.htm
	
	public Ray reflectFrom(Vector3 primitiveNormal, Point3f primitivePoint){
		Vector3 rNormal = new Vector3(primitiveNormal);
		float dotProduct = rNormal.dot(this.direction);
		rNormal.scale(-2*dotProduct);
		rNormal.add(this.direction);
//		rNormal.normalize();
		
		Point3f rEnd = new Point3f(primitivePoint);
		rEnd.add(rNormal);
		Ray newRay = new Ray(primitivePoint,rEnd);
		return newRay;
	}

//	http://www.cse.ohio-state.edu/~kerwin/refraction.html
//	http://www.yaldex.com/open-gl/ch14lev1sec1.html
	public Ray refractFrom(Vector3 primitiveNormal, Point3f primitivePoint, float eta){
		Vector3f normalToUse = new Vector3f(primitiveNormal);
		float dotProduct = normalToUse.dot(this.direction);
		
		if ( dotProduct < 0 ){
			// Ray enters
			eta = 1 / eta;
		}else{
			// Ray goes out
			normalToUse.scale(-1.0f);
		}
		Vector3f rDir = calculateRefractedDir(normalToUse,this.direction,dotProduct,eta);
		
		if ( rDir == null ){
			// No ray is refracted
			return null;
		}
		Point3f rEnd = new Point3f(primitivePoint);
		rEnd.add(rDir);
		Ray newRay = new Ray(primitivePoint,rEnd);
		return newRay;
	}
	
	private Vector3f calculateRefractedDir(Vector3f normalToUse, Vector3f thisDir, float angle, float refractivity) {
		Vector3f direction = new Vector3f(thisDir);
		
		float sinT2 = ( 1 - angle * angle ) * refractivity * refractivity;
		if ( sinT2 > 1.0 )
			// Not refracted
			return null;
		
		float scale = refractivity + (float)Math.sqrt(1 - sinT2);
		normalToUse.scale(-scale);
		
		direction.scale(refractivity);
		direction.add(normalToUse);
		return direction;
	}

}
