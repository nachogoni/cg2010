package ar.edu.itba.cg_final.states;

import ar.edu.itba.cg_final.RallyGame;

import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.scene.Node;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;

public abstract class RallyGameState  extends GameState {

	protected Node stateNode = new Node();
	protected Node rootNode;
	
	{
		this.rootNode = RallyGame.getInstance().getRootNode();
	}

	public RallyGameState() {
		super();
		KeyBindingManager.getKeyBindingManager().add("show menu", KeyInput.KEY_ESCAPE);	
	}
	
	@Override
	public void update(float tpf) {

	}
	
	// Genera una llamada al estado cuando es activdado
	public abstract void activated();

	// Genera una llamada al estado cuando es desactivdado
	public abstract void deactivated();

	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		
		if (active) {
			activated();
		} else {
			deactivated();
		}
		
	}
	
	public Node getStateNode() {
		return stateNode;
	}
	
}
