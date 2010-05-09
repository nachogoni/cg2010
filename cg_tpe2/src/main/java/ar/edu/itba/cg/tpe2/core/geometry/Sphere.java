package ar.edu.itba.cg.tpe2.core.geometry;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.core.shader.Shader;

public class Sphere extends Primitive {

	// Type: Sphere
	// Center
	// Radious
	
	
	@Override
	public String toString() {
		return "Sphere [radius=" + radius
				+ ", radiusCenter=" + radiusCenter + ", getName()=" + getName()
				+ ", getShader()=" + getShader() + "]";
	}

	Point3d radiusCenter;
	double radius;
	Transform transform;
	
	public Sphere(String name, Shader shader, Point3d radiusCenter, double radius, Transform trans) throws IllegalArgumentException {
		super(name,shader);

		if (radius <= 0)
			throw new IllegalArgumentException("Invalid radius");
		this.radius = radius;
		this.radiusCenter= radiusCenter;
		
		this.transform = trans;
	}
	
	// A = Xd^2 + Yd^2 + Zd^2
	// B = 2 * (Xd * (X0 - Xc) + Yd * (Y0 - Yc) + Zd * (Z0 - Zc))
	// C = (X0 - Xc)^2 + (Y0 - Yc)^2 + (Z0 - Zc)^2 - Sr^2
	
	public Point3d intersect(Ray ray) {
		double xd = ray.getDirection().x;
        double yd = ray.getDirection().y;
        double zd = ray.getDirection().z;
       
        double x0 = ray.getOrigin().x;
        double y0 = ray.getOrigin().y;
        double z0 = ray.getOrigin().z;
       
        double xc = radiusCenter.x;
        double yc = radiusCenter.y;
        double zc = radiusCenter.z;
       
        double a = Math.pow(xd,2) + Math.pow(yd,2) + Math.pow(zd,2);
        double b = 2 * (xd * (x0-xc) + yd*(y0 - yc) + zd * (z0-zc)) ;
        double c = Math.pow(x0-xc,2) + Math.pow(y0-yc,2) + Math.pow(z0-zc,2) - Math.pow(radius,2);
       
        double discriminant = Math.pow(b, 2) - 4*c*a;
        //System.out.println("pos="+4*c*a+"sq="+Math.pow(b, 2));
        //System.out.println("rayo"+a);
        if (discriminant < 0) {
            return null;
        }
       
        double rootResult = Math.sqrt( discriminant );
       
        double t0 = (-b + rootResult) / ( 2 *a );
        double t1 = (-b - rootResult) / ( 2 *a );
       
        Point3d ret;
        
        if (t1 < 0) {
            ret = t0 < 0 ? null : new Point3d(x0 + xd * t0 ,  y0 + yd * t0,  z0 + zd * t0);
        } else if (t0 < 0) {
            ret = new Point3d(x0 + xd * t1 ,  y0 + yd * t1,  z0 + zd * t1);
        } else {

            double tret = Math.min(t0, t1);
            ret = new Point3d(x0 + xd * tret,  y0 + yd * tret,  z0 + zd * tret);
        }
                       
        return ret;
	}

	@Override
	public List<Point3d> getBoundaryPoints() {
		ArrayList<Point3d> ret =new ArrayList<Point3d>();
		ret.add(new Point3d(radiusCenter.x+radius,radiusCenter.y,radiusCenter.z));
		ret.add(new Point3d(radiusCenter.x,radiusCenter.y+radius,radiusCenter.z));
		ret.add(new Point3d(radiusCenter.x,radiusCenter.y,radiusCenter.z+radius));
		ret.add(new Point3d(radiusCenter.x-radius,radiusCenter.y,radiusCenter.z));
		ret.add(new Point3d(radiusCenter.x,radiusCenter.y-radius,radiusCenter.z));
		ret.add(new Point3d(radiusCenter.x,radiusCenter.y,radiusCenter.z-radius));
		return ret;
	}

	@Override
	public double[] getUV(Point3d point) {
		
		Point3d p = new Point3d(point);
		
		// Get p into sphere coordinates
		p.sub(radiusCenter);

		double u = p.x / Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.y, 2) + Math.pow(p.z, 2));

		double v = Math.acos(p.y/radius) / Math.PI;
		
		// Fix range
		u += 1;
		u /= 4;

		return new double[]{u,v};
	}

	@Override
	public Vector3 getNormalAt(Point3d p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void transformWith(Matrix4d m) {
		// TODO Auto-generated method stub
		
	}
}
