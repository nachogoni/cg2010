package ar.edu.itba.cg.tpe2.core.light;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.core.colors.Specular;

public class Point extends Light {

	private Specular aSpec;
	private double power;
	private Point3d p;

	public Point(String name, Specular aSpec, double power, Point3d p) {
		super(ELightType.point, name);
		this.aSpec = aSpec;
		this.power = power;
		this.p = p;
	}

	public Specular getASpec() {
		return aSpec;
	}

	public double getPower() {
		return power;
	}

	public Point3d getP() {
		return p;
	}

	@Override
	public String toString() {
		return "Point [aSpec=" + aSpec + ", p=" + p + ", power=" + power + "]";
	}

}