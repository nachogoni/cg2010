package ar.edu.itba.cg_final.states;

import ar.edu.itba.cg_final.RallyGame;
import ar.edu.itba.cg_final.vehicles.Car;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Skybox;
import com.jme.scene.Text;
import com.jme.system.DisplaySystem;
import com.jmex.audio.AudioSystem;
import com.jmex.terrain.TerrainPage;


public class InGameState extends RallyGameState {

	private Car playerCar;
	private int width = DisplaySystem.getDisplaySystem().getWidth();
	private int height = DisplaySystem.getDisplaySystem().getHeight();
	
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
		AudioSystem.getSystem().getMusicQueue().play();
		playerCar = (Car)rootNode.getChild("PlayerCar");
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
		TerrainPage terrain = game.getTerrain();
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
		RallyGame game = RallyGame.getInstance();
		Skybox sky = game.getSkybox();
		AudioSystem.getSystem().update();
		
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
    	this.stateNode.detachChildNamed("carPos");
    	Text pos = Text.createDefaultTextLabel("carPos", String.format("Auto: (%04d,%04d)",
    			(int)(playerCar.getChassis().getLocalTranslation().x), 
    			(int)(playerCar.getChassis().getLocalTranslation().z)));
    	pos.setLocalScale(1);
    	pos.setLocalTranslation((width - (int)(pos.getWidth() * 1.2f)), speed.getHeight(), 0);
    	this.stateNode.attachChild(pos);
    	
	}


}
