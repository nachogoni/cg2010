package ar.edu.itba.cg.tpe2.core.geometry;

import java.awt.Color;

public class Specification {

	private String color_type;
	private Color color;
	private Integer specularity;
	
	public Specification(){
		
	}
	
	public Specification(String color_type, Color color){
		this.color_type = color_type;
		this.color = color;
	}
	
	public Specification(String color_type, float a, float b, float c){
		this(color_type,new Color(a, b, c));
	}

	public String getColorType() {
		return color_type;
	}

	public void setColorType(String colorType) {
		color_type = colorType;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color c) {
		this.color = c;
	}

	public void setSpecularity(Integer specularity) {
		this.specularity = specularity;
	}

	public Integer getSpecularity() {
		return specularity;
	}

	@Override
	public String toString() {
		return "Specification [aPoint=" + color + ", color_type=" + color_type
				+ ", specularity=" + specularity + "]";
	}
	
	

}

