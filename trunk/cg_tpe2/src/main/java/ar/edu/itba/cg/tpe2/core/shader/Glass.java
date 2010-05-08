package ar.edu.itba.cg.tpe2.core.shader;

import ar.edu.itba.cg.tpe2.core.colors.Specular;

public class Glass extends Shader {

	private double eta, abs_dist;
	private Specular color, abs_color;
	public Glass(String name, String type, double eta, double absDist,
			Specular color, Specular absColor) {
		super(name, type);
		this.eta = eta;
		abs_dist = absDist;
		this.color = color;
		abs_color = absColor;
	}
	public double getEta() {
		return eta;
	}
	public double getAbs_dist() {
		return abs_dist;
	}
	public Specular getColor() {
		return color;
	}
	public Specular getAbs_color() {
		return abs_color;
	}
	@Override
	public String toString() {
		return "Glass [abs_color=" + abs_color + ", abs_dist=" + abs_dist
				+ ", color=" + color + ", eta=" + eta + ", getName()="
				+ getName() + ", getType()=" + getType() + "]";
	}
	
	
}
