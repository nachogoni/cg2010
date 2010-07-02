package ar.edu.itba.cg_final.states;

import com.jme.input.KeyBindingManager;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jmex.game.state.GameStateManager;

public class MenuState extends RallyGameState {

	@Override
	public void activated() {
        Text label = Text.createDefaultTextLabel( "instructions",
        	"Apreta F para cargar el juego" );
		label.setLocalTranslation( 0, 20, 0 );
		stateNode.attachChild( label );
		
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
