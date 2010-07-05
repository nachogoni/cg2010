package ar.edu.itba.cg_final.states;
import ar.edu.itba.cg_final.RallyGame;
import ar.edu.itba.cg_final.controller.Audio;
import ar.edu.itba.cg_final.settings.GlobalSettings;
import ar.edu.itba.cg_final.vehicles.Car;

import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.system.DisplaySystem;
import com.jmex.terrain.TerrainPage;

public class InGameState extends RallyGameState {

	private int width = DisplaySystem.getDisplaySystem().getWidth();
	private int height = DisplaySystem.getDisplaySystem().getHeight();
	
	Text timeCheckPoint;
	Text speed;
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
        playerCar.getCarAudio().playRepeatedly("sound/car_neutral.ogg");//TODO
        KeyBindingManager.getKeyBindingManager().set("show menu", KeyInput.KEY_ESCAPE);
		KeyBindingManager.getKeyBindingManager().set("toggle_pause", KeyInput.KEY_P);
        
        // Speedometer
		
		speed = Text.createDefaultTextLabel("speed", String.format("%03d", 000));
    	speed.setTextColor(new ColorRGBA(77.0f/255.0f, 77.0f/255.0f, 1f, 0.95f));
    	speed.setCullHint( Spatial.CullHint.Never );
    	speed.setRenderState( Text.getDefaultFontTextureState() );
		speed.setRenderState( Text.getFontBlend() );
    	speed.setLightCombineMode(LightCombineMode.Off);
    	speed.setLocalScale(5);
    	speed.setLocalTranslation((width - (int)(speed.getWidth() * 1.2f)),0, 0);
    	this.stateNode.attachChild(speed);
    	
    	// Checkpoint Time
    	timeCheckPoint = Text.createDefaultTextLabel("CheckPointTime", "");
    	timeCheckPoint.setLocalScale(1.5f);
    	timeCheckPoint.setLocalTranslation((int)(width/2 - timeCheckPoint.getWidth()/2),
    							(int)(height/2 - timeCheckPoint.getHeight()/2), 0);
    	this.stateNode.attachChild(timeCheckPoint);
    	game.setCheckPointText(timeCheckPoint);
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
		super.update(arg0);
		
		this.audio.update();
		
		sky.getLocalTranslation().set(game.getCamara().getLocation());
		sky.updateGeometricState(0.0f, true);
		
		// Update speed
		StringBuffer speedText = speed.getText();
		speedText.replace(0, speedText.length(),
				String.format("%03d", (int)playerCar.getLinearSpeed()));
    	
    	// TODO: for degub
/*    	float [] angles = new float[3];
    	playerCar.getChassis().getLocalRotation().toAngles(angles);
    	this.stateNode.detachChildNamed("carPos");
    	Text pos = Text.createDefaultTextLabel("carPos", 
    			String.format("Auto: (%04d,%04d,%04d) @ %03.2f Terrain: (%04.2f)",
    			(int)(playerCar.getChassis().getLocalTranslation().x), 
    			(int)(playerCar.getChassis().getLocalTranslation().y), 
    			(int)(playerCar.getChassis().getLocalTranslation().z),(angles[1]),
    			(float)game.getRallyTrack().getTerrain().getHeight(
    			playerCar.getChassis().getLocalTranslation().x,
    			playerCar.getChassis().getLocalTranslation().z)));
    	pos.setLocalScale(1);
    	pos.setLocalTranslation((width - (int)(pos.getWidth() * 1.2f)), speed.getHeight(), 0);
    	this.stateNode.attachChild(pos);*/
    	
    	for (Node node : game.getCheckPointList()) {
    		if (node.hasCollision(playerCar, true)) {
    			game.passThrough(playerCar.getName(), node.getName());
    		}		    	
		}
    	
    	if (game.getRallyTrack().getFence().hasCollision(playerCar, true) ||
    			game.getRallyTrack().getForest().hasCollision(playerCar, true)){
    		GlobalSettings gs = new GlobalSettings();
    		
    		this.audio.playOnce(gs.getProperty("EFFECT.CRASH"));

    	}
	}


}
