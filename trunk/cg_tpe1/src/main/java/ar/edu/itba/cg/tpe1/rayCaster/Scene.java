package ar.edu.itba.cg.tpe1.rayCaster;

import java.util.List;

import ar.edu.itba.cg.tpe1.geometry.Primitive;

public class Scene {

	private List<Primitive> list;
	
	public Scene(String scene) {
		// TODO Auto-generated constructor stub
	}
	
	public Scene(List<Primitive> list) {
		this.list = list;
	}
	
	public List<Primitive> getList() {
		return list;
	}
	
}
