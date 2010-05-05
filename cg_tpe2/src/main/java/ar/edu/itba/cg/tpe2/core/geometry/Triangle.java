package ar.edu.itba.cg.tpe2.core.geometry;

import java.awt.Color;
import java.util.List;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.core.shader.Shader;

/**
 * Ray Intersection algorithm based on http://softsurfer.com/Archive/algorithm_0105/algorithm_0105.htm 
 *
 */
public class Triangle extends Primitive {

	private static final double DISTANCE_TOLE  = 0.00000000000001;
	Point3d p1, p2, p3;
	
	Vector3 u, v, n;
	
	double uu, uv, vv;
	
	Color color = null;
	
	public Triangle(String name, Shader shader, Point3d p1, Point3d p2, Point3d p3) throws IllegalArgumentException {
		super(name,shader);
		u = new Vector3(p1,p2);
		v = new Vector3(p1,p3);
		n = new Vector3();
		n.cross(u, v);
		if ( n.equals(new Vector3()) )
			// Triangle is either a segment or a point
			throw new IllegalArgumentException("Triangle is either a segment or a point");
	    uu = u.dot(u);
	    uv = u.dot(v);
	    vv = v.dot(v);
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}
	
	public Triangle(String name, Shader shader, Point3d p1, Point3d p2, Point3d p3, Color color) throws IllegalArgumentException {
		this(name, shader, p1,p2,p3);
		this.color = color;
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
	    
	    Vector3 w = new Vector3(p1,intersectionPoint);
	    
	    if ( ! containsPoint(w.dot(u),w.dot(v)) )
	    	return null;
	    return intersectionPoint;
	}
	
	private boolean containsPoint(double wu, double wv) {
		double D;
	    D = uv * uv - uu * vv;

	    // Get and test parametric coordinates
	    double s, t;
	    s = (uv * wv - vv * wu) / D;
	    t = (uv * wu - uu * wv) / D;
	    if(Math.abs(s) < 0.00001) s = 0;
	    if(Math.abs(t) < 0.00001) t = 0;
	    // Check if is outside the Triangle
	    if (s < 0.0 || s > 1.0 || t < 0.0 || (s + t) > (1.0 + 0.00001))
	        return false;

	    return true;
	}

	public Color getColor(Point3d point) {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color; 
	}
	
	public String toString() {
		return "( "+p1+" , "+p2+" , "+p3+" ; "+color.toString()+" )";
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
