package ar.edu.itba.cg_final.states;

import java.util.Date;

import ar.edu.itba.cg_final.RallyGame;
import ar.edu.itba.cg_final.settings.Score;
import ar.edu.itba.cg_final.settings.Scores;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Text;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameStateManager;

public class FinishedState extends RallyGameState {

	long max = 12000;
	float i = 0;
	float delta = 0.02f;
	Vector3f p;
	Camera cam;
	boolean flag = true;
	private Text instText;
	private static final String INSTRUCTIONS_TEXT = "Press ESC to go to menu";

	public static final String STATE_NAME = "FinishedGame";

	public FinishedState() {
		this.setName(STATE_NAME);
		stateNode.setName(this.getName());
		addInstructions();
	}

	private void addInstructions() {
		instText = Text.createDefaultTextLabel("Finish Instructions",
				INSTRUCTIONS_TEXT);
		instText.setTextColor(ColorRGBA.white);
		instText.setLightCombineMode(LightCombineMode.Off);
		instText.setCullHint(CullHint.Never);
		float width = instText.getWidth();
		int dispWidth = DisplaySystem.getDisplaySystem().getWidth();
		if (width > dispWidth) {
			instText.setLocalScale(dispWidth / width);
			width = dispWidth;
		}
		instText.setLocalTranslation((dispWidth - width) / 2.0f, DisplaySystem
				.getDisplaySystem().getHeight() - 20, 0);

		stateNode.attachChild(instText);
	}

	@Override
	public void activated() {
		p = RallyGame.getInstance().getPlayerCar().getPosition();
		cam = RallyGame.getInstance().getCamara();
		// Agregamos el stateNode al rootNode
		stateNode.setName(this.getName());
		rootNode.attachChild(this.stateNode);
		rootNode.updateRenderState();
		setActions();
	}

	public void setActions() {
		RallyGame rg = RallyGame.getInstance();

		InputHandler input = new InputHandler();
		input.addAction(new InputAction() {

			public void performAction(InputActionEvent evt) {
				RallyGame.getInstance().setPause(false);
				RallyGame.getInstance().setGameOver();
				((InGameState) GameStateManager.getInstance().getChild(
						InGameState.STATE_NAME)).getStateNode()
						.detachAllChildren();
				GameStateManager.getInstance().deactivateChildNamed(
						FinishedState.STATE_NAME);
				GameStateManager.getInstance().activateChildNamed(
						MenuState.STATE_NAME);
			}
		}, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_ESCAPE,
				InputHandler.AXIS_NONE, false);
		rg.setInputHandler(input);
	}

	@Override
	public void deactivated() {
		RallyGame.getInstance().setInputHandler(new InputHandler());
		rootNode.detachChild(this.stateNode);
		rootNode.updateRenderState();
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void render(float arg0) {
	}

	@Override
	public void update(float arg0) {
		super.update(arg0);

		float x = p.x + (float) Math.cos(i += delta) * 200;
		float z = p.z + (float) Math.sin(i += delta) * 200;
		cam.setLocation(new Vector3f(x, 80, z));
		cam.lookAt(p, new Vector3f(0, 1, 0));

		if (flag) {
			long raceTime = RallyGame.getInstance().getRaceTime();
			Scores scores = Scores.getInstance();
			Score userScore = new Score(new Date(), raceTime);
			scores.addScore(userScore);
			flag = false;
		}
	}

}
