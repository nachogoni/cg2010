package ar.edu.itba.cg_final.states;

import ar.edu.itba.cg_final.controller.Game;

import com.jme.scene.Node;

public class InGameState extends RallyGameState {

	@Override
	public void activated() {
		
		// Cargamos todo lo necesario en el stateNode
		stateNode.attachChild(Game.getInstance().getSkyBox());
		
		// Agregamos el stateNode al rootNode
		stateNode.setName(this.getName());
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
		this.setName("InGame");
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void render(float arg0) {
	}

	@Override
	public void update(float arg0) {
//		if (KeyBindingManager.getKeyBindingManager().isValidCommand("post",
//				false)) {
//			GameStateManager.getInstance().deactivateChildNamed(this.getName());
//			GameStateManager.getInstance().activateChildNamed("Menu");
//		}
	}

}
