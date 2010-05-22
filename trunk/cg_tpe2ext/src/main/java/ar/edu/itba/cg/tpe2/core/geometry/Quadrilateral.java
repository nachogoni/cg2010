package ar.edu.itba.cg.tpe2.core.geometry;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;

import ar.edu.itba.cg.tpe2.core.shader.Shader;

public class Quadrilateral extends Primitive {
	
	private Point3f p1, p2, p3, p4;

	// Plane equation values 0 = Ax+By+Cz+D
	private float A, B, C, D;
	
	public Quadrilateral(String name, Shader shader, Point3f p1, Point3f p2, Point3f p3, Point3f p4){
		super(name, shader);
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
	}
	
	public Point3f intersect(Ray ray) {
		
		Point3f R_0, R_d;
		
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
		R_d = new Point3f( ray.getDirection());
		
		float t = -(this.A*R_0.x + this.B*R_0.y + this.C*R_0.z + D) / 
					(this.A*R_d.x + this.B*R_d.y + this.C*R_d.z);
		
		if(t < 0)
			return null;
		
		//Now we calculate in which point it intersects
		Point3f intersectionPoint = new Point3f(R_0.x + t*R_d.x, R_0.y + t*R_d.y, R_0.z + t*R_d.z);
		
		Point3f isIn = this.containsPoint(new Vector3(this.p1, this.p2), new Vector3(this.p1, this.p3), new Vector3(this.p1, intersectionPoint));
		if(isIn == null){
			//We check the other half
			isIn = this.containsPoint(new Vector3(this.p1, this.p4), new Vector3(this.p1, this.p3), new Vector3(this.p1, intersectionPoint));
			if(isIn == null)
				return null;
		}
		
		
		return isIn;
		
	}
		
	private Point3f containsPoint(Vector3 u, Vector3 v, Vector3 w) {
		float uu, uv, vv, wu, wv, D;
	    uu = u.dot(u);
	    uv = u.dot(v);
	    vv = v.dot(v);
	    wu = w.dot(u);
	    wv = w.dot(v);
	    D = uv * uv - uu * vv;

	    // Get and test parametric coordinates
	    float s, t;
	    s = (uv * wv - vv * wu) / D;
	    t = (uv * wu - uu * wv) / D;
        // Check if is outside the Triangle
	    if (s < 0.0 || s > 1.0 || t < 0.0 || (s + t) > 1.0 )
	        return null;

	    Point3f insidePoint = (Point3f) p1.clone();
	    u.scale(s);
	    v.scale(t);
	    insidePoint.add(u);
	    insidePoint.add(v);
	    
	    if(Float.isNaN(insidePoint.x) || Float.isNaN(insidePoint.y) || Float.isNaN(insidePoint.z)){
			return null;
		}else return insidePoint;
	}
	
	@Override
	public float[] getUV(Point3f point) {
		return new float[]{0,0};
	}

	@Override
	public Vector3 getNormalAt(Point3f p, Point3f from) {
		// TODO 
		return new Vector3();
	}

	@Override
	public void transformWith(Matrix4f m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float[] getBoundaryPoints() {
		// TODO Auto-generated method stub
		return null;
	}

}
