package ar.edu.itba.cg.tpe2.core.shader;

import ar.edu.itba.cg.tpe2.core.colors.Diffuse;
import ar.edu.itba.cg.tpe2.utils.Noise;

public abstract class ProduralShader extends Shader {

	protected Noise noise;
	protected Diffuse finalColor;
	protected Diffuse initialColor;
	
	public ProduralShader(String name, String type, int depth, Diffuse initialColor, Diffuse finalColor) {
		super(name, type);
		noise = new Noise(depth);
		this.initialColor = initialColor;
		this.finalColor = finalColor;
	}
	
}
