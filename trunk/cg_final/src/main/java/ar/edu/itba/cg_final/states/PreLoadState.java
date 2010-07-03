package ar.edu.itba.cg_final.states;

import ar.edu.itba.cg_final.RallyGame;

import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jmex.game.state.GameStateManager;

public class PreLoadState extends RallyGameState {

	private Text info;
	private RallyGame game;
	private Integer task = 0;
	private Node inGameNode;
	private InGameState inGame;
	private boolean updateable = false;
	
	public PreLoadState() {
		this.setName("PreLoad");
		stateNode.setName(this.getName());
	}
	
	private void refreshLabel(String text) {
		this.stateNode.detachChildNamed("loaderInfo");
		info = Text.createDefaultTextLabel("loaderInfo", "Cargando: " + text + "...");
    	info.setLocalTranslation(  300, 400, 0);
    	this.stateNode.attachChild(info);
    	return;
	}
	
	@Override
	public void activated() {
		
		// Hacer desde el principio
		this.task = 0;
		
		rootNode.attachChild(this.stateNode);
		rootNode.updateRenderState();
		
		// Obtenemos el stateNode del InGameState para ir asignandole los modelos a crear
		inGame = (InGameState)GameStateManager.getInstance().getChild("InGame");
		inGameNode = inGame.getStateNode();

		// Borramos todos los nodos que contenga el stateNode del InGameState
		inGameNode.detachAllChildren();

		refreshLabel("...");
    	
    	// Tomamos la instancia del RallyGame
    	game = RallyGame.getInstance();
    	
    	game.setPause(true);
    	
    	updateable = true;
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
		if (updateable ) {		
			switch(task)
			{
			case 0:
				// Actualizamos la fisica
				refreshLabel("Fisica");
				game.tunePhysics(inGameNode);
				break;
			case 10:
				float xExtent = 600;
				float yExtent = 600;
				float zExtent = 600;
				// Cargamos el skybox
				refreshLabel("Skybox");
				game.createSkyBox(inGameNode, "red", xExtent, yExtent, zExtent);

				break;
			case 15:
				// Cargamos el terreno
				refreshLabel("Terreno");
				game.createTerrain(inGameNode);
				break;
			case 20:
				// Cargamos los autos
				refreshLabel("Autos");
				game.createCar(inGameNode, "PlayerCar");
				//TODO: Actualizar la posicion del auto en base a la altura del terreno...
				break;
			case 30:
				// Cargamos la configuracion del input
				refreshLabel("Teclas");
				game.initInput(inGameNode);
				break;
			case 40:
				//TODO: vienen de las settings...

			case 50:
				// Cargamos la pista
//				refreshLabel("Pista");
				// TODO
				break;
			case 60:
				// Creamos la etiqueta (????)
				refreshLabel("Etiqueta");
				game.createText(inGameNode);
				break;
			case 70:
				// Otros...
				refreshLabel("Audio");
				game.initAudio();
				break;
			case 80:
				refreshLabel("Enviroment");
				game.buildEnvironment(inGameNode);
				break;
			case 90:
				refreshLabel("Flag");
				game.buildFlag(inGameNode);
				break;
//			case 100:
//				refreshLabel("");
//				break;
//			case 110:
//				refreshLabel("");
//				break;
//			case 120:
//				refreshLabel("");
//				break;
//			case 130:
//				refreshLabel("");
//				break;
//			case 140:
//				refreshLabel("");
//				break;
//			case 150:
//				refreshLabel("");
//				break;
//			case 160:
//				refreshLabel("");
//				break;
//			case 170:
//				refreshLabel("");
//				break;
//			case 180:
//				refreshLabel("");
//				break;
			case 190:
				// Saltamos al proximo estado: InGameState
				refreshLabel("La partida!");
				GameStateManager.getInstance().deactivateChildNamed(this.getName());
				GameStateManager.getInstance().activateChildNamed("StartGame");
				break;
			default:
				break;	
			}
			// Tarea terminada -> saltar a la siguiente tarea...
			this.task++;
		}
	}
	

}
