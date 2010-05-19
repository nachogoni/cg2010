package ar.edu.itba.cg.tpe2.core.light;

import javax.vecmath.Point3f;

import ar.edu.itba.cg.tpe2.core.colors.Specular;

public class PointLight extends Light {

	private Specular aSpec;
	private Point3f p;
	
	public PointLight(String name, Specular aSpec, float power, Point3f p) {
		super(ELightType.point, name,power);
		this.aSpec = aSpec;
		this.p = p;
	}

	public Specular getASpec() {
		return aSpec;
	}

	public Point3f getP() {
		return p;
	}

	@Override
	public String toString() {
		return "Point [aSpec=" + aSpec + ", p=" + p + ", power=" + getPower() + "]";
	}

	public float getFallOff(float distanceToLight) {
		if ( distanceToLight < 0 )
			distanceToLight = -distanceToLight;
		if ( distanceToLight > getPower() )
			return 0;
		return 1 - distanceToLight/getPower();
	}

}