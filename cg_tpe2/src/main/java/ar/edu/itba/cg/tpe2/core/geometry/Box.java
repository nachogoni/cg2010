package ar.edu.itba.cg.tpe2.core.geometry;

import java.util.ArrayList;
import java.util.List;

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
	public double[] getUV(Point3d p) {
		return new double[]{0,0};
	}

}
