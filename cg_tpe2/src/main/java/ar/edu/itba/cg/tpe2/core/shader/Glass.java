package ar.edu.itba.cg.tpe2.core.shader;

import ar.edu.itba.cg.tpe2.core.geometry.Specification;

public class Glass extends Shader {

	private double eta, abs_dist;
	private Specification color, abs_color;
	public Glass(String name, String type, double eta, double absDist,
			Specification color, Specification absColor) {
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
	public Specification getColor() {
		return color;
	}
	public Specification getAbs_color() {
		return abs_color;
	}
	@Override
	public String toString() {
		return "Glass [abs_color=" + abs_color + ", abs_dist=" + abs_dist
				+ ", color=" + color + ", eta=" + eta + ", getName()="
				+ getName() + ", getType()=" + getType() + "]";
	}
	
	
}
