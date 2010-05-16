package ar.edu.itba.cg.tpe2.core.shader;

import java.awt.Color;
import java.util.List;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.core.geometry.Primitive;
import ar.edu.itba.cg.tpe2.core.geometry.Ray;
import ar.edu.itba.cg.tpe2.core.light.Light;

public abstract class Shader {

	// Name
	// Type
	private String name;
	private String type;
	
	public Shader(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	@Override
	public String toString() {
		return "Shader [name=" + name + ", type=" + type + "]";
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
	
	abstract public Color getColorAt(Point3d aPoint, Primitive primitive, List<Light> lights, Ray viewRay);
	
	public float getReflectionK(){
		return 0.0f;
	}
	
	public float getRefractionK(){
		return 0.0f;
	}
	// Implementations

	public double getEta() {
		return 0;
	}

	public Color getAbsColor() {
		return Color.BLACK;
	}

}
