package ar.edu.itba.cg_final.states;

import java.net.MalformedURLException;
import java.util.List;

import ar.edu.itba.cg_final.RallyGame;
import ar.edu.itba.cg_final.menu.RallyMenu;
import ar.edu.itba.cg_final.menu.RallyMenuPanel;
import ar.edu.itba.cg_final.menu.actions.IAction;
import ar.edu.itba.cg_final.menu.actions.MenuInputHandler;
import ar.edu.itba.cg_final.menu.items.RallyMenuItemBoolean;
import ar.edu.itba.cg_final.menu.items.RallyMenuItemInteger;
import ar.edu.itba.cg_final.menu.items.RallyMenuItemListString;
import ar.edu.itba.cg_final.menu.items.RallyMenuItemVoid;
import ar.edu.itba.cg_final.settings.GameUserSettings;
import ar.edu.itba.cg_final.settings.GlobalSettings;
import ar.edu.itba.cg_final.settings.Score;
import ar.edu.itba.cg_final.settings.Scores;
import ar.edu.itba.cg_final.utils.ResourceLoader;

import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.input.InputHandler;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Text;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.game.state.GameStateManager;

public class MenuState extends RallyGameState {

	private static final float TOLE = 0.2f;
	private RallyMenu menu;
	private RallyMenuPanel mainPanel;
	private RallyMenuPanel optionsPanel;
	private RallyMenuPanel newGamePanel;
	private RallyMenuPanel highScorePanel;
	private RallyMenuItemVoid back;
	private RallyMenuPanel confirmationPanel;
	private Text titleText;
	private Text instText;
	private ColorRGBA initialColor;
	private MenuInputHandler keyActions;
	AudioTrack menuSong;
	private boolean first;
	private RallyMenuItemVoid resumeGame;
	private RallyMenuItemBoolean highRes;
	private RallyMenuItemVoid newGame;
	final static String STATE_NAME = "Menu";
	private static final String INSTRUCTIONS_TEXT = "Use arrows to move between options. Left and right to change their values. Enter to go to submenu";

	public MenuState() {
		this.setName(STATE_NAME);
		stateNode.setName(this.getName());
		buildMenu();
		putBackGround(GlobalSettings.getInstance().getProperty("SKYBOX.NIGHT.NORTH"),
				GameUserSettings.getInstance());
		addTitle();
		addInstructions();
		stateNode.attachChild(menu.getMenuNode());
		menuSong = AudioSystem.getSystem().createAudioTrack(
				RallyGame.class.getClassLoader().getResource(
						GlobalSettings.getInstance().getProperty("MUSIC.MENU")), false);
		menuSong.setLooping(true);		
	}
	

	private void putBackGround(String backgroundImage, GameUserSettings gus) {
		int width = DisplaySystem.getDisplaySystem().getWidth();
		int height = DisplaySystem.getDisplaySystem().getHeight();
		
		MinificationFilter minF;
		MagnificationFilter maxF;

		Quad background = new Quad("background", width, height);
		background.setDefaultColor(ColorRGBA.white);
		background.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		background.setLightCombineMode(LightCombineMode.Off);
		background.setLocalTranslation(width / 2, height / 2, 0f);
		
		if ( gus.getHighRes() ) {
            minF = MinificationFilter.Trilinear; 
            maxF = MagnificationFilter.Bilinear;
        } else {
            minF = MinificationFilter.NearestNeighborNoMipMaps; 
            maxF = MagnificationFilter.NearestNeighbor;        	
        }
		
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		try {
			ts.setTexture(TextureManager.loadTexture(ResourceLoader.getURL(backgroundImage),
					minF, maxF, 1.0f, true));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		ts.setEnabled(true);
		background.setRenderState(ts);

		background.updateRenderState();
		stateNode.attachChild(background);
	}

	private void addTitle() {
		titleText = Text.createDefaultTextLabel("Title","Rally Uribe 100K");
		titleText.setLocalScale(3.5f);
		ColorRGBA textColor = ColorRGBA.green;
		textColor.interpolate(ColorRGBA.white, 0.7f);
		titleText.setTextColor(textColor);
		titleText.setLightCombineMode(LightCombineMode.Off);
		titleText.setCullHint(CullHint.Never);
		float width = titleText.getWidth();
		titleText.setLocalTranslation((DisplaySystem.getDisplaySystem().getWidth()-width)/2.0f, DisplaySystem.getDisplaySystem().getHeight()*3/4,0);
		
		initialColor = new ColorRGBA(textColor);
		menu.getMenuNode().attachChild(titleText);
		menu.getMenuNode().getLocalTranslation().y += 30;
	}

	private void addInstructions() {
		instText = Text.createDefaultTextLabel("Instructions",INSTRUCTIONS_TEXT);
		instText.setTextColor(ColorRGBA.white);
		instText.setLightCombineMode(LightCombineMode.Off);
		instText.setCullHint(CullHint.Never);
		float width = instText.getWidth();
		int dispWidth = DisplaySystem.getDisplaySystem().getWidth();
		if ( width > dispWidth ){
			instText.setLocalScale(dispWidth/width);
			width = dispWidth;
		}
		instText.setLocalTranslation((dispWidth-width)/2.0f, 10, 0);
		
		stateNode.attachChild(instText);
	}
	
	private void buildMenu() {
		menu = new RallyMenu();
		menu.addPanel(buildMainPanel());
		menu.addPanel(buildOptionsPanel());
		menu.addPanel(buildNewGamePanel());
		menu.addPanel(buildHighScorePanel());
		menu.addPanel(exitConfirmationPanel());
	}

	private RallyMenuPanel exitConfirmationPanel() {
		confirmationPanel = new RallyMenuPanel();
		
		final RallyMenuItemBoolean exit = new RallyMenuItemBoolean("Confirm exit :");
		exit.changeValue(true);
		exit.setMessages("Yes", "No");
		exit.setEnterAction(new IAction() {
			public void performAction() {
				if ( exit.getValue() )
					System.exit(0);
				keyActions.setPanel(mainPanel);
				menu.setActivePanel(mainPanel);
			}
		});
		exit.changeValue(true);
		confirmationPanel.addItem(exit);

		return confirmationPanel;
	}

	private RallyMenuPanel buildMainPanel() {
		mainPanel = new RallyMenuPanel();
		
		RallyMenuItemVoid exit = new RallyMenuItemVoid("Exit");
		exit.setEnterAction(new IAction() {
			public void performAction() {
				keyActions.setPanel(confirmationPanel);
				menu.setActivePanel(confirmationPanel
						);
			}
		});
		mainPanel.addItem(exit);
		
		RallyMenuItemVoid highScores = new RallyMenuItemVoid("High Scores");
		highScores.setEnterAction(new IAction() {
			public void performAction() {
				keyActions.setPanel(highScorePanel);
				menu.setActivePanel(highScorePanel);
			}
		});
		mainPanel.addItem(highScores);
		
		RallyMenuItemVoid options = new RallyMenuItemVoid("Options");
		options.setEnterAction(new IAction() {
			public void performAction() {
				keyActions.setPanel(optionsPanel);
				menu.setActivePanel(optionsPanel);
			}
		});
		mainPanel.addItem(options);

		resumeGame = new RallyMenuItemVoid("Resume Game");
		resumeGame.setEnterAction(new IAction() {
			public void performAction() {
				if ( RallyGame.getInstance().isPaused() ){
					RallyGame.getInstance().setPause(false);
					GameStateManager.getInstance().deactivateChildNamed(MenuState.STATE_NAME);
					GameStateManager.getInstance().activateChildNamed(InGameState.STATE_NAME);
				}
			}
		});
		mainPanel.addItem(resumeGame);
		
		newGame = new RallyMenuItemVoid("New Game");
		newGame.setEnterAction(new IAction() {
			public void performAction() {
				if ( ! RallyGame.getInstance().isPaused() ){
					keyActions.setPanel(newGamePanel);
					menu.setActivePanel(newGamePanel);
				}
			}
		});
		mainPanel.addItem(newGame);
		
		return mainPanel;
	}

	private RallyMenuPanel buildOptionsPanel() {
		optionsPanel = new RallyMenuPanel();
		
		RallyMenuItemVoid backOptions = new RallyMenuItemVoid("Back");
		backOptions.setEnterAction(new IAction() {
			public void performAction() {
				keyActions.setPanel(mainPanel);
				menu.setActivePanel(mainPanel);
			}
		});
		optionsPanel.addItem(backOptions);

		
		highRes = new RallyMenuItemBoolean("High Resolution");
		IAction setHighRes = new IAction() {
			public void performAction() {
				GameUserSettings.getInstance().setHighRes(highRes.getValue());
			}
		};
		highRes.setLeftAction(setHighRes);
		highRes.setRightAction(setHighRes);
		highRes.changeValue(GameUserSettings.getInstance().getHighRes());
		optionsPanel.addItem(highRes);


		final RallyMenuItemInteger soundVol = new RallyMenuItemInteger("Sound Effects Volume");
		IAction volSound = new IAction() {
			public void performAction() {
				GameUserSettings.getInstance().setSfxVolume(soundVol.getValue());
			}
		};
		soundVol.setLeftAction(volSound);
		soundVol.setRightAction(volSound);
		soundVol.changeValue(GameUserSettings.getInstance().getSfxVolume());
		optionsPanel.addItem(soundVol);
		
		
		final RallyMenuItemBoolean soundState = new RallyMenuItemBoolean("Sound Effects");
		IAction onOffSound = new IAction() {
			public void performAction() {
				GameUserSettings.getInstance().setSfxOn(soundState.getValue());
			}
		};
		soundState.setLeftAction(onOffSound);
		soundState.setRightAction(onOffSound);
		soundState.changeValue(GameUserSettings.getInstance().isSfxOn());
		optionsPanel.addItem(soundState);
		

		final RallyMenuItemInteger musicVol = new RallyMenuItemInteger("Music Volume");
		IAction volMusic = new IAction() {
			public void performAction() {
				GameUserSettings.getInstance().setMusicVolume(musicVol.getValue());
			}
		};
		soundVol.setLeftAction(volMusic);
		soundVol.setRightAction(volMusic);
		musicVol.changeValue(GameUserSettings.getInstance().getMusicVolume());
		optionsPanel.addItem(musicVol);
		
		
		final RallyMenuItemBoolean musicState = new RallyMenuItemBoolean("Music");
		IAction onOffMusic = new IAction() {
			public void performAction() {
				GameUserSettings.getInstance().setMusicOn(musicState.getValue());
			}
		};
		musicState.setLeftAction(onOffMusic);
		musicState.setRightAction(onOffMusic);
		musicState.changeValue(GameUserSettings.getInstance().isMusicOn());
		musicState.toggleSelect();
		optionsPanel.addItem(musicState);
		
		return optionsPanel;
	}

	private RallyMenuPanel buildNewGamePanel() {
		newGamePanel = new RallyMenuPanel();
		
		RallyMenuItemVoid backItem = new RallyMenuItemVoid("Back");
		backItem.setEnterAction(new IAction() {
			public void performAction() {
				keyActions.setPanel(mainPanel);
				menu.setActivePanel(mainPanel);
			}
		});
		newGamePanel.addItem(backItem);
		
		
		final RallyMenuItemListString skyboxitem = new RallyMenuItemListString("Sky:",GlobalSettings.getInstance().getSkyBoxesNames());
		IAction changeSkybox = new IAction() {
			public void performAction() {
				GameUserSettings.getInstance().setSkyBox(skyboxitem.getSelectedValue());
			}
		};
		skyboxitem.setLeftAction(changeSkybox);
		skyboxitem.setRightAction(changeSkybox);
		newGamePanel.addItem(skyboxitem);
		
		RallyMenuItemVoid startGameItem = new RallyMenuItemVoid("Start Game");
		startGameItem.setEnterAction(new IAction() {
			public void performAction() {
				keyActions.setPanel(mainPanel);
				menu.setActivePanel(mainPanel);
				GameStateManager.getInstance().deactivateChildNamed(MenuState.STATE_NAME);
				GameStateManager.getInstance().activateChildNamed(PreLoadState.STATE_NAME);	
			}
		});
		startGameItem.toggleSelect();
		newGamePanel.addItem(startGameItem);
		return newGamePanel;
	}

	private RallyMenuPanel buildHighScorePanel() {
		highScorePanel = new RallyMenuPanel();
		
		back = new RallyMenuItemVoid("Back");
		back.setEnterAction(new IAction() {
			public void performAction() {
				menu.setActivePanel(mainPanel);
				keyActions.setPanel(mainPanel);
			}
		});
		highScorePanel.addItem(back);
		
		List<Score> scores = Scores.getInstance().getScores();
		if ( scores == null || scores.isEmpty() ){
			back.toggleSelect();
		}else{
			for(int i = scores.size()-1 ; i >= 0 ; i--){
				Score s = scores.get(i);
				RallyMenuItemVoid score = new RallyMenuItemVoid(s.getUserID() + "     " + s.getScoreAsString());
				highScorePanel.addItem(score);
				if ( i == 0 )
					score.toggleSelect();
			}
		}
		return highScorePanel;
	}

	@Override
	public void activated() {
		if ( RallyGame.getInstance().isPlaying() ){
			newGame.setEnabled(false);
			highRes.setEnabled(false);
			resumeGame.setEnabled(true);
			mainPanel.setActiveOption(resumeGame);
		}else{
			highRes.setEnabled(true);
			resumeGame.setEnabled(false);
			newGame.setEnabled(true);
			mainPanel.setActiveOption(newGame);
		}
		menu.setActivePanel(mainPanel);
		menu.update();

		
		if ( RallyGame.getInstance().isPaused() ){
			first = true;
			addFadeController("FadeMenuGame", rootNode,
					((RallyGameState) GameStateManager.getInstance().getChild(InGameState.STATE_NAME)).getStateNode(),
					this.getStateNode(), ColorRGBA.randomColor(), 0.2f);
		}else{
			activateFunctionality();
		}
		
		this.rootNode.updateRenderState();
	}

	private void activateFunctionality(){
		menuSong.play();
		keyActions = new MenuInputHandler(mainPanel);
		RallyGame.getInstance().setInputHandler(keyActions);
		keyActions.setPanel(mainPanel);

		this.rootNode.attachChild(this.stateNode);		
	}
	
	
	@Override
	public void deactivated() {
		RallyGame.getInstance().setInputHandler(new InputHandler());
		menuSong.stop();
		rootNode.detachChild(this.stateNode);
		rootNode.updateRenderState();
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void render(float arg0) {
		stateNode.updateRenderState();
	}

	@Override
	public void update(float arg0) {
		if ( RallyGame.getInstance().isPaused() ){
			if ( !getFadeOutIn().hasFinished() ){
				fade(arg0);
				super.update(arg0);
				return;
			}else if ( first ){
				activateFunctionality();
				first = false;
			}
		}

		AudioSystem.getSystem().update();
		menu.update();
		titleText.setTextColor(titleText.getTextColor().multLocal((float) 0.99));

		float[] textColor = titleText.getTextColor().getColorArray();
		if ( textColor[0] < TOLE || textColor[1] < TOLE || textColor[2] < TOLE || textColor[3] < TOLE )
			titleText.setTextColor(new ColorRGBA(initialColor));
	}

}
