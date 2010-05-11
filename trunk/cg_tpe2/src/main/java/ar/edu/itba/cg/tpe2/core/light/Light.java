package ar.edu.itba.cg.tpe2.core.light;

public abstract class Light {

	public enum ELightType { point }

	private ELightType type;
	private String name;
	private double power;
	
	public Light(ELightType type, String name, double power) {
		this.type = type;
		this.name = name;
		this.power = power;
	}

	public double getPower() {
		return power;
	}
	
	@Override
	public String toString() {
		return super.toString()+"\nLight [name=" + name + ", type=" + type + "]";
	}
	
}
