package ar.edu.itba.cg_final.states;

import ar.edu.itba.cg_final.menu.RallyMenu;
import ar.edu.itba.cg_final.menu.RallyMenuPanel;
import ar.edu.itba.cg_final.menu.actions.IAction;
import ar.edu.itba.cg_final.menu.items.RallyMenuItemBoolean;
import ar.edu.itba.cg_final.menu.items.RallyMenuItemInteger;
import ar.edu.itba.cg_final.menu.items.RallyMenuItemListString;
import ar.edu.itba.cg_final.menu.items.RallyMenuItemVoid;
import ar.edu.itba.cg_final.settings.GameUserSettings;
import ar.edu.itba.cg_final.settings.GlobalSettings;

import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
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
		stateNode.attachChild(menu.getMenuNode());
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
		
		RallyMenuItemVoid startGameItem = new RallyMenuItemVoid("Start Game");
		startGameItem.setEnterAction(new IAction() {
			public void performAction() {
				menu.setActivePanel(mainPanel);
				GameStateManager.getInstance().deactivateChildNamed("Menu");
				GameStateManager.getInstance().activateChildNamed("PreLoad");	
			}
		});
		newGamePanel.addItem(startGameItem);
		
		final RallyMenuItemListString skyboxitem = new RallyMenuItemListString("Skybox",GlobalSettings.getInstance().getSkyBoxesNames());
		IAction changeSkybox = new IAction() {
			public void performAction() {
				GameUserSettings.getInstance().setSkyBox(skyboxitem.getSelectedValue());
			}
		};
		skyboxitem.setLeftAction(changeSkybox);
		skyboxitem.setRightAction(changeSkybox);
		skyboxitem.toggleSelect();
		newGamePanel.addItem(skyboxitem);
		
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
		back.toggleSelect();
		highScorePanel.addItem(back);
		
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
