package ar.edu.itba.cg_final.states;
import java.util.ArrayList;
import java.util.Date;

import ar.edu.itba.cg_final.RallyGame;
import ar.edu.itba.cg_final.controller.Audio;
import ar.edu.itba.cg_final.controller.Audio.soundsEffects;
import ar.edu.itba.cg_final.map.CheckPoint;
import ar.edu.itba.cg_final.vehicles.Car;

import com.jme.input.ChaseCamera;
import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameStateManager;
import com.jmex.terrain.TerrainPage;

public class InGameState extends RallyGameState {

	private int width = DisplaySystem.getDisplaySystem().getWidth();
	private int height = DisplaySystem.getDisplaySystem().getHeight();
	
	public static final String STATE_NAME = "InGame";
	
	Text timeCheckPoint;
	Text speed;
	RallyGame game;
	Skybox sky;
	Car playerCar;
	Audio audio;
	InputHandler actions;
	
	
	
	
	private Text gameTimeText;
	Boolean setCameraPos = false;
	Vector3f cameraPos = new Vector3f();
	
	
	public InGameState() {
		this.setName(STATE_NAME);
		stateNode.setName(this.getName());
		game = RallyGame.getInstance();
    	
	}
	
	@Override
	public void activated() {
		rootNode.attachChild(this.stateNode);
		rootNode.updateRenderState();

		sky = game.getSkybox();
		playerCar = game.getPlayerCar();		
		
		audio = game.getAudio();
		audio.playList();
		audio.playSound(soundsEffects.ENGINE);
		audio.unpauseAll();


		
		
		
		
		
		
		
		
        // Variables en pantalla
		gameTimeText = Text.createDefaultTextLabel("gameTimeText", "Timer: --:--");
		gameTimeText.setTextColor(new ColorRGBA(124.0f/255.0f, 252.0f/255.0f, 0f, 0.95f));
		gameTimeText.setCullHint( Spatial.CullHint.Never );
		gameTimeText.setRenderState( Text.getDefaultFontTextureState() );
		gameTimeText.setRenderState( Text.getFontBlend() );
		gameTimeText.setLightCombineMode(LightCombineMode.Off);
		gameTimeText.setLocalScale(1.8f);
		gameTimeText.setLocalTranslation(0, height - gameTimeText.getHeight(), 0);
    	this.stateNode.attachChild(gameTimeText);

 
    	
    	
    	
    	
    	
    	
    	
    	
    	
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
    	timeCheckPoint.setTextColor(new ColorRGBA(124.0f/255.0f, 252.0f/255.0f, 0f, 0.95f));
    	timeCheckPoint.setCullHint( Spatial.CullHint.Never );
    	timeCheckPoint.setRenderState( Text.getDefaultFontTextureState() );
    	timeCheckPoint.setRenderState( Text.getFontBlend() );
    	timeCheckPoint.setLightCombineMode(LightCombineMode.Off);
    	timeCheckPoint.setLocalScale(1.5f);
    	timeCheckPoint.setLocalTranslation((int)(width/2 - timeCheckPoint.getWidth()/2),
    							(int)(height/2 - timeCheckPoint.getHeight()/2), 0);
    	this.stateNode.attachChild(timeCheckPoint);

    	game.setCheckPointText(timeCheckPoint);
    	
    	
    	RallyGame.getInstance().setInputHandler(inGameActions());
	}
	
    public InputHandler inGameActions() {
    	//InputHandler input;
    	//InputHandler cameraInputHandler;
    	RallyGame rg = RallyGame.getInstance();
    	
    	InputHandler input = new InputHandler();
    	Car car = rg.getPlayerCar();
    	InputHandler cameraInputHandler = new ChaseCamera( rg.getCamara(), car.getChassis().getChild( 0 ) );
    	
    	// Simple chase camera
        input.addToAttachedHandlers( cameraInputHandler );
        
        // Attaching the custom input actions (and its respective keys) to the carInput handler.
        input.addAction( new AccelAction( car, 1 ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_UP, InputHandler.AXIS_NONE, false );
        input.addAction( new AccelAction( car, -1 ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_DOWN, InputHandler.AXIS_NONE, false );
        input.addAction( new SteerAction( car, -1 ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_LEFT, InputHandler.AXIS_NONE, false );
        input.addAction( new SteerAction( car, 1 ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_RIGHT, InputHandler.AXIS_NONE, false );
        input.addAction( new ResetAction( car, rg ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_S, InputHandler.AXIS_NONE, false );
        input.addAction( new ShowMenuAction(), InputHandler.DEVICE_KEYBOARD, 
        		KeyInput.KEY_ESCAPE, InputHandler.AXIS_NONE, false);

        
        
        input.addAction( new ChangeCameraAction(), InputHandler.DEVICE_KEYBOARD, 
        		KeyInput.KEY_C, InputHandler.AXIS_NONE, false);
        
        return input;
    }	

	@Override
	public void deactivated() {
		rootNode.detachChild(this.stateNode);
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
		//TODO arreglar la condicion de la camara porque anda media chota
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
		
		// Update speed
		StringBuffer speedText = speed.getText();
		speedText.replace(0, speedText.length(),
				String.format("%03d", (int)playerCar.getLinearSpeed()));
    	

		
		
		
		
		
		
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
	    		Date checkPointTimeDate = new Date(checkPointTime);
	    		lap = String.format(" CheckPoint: %02d:%02d",checkPointTimeDate.getMinutes(), checkPointTimeDate.getSeconds());
	    	}
	    	long raceTime  = new Date().getTime() - initTime + (long)game.getPauseTime();
	    	Date raceTimeDate = new Date(raceTime);
	    	StringBuffer varText = gameTimeText.getText();
	    	varText.replace(0, varText.length(),String.
	    			format("Time: %02d:%02d%s",raceTimeDate.getMinutes(), raceTimeDate.getSeconds(),lap));
    	}
		
		
		
		

		
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
    			// Actualizamos la posicion del cartel
    	    	timeCheckPoint.setLocalTranslation((int)(width/2 - timeCheckPoint.getWidth()/2),
						(int)(height/2 - timeCheckPoint.getHeight()/2), 0);
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
			RallyGame.getInstance().setPause(true);
			GameStateManager.getInstance().deactivateAllChildren();
			GameStateManager.getInstance().activateChildNamed(MenuState.STATE_NAME);
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
