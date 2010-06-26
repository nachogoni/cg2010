package ar.edu.itba.cg_final.states;

import ar.edu.itba.cg_final.map.SkyBox;

import com.jme.input.KeyBindingManager;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameStateManager;

public class PreLoadState extends RallyGameState {

	private Skybox skyBox;

	@Override
	public void activated() {
		rootNode.attachChild(this.stateNode);
		rootNode.updateRenderState();
	}

	@Override
	public void deactivated() {
		rootNode.detachChild(this.stateNode);
		rootNode.updateRenderState();
	}

	@Override
	public void initGameState(Node rootNode) {
		super.initGameState(rootNode);
		this.setName("PreLoad");
		skyBox = SkyBox.getRedSkyBox(DisplaySystem.getDisplaySystem(), 200,
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
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("prev",
				false)) {
			GameStateManager.getInstance().deactivateChildNamed(this.getName());
			GameStateManager.getInstance().activateChildNamed("InGame");
		}
	}

}
