package ar.edu.itba.cg.tpe2.core.light;

public abstract class Light {

	// Type
	// Name
	
	
	// Implementatios
	
	// Type: Point
		// Color
		// Power
		// Point
	
	public enum ELightType { point }

	private ELightType type;
	private String name;
	
	public Light(ELightType type, String name) {
		this.type = type;
		this.name = name;
	}
	
}
