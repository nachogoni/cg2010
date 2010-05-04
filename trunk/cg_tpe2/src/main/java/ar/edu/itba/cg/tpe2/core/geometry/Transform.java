package ar.edu.itba.cg.tpe2.core.geometry;

import javax.vecmath.Point3d;

public class Transform {
	Point3d translate;
	Double scaleu, scalex, scaley, scalez;
	Integer	rotatex, rotatey, rotatez;
	
	public Transform(){
		
	}
	
	public Point3d getTranslate() {
		return translate;
	}
	public void setTranslate(Point3d translate) {
		this.translate = translate;
	}
	public Double getScaleu() {
		return scaleu;
	}
	public void setScaleu(Double scaleu) {
		this.scaleu = scaleu;
	}
	public Double getScalex() {
		return scalex;
	}
	public void setScalex(Double scalex) {
		this.scalex = scalex;
	}
	public Double getScaley() {
		return scaley;
	}
	public void setScaley(Double scaley) {
		this.scaley = scaley;
	}
	public Double getScalez() {
		return scalez;
	}
	public void setScalez(Double scalez) {
		this.scalez = scalez;
	}
	public Integer getRotatex() {
		return rotatex;
	}
	public void setRotatex(Integer rotatex) {
		this.rotatex = rotatex;
	}
	public Integer getRotatey() {
		return rotatey;
	}
	public void setRotatey(Integer rotatey) {
		this.rotatey = rotatey;
	}
	public Integer getRotatez() {
		return rotatez;
	}
	public void setRotatez(Integer rotatez) {
		this.rotatez = rotatez;
	}

	@Override
	public String toString() {
		return "Transform [rotatex=" + rotatex + ", rotatey=" + rotatey
				+ ", rotatez=" + rotatez + ", scaleu=" + scaleu + ", scalex="
				+ scalex + ", scaley=" + scaley + ", scalez=" + scalez
				+ ", translate=" + translate + "]";
	}
	
	
	
}
