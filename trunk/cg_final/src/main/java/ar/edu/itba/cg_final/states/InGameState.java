package ar.edu.itba.cg_final.states;

import ar.edu.itba.cg_final.RallyGame;

import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jmex.terrain.TerrainPage;

public class InGameState extends RallyGameState {

	@Override
	public void activated() {
		
		// Cargamos todo lo necesario en el stateNode
		stateNode.attachChild(RallyGame.getInstance().getSkyBox());
		
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
		//TODO arreglar la condicion de la camara porque anda media chota
		/*RallyGame game = RallyGame.getInstance();
		Camera cam = game.getCamara();
		TerrainPage terrain = game.getTerrain();
        //We don't want the chase camera to go below the world, so always keep 
        //it 2 units above the level.
        if(cam.getLocation().y < (terrain.getHeight(cam.getLocation()))) {
            cam.getLocation().y = terrain.getHeight(cam.getLocation());
            cam.update();
        }
        rootNode.updateGeometricState(arg0, true);*/
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
