package ar.edu.itba.cg_final.states;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;

import ar.edu.itba.cg_final.RallyGame;
import ar.edu.itba.cg_final.controller.Audio;
import ar.edu.itba.cg_final.controller.Audio.soundsEffects;
import ar.edu.itba.cg_final.map.CheckPoint;
import ar.edu.itba.cg_final.settings.GameUserSettings;
import ar.edu.itba.cg_final.settings.GlobalSettings;
import ar.edu.itba.cg_final.utils.ResourceLoader;
import ar.edu.itba.cg_final.vehicles.Car;

import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.input.ChaseCamera;
import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.game.state.GameStateManager;
import com.jmex.terrain.TerrainPage;

public class InGameState extends RallyGameState {

	private int width = DisplaySystem.getDisplaySystem().getWidth();
	private int height = DisplaySystem.getDisplaySystem().getHeight();
	
	public static final String STATE_NAME = "InGame";
	
	Text timeCheckPoint;
	RallyGame game;
	Skybox sky;
	Car playerCar;
	Audio audio;
	InputHandler actions;
	InputHandler backupHanlder;
	
	Quad map;
	Quad carDisk;
	Node needleNode;
	Quad speedometer;
	Node mapCheckpoints;
	private Text gameTimeText;
	Boolean setCameraPos = false;
	Vector3f cameraPos = new Vector3f();
	private Text screenShotText;
	
	
	public InGameState() {
		this.setName(STATE_NAME);
		stateNode.setName(this.getName());
		game = RallyGame.getInstance();
		
		// Variables en pantalla
		createTimer();
	
		createSpeedmeter(GlobalSettings.getInstance(), GameUserSettings.getInstance());

		createCheckpointTime();
		createScreenShotTime();
	}
	
	private void createCheckpointTime() {
    	timeCheckPoint = Text.createDefaultTextLabel("CheckPointTime", "");
    	timeCheckPoint.setTextColor(new ColorRGBA(124.0f/255.0f, 252.0f/255.0f, 0f, 0.95f));
    	timeCheckPoint.setCullHint( Spatial.CullHint.Never );
    	timeCheckPoint.setRenderState( Text.getDefaultFontTextureState() );
    	timeCheckPoint.setRenderState( Text.getFontBlend() );
    	timeCheckPoint.setLightCombineMode(LightCombineMode.Off);
    	timeCheckPoint.setLocalScale(1.5f);
    	timeCheckPoint.setLocalTranslation((int)(width/2 - timeCheckPoint.getWidth()/2),
    							(int)(2*height/3 - timeCheckPoint.getHeight()/2), 0);
	}

	private void createScreenShotTime() {
		screenShotText = Text.createDefaultTextLabel("ScreenShot", "Screenshot!!");
		screenShotText.setTextColor(new ColorRGBA(124.0f/255.0f, 252.0f/255.0f, 0f, 0.95f));
		screenShotText.setCullHint( Spatial.CullHint.Never );
		screenShotText.setRenderState( Text.getDefaultFontTextureState() );
		screenShotText.setRenderState( Text.getFontBlend() );
		screenShotText.setLightCombineMode(LightCombineMode.Off);
		screenShotText.setLocalScale(1.5f);
		screenShotText.setLocalTranslation((int)(width/2 - screenShotText.getWidth()/2),
    							(int)(height/3 - screenShotText.getHeight()/2), 0);
		StringBuffer timeText = screenShotText.getText();
		timeText.replace(0, timeText.length(),"");
	}

	private void createMap(GlobalSettings gs, GameUserSettings gus) {
		// Map
		map = new Quad("map", 99*1.5f, 65*1.5f);
		map.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		map.setLightCombineMode(LightCombineMode.Off);
		map.setLocalTranslation(map.getWidth()/2, map.getHeight()/2, 0f);
		map.updateRenderState();
		TextureState mapts = DisplaySystem.getDisplaySystem().getRenderer()
		.createTextureState();
		
        MinificationFilter minF; 
        MagnificationFilter maxF;
        if ( gus.getHighRes() ) {
            minF = MinificationFilter.Trilinear; 
            maxF = MagnificationFilter.Bilinear;
        } else {
            minF = MinificationFilter.NearestNeighborNoMipMaps; 
            maxF = MagnificationFilter.NearestNeighbor;        	
        }
		
		try {
			mapts.setTexture(TextureManager.loadTexture(ResourceLoader.
					getURL(gs.getProperty("TRACK1.TEXTURE.LOW")),
					minF, maxF, 1.0f, true));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		mapts.setEnabled(true);
		map.setRenderState(mapts);
		map.updateRenderState();		
		
		mapCheckpoints = new Node();
    	// CheckPoint Positions
    	ArrayList<CheckPoint> c = game.getCheckPointList();
    	for (int i = 0; i < c.size(); i++) {
    		Quad cp = new Quad("mapCP"+i, 7, 7);
    		cp.setRenderQueueMode(Renderer.QUEUE_ORTHO);
    		if (i == 0) {
    			cp.setDefaultColor(ColorRGBA.red);
    		} else {
    			cp.setDefaultColor(ColorRGBA.orange);
    		}
    		cp.setLightCombineMode(LightCombineMode.Off);
    		cp.setLocalTranslation(map.getCenter().x, map.getCenter().y, 0f);
    		cp.setLocalTranslation(
    				(map.getWidth() / 2) * c.get(i).get3DPosition().x / 5000 + map.getCenter().x,
    				(map.getHeight() / 2) * c.get(i).get3DPosition().z / 5000 + map.getCenter().y, 0f);
    		cp.updateRenderState();
    		mapCheckpoints.attachChild(cp);
    	}

    	// Map car position
    	carDisk = new Quad("mapCarPos", 5, 5);
    	carDisk.setRenderQueueMode(Renderer.QUEUE_ORTHO);
    	carDisk.setDefaultColor(ColorRGBA.yellow);
    	carDisk.setLightCombineMode(LightCombineMode.Off);
    	carDisk.setLocalTranslation(map.getCenter().x, map.getCenter().y, 0f);
    	carDisk.updateRenderState();
	}

	private void createSpeedmeter(GlobalSettings gs, GameUserSettings gus) {
    	// Speedometer
    	float scale = 0.5f;
		speedometer = new Quad("speedometer", 564, 368);
		speedometer.setDefaultColor(ColorRGBA.white);
		speedometer.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		speedometer.setLightCombineMode(LightCombineMode.Off);
		speedometer.setLocalScale(scale);
		speedometer.setLocalTranslation(width-speedometer.getWidth()*scale/2, 
				speedometer.getHeight()*scale/2, 0f);

        MinificationFilter minF; 
        MagnificationFilter maxF;
        if ( gus.getHighRes() ) {
            minF = MinificationFilter.Trilinear; 
            maxF = MagnificationFilter.Bilinear;
        } else {
            minF = MinificationFilter.NearestNeighborNoMipMaps; 
            maxF = MagnificationFilter.NearestNeighbor;        	
        }
        
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		try {
			ts.setTexture(TextureManager.loadTexture(ResourceLoader.
					getURL(gs.getProperty("CAR.SPEEDOMETER.TEXTURE")),
					minF, maxF, 1.0f, true));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		ts.setEnabled(true);
        BlendState blendState = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        blendState.setBlendEnabled( true );
        blendState.setSourceFunction( BlendState.SourceFunction.SourceAlpha );
        blendState.setDestinationFunction( BlendState.DestinationFunction.OneMinusSourceAlpha );
        blendState.setTestEnabled( true );
        blendState.setTestFunction( BlendState.TestFunction.GreaterThanOrEqualTo );
        blendState.setEnabled( true );   
        speedometer.setRenderState(ts);
        speedometer.setRenderState(blendState);
		speedometer.updateRenderState();
		
	   	// Speedometer's needle
		Quad needle = new Quad("needle", 2, speedometer.getHeight()*scale/1.8f);
		needle.setDefaultColor(ColorRGBA.orange);
		needle.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		needle.setLightCombineMode(LightCombineMode.Off);
		needle.setLocalTranslation(0f, needle.getHeight()/2, 0f);
		needle.updateRenderState();
		needleNode = new Node();
		needleNode.attachChild(needle);
		needleNode.setLocalTranslation(width-speedometer.getWidth()*scale/2-needle.getWidth()*scale/2, 
				speedometer.getHeight()*scale/3.2f, 0f);
		needleNode.setLocalRotation(new Quaternion(new float[]{0,0,112.5f*3.1415f/180}));
			
		
	}

	private void createTimer() {
		gameTimeText = Text.createDefaultTextLabel("gameTimeText", "Timer: --:--");
		gameTimeText.setTextColor(new ColorRGBA(124.0f/255.0f, 252.0f/255.0f, 0f, 0.95f));
		gameTimeText.setCullHint( Spatial.CullHint.Never );
		gameTimeText.setRenderState( Text.getDefaultFontTextureState() );
		gameTimeText.setRenderState( Text.getFontBlend() );
		gameTimeText.setLightCombineMode(LightCombineMode.Off);
		gameTimeText.setLocalScale(1.5f);
		gameTimeText.setLocalTranslation(0, height - gameTimeText.getHeight(), 0);	
	}

	@Override
	public void activated() {
		rootNode.attachChild(this.stateNode);
		rootNode.updateRenderState();

		sky = game.getSkybox();
		playerCar = game.getPlayerCar();		
		
		if (audio == null) {
			audio = game.getAudio();
			audio.playList();
		}
		audio.playSound(soundsEffects.ENGINE);
		audio.unpauseAll();

		//Timer
    	stateNode.attachChild(gameTimeText);
    	//Speedmeter
    	stateNode.attachChild(speedometer);
    	stateNode.attachChild(needleNode);
    	//Map
    	if (map == null) {
    		createMap(GlobalSettings.getInstance(), GameUserSettings.getInstance());
    	}
    	stateNode.attachChild(map);
    	stateNode.attachChild(mapCheckpoints);
    	stateNode.attachChild(carDisk);

    	// Checkpoint Time
    	stateNode.attachChild(timeCheckPoint);
    	game.setCheckPointText(timeCheckPoint);
    	
    	// ScreenShot text
    	stateNode.attachChild(screenShotText);
    	game.setScreenShotText(screenShotText);
    	
    	if (actions == null) {
    		actions = inGameActions();
    	}
    	backupHanlder = RallyGame.getInstance().getInputHandler();
    	
		RallyGame.getInstance().setInputHandler(actions);
    	
	}
	
    public InputHandler inGameActions() {
    	//InputHandler input;
    	//InputHandler cameraInputHandler;
    	RallyGame rg = RallyGame.getInstance();
    	GlobalSettings gs = GlobalSettings.getInstance();
    	
    	InputHandler input = new InputHandler();
    	Car car = rg.getPlayerCar();
    	InputHandler cameraInputHandler = new ChaseCamera( rg.getCamara(), car.getChassis().getChild( 0 ) );
    	
    	// Simple chase camera
        input.addToAttachedHandlers( cameraInputHandler );
        
        // Attaching the custom input actions (and its respective keys) to the carInput handler.
        input.addAction( new AccelAction( car, 1 ),
                InputHandler.DEVICE_KEYBOARD, gs.getHexProperty("FORWARD"), InputHandler.AXIS_NONE, false );
        input.addAction( new AccelAction( car, -1 ),
                InputHandler.DEVICE_KEYBOARD, gs.getHexProperty("BACKWARD"), InputHandler.AXIS_NONE, false );
        input.addAction( new SteerAction( car, -1 ),
                InputHandler.DEVICE_KEYBOARD, gs.getHexProperty("STEERLEFT"), InputHandler.AXIS_NONE, false );
        input.addAction( new SteerAction( car, 1 ),
                InputHandler.DEVICE_KEYBOARD, gs.getHexProperty("STEERRIGHT"), InputHandler.AXIS_NONE, false );
        input.addAction( new ResetAction( car, rg ),
                InputHandler.DEVICE_KEYBOARD, gs.getHexProperty("RETURN"), InputHandler.AXIS_NONE, false );
        input.addAction( new ShowMenuAction(), InputHandler.DEVICE_KEYBOARD, 
        		gs.getHexProperty("EXIT"), InputHandler.AXIS_NONE, false);

        input.addAction( new ChangeCameraAction(), InputHandler.DEVICE_KEYBOARD, 
        		gs.getHexProperty("CAMERA"), InputHandler.AXIS_NONE, false);
        
        return input;
    }	

	@Override
	public void deactivated() {
		RallyGame.getInstance().setInputHandler(backupHanlder);
		
		stateNode.detachChild(gameTimeText);
		stateNode.detachChild(speedometer);
		stateNode.detachChild(needleNode);
		stateNode.detachChild(map);
	   	stateNode.detachChild(mapCheckpoints);
    	stateNode.detachChild(carDisk);		
    	stateNode.detachChild(timeCheckPoint);
    	stateNode.detachChild(screenShotText);
		rootNode.detachChild(stateNode);
		rootNode.updateRenderState();
		if (audio != null) {
			audio.pauseAll();
		}
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void render(float arg0) {
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
		
		float carSpeed = playerCar.getLinearSpeed();
		if (carSpeed > 200)
			carSpeed = 200;
		float angle = 112.5f - 225 * carSpeed / 200;
		needleNode.setLocalRotation(new Quaternion(new float[]{0,0,angle*3.1415f/180}));
		
		if (setCameraPos == true) {
			game.getCamara().setLocation(new Vector3f(cameraPos.x+300,cameraPos.y+100,cameraPos.z+300));
			game.getCamara().lookAt(cameraPos, new Vector3f(0,1,0));
		}
		
		sky.getLocalTranslation().set(game.getCamara().getLocation());
		sky.updateGeometricState(0.0f, true);
		
		long actual = game.getActualTime();
    	if (actual > 0) {
    		long initTime = game.getInitTime();
	    	long checkPointTime = game.getCheckPointTime();
	    	String lap = "";
	    	if (checkPointTime > 0) {
	    		int lapCount = game.getLapCount();
	    		String laps = "";
	    		if (lapCount > 0) {
	    			laps = String.format(" Lap: %d", lapCount);
	    		}
	    		Date checkPointTimeDate = new Date(checkPointTime);
	    		lap = String.format(" CheckPoint: %02d:%02d%s",
	    				checkPointTimeDate.getMinutes(), checkPointTimeDate.getSeconds(),
	    				laps);
	    	}
	    	long raceTime  = new Date().getTime() - initTime - game.getPauseTime();
	    	Date raceTimeDate = new Date(raceTime);
	    	StringBuffer varText = gameTimeText.getText();
	    	varText.replace(0, varText.length(),String.
	    			format("Time: %02d:%02d%s",raceTimeDate.getMinutes(), raceTimeDate.getSeconds(),lap));
    	}
		
		carDisk.setLocalTranslation(
				(map.getWidth() / 2) * playerCar.getPosition().x / 5000 + map.getCenter().x,
				(map.getHeight() / 2) * playerCar.getPosition().z / 5000 + map.getCenter().y, 0f);
		
    	
    	for (Node node : game.getCheckPointList()) {
    		if (node.hasCollision(playerCar, true)) {
    			game.passThrough(playerCar.getName(), node.getName());
    			// Actualizamos la posicion del cartel
    	    	timeCheckPoint.setLocalTranslation((int)(width/2 - timeCheckPoint.getWidth()/2),
						(int)(2*height/3 - timeCheckPoint.getHeight()/2), 0);
    		}		    	
		}
		if ( game.getRallyTrack().getForest().hasCollision(playerCar, true)) {
    		this.audio.playSound(soundsEffects.HIT_SOUND);
		}
    	if (game.getRallyTrack().getFence().hasCollision(playerCar, true)){
    		this.audio.playSound(soundsEffects.HIT_SOUND);
    	}
     	if (game.getRallyTrack().getPyramids().hasCollision(playerCar, true)){
     		this.audio.playSound(soundsEffects.HIT_SOUND);
    	}
     	if (game.getRallyTrack().getObstacles().hasCollision(playerCar, true)){
     		this.audio.playSound(soundsEffects.HIT_SOUND);
    	}
	}
	
	
    /**
     * Simple input action for accelerating and braking the car.
     */
    private class AccelAction implements InputActionInterface {
        Car car;
        int direction;

        public AccelAction( final Car car, final int direction ) {
            this.car = car;
            this.direction = direction;
        }

        public void performAction( final InputActionEvent e ) {
            // If the key has just been pressed, lets accelerate in the desired direction
            if ( e.getTriggerPressed() ) {
                car.accelerate( direction );
            }
            // Otherwise, lets release the wheels.
            else {
                car.releaseAccel();
            }
        }
    }

    private class ResetAction extends InputAction {
    	Car car;
    	TerrainPage tr;
    	Vector3f pos;
    	
    	public ResetAction(Car car, RallyGame rg) {
    		this.car = car;
    		this.tr = rg.getRallyTrack().getTerrain();
    		this.pos = rg.getLastCheckPointPosition();
		}
    	
        public void performAction( InputActionEvent evt ) {
        	car.setPosition(pos.x, tr.getHeight(pos.x, pos.z)-150+20, pos.z);
        }
    }
    
    /**
     * Simple input action for steering the wheel.
     */
    private class SteerAction implements InputActionInterface {
        Car car;

        int direction;

        public SteerAction( Car car, int direction ) {
            this.car = car;
            this.direction = direction;
        }

        public void performAction( final InputActionEvent e ) {
            // If the key is down (left or right) lets steer
            if ( e.getTriggerPressed() ) {
                car.steer( direction );
            }
            // If it's up, lets unsteer
            else {
                car.unsteer();
            }
        }

    }
    private class ShowMenuAction implements InputActionInterface {

        public void performAction( final InputActionEvent e ) {
        	if ( e.getTriggerPressed() ) {
				RallyGame.getInstance().setPause(true);
	//			GameStateManager.getInstance().deactivateAllChildren();
				GameStateManager.getInstance().deactivateChildNamed(InGameState.STATE_NAME);
				GameStateManager.getInstance().activateChildNamed(MenuState.STATE_NAME);
        	}
		}

    }	    
    
    
    private class ChangeCameraAction implements InputActionInterface {
    	
    	private ArrayList<CheckPoint> checkPointList;
    	private int checkPointCount;
    	private int actual;
    	
    	public ChangeCameraAction() {
			this.checkPointList = game.getCheckPointList();
			this.checkPointCount = this.checkPointList.size();
			this.actual = 0;
		}
    	
    	public void performAction( final InputActionEvent e ) {
    		
    		if (this.actual == this.checkPointCount) {
    			// Deshabilito el seteo
    			setCameraPos = false;
    			this.actual = 0;
    		} else {
    			// Itero entre los checkpoints
    			cameraPos.set(this.checkPointList.get(this.actual++).get3DPosition());
    			setCameraPos = true;
    		}

    	}
    	
    }
    
    
    
    
    
    
    
}
