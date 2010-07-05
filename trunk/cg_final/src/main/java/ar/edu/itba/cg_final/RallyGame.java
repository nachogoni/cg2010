package ar.edu.itba.cg_final;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import ar.edu.itba.cg_final.controller.Audio;
import ar.edu.itba.cg_final.map.CheckPoint;
import ar.edu.itba.cg_final.map.RallySkyBox;
import ar.edu.itba.cg_final.map.RallyTrack;
import ar.edu.itba.cg_final.settings.GlobalSettings;
import ar.edu.itba.cg_final.states.FinishedState;
import ar.edu.itba.cg_final.states.InGameState;
import ar.edu.itba.cg_final.states.MenuState;
import ar.edu.itba.cg_final.states.PreLoadState;
import ar.edu.itba.cg_final.states.RallyGameState;
import ar.edu.itba.cg_final.states.StartState;
import ar.edu.itba.cg_final.vehicles.Car;

import com.jme.app.BaseSimpleGame;
import com.jme.input.ChaseCamera;
import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.Text;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameStateManager;
import com.jmex.physics.PhysicsDebugger;
import com.jmex.physics.PhysicsSpace;
import com.jmex.terrain.TerrainPage;

public class RallyGame extends BaseSimpleGame {
	
	// Singleton
	private static RallyGame instance;
	
	private RallyTrack rallyTrack;

	private HashMap<String, String> positions = new HashMap<String, String>();
	private HashMap<String, String> checkPoints = new HashMap<String, String>();

	private GameStateManager gameStateManager;
	private PhysicsSpace physicsSpace;
	protected InputHandler cameraInputHandler;
	protected boolean showPhysics;
	private float physicsSpeed = 1;
	private List<CheckPoint> checkPointList;
	Text label;
	Car car;
	ResetAction resetAction;
	
	private Audio audio;

	private Skybox skybox;	
	
    //the flag to grab
	private Boolean screenshot = false;
//	private int i = 0;
//	private static FileWriter aFile;
//	private static BufferedWriter aWriter;
	
	public RallyGame() {
		instance = this;
	}
	
	public static RallyGame getInstance() {
		if (instance == null)
			instance = new RallyGame();
		return instance;
	}	
	
	public static void main(String[] args) throws IOException {
//		aFile = new FileWriter("waka");
//		aWriter = new BufferedWriter(aFile);
		RallyGame app = new RallyGame();
		app.setConfigShowMode(ConfigShowMode.ShowIfNoConfig);
		app.start();
//		aWriter.close();
	}

	@Override
	protected void simpleInitGame() {

		// Creamos los estados en el juego y mostramos el menu
		RallyGameState menuState = new MenuState();
		PreLoadState preLoadState = new PreLoadState();
		RallyGameState startGameState = new StartState();
		RallyGameState inGameState = new InGameState();
		RallyGameState finishedGameState = new FinishedState();

		GameStateManager.create();

		gameStateManager = GameStateManager.getInstance();

		KeyBindingManager.getKeyBindingManager().removeAll();

		menuState.setActive(true);
		preLoadState.setActive(false);
		startGameState.setActive(false);
		inGameState.setActive(false);
		finishedGameState.setActive(false);
		
		gameStateManager.attachChild(menuState);
		gameStateManager.attachChild(preLoadState);
		gameStateManager.attachChild(startGameState);
		gameStateManager.attachChild(inGameState);
		gameStateManager.attachChild(finishedGameState);

		KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);
		KeyBindingManager.getKeyBindingManager().set("next", KeyInput.KEY_F);
		KeyBindingManager.getKeyBindingManager().set("prev", KeyInput.KEY_G);
		KeyBindingManager.getKeyBindingManager().set("post", KeyInput.KEY_H);
		KeyBindingManager.getKeyBindingManager().set("toggle_pause", KeyInput.KEY_P);
		KeyBindingManager.getKeyBindingManager().set("screenshot", KeyInput.KEY_0);
		//KeyBindingManager.getKeyBindingManager().set("print", KeyInput.KEY_9);
	}
	
	public void setPause(boolean state) {
		this.pause = state;
	}
	
	public Node getRootNode() {
		return rootNode;
	}
	
	// Metodos para la creacion de los elementos del juego (usados en el preloader)
	
	
	public void initAudio() {
		GlobalSettings gs = new GlobalSettings();
		audio = new Audio();//TODO
		audio.addAudio(gs.getProperty("MUSIC.SONG2.PATH"));//TODO
		audio.addAudio(gs.getProperty("MUSIC.SONG1.PATH"));
	}
	
	public void passThrough(String player, String checkPoint) {
		if (positions.get(player).equals(checkPoint)) {
			positions.put(player, checkPoints.get(checkPoint));
			Audio sound = new Audio();
			sound.playOnce("sound/hit.ogg");
		}
		return;
	}
	
	public List<CheckPoint> getCheckPointList() {
		return checkPointList;
	}
    
    public void buildCheckpoint(Node inGameStateNode, GlobalSettings gs) {
    	TerrainPage terrain = rallyTrack.getTerrain();
    	String last = null;
    	String actual = null;
    	String first = null;
    	checkPointList = rallyTrack.createCheckPoints(gs);

    	for (Iterator<CheckPoint> iterator = checkPointList.iterator(); iterator.hasNext();) {
			CheckPoint checkpoint = (CheckPoint) iterator.next();
			// Actualizamos la altura segun el terreno
			Vector2f pos = checkpoint.get2DPosition();
			checkpoint.setPosition(new Vector3f(pos.x, 
					terrain.getHeight(pos) - 100, pos.y));
			// Agregamos el checkpoint al nodo del estado que me pasan
			inGameStateNode.attachChild(checkpoint);
			// Creamos la lista de checkpoints para la pista
			actual = checkpoint.getName();
			if (last != null) {
				checkPoints.put(last, actual);
				last = actual;
			} else {
				first = actual;
				last = actual;
			}
		}
		checkPoints.put(actual, first);
		// Seteamos el punto de partida
		positions.put(car.getName(), first);
    }
	
	/**
	 * @return speed set by {@link #setPhysicsSpeed(float)}
	 */
	public float getPhysicsSpeed() {
		return physicsSpeed;
	}

	/**
	 * The multiplier for the physics time. Default is 1, which means normal
	 * speed. 0 means no physics processing.
	 * 
	 * @param physicsSpeed
	 *            new speed
	 */
	public void setPhysicsSpeed(float physicsSpeed) {
		this.physicsSpeed = physicsSpeed;
	}

	@Override
	protected void initSystem() {
		super.initSystem();

		/** Create a basic input controller. */
		cameraInputHandler = new FirstPersonHandler(cam, 50, 1);
		input = new InputHandler();
		input.addToAttachedHandlers(cameraInputHandler);

		setPhysicsSpace(PhysicsSpace.create());

		input.addAction(new InputAction() {
			public void performAction(InputActionEvent evt) {
				if (evt.getTriggerPressed()) {
					showPhysics = !showPhysics;
				}
			}
		}, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_V,
				InputHandler.AXIS_NONE, false);
	}

	/**
	 * @return the physics space for this simple game
	 */
	public PhysicsSpace getPhysicsSpace() {
		return physicsSpace;
	}

	/**
	 * @param physicsSpace
	 *            The physics space for this simple game
	 */
	protected void setPhysicsSpace(PhysicsSpace physicsSpace) {
		if (physicsSpace != this.physicsSpace) {
			if (this.physicsSpace != null)
				this.physicsSpace.delete();
			this.physicsSpace = physicsSpace;
		}
	}

	private boolean firstFrame = true;

	/**
	 * Called every frame to update scene information.
	 * 
	 * @param interpolation
	 *            unused in this implementation
	 * @see BaseSimpleGame#update(float interpolation)
	 */
	@Override
	protected final void update(float interpolation) {
		// disable input as we want it to be updated _after_ physics
		// in your application derived from BaseGame you can simply make the
		// call to InputHandler.update later
		// in your game loop instead of this disabling and re-enabling

		super.update(interpolation);

		if (!pause) {
			float tpf = this.tpf;
			if (tpf > 0.2 || Float.isNaN(tpf)) {
				Logger
						.getLogger(PhysicsSpace.LOGGER_NAME)
						.warning(
								"Maximum physics update interval is 0.2 seconds - capped.");
				tpf = 0.2f;
			}
			getPhysicsSpace().update(tpf * physicsSpeed);
		}

		input.update(tpf);

		if (!pause) {
			/** Call simpleUpdate in any derived classes of SimpleGame. */
			simpleUpdate();

			/** Update controllers/render states/transforms/bounds for rootNode. */
			rootNode.updateGeometricState(tpf, true);
			statNode.updateGeometricState(tpf, true);
		}

		if (firstFrame) {
			// drawing and calculating the first frame usually takes longer than
			// the rest
			// to avoid a rushing simulation we reset the timer
			timer.reset();
			firstFrame = false;
		}
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("screenshot",
				false)){
			screenshot = true;
//			float [] angles = new float[3];
//	    	car.getChassis().getLocalRotation().toAngles(angles);
//			try {
//				aWriter.write("TRACK1.CHECKPOINT" + i + ".POS=" + (int) car.getChassis().getLocalTranslation().x + 
//						", " + (int) car.getChassis().getLocalTranslation().z + "\n");
//				aWriter.write("TRACK1.CHECKPOINT" + i + ".ROT=" + angles[1]);
//				i++;
//				aWriter.flush();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
//		if (KeyBindingManager.getKeyBindingManager().isValidCommand("print",
//				false))
//		{
//			float [] angles = new float[3];
//	    	car.getChassis().getLocalRotation().toAngles(angles);
//			try {
//				aWriter.write("TRACK1.CHECKPOINT" + i + ".POS=" + (int) car.getChassis().getLocalTranslation().x + 
//						", " + (int) car.getChassis().getLocalTranslation().z + "\n");
//				aWriter.write("TRACK1.CHECKPOINT" + i + ".ROT=" + angles[1]);
//				i++;
//				aWriter.flush();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		GameStateManager.getInstance().update(tpf);

	}

	@Override
	protected void updateInput() {
		// don't input here but after physics update
	}

	/**
	 * This is called every frame in BaseGame.start(), after update()
	 * 
	 * @param interpolation
	 *            unused in this implementation
	 * @see com.jme.app.AbstractGame#render(float interpolation)
	 */
	@Override
	protected final void render(float interpolation) {
		super.render(interpolation);

		preRender();

		Renderer r = display.getRenderer();

		/** Draw the rootNode and all its children. */
		r.draw(rootNode);

		/** Call simpleRender() in any derived classes. */
		simpleRender();

		/** Draw the stats node to show our stat charts. */
		r.draw(statNode);

		doDebug(r);
		
		GameStateManager.getInstance().render(tpf);
		
		if( screenshot ){
			screenshot = false;
			System.out.println("Screenshot!!!");
			r.takeScreenShot(new Date().toString());
		}
		
		
	}

	/**
     * 
     */
	protected void preRender() {

	}

	@Override
	protected void doDebug(Renderer r) {
		super.doDebug(r);

		if (showPhysics) {
			PhysicsDebugger.drawPhysics(getPhysicsSpace(), r);
		}
	}

	
	
	
	
	public void createForest(Node inGameStateNode) {
		rallyTrack.initForest(inGameStateNode);
	}
	
    public void initInput(Node inGameStateNode) {
        // Simple chase camera
        input.removeFromAttachedHandlers( cameraInputHandler );
        cameraInputHandler = new ChaseCamera( cam, car.getChassis().getChild( 0 ) );
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

        resetAction = new ResetAction();
        input.addAction( resetAction, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_S, InputHandler.AXIS_NONE, false );
    }
	
    public void tunePhysics(Node inGameStateNode) {
        getPhysicsSpace().setAutoRestThreshold( 0.2f );
        // Otherwise it would be VERY slow.
        this.setPhysicsSpeed( 4 );
    }

    public void createCar(Node inGameStateNode, GlobalSettings gs) {
    		
    	car = new Car( getPhysicsSpace() );
    	car.setName("PlayerCar");
    	
    	/*this.car.setPosition(terrain.getWorldBound().getCenter().x,
    			terrain.getWorldBound().getCenter().y+24,
    			terrain.getWorldBound().getCenter().z);*/
    	
    	TerrainPage tp = rallyTrack.getTerrain();
    	
    	final Vector2f pos = gs.get2DVectorProperty("TRACK1.CAR.POSITION");
    	
    	car.setPosition(pos.x, tp.getHeight(pos)-150+24, pos.y);
    	
//		Quaternion x180 = new Quaternion();
//		x180.fromAngleAxis(FastMath.DEG_TO_RAD * 180, new Vector3f(0, 0, 1));
    	
        inGameStateNode.attachChild( car );
        inGameStateNode.updateGeometricState(0, true);			
    	
    }
    public void createRallyTrack(Node inGameStateNode, GlobalSettings gs) {
    	RallyTrack rt = new RallyTrack(gs);
    	
    	this.rallyTrack = rt;
    	inGameStateNode.attachChild(rt);
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
        public void performAction( InputActionEvent evt ) {
            car.setPosition( -500, 50, 600 );
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
    
    // Needed to apply restrictions on camera so it wont go below the terrain 
	public Camera getCamara() {
		return this.cam;
	}

	// Create skybox
	public void createSkyBox(Node inGameStateNode, String sky, float xExtent, float yExtent, float zExtent) {
		Skybox skyBox;
		if (sky.equals("day") == true) {
			skyBox = RallySkyBox.getDaySkyBox(
					DisplaySystem.getDisplaySystem(), xExtent, yExtent, zExtent);
			
		} else if (sky.equals("red")) {
			skyBox = RallySkyBox.getRedSkyBox(
					DisplaySystem.getDisplaySystem(), xExtent, yExtent, zExtent);
		} else if (sky.equals("night")) {
			skyBox = RallySkyBox.getNightSkyBox(
					DisplaySystem.getDisplaySystem(), xExtent, yExtent, zExtent);
		} else {
			skyBox = RallySkyBox.getNightSkyBox(
					DisplaySystem.getDisplaySystem(), xExtent, yExtent, zExtent);
		}
		inGameStateNode.attachChild(skyBox);
		this.skybox = skyBox; 
	}
	
	public Skybox getSkybox() {
		return skybox;
	}
	
	
	public LightState getLightState() {
		return lightState;
	}
	
	public RallyTrack getRallyTrack() {
		return rallyTrack;
	}
	
	public Car getPlayerCar(){
		return car;
	}

	public Audio getAudio() {
		return audio;
	}
	
}
