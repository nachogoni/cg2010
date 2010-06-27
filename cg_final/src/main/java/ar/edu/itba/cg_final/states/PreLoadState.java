package ar.edu.itba.cg_final.states;

import ar.edu.itba.cg_final.controller.Game;
import ar.edu.itba.cg_final.map.RallySkyBox;

import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameStateManager;

public class PreLoadState extends RallyGameState {

	@Override
	public void activated() {
        Text label = Text.createDefaultTextLabel( "instructions", "Cargando..." );
        label.setLocalTranslation( 0, 20, 0 );
        stateNode.attachChild( label );
		stateNode.updateRenderState();

        rootNode.attachChild(this.stateNode);
		rootNode.updateRenderState();

		
		// TODO: Ver donde va esto

		Game game = Game.getInstance();

		// TODO: tomarlos desde GlobalSettings
		float xExtent = 200;
		float yExtent = 200;
		float zExtent = 200;
		
		// Cargamos todo lo de GameUserSettings
		game.setSkyBox(RallySkyBox.getRedSkyBox(DisplaySystem
				.getDisplaySystem(), xExtent, yExtent, zExtent));

		// Cargamos todo lo del GlobalSettings
		// Cargamos todo el entorno del juego
		// Saltamos al proximo estado: InGameState
		GameStateManager.getInstance().deactivateChildNamed(this.getName());
		GameStateManager.getInstance().activateChildNamed("InGame");

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

		// TODO: Podria tener una pantallita de cargando...
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void render(float arg0) {
	}

	@Override
	public void update(float arg0) {
	}

}
