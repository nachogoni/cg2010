package ar.edu.itba.cg.tpe2.core.shader;

import java.awt.Color;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.core.geometry.Primitive;

public abstract class Shader {

	// Name
	// Type
	private String name;
	private String type;
	protected Primitive primitive;
	
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
	
	abstract public Color getColorAt(Point3d aPoint);
	
	public void setPrimitive(Primitive p){
		primitive = p;
	}
	
	// Implementations
	
	// Type: Glass
		// Eta
		// Color
		// Absortion
			// Distance
			// Color
	
	// Type: Phong
		// Spec
		// Samples
		// Texture
		// Diff
	
	// Type: Mirror
		// Refl
	
}
