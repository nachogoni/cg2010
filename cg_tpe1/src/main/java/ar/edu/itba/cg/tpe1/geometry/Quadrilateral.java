package ar.edu.itba.cg.tpe1.geometry;

import java.awt.Color;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Quadrilateral implements Primitive {
	
	private Point3d p1, p2, p3, p4;
	private Color aColor;

	// Plane equation values 0 = Ax+By+Cz+D
	private double A, B, C, D;
	
	public Quadrilateral(Point3d p1, Point3d p2, Point3d p3, Point3d p4){
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
	}
	
	public Quadrilateral(Point3d p1, Point3d p2, Point3d p3, Point3d p4, Color aColor){
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
		this.aColor = aColor;
	}

	
	public Point3d intersect(Ray ray) {
		
		Point3d R_0, R_d;
		
		//First, we get the equation of the plane
		this.A = this.p1.y*(this.p2.z - this.p3.z) + this.p2.y*(this.p3.z - this.p1.z) + this.p3.y*(this.p1.z - this.p2.z);
		this.B = this.p1.z*(this.p2.x - this.p3.x) + this.p2.z*(this.p3.x - this.p1.x) + this.p3.z*(this.p1.x - this.p2.x);
		this.C = this.p1.x*(this.p2.y - this.p3.y) + this.p2.x*(this.p3.y - this.p1.y) + this.p3.x*(this.p1.y - this.p2.y);
		this.D = -this.p1.x*(this.p2.y*this.p3.z - this.p3.y*this.p2.z) - 
					this.p2.x*(this.p3.y*this.p1.z - this.p1.y*this.p3.z) - 
					this.p3.x*(this.p1.y*this.p2.z - this.p2.y*this.p1.z);
		
		//System.out.println("Ax + By + Cz + D = " + this.A + "x + " + this.B + "y + " + this.C + "z + " + this.D);
		//Now, we calculate if the Ray (R(t) = R_0 + t*R_d) intersects the given plane
		R_0 = ray.getOrigin();
		R_d = ray.getDirection();
		
		double t = -(this.A*R_0.x + this.B*R_0.y + this.C*R_0.z + D) / 
					(this.A*R_d.x + this.B*R_d.y + this.C*R_d.z);
		
		if(t < 0)
			return null;
		
		//Now we calculate in which point it intersects
		Point3d intersectionPoint = new Point3d(R_0.x + t*R_d.x, R_0.y + t*R_d.y, R_0.z + t*R_d.z);
		
//		//Now we check if it's inside the Quadrilateral
//		Vector3d v_1, v_3, v_4, v_5;
//		v_1 = new Vector3d(this.p2.x - this.p1.x, this.p2.y - this.p1.y, this.p2.z - this.p1.z);
//		v_3 = new Vector3d(this.p4.x - this.p3.x, this.p4.y - this.p3.y, this.p4.z - this.p3.z);
//		v_4 = new Vector3d(intersectionPoint.x - this.p1.x, intersectionPoint.y - this.p1.y, intersectionPoint.z - this.p1.z);
//		v_5 = new Vector3d(intersectionPoint.x - this.p3.x, intersectionPoint.y - this.p3.y, intersectionPoint.z - this.p3.z);
//	
//		//We normalize the vectors
//		v_1.normalize();
//		v_3.normalize();
//		v_4.normalize();
//		v_5.normalize();
//		
//		//We calculate dot products
//		double v1_4, v3_5;
//		v1_4 = v_1.dot(v_4);
//		v3_5 = v_3.dot(v_5);
//		
//		if(v1_4 > 0 && v3_5 > 0 ){
//			System.out.printf("Intercepto: " + intersectionPoint.toString() + "\n");
//			return intersectionPoint;
//		}else return null;
		
		Point3d isIn = this.containsPoint(new Vector3(this.p2, this.p1), new Vector3(this.p3, this.p1), new Vector3(intersectionPoint, this.p1));
		if(isIn == null){
			//We check the other half
			isIn = this.containsPoint(new Vector3(this.p4, this.p1), new Vector3(this.p3, this.p1), new Vector3(intersectionPoint, this.p1));
		}
		
		return isIn;
		
	}
		
	public Color getColor(Point3d point) {
		// TODO Auto-generated method stub
		return this.aColor;
	}
	
	public void setColor(Color color) {
		// TODO Auto-generated method stub
		this.aColor = color;
		
	}
	
	private Point3d containsPoint(Vector3 u, Vector3 v, Vector3 w) {
		double uu, uv, vv, wu, wv, D;
	    uu = u.dot(u);
	    uv = u.dot(v);
	    vv = v.dot(v);
	    wu = w.dot(u);
	    wv = w.dot(v);
	    D = uv * uv - uu * vv;

	    // Get and test parametric coordinates
	    double s, t;
	    s = (uv * wv - vv * wu) / D;
	    t = (uv * wu - uu * wv) / D;
        // Check if is outside the Triangle
	    if (s < 0.0 || s > 1.0 || t < 0.0 || (s + t) > 1.0 )
	        return null;

	    Point3d insidePoint = (Point3d) p1.clone();
	    u.scale(s);
	    v.scale(t);
	    insidePoint.add(u);
	    insidePoint.add(v);
	    return insidePoint;
	}
	
}
