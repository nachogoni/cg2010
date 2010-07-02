package ar.edu.itba.cg_final;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import ar.edu.itba.cg_final.states.InGameState;
import ar.edu.itba.cg_final.states.MenuState;
import ar.edu.itba.cg_final.states.PreLoadState;
import ar.edu.itba.cg_final.states.RallyGameState;
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
import com.jme.math.Ray;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.CullState.Face;
import com.jme.scene.state.FogState.DensityFunction;
import com.jme.scene.state.FogState.Quality;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.game.state.GameStateManager;
import com.jmex.physics.PhysicsDebugger;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.FaultFractalHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;

public class RallyGame extends BaseSimpleGame {
	
	private GameStateManager gameStateManager;
	private PhysicsSpace physicsSpace;
	protected InputHandler cameraInputHandler;
	protected boolean showPhysics;
	private float physicsSpeed = 1;
	
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
		RallyGameState inGameState = new InGameState();

		GameStateManager.create();

		gameStateManager = GameStateManager.getInstance();

		menuState.initGameState(rootNode);
		preLoadState.initGameState(rootNode);
		inGameState.initGameState(rootNode);

		menuState.setActive(true);
		preLoadState.setActive(false);
		inGameState.setActive(false);
		
		// Seteamos el juego
		preLoadState.setRallyGame(this);

		gameStateManager.attachChild(menuState);
		gameStateManager.attachChild(preLoadState);
		gameStateManager.attachChild(inGameState);

		KeyBindingManager.getKeyBindingManager().removeAll();
		KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);
		KeyBindingManager.getKeyBindingManager().set("next", KeyInput.KEY_F);
		KeyBindingManager.getKeyBindingManager().set("prev", KeyInput.KEY_G);
		KeyBindingManager.getKeyBindingManager().set("post", KeyInput.KEY_H);
		KeyBindingManager.getKeyBindingManager().set("toggle_pause", KeyInput.KEY_P);
	}
	
	// Metodos para la creacion de los elementos del juego (usados en el preloader)
	
	
	
	
	
	
	
	
	



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

    public void createCar(Node inGameStateNode) {
    	this.car = new Car( getPhysicsSpace() );
        inGameStateNode.attachChild( car );
    }
	
    public void createTerrain(Node inGameStateNode) {
    	
    	statNode.setRenderQueueMode( Renderer.QUEUE_ORTHO );

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

        FaultFractalHeightMap heightMap = new FaultFractalHeightMap( 257, 32, 0, 255,
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
        Texture t1 = TextureManager.loadTexture(
                pt.getImageIcon().getImage(),
                MinificationFilter.Trilinear,
                MagnificationFilter.Bilinear,
                true );
        ts.setTexture( t1, 0 );

        Texture t2 = TextureManager.loadTexture( RallyGameCar.class.getClassLoader().
                getResource(
                "texture/Detail.jpg" ),
                MinificationFilter.Trilinear,
                MagnificationFilter.Bilinear );
        ts.setTexture( t2, 1 );
        t2.setWrap( WrapMode.Repeat );

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
        page.setRenderState( ts );

        FogState fs = DisplaySystem.getDisplaySystem().getRenderer().createFogState();
        fs.setDensity( 0.5f );
        fs.setEnabled( true );
        fs.setColor( new ColorRGBA( 0.5f, 0.5f, 0.5f, 0.5f ) );
        fs.setEnd( 1000 );
        fs.setStart( 500 );
        fs.setDensityFunction( DensityFunction.Linear );
        fs.setQuality(Quality.PerVertex);
        page.setRenderState( fs );
    	
        final StaticPhysicsNode staticNode = getPhysicsSpace().createStaticNode();

        staticNode.attachChild( page );

        staticNode.getLocalTranslation().set( 0, -150, 0 );

        inGameStateNode.attachChild( staticNode );
        staticNode.generatePhysicsGeometry();
        //initialize OBBTree of terrain
        inGameStateNode.findPick( new Ray( new Vector3f(), new Vector3f( 1, 0, 0 ) ), new BoundingPickResults() );        
        
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
	
	
	
}
