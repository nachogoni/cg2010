package ar.edu.itba.cg.tpe2.core.colors;

import java.awt.Color;

public class Specular {

	private String color_type;
	private Color color;
	private Integer specularity;
	
	public Specular(){
		
	}
	
	public Specular(String color_type, Color color){
		this.color_type = color_type;
		this.color = color;
	}
	
	public Specular(String color_type, float a, float b, float c){
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
		return "Specular [aPoint=" + color + ", color_type=" + color_type
				+ ", specularity=" + specularity + "]";
	}
	
	

}

