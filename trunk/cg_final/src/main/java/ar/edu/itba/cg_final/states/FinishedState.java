package ar.edu.itba.cg_final.states;

import java.util.Date;

import ar.edu.itba.cg_final.RallyGame;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jmex.game.state.GameStateManager;


public class FinishedState extends RallyGameState {
	
	private long start;

	long max = 12000;
    float i = 0;
    float delta = 0.02f;
	Vector3f p;
	Camera cam;
	
	public static final String STATE_NAME = "FinishedGame"; 
	
	public FinishedState() {
		this.setName(STATE_NAME);
		stateNode.setName(this.getName());
	}
		
	@Override
	public void activated() {
		p = RallyGame.getInstance().getPlayerCar().getPosition();
		cam = RallyGame.getInstance().getCamara();
		start = new Date().getTime();
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

		if (diff > max) {
			RallyGame.getInstance().setPause(false);
			((InGameState)GameStateManager.getInstance().getChild("InGame")).getStateNode().detachAllChildren();
			GameStateManager.getInstance().activateChildNamed("Menu");
			GameStateManager.getInstance().deactivateChildNamed(this.getName());
		} else {
	        float x = p.x + (float)Math.cos(i+=delta)*200;
	        float z = p.z + (float)Math.sin(i+=delta)*200;
	        cam.setLocation(new Vector3f(x, 80, z));
	        cam.lookAt(p, new Vector3f(0,1,0));
		}
	}

}
