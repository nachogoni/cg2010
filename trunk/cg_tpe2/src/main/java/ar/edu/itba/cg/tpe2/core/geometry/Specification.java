package ar.edu.itba.cg.tpe2.core.geometry;

import javax.vecmath.Point3d;

public class Specification {

	private String color_type;
	private Point3d aPoint;
	private Integer specularity;
	
	public Specification(){
		
	}
	
	public Specification(String color_type, Point3d aPoint){
		this.color_type = color_type;
		this.aPoint = aPoint;
	}
	
	public Specification(String color_type, Double a, Double b, Double c){
		this(color_type,new Point3d(a, b, c));
	}

	public String getColorType() {
		return color_type;
	}

	public void setColorType(String colorType) {
		color_type = colorType;
	}

	public Point3d getValue() {
		return aPoint;
	}

	public void setValue(Point3d aPoint) {
		this.aPoint = aPoint;
	}

	public void setSpecularity(Integer specularity) {
		this.specularity = specularity;
	}

	public Integer getSpecularity() {
		return specularity;
	}

	@Override
	public String toString() {
		return "Specification [aPoint=" + aPoint + ", color_type=" + color_type
				+ ", specularity=" + specularity + "]";
	}
	
	

}

