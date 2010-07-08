package ar.edu.itba.cg_final;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;
import ar.edu.itba.cg_final.controller.Audio;
import ar.edu.itba.cg_final.controller.Audio.soundsEffects;
import ar.edu.itba.cg_final.map.CheckPoint;
import ar.edu.itba.cg_final.map.RallySkyBox;
import ar.edu.itba.cg_final.map.RallyTrack;
import ar.edu.itba.cg_final.settings.GameUserSettings;
import ar.edu.itba.cg_final.settings.GlobalSettings;
import ar.edu.itba.cg_final.states.FinishedState;
import ar.edu.itba.cg_final.states.InGameState;
import ar.edu.itba.cg_final.states.MenuState;
import ar.edu.itba.cg_final.states.PreLoadState;
import ar.edu.itba.cg_final.states.RallyGameState;
import ar.edu.itba.cg_final.states.StartState;
import ar.edu.itba.cg_final.vehicles.Car;
import com.jme.app.BaseSimpleGame;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.light.DirectionalLight;
import com.jme.light.PointLight;
import com.jme.light.SpotLight;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
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
	private long initTime = new Date().getTime();
	private Text timeCheckPoint;
	private boolean showCheckPointTime = false;
	private float checkPointTimer;
	private GameStateManager gameStateManager;
	private PhysicsSpace physicsSpace;
	protected boolean showPhysics;
	private float physicsSpeed = 1;
	private ArrayList<CheckPoint> checkPointList;
	Text label;
	Car car;
	Vector3f lastCheckPoint = new Vector3f();
	private long checkPointTime = -1;
	private long actualTime = -1;
		
	private Audio audio;
	private Skybox skybox;	
	
    //the flag to grab
	private Boolean screenshot = false;
//	private int i = 4;
//	private static FileWriter aFile;
//	private static BufferedWriter aWriter;

	private boolean playing = false;
	private float pauseTime = 0;
	private String firstCheckPoint = null;
	private int laps = 0;
	private long raceTime = 0;
	
	public long getRaceTime() {
		return raceTime;
	}

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
	protected void initGame() {
		super.initGame();
		//Disable F4
		graphNode.getChild("f4").setLocalTranslation(-100, -100, -100);
	}
	
	@Override
	public void simpleInitGame() {
		
		// Creamos los estados en el juego y mostramos el menu
		RallyGameState menuState = new MenuState();
		PreLoadState preLoadState = new PreLoadState();
		RallyGameState startGameState = new StartState();
		RallyGameState inGameState = new InGameState();
		RallyGameState finishedGameState = new FinishedState();

		GameStateManager.create();

		gameStateManager = GameStateManager.getInstance();
		gameStateManager.detachAllChildren();
		
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

		KeyBindingManager.getKeyBindingManager().set("screenshot", KeyInput.KEY_0);
	}

	public void setPlaying() {
		playing = true;
		((RallyGameState)gameStateManager.getChild("InGame")).
		getStateNode().attachChild(timeCheckPoint);
	}
	
	public void setPause(boolean state) {
		if (playing)
			this.pause = state;
	}
	
	public boolean isPaused(){
		return this.pause;
	}
	
	public Node getRootNode() {
		return rootNode;
	}
	
	public void initAudio(GlobalSettings gs) {
		audio = new Audio();
		audio.addSong(gs.getProperty("MUSIC.SONG1.PATH"));
		audio.addSong(gs.getProperty("MUSIC.SONG2.PATH"));
		
		audio.addSound(gs.getProperty("EFFECT.CRASH"), soundsEffects.HIT_SOUND);
		audio.addSound(gs.getProperty("CAR.CHECKPOINT.SOUND"), soundsEffects.CHECKPOINT);
		
		audio.addSound(gs.getProperty("EFFECT.NEUTRAL"), soundsEffects.ENGINE, true);
		audio.addSound(gs.getProperty("EFFECT.ENGINE"), soundsEffects.ACEL);
		
	}
	
	public void setCheckPointText(Text text) {
		timeCheckPoint = text;
	}
	
	public void passThrough(String player, String checkPoint) {
		if (positions.get(player).equals(checkPoint)) {
			String lap = "Checkpoint time: ";
			lastCheckPoint.set(car.getPosition());
			positions.put(player, checkPoints.get(checkPoint));
//			audio.playSound(soundsEffects.CHECKPOINT);
			// Take last time
			actualTime = new Date().getTime();
			StringBuffer timeText = timeCheckPoint.getText();
			showCheckPointTime = true;
			checkPointTimer = 3;
			// Check laps!
			if (checkPoint.equals(firstCheckPoint)) {
				// Informamos que dio una vuelta
				timeCheckPoint.setLocalScale(3f);
				lap = "Lap " + String.valueOf(laps) + ": ";
				if (laps == 0) {
					// Iniciamos el contador de tiempo
					lap = "Timer: ";
					initTime = new Date().getTime();
					actualTime = initTime;
				} else if (laps == 3) { //TODO == 3 para que sean 3 vueltas!
					// Termino el juego
					lap = "Race: ";
					checkPointTimer = 300;
					this.setPause(true);
					// Desactivamos el estado del juego
					gameStateManager.deactivateChildNamed("InGame");
					// Agregamos el nodo del juego para seguir mostrandolo
					((RallyGameState)gameStateManager.getChild("FinishedGame")).getStateNode().
						attachChild(((RallyGameState)gameStateManager.getChild("InGame")).getStateNode());
					car.isLocked(true);
					// Activamos el estado final
					gameStateManager.activateChildNamed("FinishedGame");
				}
				laps++;
			} else {
				timeCheckPoint.setLocalScale(1.5f);
			}
			// Creamos el string a mostrar
			raceTime  = actualTime - initTime + (long)pauseTime;
			checkPointTime = raceTime;
			Date date = new Date(raceTime);
			timeText.replace(0, timeText.length(),String.
					format("%s%02d:%02d",lap, date.getMinutes(), date.getSeconds()));
		}
		return;
	}
	
	public ArrayList<CheckPoint> getCheckPointList() {
		return checkPointList;
	}
    
    public void buildCheckpoint(Node inGameStateNode, GlobalSettings gs) {
    	TerrainPage terrain = rallyTrack.getTerrain();
    	String last = null;
    	String actual = null;
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
				firstCheckPoint  = actual;
				lastCheckPoint.set(checkpoint.get3DPosition());
				last = actual;
			}
		}
		checkPoints.put(actual, firstCheckPoint);
		// Seteamos el punto de partida
		positions.put(car.getName(), firstCheckPoint);
		// Apuntamos el auto a la partida
		
//		// Fix de la posicion de la camara
//		Vector3f p = car.getPosition();
//		cam.setLocation(new Vector3f(p.x-300,p.y+100,p.z+300));
//		cam.lookAt(p, new Vector3f(0,1,0));
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
		setPhysicsSpace(PhysicsSpace.create());
		input = new InputHandler();
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
		} else {
			pauseTime+=tpf;
		}

		if (showCheckPointTime) {
			checkPointTimer-=tpf;
			if (checkPointTimer <= 0) {
				StringBuffer timeText = timeCheckPoint.getText();
				timeText.replace(0, timeText.length(),"");
				showCheckPointTime = false;
			}
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
//			try {
//				aWriter.write("TRACK1.OBSTACLES"+ i +".POS="+car.getPosition().x+","+car.getPosition().z);
//				aWriter.flush();
//				i++;
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

		}
		
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
	

	
    public void tunePhysics(Node inGameStateNode) {
        getPhysicsSpace().setAutoRestThreshold( 0.2f );
        // Otherwise it would be VERY slow.
        this.setPhysicsSpeed( 4 );
    }

    public void createCar(Node inGameStateNode, GlobalSettings gs) {
    		
    	car = new Car( getPhysicsSpace(), gs, audio );
    	car.setName("PlayerCar");
    	
    	TerrainPage tp = rallyTrack.getTerrain();
    	
    	final Vector2f pos = gs.get2DVectorProperty("TRACK1.CAR.POSITION");
    	car.setPosition(pos.x, tp.getHeight(pos)-150+20, pos.y);
//    	car.setRotation(0, gs.getFloatProperty("TRACK1.CAR.ROTATION"), 0);
//        car.getLocalRotation().set(new Quaternion(new float[]{0, gs.getFloatProperty("TRACK1.CAR.ROTATION"), 0}));
    	//TODO: rotarlo...
        inGameStateNode.attachChild( car );
        inGameStateNode.updateGeometricState(0, true);
        
    }
    public void createRallyTrack(Node inGameStateNode, GlobalSettings gs) {
    	RallyTrack rt = new RallyTrack(gs);
    	
    	this.rallyTrack = rt;
    	inGameStateNode.attachChild(rt);
    }
    
    // Needed to apply restrictions on camera so it wont go below the terrain 
	public Camera getCamara() {
		return this.cam;
	}

	// Create skybox
	public void createSkyBox(Node inGameStateNode, GlobalSettings gs, GameUserSettings us) {
		Skybox skyBox = RallySkyBox.getSkyBox(gs, us.getSkybox(), 
				DisplaySystem.getDisplaySystem());
		inGameStateNode.attachChild(skyBox);
		this.skybox = skyBox; 
	}
	
	public void createLights(){
		GlobalSettings gs = new GlobalSettings();
		TerrainPage tp = rallyTrack.getTerrain();
		Vector2f pos;
		
		LightState ls = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
		
		//Create a Basic Directional Light
		DirectionalLight dl = new DirectionalLight();
		dl.setDirection(new Vector3f(0,-1,0));
		dl.setDiffuse(new ColorRGBA(
				gs.getFloatProperty("TRACK1.AMBIENT.LIGHT.R"), 
				gs.getFloatProperty("TRACK1.AMBIENT.LIGHT.G"), 
				gs.getFloatProperty("TRACK1.AMBIENT.LIGHT.B"), 
				gs.getFloatProperty("TRACK1.AMBIENT.LIGHT.A")));
		dl.setAmbient(new ColorRGBA(
				gs.getFloatProperty("TRACK1.AMBIENT.LIGHT.R"), 
				gs.getFloatProperty("TRACK1.AMBIENT.LIGHT.G"), 
				gs.getFloatProperty("TRACK1.AMBIENT.LIGHT.B"), 
				gs.getFloatProperty("TRACK1.AMBIENT.LIGHT.A")));
		dl.setEnabled(true);
		
		ls.attach(dl);
		ls.setTwoSidedLighting(true);
		

		//If there is a first checkpoint it has a Spotlight on top.  Go through it and your car will turn red
		int i = gs.getIntProperty("TRACK1.CHECKPOINT.COUNT");
		if(i > 0){
			pos = gs.get2DVectorProperty("TRACK1.CHECKPOINT1.POS");
		   	SpotLight sl = new SpotLight();
			sl.setDiffuse(new ColorRGBA(
					gs.getFloatProperty("TRACK1.LIGHT.SPOTLIGHT.R"), 
					gs.getFloatProperty("TRACK1.LIGHT.SPOTLIGHT.G"), 
					gs.getFloatProperty("TRACK1.LIGHT.SPOTLIGHT.B"), 
					gs.getFloatProperty("TRACK1.LIGHT.SPOTLIGHT.A")));
			sl.setAmbient(new ColorRGBA(0.75f, 0.75f, 0.75f, 1.0f));
			sl.setDirection(new Vector3f(0, -1, 0));
			sl.setLocation(new Vector3f(pos.x, tp.getHeight(pos)-150+100, pos.y));
			sl.setAngle(60);
			sl.setEnabled(true);
			ls.attach(sl);
			ls.setTwoSidedLighting(true);
		}
		
		//Insert Point Lights up to 5. JME Only supports 8 lights.
		i = gs.getIntProperty("TRACK1.POINTLIGHT.COUNT");
		for(int j = 1; j <= i && j <= 5; j++){
			pos = gs.get2DVectorProperty("TRACK1.POINTLIGHT" + j + ".POS");
			PointLight pl = new PointLight();
			pl.setDiffuse(new ColorRGBA(
					gs.getFloatProperty("TRACK1.POINTLIGHT" + j + ".R"), 
					gs.getFloatProperty("TRACK1.POINTLIGHT" + j + ".G"), 
					gs.getFloatProperty("TRACK1.POINTLIGHT" + j + ".B"), 
					gs.getFloatProperty("TRACK1.POINTLIGHT" + j + ".A")));
//			pl.setAmbient(new ColorRGBA(0.75f, 0.75f, 0.75f, 1.0f));
			pl.setLocation(new Vector3f(pos.x, tp.getHeight(pos)-150+50, pos.y));
			pl.setEnabled(true);
			ls.attach(pl);
			ls.setTwoSidedLighting(true);
		}
		

		rootNode.setRenderState(ls);
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
	
	public void setInputHandler(InputHandler input) {
		this.input = input;
	}
	// Metodo que retorna un objeto al que le atacheamos las acciones del teclado
	public InputHandler getInputHandler() {
		return this.input;
	}

	public Vector3f getLastCheckPointPosition() {
		return lastCheckPoint;
	}

	public long getInitTime() {
		return initTime;
	}

	public float getPauseTime() {
		return pauseTime;
	}

	public long getCheckPointTime() {
		return checkPointTime;
	}
	
	public long getActualTime() {
		return actualTime;
	}
	
}
