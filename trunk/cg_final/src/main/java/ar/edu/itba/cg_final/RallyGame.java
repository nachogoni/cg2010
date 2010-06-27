package ar.edu.itba.cg_final;

import java.util.logging.Logger;

import ar.edu.itba.cg_final.states.InGameState;
import ar.edu.itba.cg_final.states.PreLoadState;
import ar.edu.itba.cg_final.states.MenuState;
import ar.edu.itba.cg_final.states.RallyGameState;

import com.jme.app.BaseSimpleGame;
import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.renderer.Renderer;
import com.jmex.game.state.GameStateManager;
import com.jmex.physics.PhysicsDebugger;
import com.jmex.physics.PhysicsSpace;

public class RallyGame extends BaseSimpleGame {

	
	
	private GameStateManager gameStateManager;

	public static void main(String[] args) {
		RallyGame app = new RallyGame();
		app.setConfigShowMode(ConfigShowMode.ShowIfNoConfig);
		app.start();
	}

	@Override
	protected void simpleInitGame() {

		RallyGameState menuState = new MenuState();
		RallyGameState preLoadState = new PreLoadState();
		RallyGameState inGameState = new InGameState();

		GameStateManager.create();

		gameStateManager = GameStateManager.getInstance();

		menuState.initGameState(rootNode);
		preLoadState.initGameState(rootNode);
		inGameState.initGameState(rootNode);

		menuState.setActive(true);
		preLoadState.setActive(false);
		inGameState.setActive(false);

		gameStateManager.attachChild(menuState);
		gameStateManager.attachChild(preLoadState);
		gameStateManager.attachChild(inGameState);

		KeyBindingManager.getKeyBindingManager().removeAll();
		KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);
		KeyBindingManager.getKeyBindingManager().set("next", KeyInput.KEY_F);
		KeyBindingManager.getKeyBindingManager().set("prev", KeyInput.KEY_G);
		KeyBindingManager.getKeyBindingManager().set("post", KeyInput.KEY_H);
		
	}
	
	
	
	
	
	
	
	
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

}
