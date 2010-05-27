 package ar.edu.itba.cg.tpe2.core.geometry;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import ar.edu.itba.cg.tpe2.core.shader.Shader;

public class Plane extends Primitive {

	Point3f p1;
	Vector3f n;
	Vector3f vu = null;
	Vector3f vv = null;

	Transform transform;

    private static final float DISTANCE_TOLE  = 0.00000000000001f;

	private static final float TEXTURE_BLOCK  = 10f;
	
	public Plane(String name, Shader shader, Point3f p1, Vector3f n, Transform trans) throws IllegalArgumentException{
		super(name,shader);
		if ( n.equals(new Vector3()) )
			throw new IllegalArgumentException("Normal of a plane shouldn't be the 0 vector");
		
		this.n = n;
		this.p1 = p1;
		
		this.transform = trans;
		
		if(this.transform != null){
			// Debo correr la transformacion
			this.transform.applyTransform(this);
		}
	}
	
	public Plane(String name, Shader shader, Point3f p1, Point3f p2, Point3f p3, Transform trans) throws IllegalArgumentException {
		super(name,shader);
		Vector3 normal = new Vector3();	
		normal.cross(new Vector3(p1,p2), new Vector3(p1,p3));
		if ( normal.equals(new Vector3()) )
			throw new IllegalArgumentException("Normal of a plane shouldn't be the 0 vector");
		
		this.n = normal;
		this.p1 = p1;
		
		this.transform = trans;
		
		if(this.transform != null){
			// Debo correr la transformacion
			this.transform.applyTransform(this);
		}
	}
		
	public Point3f intersect(Ray ray) {

		// ray direction vector
		Vector3 dir = new Vector3(ray.getDirection());
	    
		Vector3 w0 = new Vector3(p1,ray.getOrigin());
		
		float a = -n.dot(w0);
		float b = n.dot(dir);
		
        // Check if ray is parallel to triangle plane
	    if ( Math.abs(b) < DISTANCE_TOLE ) {
	    	// Ray lies in triangle plane ( Determinant is near zero )
	        if ( a < DISTANCE_TOLE && a > -DISTANCE_TOLE )
	            return null;
	        // Ray disjoint from plane
	        else
	        	return null;
	    }
	    
	    float r = a/b;
	    
	    // Check if ray goes away from triangle
	    if ( r < 0.0 )
	    	return null;
	    
	    Point3f intersectionPoint = new Point3f ( ray.getOrigin() );
	    dir.scale(r);
	    intersectionPoint.add(dir);
	    return intersectionPoint;
	}
		
	@Override
	public float[] getUV(Point3f point) {
	
    	Point3f p = new Point3f(point);
    	
    	p.sub(p1);

    	if((vu == null) || (vv == null)) {
    		vv = new Vector3f(p);
            vu = new Vector3f();
            vu.cross(vv, n);
            vu.normalize();
            vv.normalize();
        }
        
        Vector3f p1p = new Vector3f(p);
        
        float u = (p1p.dot(vu));
        float v = (p1p.dot(vv));
        
		u = ((u % TEXTURE_BLOCK) / TEXTURE_BLOCK);
		v = ((v % TEXTURE_BLOCK) / TEXTURE_BLOCK);
		
		if (v < 0)
			v *= -1;
		if (u < 0)
			u *= -1;
		
		return new float[]{u,v};
	}

	@Override
	public Vector3 getNormalAt(Point3f p, Point3f from) {
		return new Vector3(n);
	}

	@Override
	public void transformWith(Matrix4f m) {
		m.transform(p1);
		m.transform(n);
	}

	@Override
	public String toString() {
		return "Plane [n=" + n + ", p1=" + p1 + "]";
	}

	@Override
	public float[] getBoundaryPoints() {
		//TODO
		return null;
	}

	@Override
	public Point3f getReferencePoint() {
		return new Point3f(p1);
	}

}
