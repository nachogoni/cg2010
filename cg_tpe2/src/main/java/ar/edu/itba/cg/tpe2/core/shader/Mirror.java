package ar.edu.itba.cg.tpe2.core.shader;

import ar.edu.itba.cg.tpe2.core.geometry.Specification;

public class Mirror extends Shader {

	private Specification spec;

	public Mirror(String name, String type, Specification spec) {
		super(name, type);
		this.spec = spec;
	}

	public Specification getSpec() {
		return spec;
	}

	@Override
	public String toString() {
		return "Mirror [spec=" + spec + ", getName()=" + getName()
				+ ", getType()=" + getType() + "]";
	}
	
	
	
}
