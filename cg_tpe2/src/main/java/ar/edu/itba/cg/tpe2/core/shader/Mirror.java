package ar.edu.itba.cg.tpe2.core.shader;

import ar.edu.itba.cg.tpe2.core.geometry.Specular;

public class Mirror extends Shader {

	private Specular spec;

	public Mirror(String name, String type, Specular spec) {
		super(name, type);
		this.spec = spec;
	}

	public Specular getSpec() {
		return spec;
	}

	@Override
	public String toString() {
		return "Mirror [spec=" + spec + ", getName()=" + getName()
				+ ", getType()=" + getType() + "]";
	}
	
	
	
}
