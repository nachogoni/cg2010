package ar.edu.itba.cg_final.states;

import ar.edu.itba.cg_final.RallyGame;
import ar.edu.itba.cg_final.states.utils.RallyFadeOutIn;

import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jmex.game.state.GameState;

public abstract class RallyGameState  extends GameState {

	protected Node stateNode = new Node();
	protected Node rootNode;
	private RallyFadeOutIn fio;
	
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


	protected void addFadeController(String string, Node rootNode, Node outStateNode, Node inStateNode, ColorRGBA colorRGBA, float speed) {
		fio = new RallyFadeOutIn(string, rootNode, outStateNode, inStateNode, colorRGBA, speed);
        fio.attachTo(rootNode);
	}
	
	protected RallyFadeOutIn getFadeOutIn(){
		return fio;
	}
	
	protected void fade(float timeF){
		float time = timeF * fio.getSpeed();
		time *= 8;
		ColorRGBA color = fio.getFadeColor();
		if (fio.getCurrentStage() == 0) {
			color.a += time;
			fio.setFadeColor(color);
			if (fio.getFadeColor().a >= 1.0f) {
				fio.detachChild(fio.getFadeOutNode());
				fio.attachChild(fio.getFadeInNode());
				fio.setCurrentStage(fio.getCurrentStage() + 1);
			}
		} else if (fio.getCurrentStage() == 1) {
			color.a -= time;
			fio.setFadeColor(color);
			if (fio.getFadeColor().a <= 0.0f) {
				fio.setCurrentStage(fio.getCurrentStage() + 1);
			}
		} else if (fio.getCurrentStage() == 2) {
			fio.detachFromNode();
			fio.setFinished();
		}
	}


}
