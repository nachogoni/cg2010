package ar.edu.itba.cg.tpe2.core.shader;

import ar.edu.itba.cg.tpe2.core.geometry.Specification;

public class Phong extends Shader {

	private String texture;
	private int samples;
	private Specification spec;
	
	@Override
	public String toString() {
		return "Phong [samples=" + samples + ", spec=" + spec + ", texture="
				+ texture + ", getName()=" + getName() + ", getType()="
				+ getType() + "]";
	}

	public String getTexture() {
		return texture;
	}

	public int getSamples() {
		return samples;
	}

	public Specification getSpec() {
		return spec;
	}

	public Phong(String name, String type, String texture, int samples,
			Specification spec) {
		super(name, type);
		this.texture = texture;
		this.samples = samples;
		this.spec = spec;
	}
	
	
	
}
