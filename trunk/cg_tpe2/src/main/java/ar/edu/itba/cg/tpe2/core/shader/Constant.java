package ar.edu.itba.cg.tpe2.core.shader;

import java.awt.Color;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.core.geometry.Primitive;

public class Constant extends Shader {

	Color color;
	
	public Constant(String name, String type, float r, float g, float b) {
		super(name, type);
		this.color = new Color(r,g,b);
	}
	
	public Constant(String name, String type, Color color) {
		super(name, type);
		this.color = color;
	}

	@Override
	public Color getColorAt(Point3d point, Primitive primitive) {
		return color;
	}

}
