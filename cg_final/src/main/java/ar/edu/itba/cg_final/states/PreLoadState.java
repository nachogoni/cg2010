package ar.edu.itba.cg_final.states;

import ar.edu.itba.cg_final.RallyGame;
import ar.edu.itba.cg_final.controller.Game;
import ar.edu.itba.cg_final.map.RallySkyBox;

import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameStateManager;

public class PreLoadState extends RallyGameState {

	private RallyGame theGame;
	
	@Override
	public void activated() {
		// Obtenemos el stateNode del InGameState para ir asignandole los modelos a crear
		InGameState inGame = (InGameState)GameStateManager.getInstance().getChild("InGame");
		Node inGameNode = inGame.getStateNode();
		
		// Borramos todos los nodos que contenga el stateNode del InGameState
		inGameNode.detachAllChildren();
		
		// 
		Game game = Game.getInstance();

		// ** Cargamos todo lo del GlobalSettings
		float xExtent = 200;
		float yExtent = 200;
		float zExtent = 200;
		
		// ** Cargamos todo lo de GameUserSettings (TODO: Sacar todo de lo que setea el menu)
		

		// ** Cargamos todo el entorno del juego
		theGame.tunePhysics(inGameNode);
		
		// Cargamos el auto
		theGame.createCar(inGameNode);

		// Cargamos la configuracion del input
		theGame.initInput(inGameNode);
		
		// Cargamos el skybox
		game.setSkyBox(RallySkyBox.getRedSkyBox(DisplaySystem
				.getDisplaySystem(), xExtent, yExtent, zExtent));

		
		// Cargamos el terreno
		theGame.createTerrain(inGameNode);
		
		// Cargamos la pista
		

		
		

		// Otros...
		theGame.createText(inGameNode);

		
		

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
	
	public void setRallyGame(RallyGame theGame) {
		this.theGame = theGame;
	}
}
