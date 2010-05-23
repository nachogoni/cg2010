package ar.edu.itba.cg.tpe2.core.geometry;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;

import ar.edu.itba.cg.tpe2.core.shader.Shader;

public class Sphere extends Primitive {

	@Override
	public String toString() {
		return "Sphere [radius=" + radius
				+ ", radiusCenter=" + radiusCenter + ", getName()=" + getName()
				+ ", getShader()=" + getShader() + "]";
	}

	Point3f radiusCenter;
	float radius;
	Transform transform;
	
	public Sphere(String name, Shader shader, Point3f radiusCenter, float radius, Transform trans) throws IllegalArgumentException {
		super(name,shader);

		if (radius <= 0)
			throw new IllegalArgumentException("Invalid radius");
		this.radius = radius;
		this.radiusCenter= radiusCenter;
		
		this.transform = trans;
		
		if(this.transform != null){
			// Debo correr la transformacion
			this.transform.applyTransform(this);
		}
	}
	
	// A = Xd^2 + Yd^2 + Zd^2
	// B = 2 * (Xd * (X0 - Xc) + Yd * (Y0 - Yc) + Zd * (Z0 - Zc))
	// C = (X0 - Xc)^2 + (Y0 - Yc)^2 + (Z0 - Zc)^2 - Sr^2
	
	public Point3f intersect(Ray ray) {
		float xd = ray.getDirection().x;
        float yd = ray.getDirection().y;
        float zd = ray.getDirection().z;
       
        float x0 = ray.getOrigin().x;
        float y0 = ray.getOrigin().y;
        float z0 = ray.getOrigin().z;
       
        float xc = radiusCenter.x;
        float yc = radiusCenter.y;
        float zc = radiusCenter.z;
       
        float a = (xd*xd) +(yd*yd) + (zd*zd);
        float b = 2 * (xd * (x0-xc) + yd*(y0 - yc) + zd * (z0-zc)) ;
        float c = ((x0-xc)*(x0-xc)) + ((y0-yc)*(y0-yc)) + ((z0-zc)*(z0-zc)) - (radius*radius);
       
        float discriminant = (b*b) - 4*c*a;

        if (discriminant < 0) {
            return null;
        }
        
        float rootResult = (float)Math.sqrt(discriminant);
       
        float t0 = (-b + rootResult) / (2 * a);
        float t1 = (-b - rootResult) / (2 * a);
       
        Point3f ret;
        
        if (t1 < 0) {
            ret = t0 < 0 ? null : new Point3f(x0 + xd * t0 ,  y0 + yd * t0,  z0 + zd * t0);
        } else if (t0 < 0) {
            ret = new Point3f(x0 + xd * t1 , y0 + yd * t1, z0 + zd * t1);
        } else {

            float tret = Math.min(t0, t1);
            ret = new Point3f(x0 + xd * tret,  y0 + yd * tret,  z0 + zd * tret);
        }
                       
        return ret;
	}

	@Override
	public float[] getUV(Point3f point) {
		
		Point3f p = new Point3f(point);
		
		// Get p into sphere coordinates
		p.sub(radiusCenter);

		float u = p.x / (float)Math.sqrt((p.x*p.x) + (p.y*p.y) + (p.z*p.z));

		float v = (float)(Math.acos(p.y/radius) / Math.PI);
		
		// Fix range
		u += 1;
		u /= 4;

		return new float[]{u,v};
	}

	@Override
	public Vector3 getNormalAt(Point3f p, Point3f from) {
		
		Vector3 v = new Vector3(p, radiusCenter);

		v.scale(-1);
		
		v.normalize();
		
		return v;
	}

	@Override
	public void transformWith(Matrix4f m) {
		// TODO Auto-generated method stub
		m.transform(radiusCenter);	
	}
	
	public void scaleu(float scale){
		this.radius *= scale;
	}
	
	public void scalex(float scale){
		this.radius *= scale;
	}
	@Override
	public float[] getBoundaryPoints() {
		float [] extremes = {Math.min(radiusCenter.x-radius, radiusCenter.x+radius),
				Math.max(radiusCenter.x-radius, radiusCenter.x+radius),
				Math.min(radiusCenter.y-radius, radiusCenter.y+radius),
				Math.max(radiusCenter.y-radius, radiusCenter.y+radius),
				Math.min(radiusCenter.z-radius, radiusCenter.z+radius),
				Math.max(radiusCenter.z-radius, radiusCenter.z+radius)}; 
		return extremes;
	}
	
	public void scaley(float scale){
		this.radius *= scale;
	}
	
	public void scalez(float scale){
		this.radius *= scale;
	}
	
	@Override
	public Point3f getReferencePoint() {
		return new Point3f(this.radiusCenter);
	}
}
