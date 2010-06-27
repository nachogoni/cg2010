package ar.edu.itba.cg_final.controller;

import com.jme.scene.Skybox;


public class Game {

	private static Game instance;
	
	private Audio audio;
	private Video video;
	private Terrain terrain;
	
	private Skybox skyBox;
	
	//TODO: otras

	private Game() {
		instance = this;
	}
	
	public static Game getInstance() {
		if (instance == null)
			instance = new Game();
		return instance;
	}
	
	//TODO: setters de los objetos cargados por el loader
	
	public void setSkyBox(Skybox skyBox) {
		this.skyBox = skyBox;
	}
	
	public Skybox getSkyBox() {
		return skyBox;
	}
}
