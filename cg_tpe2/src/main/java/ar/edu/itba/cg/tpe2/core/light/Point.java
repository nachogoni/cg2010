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
	
}
