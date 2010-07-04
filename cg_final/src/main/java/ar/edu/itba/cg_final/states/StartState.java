package ar.edu.itba.cg_final.states;

import java.util.Date;

import ar.edu.itba.cg_final.RallyGame;

import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.scene.Text;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameStateManager;

public class StartState extends RallyGameState {

	private long start;
	private Text counter;
	
	public StartState() {
		this.setName("StartGame");
		this.stateNode.setName(this.getName());
	}
	
	private void refreshLabel(String text) {
		this.stateNode.detachChildNamed("counter");
		counter = Text.createDefaultTextLabel("counter", text);
		counter.setLocalScale(10f);
		counter.setLocalTranslation(
				(DisplaySystem.getDisplaySystem().getWidth() - counter.getWidth())/2,
				(DisplaySystem.getDisplaySystem().getHeight() - counter.getHeight())/2, 0);
    	this.stateNode.attachChild(counter);
		rootNode.updateRenderState();
    	return;
	}
	
	@Override
	public void activated() {
		start = new Date().getTime();
		// Agregamos el stateNode al rootNode
		stateNode.setName(this.getName());
		rootNode.attachChild(this.stateNode);
		GameStateManager.getInstance().activateChildNamed("InGame");
		KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);
		KeyBindingManager.getKeyBindingManager().set("toggle_pause", KeyInput.KEY_P);
		KeyBindingManager.getKeyBindingManager().set("screenshot", KeyInput.KEY_0);
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
		long diff = (new Date().getTime()) - start;
		
		if ((diff > 500) && (diff < 2000)) {
			// Get Ready!
			refreshLabel("Ready?");
		} else if ((diff > 2000) && (diff < 3000)) {
			// Paso 1 segundo
			refreshLabel("3");
		} else if ((diff > 3000) && (diff < 4000)) {
			// Pasaron 2 segundos
			refreshLabel("2");
		} else if ((diff > 4000) && (diff < 5000)) {
			// Pasaron 3 segundos
			refreshLabel("1");
		} else if ((diff > 5000) && (diff < 6500)) {
			// Start!!
			refreshLabel("GO!");
			RallyGame.getInstance().setPause(false);
			
		} else if (diff > 6500) {
			// Sacar el cartel
			GameStateManager.getInstance().deactivateChildNamed(this.getName());
		}

	}

}
