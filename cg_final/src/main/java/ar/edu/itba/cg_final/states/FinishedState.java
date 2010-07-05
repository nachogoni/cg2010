package ar.edu.itba.cg_final.states;

import java.util.Date;

import ar.edu.itba.cg_final.RallyGame;

import com.jme.scene.Text;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameStateManager;


public class FinishedState extends RallyGameState {
	
	private long start;
	private Text counter;
	
	public FinishedState() {
		this.setName("FinishedGame");
		stateNode.setName(this.getName());
	}
	
	private void refreshLabel(String text) {
		StringBuffer lastText = counter.getText();
		lastText.replace(0, lastText.length(), text);
		counter.setLocalTranslation(
				(DisplaySystem.getDisplaySystem().getWidth() - counter.getWidth())/2,
				(DisplaySystem.getDisplaySystem().getHeight() - counter.getHeight())/2, 0);
		stateNode.updateRenderState();
    	return;
	}
	
	@Override
	public void activated() {
		start = new Date().getTime();
		counter = Text.createDefaultTextLabel("counter", "");
		counter.setLocalScale(10f);
    	this.stateNode.attachChild(counter);
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
	public void cleanup() {
	}

	@Override
	public void render(float arg0) {
	}

	@Override
	public void update(float arg0) {
		super.update(arg0);
		long diff = (new Date().getTime()) - start;
		refreshLabel(String.valueOf((int)diff));
		if (diff > 5000) {
			RallyGame.getInstance().setPause(false);
			((InGameState)GameStateManager.getInstance().getChild("InGame")).getStateNode().detachAllChildren();
			GameStateManager.getInstance().activateChildNamed("Menu");
			GameStateManager.getInstance().deactivateChildNamed(this.getName());
		}
	}

}
