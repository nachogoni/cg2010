package ar.edu.itba.cg.tpe2.core.shader;

import java.awt.Color;
import java.util.List;

import javax.vecmath.Point3f;

import ar.edu.itba.cg.tpe2.core.geometry.Primitive;
import ar.edu.itba.cg.tpe2.core.geometry.Ray;
import ar.edu.itba.cg.tpe2.core.light.Light;

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

	public Color getColorAt(Point3f aPoint, Primitive primitive, List<Light> lights, Ray viewRay) {
		return color;
	}

}
