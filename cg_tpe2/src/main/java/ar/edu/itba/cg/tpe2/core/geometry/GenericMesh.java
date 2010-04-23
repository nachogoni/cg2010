package ar.edu.itba.cg.tpe2.core.geometry;

import java.awt.Color;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.core.shader.Shader;

public class GenericMesh extends Primitive {

	// Type: GenericMesh
	// Point's...
	// Triangle's...
	// Normals?
	// UVS?
	
	public GenericMesh(String name, Shader shader) {
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
	public Color getColor(Point3d point) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
