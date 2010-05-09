package ar.edu.itba.cg.tpe2.core.geometry;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
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

	BufferedImage img;
	Point3d radiusCenter;
	double radius;
	
	public Sphere(String name, Shader shader, Point3d radiusCenter, double radius) throws IllegalArgumentException {
		super(name,shader);

		if (radius <= 0)
			throw new IllegalArgumentException("Invalid radius");
		this.radius = radius;
		this.radiusCenter= radiusCenter;
		
		
		try {
			
			File input = new File("earth.jpg");
			img = ImageIO.read(input);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
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
	/*
	public Color getColor(Point3d point) {
		
		Point3d p = new Point3d(point);
		
		// Get p into sphere coordinates
		p.sub(radiusCenter);
		
		// Get uv coordinates
		double u = p.x / Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.y, 2) + Math.pow(p.z, 2));
		double v = p.y / Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.y, 2) + Math.pow(p.z, 2));
		
		if (u > 1 || u < -1)
			System.out.println(u);
		u++;
		u/=2;
		
		Color color = new Color(img.getRGB((int)(u * img.getWidth()), (int)(v * img.getHeight())/2 + img.getHeight() / 2));
		
		return color;
	}
	
	}*/
	
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
		return new ArrayList<Point3d>();
	}

	@Override
	public double[] getUV(Point3d p) {
		Point3d point = new Point3d(p);
		
		// Get p into sphere coordinates
		point.sub(radiusCenter);
		
		// Get uv coordinates
		double u = point.x / Math.sqrt(Math.pow(point.x, 2) + Math.pow(point.y, 2) + Math.pow(point.z, 2));
		double v = point.y / Math.sqrt(Math.pow(point.x, 2) + Math.pow(point.y, 2) + Math.pow(point.z, 2));
		
		if (u > 1 || u < -1)
			System.out.println(u);
		u++;
		u/=2;
		
		return new double[]{u,v};
	}
}
