package ar.edu.itba.cg.tpe2.core.shader;

import java.awt.Color;
import java.util.List;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.core.colors.Diffuse;
import ar.edu.itba.cg.tpe2.core.colors.Specular;
import ar.edu.itba.cg.tpe2.core.geometry.Primitive;
import ar.edu.itba.cg.tpe2.core.light.Light;

public class Phong extends Shader {

	private Diffuse diffuse;
	private int samples;
	private Specular spec;
	
	public Phong(String name, String type, Diffuse diffuse, int samples, Specular spec) {
		super(name, type);
		this.diffuse = diffuse;
		this.samples = samples;
		this.spec = spec;
	}
	
	public Diffuse getDiffuse() {
		return diffuse;
	}

	public int getSamples() {
		return samples;
	}

	public Specular getSpec() {
		return spec;
	}

	@Override
	public String toString() {
		return "Phong [samples=" + samples + ", spec=" + spec + ", texture="
				+ diffuse + ", getName()=" + getName() + ", getType()="
				+ getType() + "]";
	}

	@Override
	public Color getColorAt(Point3d aPoint, Primitive primitive, List<Light> lights) {
		Color colorAt = diffuse.getColorAt(aPoint,primitive);
		Color color = spec.getColor();
		Integer specularity = spec.getSpecularity();
		Integer diffusity = 1;
		return diffuse.getColorAt(aPoint,primitive);
	}
	
}
