package ar.edu.itba.cg_final.states;

import ar.edu.itba.cg_final.RallyGame;
import ar.edu.itba.cg_final.map.RallySkyBox;

import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameStateManager;

public class PreLoadState extends RallyGameState {

	@Override
	public void activated() {
		// Obtenemos el stateNode del InGameState para ir asignandole los modelos a crear
		InGameState inGame = (InGameState)GameStateManager.getInstance().getChild("InGame");
		Node inGameNode = inGame.getStateNode();
		
		// Borramos todos los nodos que contenga el stateNode del InGameState
		inGameNode.detachAllChildren();
		
		// 
		RallyGame game = RallyGame.getInstance();
		
		// ** Cargamos todo lo del GlobalSettings
		float xExtent = 1000;
		float yExtent = 1000;
		float zExtent = 1000;
		
		// ** Cargamos todo lo de GameUserSettings (TODO: Sacar todo de lo que setea el menu)
		

		// ** Cargamos todo el entorno del juego
		game.tunePhysics(inGameNode);
		
		// Cargamos el auto
		game.createCar(inGameNode);

		// Cargamos la configuracion del input
		game.initInput(inGameNode);
		
		// Cargamos el skybox
		stateNode.attachChild(RallySkyBox.getRedSkyBox(DisplaySystem
				.getDisplaySystem(), xExtent, yExtent, zExtent));
		
		// Cargamos el terreno
		game.createTerrain(inGameNode);
		
		// Cargamos la pista

		// Otros...
		game.createText(inGameNode);


		// Actualizamos el rootNode con el stateNode
        rootNode.attachChild(this.stateNode);
		rootNode.updateRenderState();
		
		// ** Saltamos al proximo estado: InGameState
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
