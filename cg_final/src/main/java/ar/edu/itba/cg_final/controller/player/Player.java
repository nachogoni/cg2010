package ar.edu.itba.cg_final.controller.player;

import com.jme.math.Vector3f;


public abstract class Player {

	private String name;
	private float acceleration;
	private float speed;
	private float maxSpeed;
	private float minSpeed;
	private Vector3f position;
	private Vector3f direction;
	
	public Player(String name) {
		this.name = name;
		this.speed = 0.0f;
		this.position = new Vector3f();
		this.direction = new Vector3f();
	}
	
	public Player(String name, float acceleration, float maxSpeed, float minSpeed, Vector3f position, Vector3f direction) {
		this(name);
		this.acceleration = acceleration;
		this.maxSpeed = maxSpeed;
		this.minSpeed = minSpeed;
		this.position = position.clone();
		this.direction = direction.clone();
	}

	public float getSpeed() {
		return this.speed;
	}
	
	public float getAcceleration() {
		return this.acceleration;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
		return;
	}
	
	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
		return;
	}
	
	public String getName() {
		return name;
	}

	public float getMaxSpeed() {
		return this.maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public float getMinSpeed() {
		return this.minSpeed;
	}

	public void setMinSpeed(float minSpeed) {
		this.minSpeed = minSpeed;
	}

	public Vector3f getPosition() {
		return this.position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getDirection() {
		return this.direction;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}
	
}
