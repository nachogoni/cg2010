package ar.edu.itba.cg_final.states;

import ar.edu.itba.cg_final.RallyGame;
import ar.edu.itba.cg_final.settings.GameUserSettings;
import ar.edu.itba.cg_final.settings.GlobalSettings;

import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.shape.Quad;
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
	Quad bar;
	
	public static final String STATE_NAME = "PreLoad";

	private int width = DisplaySystem.getDisplaySystem().getWidth();
	private int height = DisplaySystem.getDisplaySystem().getHeight();

	public PreLoadState() {
		this.setName(STATE_NAME);
		stateNode.setName(this.getName());

		// Creamos el texto de informacion
		info = Text.createDefaultTextLabel("loaderInfo", "Cargando...");
		info.setLocalTranslation(width * 0.35f, height * 0.5f, 0);
		info.setTextColor(new ColorRGBA(255.0f/255.0f, 255.0f/255.0f, 255.0f/255.0f, 0.95f));
		info.setCullHint( Spatial.CullHint.Never );
		info.setRenderState( Text.getDefaultFontTextureState() );
		info.setRenderState( Text.getFontBlend() );
		info.setLightCombineMode(LightCombineMode.Off);
		stateNode.attachChild(info);

		// Barra de carga
    	bar = new Quad("bar", 6, 10);
    	bar.setRenderQueueMode(Renderer.QUEUE_ORTHO);
    	bar.setLightCombineMode(LightCombineMode.Off);
    	bar.setLocalTranslation(width/2,height/3,0);
    	stateNode.attachChild(bar);
		
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
				bar.getLocalScale().setX(task);
				game.tunePhysics(inGameNode);
				break;
			case 10:
				// Cargamos el skybox
				refreshLabel("Skybox");
				bar.getLocalScale().setX(task);
				game.createSkyBox(inGameNode, gs, gus);
				break;
			case 20:
				// Cargamos el terreno
				refreshLabel("Terreno");
				bar.getLocalScale().setX(task);
				game.createRallyTrack(inGameNode, gs, gus);
				break;
			case 30:
				// Audio
				refreshLabel("Audio");
				bar.getLocalScale().setX(task);
				game.initAudio(gs);
				break;
			case 40:
				// Cargamos los autos
				refreshLabel("Autos");
				bar.getLocalScale().setX(task);
				game.createCar(inGameNode, gs);
				break;
			case 50:
				// Cargamos la pista
				refreshLabel("Arboles");
				bar.getLocalScale().setX(task);
				game.createForest(inGameNode);
				break;
			case 60:
				refreshLabel("CheckPoints");
				bar.getLocalScale().setX(task);
				game.buildCheckpoint(inGameNode, gs, gus);
				break;
			case 70:
				// Saltamos al proximo estado: InGameState
				bar.getLocalScale().setX(task);
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
