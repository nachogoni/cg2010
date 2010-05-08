package ar.edu.itba.cg.tpe2.core.shader;

import ar.edu.itba.cg.tpe2.core.colors.Specular;

public class Phong extends Shader {

	private String texture;
	private int samples;
	private Specular spec;
	
	public Phong(String name, String type, String texture, int samples,
			Specular spec) {
		super(name, type);
		this.texture = texture;
		this.samples = samples;
		this.spec = spec;
	}
	
	public String getTexture() {
		return texture;
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
				+ texture + ", getName()=" + getName() + ", getType()="
				+ getType() + "]";
	}
	
	
}
