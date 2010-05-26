package ar.edu.itba.cg_final.controller.player;

import com.jme.math.Vector3f;

public class Human extends Player {

	public Human(String name) {
		super(name);
	}

	public Human(String name, float acceleration, float maxSpeed, float minSpeed, Vector3f position, Vector3f direction) {
		super(name, acceleration, maxSpeed, minSpeed, position, direction);
	}
	
}
