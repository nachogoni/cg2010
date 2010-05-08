package ar.edu.itba.cg.tpe2.core.shader;

import java.awt.Color;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.core.colors.Diffuse;
import ar.edu.itba.cg.tpe2.core.colors.Specular;

public class Phong extends Shader {

	private Diffuse diffuse;
	private int samples;
	private Specular spec;
	
	public Phong(String name, String type, Diffuse diffuse, int samples,
			Specular spec) {
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
	public Color getColorAt(Point3d aPoint) {
		// TODO!!!
		return diffuse.getColorAt(1, 1);
	}
	
	
	
}
