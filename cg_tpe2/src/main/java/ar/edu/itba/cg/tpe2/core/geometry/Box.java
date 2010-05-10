package ar.edu.itba.cg.tpe2.core.geometry;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.core.shader.Shader;

public class Box extends Primitive {

	// Type: Box
	//  
	
	
	public Box(String name, Shader shader) {
		super(name, shader);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public Point3d intersect(Ray ray) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Point3d> getBoundaryPoints() {
		// TODO Auto-generated method stub
		return new ArrayList<Point3d>();
	}

	@Override
	public double[] getUV(Point3d point) {
		return new double[]{0,0};
	}

	@Override
	public Vector3 getNormalAt(Point3d p) {
		// TODO 
		return new Vector3();
	}


	@Override
	public void transformWith(Matrix4d m) {
		// TODO Auto-generated method stub
		
	}

}
