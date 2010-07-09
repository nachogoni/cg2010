package ar.edu.itba.cg_final.states;

import ar.edu.itba.cg_final.RallyGame;
import ar.edu.itba.cg_final.settings.GameUserSettings;
import ar.edu.itba.cg_final.settings.GlobalSettings;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameStateManager;

public class PreLoadState extends RallyGameState {
	
	private Text info;
	private RallyGame game;
	private Integer task = 0;
	private Node inGameNode;
	private InGameState inGame;
	private boolean updateable = false;
	GlobalSettings gs;
	GameUserSettings gus;
	
	public static final String STATE_NAME = "PreLoad";

	private int width = DisplaySystem.getDisplaySystem().getWidth();
	private int height = DisplaySystem.getDisplaySystem().getHeight();

	public PreLoadState() {
		this.setName(STATE_NAME);
		stateNode.setName(this.getName());
	}

	private void refreshLabel(String text) {
		StringBuffer lastText = info.getText();
		lastText.replace(0, lastText.length(), "Cargando: " + text + "...");
		return;
	}

	@Override
	public void activated() {

		gs = GlobalSettings.getInstance();
		gus = GameUserSettings.getInstance();

		// Hacer desde el principio
		this.task = 0;

		rootNode.attachChild(this.stateNode);
		rootNode.updateRenderState();

		// Obtenemos el stateNode del InGameState para ir asignandole los
		// modelos a crear
		inGame = (InGameState) GameStateManager.getInstance()
				.getChild(InGameState.STATE_NAME);
		inGameNode = inGame.getStateNode();

		// Borramos todos los nodos que contenga el stateNode del InGameState
		inGameNode.detachAllChildren();

		// Creamos el texto de informacion
		info = Text.createDefaultTextLabel("loaderInfo", "Cargando...");
		info.setLocalTranslation(width * 0.35f, height * 0.5f, 0);
		info.setTextColor(new ColorRGBA(255.0f/255.0f, 255.0f/255.0f, 255.0f/255.0f, 0.95f));
		info.setCullHint( Spatial.CullHint.Never );
		info.setRenderState( Text.getDefaultFontTextureState() );
		info.setRenderState( Text.getFontBlend() );
		info.setLightCombineMode(LightCombineMode.Off);
		this.stateNode.attachChild(info);

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
		if (updateable) {
			switch (task) {
			case 0:
				// Actualizamos la fisica
				refreshLabel("Fisica");
				game.tunePhysics(inGameNode);
				break;
			case 10:
				// Cargamos el skybox
				refreshLabel("Skybox");
				game.createSkyBox(inGameNode, gs, gus);
				break;
			case 15:
				// Cargamos el terreno
				refreshLabel("Terreno");
				game.createRallyTrack(inGameNode, gs, gus);
				break;
			case 20:
				// Audio
				refreshLabel("Audio");
				game.initAudio(gs);
				break;
			case 30:
				// Cargamos los autos
				refreshLabel("Autos");
				game.createCar(inGameNode, gs);
				break;
			case 40:
				// Cargamos la configuracion del input
				//refreshLabel("Teclas");
				//game.initInput(inGameNode);
				break;
			case 50:
				// Cargamos la pista
				refreshLabel("Arboles");
				game.createForest(inGameNode);
				break;
			case 60:
				refreshLabel("CheckPoints");
				game.buildCheckpoint(inGameNode, gs, gus);
				break;
			case 70:
				// Saltamos al proximo estado: InGameState
				GameStateManager.getInstance().deactivateChildNamed(this.getName());
				GameStateManager.getInstance().activateChildNamed(StartState.STATE_NAME);
				break;
			default:
				break;
			}
			// Tarea terminada -> saltar a la siguiente tarea...
			this.task++;
		}
	}

}
