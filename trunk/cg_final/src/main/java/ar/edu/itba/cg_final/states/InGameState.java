package ar.edu.itba.cg_final.states;

import ar.edu.itba.cg_final.RallyGame;
import ar.edu.itba.cg_final.controller.Audio;
import ar.edu.itba.cg_final.vehicles.Car;

import com.jme.renderer.Camera;
import com.jme.scene.Skybox;
import com.jme.scene.Text;
import com.jme.system.DisplaySystem;
import com.jmex.terrain.TerrainPage;


public class InGameState extends RallyGameState {

	private int width = DisplaySystem.getDisplaySystem().getWidth();
	private int height = DisplaySystem.getDisplaySystem().getHeight();
	
	RallyGame game;
	Skybox sky;
	Car playerCar;
	Audio audio;
	
	public InGameState() {
		this.setName("InGame");
		stateNode.setName(this.getName());
	}
	
	@Override
	public void activated() {
		// Agregamos el stateNode al rootNode
		stateNode.setName(this.getName());
		rootNode.attachChild(this.stateNode);
		rootNode.updateRenderState();

		game = RallyGame.getInstance();
		sky = game.getSkybox();
		playerCar = game.getPlayerCar();
		this.audio = game.getAudio();
		this.audio.play();
	}

	@Override
	public void deactivated() {
		rootNode.detachChild(this.stateNode);
		rootNode.updateRenderState();
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void render(float arg0) {
		//TODO arreglar la condicion de la camara porque anda media chota
		RallyGame game = RallyGame.getInstance();
		Camera cam = game.getCamara();
		TerrainPage terrain = game.getRallyTrack().getTerrain();
        //We don't want the chase camera to go below the world, so always keep 
        //it 2 units above the level.
		final float fix = 130;
        if(cam.getLocation().y < (terrain.getHeight(cam.getLocation())-fix)) {
            cam.getLocation().y =  terrain.getHeight(cam.getLocation())-fix;
            cam.update();
        }
        rootNode.updateGeometricState(arg0, true);
	}

	@Override
	public void update(float arg0) {
//		if (KeyBindingManager.getKeyBindingManager().isValidCommand("post",
//				false)) {
//			GameStateManager.getInstance().deactivateChildNamed(this.getName());
//			GameStateManager.getInstance().activateChildNamed("FinishedGame");
//		}
		
		this.audio.update();
		
		sky.getLocalTranslation().set(game.getCamara().getLocation());
		sky.updateGeometricState(0.0f, true);
		
		// Update speed
		Integer carSpeed = (int)playerCar.getLinearSpeed();
		this.stateNode.detachChildNamed("speed");
		Text speed = Text.createDefaultTextLabel("speed", String.format("%03d", carSpeed));
    	speed.setLocalScale(5);
    	speed.setLocalTranslation((width - (int)(speed.getWidth() * 1.2f)),0, 0);
    	this.stateNode.attachChild(speed);
		
    	// TODO: for degub
    	float [] angles = new float[3];
    	playerCar.getChassis().getLocalRotation().toAngles(angles);
    	this.stateNode.detachChildNamed("carPos");
    	Text pos = Text.createDefaultTextLabel("carPos", String.format("Auto: (%04d,%04d) @ %03f",
    			(int)(playerCar.getChassis().getLocalTranslation().x), 
    			(int)(playerCar.getChassis().getLocalTranslation().z),(angles[1])));
    	pos.setLocalScale(1);
    	pos.setLocalTranslation((width - (int)(pos.getWidth() * 1.2f)), speed.getHeight(), 0);
    	this.stateNode.attachChild(pos);
    	
    	
//		if (game.getRootNode().getChild("check").hasCollision(playerCar, true)) {
//			System.out.println("Choco");
//		}		    	
    	if (game.getRallyTrack().getFence().hasCollision(playerCar, true)){
    		System.out.println("Me la di contra la fence!!!!");
    		this.audio.playOnce("sound/hit.ogg");
    	}
	}


}
