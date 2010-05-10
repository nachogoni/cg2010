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
		
		Point3d dirNormalized = new Point3d();
		double module = Math.sqrt(direction.x*direction.x + direction.y*direction.y +direction.z*direction.z);
		
		
		dirNormalized.set(direction.x/module, direction.y/module, direction.z/module);
		//System.out.println("rayo x:"+dirNormalized.x+", y:"+dirNormalized.y+", z:"+dirNormalized.z);
		this.origin = origin;
		this.direction = dirNormalized;
	}
	
	/**
	 * Constructs a ray at (0,0,0) with the given direction
	 * 
	 * 
	 * @param direction Direction for the ray
	 */
	public Ray(Vector3 direction){
		this.origin = new Point3d();
		this.direction = new Point3d(direction);
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

	public Ray reflectFrom(Vector3 primitiveNormal, Point3d primitivePoint){
		Vector3 rNormal = new Vector3(primitiveNormal);
		double dotProduct = rNormal.dot(this.getDirectionAsVector());
		rNormal.scale(-2*dotProduct);
		rNormal.add(this.direction);
		rNormal.normalize();
		
		Point3d rEnd = new Point3d(primitivePoint);
		rEnd.add(rNormal);
		Ray newRay = new Ray(primitivePoint,rEnd);
		return newRay;
	}

	public Ray refractFrom(Vector3 primitiveNormal, Point3d primitivePoint, double refractivity){
		Vector3 normalToUse = new Vector3(primitiveNormal);
		Vector3 thisDir = this.getDirectionAsVector();
		double dotProduct = thisDir.dot(primitiveNormal);
		
		
		if ( dotProduct < 0 ){
			// Ray enters
			refractivity = 1 / refractivity;
		}else{
			// Ray goes out
			normalToUse.scale(-1.0);
		}
		Vector3 rDir = calculateRefractedDir(normalToUse,thisDir,refractivity);
		
		if ( rDir.equals(new Vector3()) ){
			// No ray is refracted
			return null;
		}
		Point3d rEnd = new Point3d(primitivePoint);
		rEnd.add(rDir);
		Ray newRay = new Ray(primitivePoint,rEnd);
		return newRay;
	}
	
	private Vector3 calculateRefractedDir(Vector3 normalToUse, Vector3 thisDir, double refractivity) {
		Vector3 direction = new Vector3();
		double dotProduct = -normalToUse.dot(thisDir);
		double squareProduct = 1 - ( 1 - dotProduct * dotProduct ) * refractivity * refractivity;
		if ( squareProduct <= 0 )
			// Not refracted
			return direction;
		
		direction.add(thisDir);
		direction.scale(refractivity);
		double scale = refractivity * dotProduct - Math.sqrt(squareProduct);
		normalToUse.scale(scale);
		normalToUse.add(direction);
		normalToUse.normalize();
		return normalToUse;
	}

	private Vector3 getDirectionAsVector() {
		return new Vector3(this.origin, this.direction);
	}
}
