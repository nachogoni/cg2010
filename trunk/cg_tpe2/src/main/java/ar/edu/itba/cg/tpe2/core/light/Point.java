package ar.edu.itba.cg.tpe2.core.light;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.core.geometry.Specification;

public class Point extends Light {

	private Specification aSpec;
	private double power;
	private Point3d p;

	public Point(String name, Specification aSpec, double power, Point3d p) {
		super(ELightType.point, name);
		this.aSpec = aSpec;
		this.power = power;
		this.p = p;
	}

	public Specification getASpec() {
		return aSpec;
	}

	public double getPower() {
		return power;
	}

	public Point3d getP() {
		return p;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Specification: ")
			.append(aSpec)
			.append("\nPower: ")
			.append(power)
			.append("\nAt: ")
			.append(p);
		
		return sb.toString();
	}	
}
