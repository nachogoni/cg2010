package ar.edu.itba.cg_final;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import ar.edu.itba.cg_final.map.Checkpoint;
import ar.edu.itba.cg_final.map.Flag;
import ar.edu.itba.cg_final.map.RallySkyBox;
import ar.edu.itba.cg_final.states.FinishedState;
import ar.edu.itba.cg_final.states.InGameState;
import ar.edu.itba.cg_final.states.MenuState;
import ar.edu.itba.cg_final.states.PreLoadState;
import ar.edu.itba.cg_final.states.RallyGameState;
import ar.edu.itba.cg_final.states.StartState;
import ar.edu.itba.cg_final.terrain.ForceFieldFence;
import ar.edu.itba.cg_final.vehicles.Car;

import com.jme.app.BaseSimpleGame;
import com.jme.image.Texture;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.CombinerFunctionRGB;
import com.jme.image.Texture.CombinerOperandRGB;
import com.jme.image.Texture.CombinerScale;
import com.jme.image.Texture.CombinerSource;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.image.Texture.WrapMode;
import com.jme.input.ChaseCamera;
import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;
import com.jme.intersection.BoundingPickResults;
import com.jme.light.DirectionalLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Ray;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.Text;
import com.jme.scene.state.CullState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.CullState.Face;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.MusicTrackQueue;
import com.jmex.audio.MusicTrackQueue.RepeatType;
import com.jmex.game.state.GameStateManager;
import com.jmex.physics.PhysicsDebugger;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.FaultFractalHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;

public class RallyGame extends BaseSimpleGame {
	
	private static final Logger logger = Logger.getLogger(RallyGame.class
			.getName());	
	
	// Singleton
	private static RallyGame instance;
	
	private GameStateManager gameStateManager;
	private PhysicsSpace physicsSpace;
	protected InputHandler cameraInputHandler;
	protected boolean showPhysics;
	private float physicsSpeed = 1;
	private TerrainPage terrain;
	private ForceFieldFence fence;
    //the flag to grab
	
	public RallyGame() {
		instance = this;
	}
	
	public static RallyGame getInstance() {
		if (instance == null)
			instance = new RallyGame();
		return instance;
	}	
	
	public static void main(String[] args) {
		RallyGame app = new RallyGame();
		app.setConfigShowMode(ConfigShowMode.ShowIfNoConfig);
		app.start();
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

		KeyBindingManager.getKeyBindingManager().removeAll();
		KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);
		KeyBindingManager.getKeyBindingManager().set("next", KeyInput.KEY_F);
		KeyBindingManager.getKeyBindingManager().set("prev", KeyInput.KEY_G);
		KeyBindingManager.getKeyBindingManager().set("post", KeyInput.KEY_H);
		KeyBindingManager.getKeyBindingManager().set("toggle_pause", KeyInput.KEY_P);
	}
	
	public void setPause(boolean state) {
		this.pause = state;
	}
	
	public Node getRootNode() {
		return rootNode;
	}
	
	// Metodos para la creacion de los elementos del juego (usados en el preloader)
	
	
	public void initAudio() {
		try {
			AudioTrack track = AudioSystem.getSystem().createAudioTrack(
					RallyGame.class.getClassLoader().getResource(
							"sound/maninside.ogg"), false);
			AudioTrack track2 = AudioSystem.getSystem().createAudioTrack(
					RallyGame.class.getClassLoader().getResource(
							"sound/cartonero.ogg"), false);

			MusicTrackQueue queue = AudioSystem.getSystem().getMusicQueue();
			queue.setCrossfadeinTime(0);
			queue.setRepeatType(RepeatType.ALL);
			queue.addTrack(track);
			queue.addTrack(track2);
		} catch (Exception e) {
			logger.logp(Level.SEVERE, this.getClass().toString(),
					"simpleAppletSetup()", "Exception", e);			
		}	
	}
	
	
    /**
     * buildEnvironment will create a fence. 
     */
    public void buildEnvironment(Node inGameStateNode) {
        //This is the main node of our fence
        fence = new ForceFieldFence("fence");
        
        //we will do a little 'tweaking' by hand to make it fit in the terrain a bit better.
        //first we'll scale the entire "model" by a factor of 5
        fence.setLocalScale(320);
        
        //now let's move the fence to to the height of the terrain and in a little bit.
        
        fence.setLocalTranslation(new Vector3f(0, 
        		terrain.getHeight(25,25)+10, 
        		0));
        
        fence.updateGeometricState(0, true);
        
        final StaticPhysicsNode staticNode = getPhysicsSpace().createStaticNode();

        staticNode.attachChild( fence );
        
        staticNode.getLocalTranslation().set( terrain.getWorldBound().getCenter().x - fence.getWorldBound().getCenter().x, 
        		-150, terrain.getWorldBound().getCenter().z - fence.getWorldBound().getCenter().z);

        inGameStateNode.attachChild( staticNode );
        staticNode.generatePhysicsGeometry();
        //initialize OBBTree of terrain
        inGameStateNode.findPick( new Ray( new Vector3f(), new Vector3f( 1, 0, 0 ) ), new BoundingPickResults() );      
    }
    
    public void buildCheckpoint(Node inGameStateNode) {
        //This is the main node of our fence
    	Node cp = new Checkpoint("check");
    	
        //we will do a little 'tweaking' by hand to make it fit in the terrain a bit better.
        //first we'll scale the entire "model" by a factor of 5
        cp.setLocalScale(5);
        
        //now let's move the fence to to the height of the terrain and in a little bit.
        
        /*cp.setLocalTranslation(new Vector3f(0, 
        		terrain.getHeight(25,25)+300, 
        		0));*/
        
        cp.updateGeometricState(0, true);
        
        final StaticPhysicsNode staticNode = getPhysicsSpace().createStaticNode();

        staticNode.attachChild( cp );
        
        final Vector3f point = new Vector3f(terrain.getWorldBound().getCenter().x,
        		terrain.getWorldBound().getCenter().y,
        		terrain.getWorldBound().getCenter().z); 
        
        cp.getLocalTranslation().set(point);

        inGameStateNode.attachChild( cp );
        //staticNode.generatePhysicsGeometry();
    }
	
	
    /**
     * we created a new Flag class, so we'll use it to add the flag to the world.
     * This is the flag that we desire, the one to get.
     *
     */
    public void buildFlag(Node inGameStateNode) {
//    	flag = new Flag(10,0,10,2);
    	float x = FastMath.nextRandomFloat() * 10000 - 5000;
    	float z = FastMath.nextRandomFloat() * 10000 - 5000;
    	//TODO: ver altura
        Flag flag = new Flag(x, 0/*terrain.getHeight(x, z)*/, z, 2);
        inGameStateNode.attachChild(flag);
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

	
	
	
	
	
	
	
	
	
	Text label;
	Car car;
	ResetAction resetAction;

	private Skybox skybox;
	
    public void createText(Node inGameStateNode) {
        label = Text.createDefaultTextLabel( "instructions",
                "Use arrows to drive. Use the mouse wheel to control the chase camera. S to reset the car." );
        label.setLocalTranslation( 0, 20, 0 );
        inGameStateNode.attachChild( label );
        inGameStateNode.updateRenderState();
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

    public void createCar(Node inGameStateNode, String carName) {
    	
    	this.car = new Car( getPhysicsSpace() );
    	this.car.setName(carName);
    	
    	/*this.car.setPosition(terrain.getWorldBound().getCenter().x,
    			terrain.getWorldBound().getCenter().y+24,
    			terrain.getWorldBound().getCenter().z);*/
    	this.car.setPosition(600, terrain.getHeight(600,-520)-150+24, -520);
    	
		Quaternion x180 = new Quaternion();
		x180.fromAngleAxis(FastMath.DEG_TO_RAD * 180, new Vector3f(0, 0, 1));
    	
        inGameStateNode.attachChild( car );
        inGameStateNode.updateGeometricState(0, true);
    	
    }
	
    public void createTerrain(Node inGameStateNode) {
    	

        DirectionalLight dl = new DirectionalLight();
        dl.setDiffuse( new ColorRGBA( 1.0f, 1.0f, 1.0f, 1.0f ) );
        dl.setDirection( new Vector3f( 1, -0.5f, 1 ) );
        dl.setEnabled( true );
        lightState.attach( dl );

        DirectionalLight dr = new DirectionalLight();
        dr.setEnabled( true );
        dr.setDiffuse( new ColorRGBA( 1.0f, 1.0f, 1.0f, 1.0f ) );
        dr.setAmbient( new ColorRGBA( 0.5f, 0.5f, 0.5f, 1.0f ) );
        dr.setDirection( new Vector3f( 0.5f, -0.5f, 0 ) );

        lightState.attach( dr );

        display.getRenderer().setBackgroundColor( new ColorRGBA( 0.5f, 0.5f, 0.5f, 1 ) );

        FaultFractalHeightMap heightMap = new FaultFractalHeightMap( 1025, 32, 0, 20,
                0.75f, 3 );
        Vector3f terrainScale = new Vector3f( 10, 1, 10 );
        heightMap.setHeightScale( 0.001f );
        
        TerrainPage page = new TerrainPage( "Terrain", 33, heightMap.getSize(), terrainScale,
                heightMap.getHeightMap() );
        
        page.setDetailTexture( 1, 16 );
        
        CullState cs = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
        cs.setCullFace(Face.Back);
        cs.setEnabled( true );
        page.setRenderState( cs );
        //TODO deprecado. ahora se usa una imagen
        ProceduralTextureGenerator pt = new ProceduralTextureGenerator( heightMap );
        pt.addTexture( new ImageIcon( RallyGameCar.class.getClassLoader().getResource(
                "texture/grassb.png" ) ), -128, 0, 128 );
        pt.addTexture( new ImageIcon( RallyGameCar.class.getClassLoader().getResource(
                "texture/dirt.jpg" ) ), 0, 128, 255 );
        pt.addTexture( new ImageIcon( RallyGameCar.class.getClassLoader().getResource(
                "texture/highest.jpg" ) ), 128, 255, 384 );
        
        pt.createTexture( 512 );
        
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        ts.setEnabled( true );
        Texture t2 = TextureManager.loadTexture(
                pt.getImageIcon().getImage(),
                MinificationFilter.Trilinear,
                MagnificationFilter.Bilinear,
                true );
        //ts.setTexture( t2, 0 );
        
        Texture t3 = TextureManager.loadTexture( RallyGameCar.class.getClassLoader().
                getResource(
                "texture/Detail.jpg" ),
                MinificationFilter.Trilinear,
                MagnificationFilter.Bilinear );
        ts.setTexture( t3, 1 );
        t3.setWrap( WrapMode.Repeat );

        
        Texture t1 = TextureManager.loadTexture( RallyGameCar.class.getClassLoader().
                getResource(
                "texture/autodromo2.png" ),
                MinificationFilter.Trilinear,
                MagnificationFilter.Bilinear);
        ts.setTexture( t1, 0 );
        //t3.set;
        //t3.setWrap( WrapMode.BorderClamp );        
        
        t1.setApply( ApplyMode.Combine );
        t1.setCombineFuncRGB( CombinerFunctionRGB.Modulate );
        t1.setCombineSrc0RGB( CombinerSource.CurrentTexture );
        t1.setCombineOp0RGB( CombinerOperandRGB.SourceColor );
        t1.setCombineSrc1RGB( CombinerSource.PrimaryColor );
        t1.setCombineOp1RGB( CombinerOperandRGB.SourceColor );
        t1.setCombineScaleRGB( CombinerScale.One );
        

        t2.setApply( ApplyMode.Combine );
        t2.setCombineFuncRGB( CombinerFunctionRGB.AddSigned );
        t2.setCombineSrc0RGB( CombinerSource.CurrentTexture );
        t2.setCombineOp0RGB( CombinerOperandRGB.SourceColor );
        t2.setCombineSrc1RGB( CombinerSource.Previous );
        t2.setCombineOp1RGB( CombinerOperandRGB.SourceColor );
        t2.setCombineScaleRGB( CombinerScale.One );
        
        t3.setApply( ApplyMode.Combine );
        t3.setCombineFuncRGB( CombinerFunctionRGB.AddSigned);
        t3.setCombineSrc0RGB( CombinerSource.CurrentTexture );
        t3.setCombineOp0RGB( CombinerOperandRGB.SourceColor );
        t3.setCombineSrc1RGB( CombinerSource.Previous );
        t3.setCombineOp1RGB( CombinerOperandRGB.SourceColor );
        t3.setCombineScaleRGB( CombinerScale.One);
        
        page.setRenderState( ts );        
    	
        final StaticPhysicsNode staticNode = getPhysicsSpace().createStaticNode();

        staticNode.attachChild( page );

        staticNode.getLocalTranslation().set( 0, -150, 0 );
        
        inGameStateNode.attachChild( staticNode );
        staticNode.generatePhysicsGeometry();
        //initialize OBBTree of terrain
        inGameStateNode.findPick( new Ray( new Vector3f(), new Vector3f( 1, 0, 0 ) ), new BoundingPickResults() );      
        this.terrain = page;

        /*
        //SE AGREGA ARBOLITO
        //TODO sacar esto de aca y meterlo en una funcion que cargue el bosque.
        //Ponerlo despues de que se cargue el auto
        BlendState blendState = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        blendState.setBlendEnabled( true );
        blendState.setSourceFunction( BlendState.SourceFunction.SourceAlpha );
        blendState.setDestinationFunction( BlendState.DestinationFunction.OneMinusSourceAlpha );
        blendState.setTestEnabled( true );
        blendState.setTestFunction( BlendState.TestFunction.GreaterThanOrEqualTo );
        blendState.setEnabled( true );                
        
        Quad q = new Quad("Quad");
        TextureState ts2 = display.getRenderer().createTextureState();
        ts2.setEnabled(true);
        Texture t4 = TextureManager.loadTexture(
            RallyGame.class.getClassLoader().getResource(
            "images/tree1.png"), 
            MinificationFilter.Trilinear,
            MagnificationFilter.Bilinear );
        
        ts2.setTexture(t4);
        //t3.setCombineFuncAlpha(CombinerFunctionAlpha.)
        
        q.setRenderState(ts2);
        q.setRenderState(blendState);
        q.updateRenderState();
     
        BillboardNode billboard = new BillboardNode("Billboard");
        billboard.setAlignment(BillboardNode.AXIAL);
        billboard.attachChild(q);   
        billboard.setLocalScale(100f);
        billboard.setLocalTranslation(terrain.getWorldBound().getCenter().x,
        		terrain.getWorldBound().getCenter().y+65,
        		terrain.getWorldBound().getCenter().z);
        inGameStateNode.attachChild(billboard);        
        */
        
        
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
            car.setPosition( 0, 50, 0 );
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
	
	public TerrainPage getTerrain() {
		return terrain;
	}
}
