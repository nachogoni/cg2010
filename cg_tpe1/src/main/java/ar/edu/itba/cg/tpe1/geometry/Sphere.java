package ar.edu.itba.cg.tpe1.geometry;

import java.awt.Color;

import javax.vecmath.Point3d;

public class Sphere implements Primitive {

	Point3d radiusCenter;
	double radius;
	Color color = null;
	
	public Sphere(Point3d radiusCenter, double radius) throws IllegalArgumentException {
		if (radius <= 0)
			throw new IllegalArgumentException("Invalid radius");
		this.radius = radius;
		this.radiusCenter= radiusCenter;
	}
	
	public Sphere(Point3d radiusCenter, double radius, Color color) throws IllegalArgumentException {
		if (radius <= 0)
			throw new IllegalArgumentException("Invalid radius");
		this.radius = radius;
		this.radiusCenter= radiusCenter;
		this.color = color;
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
		double t0 = (-b + rootResult) / ( 2 *a ); //* a 
		//Ri = [xi, yi, zi] = [x0 + xd * ti ,  y0 + yd * ti,  z0 + zd * ti]
		
		Point3d ret;
		
		if (t0 > 0) {
			ret = new Point3d(x0 + xd * t0 ,  y0 + yd * t0,  z0 + zd * t0);
		} else {
			double t1 = (-b - rootResult) / ( 2 *a );//* a
			ret = new Point3d(x0 + xd * t1 ,  y0 + yd * t1,  z0 + zd * t1);
		}
						
		return ret;
	}
	
	public Color getColor(Point3d point) {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color; 
	}
	
}
