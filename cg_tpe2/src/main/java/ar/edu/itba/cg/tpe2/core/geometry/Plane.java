 package ar.edu.itba.cg.tpe2.core.geometry;

import java.awt.Color;
import java.util.List;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.core.shader.Shader;

public class Plane extends Primitive {

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
	private static final double DISTANCE_TOLE  = 0.00000000000001;
	Color color = null;
	
	public Plane(String name, Shader shader, Point3d p1, Vector3 n) throws IllegalArgumentException{
		super(name,shader);
		if ( n.equals(new Vector3()) )
			throw new IllegalArgumentException("Normal of a plane shouldn't be the 0 vector");
		
		this.n = n;
		this.p1 = p1;
	}
	
	public Plane(String name, Shader shader, Point3d p1, Point3d p2, Point3d p3) throws IllegalArgumentException {
		super(name,shader);
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

		// ray direction vector
		Vector3 dir = new Vector3(ray.getOrigin(),destiny);
	    
		Vector3 w0 = new Vector3(p1,ray.getOrigin());
		
		double a = -n.dot(w0);
		double b = n.dot(dir);
		
        // Check if ray is parallel to triangle plane
	    if ( Math.abs(b) < DISTANCE_TOLE ) {
	    	// Ray lies in triangle plane ( Determinant is near zero )
	        if ( a < DISTANCE_TOLE && a > -DISTANCE_TOLE )
	            return null;
	        // Ray disjoint from plane
	        else
	        	return null;
	    }
	    
	    double r = a/b;
	    
	    // Check if ray goes away from triangle
	    if ( r < 0.0 )
	    	return null;
	    
	    Point3d intersectionPoint = new Point3d ( ray.getOrigin() );
	    dir.scale(r);
	    intersectionPoint.add(dir);
	    return intersectionPoint;
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
	
	@Override
	public void rotatex(double angle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotatey(double angle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotatez(double angle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scaleu(double scale) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scalex(double scale) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scaley(double scale) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scalez(double scale) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Point3d> getBoundaryPoints() {
		// TODO Auto-generated method stub
		return null;
	}




}
