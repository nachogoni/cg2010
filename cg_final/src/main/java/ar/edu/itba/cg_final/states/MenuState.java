package ar.edu.itba.cg_final.states;

import ar.edu.itba.cg_final.map.SkyBox;

import com.jme.input.KeyBindingManager;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameStateManager;

public class MenuState extends RallyGameState {

	private Skybox skyBox;

	@Override
	public void activated() {
		this.rootNode.attachChild(this.stateNode);
		this.rootNode.updateRenderState();
	}

	@Override
	public void deactivated() {
		rootNode.detachChild(this.stateNode);
		rootNode.updateRenderState();
	}

	@Override
	public void initGameState(Node rootNode) {
		super.initGameState(rootNode);
		this.setName("Menu");
		skyBox = SkyBox.getNightSkyBox(DisplaySystem.getDisplaySystem(), 200,
				200, 200);
		stateNode.attachChild(skyBox);
		stateNode.setName(this.getName());
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(float arg0) {
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("next",
				false)) {
			GameStateManager.getInstance().deactivateChildNamed(this.getName());
			GameStateManager.getInstance().activateChildNamed("PreLoad");
		}
	}

}
