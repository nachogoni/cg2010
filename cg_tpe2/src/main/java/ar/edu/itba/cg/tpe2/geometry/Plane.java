package ar.edu.itba.cg.tpe2.geometry;

import java.awt.Color;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.core.shader.Shader;
import ar.edu.itba.cg.tpe2.geometry.Vector3;

public class Plane implements Primitive {

	// Type: Plane
	// Point
	// Normal
	
	// y
	
	// Type: Plane
	// Point
	// Point
	// Point

	Point3d p1, p2, p3;
	Vector3 u, v, n;
	
	Color color = null;
	
	public Plane(String name, Shader shader, Point3d p1, Vector3 n) throws IllegalArgumentException{
		if ( n.equals(new Vector3()) )
			throw new IllegalArgumentException("Normal of a plane shouldn't be the 0 vector");
		
		this.n = n;
		this.p1 = p1;
	}
	
	public Plane(String name, Shader shader, Point3d p1, Point3d p2, Point3d p3) throws IllegalArgumentException {
		Vector3 normal = new Vector3();	
		normal.cross(new Vector3(p1,p2), new Vector3(p1,p3));
		if ( normal.equals(new Vector3()) )
			throw new IllegalArgumentException("Normal of a plane shouldn't be the 0 vector");
		
		this.n = normal;
		this.p1 = p1;
	}
		
	public Point3d intersect(Ray ray) {
		Point3d destiny = (Point3d) ray.getOrigin().clone();
		destiny.add(ray.getDirection());

		// compute vector that goes from p1 to the end of the ray
		Vector3 r = new Vector3(p1,destiny);
		
		// From http://en.citizendium.org/wiki/Plane_%28geometry%29
		if ( n.dot(r) != 0 )
			// Point not in plane
			return null;
		
	    return ray.getDirection();
	}
		
	@Override
	public Color getColor(Point3d point) {
		return color;
	}

	public void setColor(Color color) {
		this.color = color; 
	}
		
	public String toString() {
		if ( color != null )
			return "( "+p1+" , "+p2+" , "+p3+" ; "+color.toString()+" )";
			
		return "( "+p1+" , "+p2+" , "+p3+" )";
	}
	
}
