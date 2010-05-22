package ar.edu.itba.cg.tpe2.core.shader;

import ar.edu.itba.cg.tpe2.utils.Noise;

public abstract class ProduralShader extends Shader {

	protected Noise noise;
	
	public ProduralShader(String name, String type, int depth) {
		super(name, type);
		noise = new Noise(depth);
	}
	
}
