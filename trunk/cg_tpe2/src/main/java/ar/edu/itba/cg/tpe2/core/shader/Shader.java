package ar.edu.itba.cg.tpe2.core.shader;

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
