package ar.edu.itba.cg.tpe2.core.shader;

import java.awt.Color;
import java.util.List;

import javax.vecmath.Point3f;

import ar.edu.itba.cg.tpe2.core.colors.Specular;
import ar.edu.itba.cg.tpe2.core.geometry.Primitive;
import ar.edu.itba.cg.tpe2.core.geometry.Ray;
import ar.edu.itba.cg.tpe2.core.light.Light;

public class Glass extends Shader {

	private float eta, abs_dist;
	private Specular color, abs_color;
	public Glass(String name, String type, float eta, float absDist,
			Specular color, Specular absColor) {
		super(name, type);
		this.eta = eta;
		abs_dist = absDist;
		this.color = color;
		abs_color = absColor;
	}
	
	public float getEta() {
		return eta;
	}
	
	public float getAbs_dist() {
		return abs_dist;
	}
	public Specular getColor() {
		return color;
	}
	public Color getAbsColor() {
		return abs_color.getColor();
	}
	@Override
	public String toString() {
		return "Glass [abs_color=" + abs_color + ", abs_dist=" + abs_dist
				+ ", color=" + color + ", eta=" + eta + ", getName()="
				+ getName() + ", getType()=" + getType() + "]";
	}
	@Override
	public Color getColorAt(Point3f aPoint, Primitive primitive, List<Light> lights, Ray viewRay) {
		return color.getColor();
	}

	public float getRefractionK(){
		return 1.0f;
	}
}
