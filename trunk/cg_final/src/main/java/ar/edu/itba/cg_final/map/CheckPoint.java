package ar.edu.itba.cg_final.map;

import ar.edu.itba.cg_final.map.action.Action;


public class CheckPoint {

	String name;
	CheckPoint next = null;
	Action action = null;
	
	public CheckPoint(String name) {
		this.name = name;
	}
	
	public CheckPoint(String name, CheckPoint nextCheckPoint) {
		this(name);
		this.next = nextCheckPoint;
	}
	
	public String getName() {
		return this.name;
	}
	
	public CheckPoint getNext() {
		return this.next;
	}

	public void setAction(Action newAction) {
		this.action = newAction;
	}
	
	public void performAction() {
		if (this.action != null)
			action.performAction();
	}
	
}
