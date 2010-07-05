package ar.edu.itba.cg_final.states;

import java.awt.Font;
import java.util.List;

import ar.edu.itba.cg_final.menu.RallyMenu;
import ar.edu.itba.cg_final.menu.RallyMenuPanel;
import ar.edu.itba.cg_final.menu.actions.IAction;
import ar.edu.itba.cg_final.menu.items.RallyMenuItemBoolean;
import ar.edu.itba.cg_final.menu.items.RallyMenuItemInteger;
import ar.edu.itba.cg_final.menu.items.RallyMenuItemListString;
import ar.edu.itba.cg_final.menu.items.RallyMenuItemVoid;
import ar.edu.itba.cg_final.settings.GameUserSettings;
import ar.edu.itba.cg_final.settings.GlobalSettings;
import ar.edu.itba.cg_final.settings.Score;
import ar.edu.itba.cg_final.settings.Scores;

import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.system.DisplaySystem;
import com.jmex.font3d.Font3D;
import com.jmex.font3d.Text3D;
import com.jmex.font3d.effects.Font3DBorder;
import com.jmex.font3d.effects.Font3DGradient;
import com.jmex.game.state.GameStateManager;

public class MenuState extends RallyGameState {

	private RallyMenu menu;
	private RallyMenuPanel mainPanel;
	private RallyMenuPanel optionsPanel;
	private RallyMenuPanel newGamePanel;
	private RallyMenuPanel highScorePanel;
	private RallyMenuItemVoid back;
	private RallyMenuPanel confirmationPanel;

	public MenuState() {
		this.setName("Menu");
		stateNode.setName(this.getName());
		buildMenu();
		addTitle();
		stateNode.attachChild(menu.getMenuNode());
	}
	
	private void addTitle() {
		/*
		Font3D font = new Font3D(new Font("Verdana", Font.PLAIN, 4), 0.1f, true, true, true);
		FirstPersonHandler
//        Font3DGradient gradient = new Font3DGradient(new Vector3f(0.5f,1,0), ColorRGBA.lightGray, new ColorRGBA(0,0,1,1f));
//        gradient.applyEffect(font);
//        Font3DTexture fonttexture = new Font3DTexture(TestTextureState.class.getClassLoader().getResource("jmetest/data/model/marble.bmp"));
//        fonttexture.applyEffect(myfont);
//        Font3DBorder fontborder = new Font3DBorder(0.05f, ColorRGBA.red, new ColorRGBA(1,1,0,0.3f),font);
//        fontborder.applyEffect(font);
		Text3D text = font.createText("Rally Uribe 100K", 4, 0);
		text.setLocalScale(new Vector3f(1.0f, 1f, 1f));
		float width = text.getWidth();
//		text.setLocalTranslation((DisplaySystem.getDisplaySystem().getWidth()-width)/2.0f, 0,0);
		menu.getMenuNode().attachChild(text);
		*/
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
				menu.setActivePanel(confirmationPanel
						);
			}
		});
		mainPanel.addItem(exit);
		
		RallyMenuItemVoid highScores = new RallyMenuItemVoid("High Scores");
		highScores.setEnterAction(new IAction() {
			public void performAction() {
				menu.setActivePanel(highScorePanel);
			}
		});
		mainPanel.addItem(highScores);
		
		RallyMenuItemVoid options = new RallyMenuItemVoid("Options");
		options.setEnterAction(new IAction() {
			public void performAction() {
				menu.setActivePanel(optionsPanel);
			}
		});
		mainPanel.addItem(options);
		
		RallyMenuItemVoid newGame = new RallyMenuItemVoid("New Game");
		newGame.setEnterAction(new IAction() {
			public void performAction() {
				menu.setActivePanel(newGamePanel);
			}
		});
		newGame.toggleSelect();
		
		mainPanel.addItem(newGame);
		
		return mainPanel;
	}

	private RallyMenuPanel buildOptionsPanel() {
		optionsPanel = new RallyMenuPanel();
		
		RallyMenuItemVoid backOptions = new RallyMenuItemVoid("Back");
		backOptions.setEnterAction(new IAction() {
			public void performAction() {
				menu.setActivePanel(mainPanel);
			}
		});
		optionsPanel.addItem(backOptions);

		
		final RallyMenuItemBoolean highRes = new RallyMenuItemBoolean("High Resolution");
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
				menu.setActivePanel(mainPanel);
			}
		});
		newGamePanel.addItem(backItem);
		
		
		final RallyMenuItemListString skyboxitem = new RallyMenuItemListString("Skybox",GlobalSettings.getInstance().getSkyBoxesNames());
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
				menu.setActivePanel(mainPanel);
				GameStateManager.getInstance().deactivateChildNamed("Menu");
				GameStateManager.getInstance().activateChildNamed("PreLoad");	
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
			}
		});
		highScorePanel.addItem(back);
		
		List<Score> scores = Scores.getInstance().getScores();
		for(int i = scores.size()-1 ; i >= 0 ; i--){
			Score s = scores.get(i);
			RallyMenuItemVoid score = new RallyMenuItemVoid(s.getUserID() + " " + s.getScore());
			highScorePanel.addItem(score);
			if ( i == 0 )
				score.toggleSelect();
		}
		return highScorePanel;
	}

	@Override
	public void activated() {
		KeyBindingManager.getKeyBindingManager().removeAll();
		KeyBindingManager.getKeyBindingManager().add("up", KeyInput.KEY_UP);
		KeyBindingManager.getKeyBindingManager().add("down", KeyInput.KEY_DOWN);
		KeyBindingManager.getKeyBindingManager().add("left", KeyInput.KEY_LEFT);
		KeyBindingManager.getKeyBindingManager().add("right", KeyInput.KEY_RIGHT);
		KeyBindingManager.getKeyBindingManager().add("enter", KeyInput.KEY_RETURN);

		this.rootNode.attachChild(this.stateNode);
		this.rootNode.updateRenderState();
	}

	@Override
	public void deactivated() {
		KeyBindingManager.getKeyBindingManager().removeAll();
		rootNode.detachChild(this.stateNode);
		rootNode.updateRenderState();
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float arg0) {
		stateNode.updateRenderState();
	}

	@Override
	public void update(float arg0) {
		menu.update();
	}

}
