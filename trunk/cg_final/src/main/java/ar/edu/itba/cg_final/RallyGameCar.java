package ar.edu.itba.cg_final;

import java.net.MalformedURLException;
import java.util.logging.Logger;

import ar.edu.itba.cg_final.map.RallySkyBox;
import ar.edu.itba.cg_final.utils.ResourceLoader;
import ar.edu.itba.cg_final.vehicles.Car;

import com.jme.app.BaseSimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.image.Texture.WrapMode;
import com.jme.input.ChaseCamera;
import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.shape.Box;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jmex.physics.PhysicsDebugger;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;

public class RallyGameCar extends BaseSimpleGame {

    private PhysicsSpace physicsSpace;
    
    protected InputHandler cameraInputHandler;

    protected boolean showPhysics;

    private float physicsSpeed = 1;

    /**
     * @return speed set by {@link #setPhysicsSpeed(float)}
     */
    public float getPhysicsSpeed() {
        return physicsSpeed;
    }

    /**
     * The multiplier for the physics time. Default is 1, which means normal speed. 0 means no physics processing.
     * @param physicsSpeed new speed
     */
    public void setPhysicsSpeed( float physicsSpeed ) {
        this.physicsSpeed = physicsSpeed;
    }

    @Override
    protected void initSystem() {
        super.initSystem();

        /** Create a basic input controller. */
        cameraInputHandler = new FirstPersonHandler( cam, 50, 1 );
        input = new InputHandler();
        input.addToAttachedHandlers( cameraInputHandler );

        setPhysicsSpace( PhysicsSpace.create() );

        input.addAction( new InputAction() {
            public void performAction( InputActionEvent evt ) {
                if ( evt.getTriggerPressed() ) {
                    showPhysics = !showPhysics;
                }
            }
        }, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_V, InputHandler.AXIS_NONE, false );
    }

    /**
     * @return the physics space for this simple game
     */
    public PhysicsSpace getPhysicsSpace() {
        return physicsSpace;
    }

    /**
     * @param physicsSpace The physics space for this simple game
     */
	protected void setPhysicsSpace(PhysicsSpace physicsSpace) {
		if ( physicsSpace != this.physicsSpace ) {
			if ( this.physicsSpace != null )
	       		this.physicsSpace.delete();
			this.physicsSpace = physicsSpace;
		}
	}

    private boolean firstFrame = true;

    /**
     * Called every frame to update scene information.
     *
     * @param interpolation unused in this implementation
     * @see BaseSimpleGame#update(float interpolation)
     */
    @Override
    protected final void update( float interpolation ) {
        // disable input as we want it to be updated _after_ physics
        // in your application derived from BaseGame you can simply make the call to InputHandler.update later
        // in your game loop instead of this disabling and re-enabling

        super.update( interpolation );

        if ( !pause ) {
            float tpf = this.tpf;
            if ( tpf > 0.2 || Float.isNaN( tpf ) ) {
                Logger.getLogger( PhysicsSpace.LOGGER_NAME ).warning( "Maximum physics update interval is 0.2 seconds - capped." );
                tpf = 0.2f;
            }
            getPhysicsSpace().update( tpf * physicsSpeed );
        }
        
        input.update( tpf );

        if ( !pause ) {
            /** Call simpleUpdate in any derived classes of SimpleGame. */
            simpleUpdate();

            /** Update controllers/render states/transforms/bounds for rootNode. */
            rootNode.updateGeometricState( tpf, true );
            statNode.updateGeometricState( tpf, true );
        }

        if ( firstFrame )
        {
            // drawing and calculating the first frame usually takes longer than the rest
            // to avoid a rushing simulation we reset the timer
            timer.reset();
            firstFrame = false;
        }
    }

    @Override
    protected void updateInput() {
        // don't input here but after physics update
    }

    /**
     * This is called every frame in BaseGame.start(), after update()
     *
     * @param interpolation unused in this implementation
     * @see com.jme.app.AbstractGame#render(float interpolation)
     */
    @Override
    protected final void render( float interpolation ) {
        super.render( interpolation );

        preRender();

        Renderer r = display.getRenderer();

        /** Draw the rootNode and all its children. */
        r.draw( rootNode );

        /** Call simpleRender() in any derived classes. */
        simpleRender();
        
        /** Draw the stats node to show our stat charts. */
        r.draw( statNode );

        doDebug(r);
    }

    /**
     * 
     */
    protected void preRender() {

    }

    @Override
    protected void doDebug(Renderer r) {
        super.doDebug(r);

        if ( showPhysics ) {
            PhysicsDebugger.drawPhysics( getPhysicsSpace(), r );
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    Text label;
    
    
	Car car;
	ResetAction resetAction;
	Skybox skyBox;
	
	public static void main(String[] args) {
		RallyGameCar app = new RallyGameCar();
		app.setConfigShowMode(ConfigShowMode.ShowIfNoConfig);
		app.start();
	}
	
	@Override
	protected void simpleInitGame() {

		skyBox = RallySkyBox.getRedSkyBox(display, 500, 500, 500);
		rootNode.attachChild(skyBox);
		tunePhysics();
		createFloor();
		createCar();
		initInput();
		createText();
        // Lets be nice and update our scene graph properties.
        rootNode.updateRenderState();

        resetAction.performAction( null );
	}
	
	
	
    private void createText() {
        label = Text.createDefaultTextLabel( "instructions",
                "Use arrows to drive. Use the mouse wheel to control the chase camera. S to reset the car." );
        label.setLocalTranslation( 0, 20, 0 );
        statNode.attachChild( label );
        statNode.updateRenderState();
    }
	
    private void initInput() {
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
	
    private void tunePhysics() {
        getPhysicsSpace().setAutoRestThreshold( 0.2f );
        // Otherwise it would be VERY slow.
        setPhysicsSpeed( 4 );
    }

    private void createCar() {
        car = new Car( getPhysicsSpace() );
        rootNode.attachChild( car );
    }
	
    private void createFloor() {
        Spatial floorVisual = new Box( "floor", new Vector3f(), 10000, 0.1f, 10000 );
        floorVisual.setModelBound( new BoundingBox() );
        floorVisual.updateModelBound();
        StaticPhysicsNode floor = getPhysicsSpace().createStaticNode();
        floor.attachChild( floorVisual );
        floor.generatePhysicsGeometry();
        floor.setMaterial( Material.CONCRETE );
        floor.setLocalTranslation( new Vector3f( 0, -200f, 0 ) );
        rootNode.attachChild( floor );

        // Glueing the texture on the floor.
        final TextureState wallTextureState = display.getRenderer().createTextureState();
        try {
			wallTextureState.setTexture( TextureManager.loadTexture( ResourceLoader.getURL("texture/dirt.jpg"), 
					MinificationFilter.NearestNeighborLinearMipMap, MagnificationFilter.NearestNeighbor ) );
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        wallTextureState.getTexture().setScale( new Vector3f( 256, 256, 1 ) );
        wallTextureState.getTexture().setWrap( WrapMode.Repeat );
        floorVisual.setRenderState( wallTextureState );
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


