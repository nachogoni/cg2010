package ar.edu.itba.cg.tpe2.core.light;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.core.colors.Specular;

public class PointLight extends Light {

	private Specular aSpec;
	private Point3d p;
	
	public PointLight(String name, Specular aSpec, double power, Point3d p) {
		super(ELightType.point, name,power);
		this.aSpec = aSpec;
		this.p = p;
	}

	public Specular getASpec() {
		return aSpec;
	}

	public Point3d getP() {
		return p;
	}

	@Override
	public String toString() {
		return "Point [aSpec=" + aSpec + ", p=" + p + ", power=" + getPower() + "]";
	}

	
	
	public float getFallOff(double distanceToLight) {
//		(1./(     log(xs.+1).+1    )    ).*100;
		if ( distanceToLight < 0 )
			return 0;
		return (float) ((1/ (Math.log(distanceToLight+1) +1))*getPower());
//		return (float) ((1/ (Math.log(distanceToLight+1) +1)));
	}

}