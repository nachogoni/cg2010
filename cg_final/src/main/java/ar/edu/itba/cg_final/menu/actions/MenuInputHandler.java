package ar.edu.itba.cg_final.menu.actions;

import java.util.List;

import ar.edu.itba.cg_final.menu.RallyMenuItem;
import ar.edu.itba.cg_final.menu.RallyMenuPanel;
import ar.edu.itba.cg_final.settings.GlobalSettings;

import com.jme.input.InputHandler;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;

public class MenuInputHandler extends InputHandler {
	
	RallyMenuPanel panel;
	
	
	public void setPanel(RallyMenuPanel panel) {
		this.panel = panel;
	}
	
	public MenuInputHandler(RallyMenuPanel panel) {
		this.panel = panel;
		GlobalSettings gs = GlobalSettings.getInstance();
		
        this.addAction( new UpAction(), InputHandler.DEVICE_KEYBOARD, 
        		gs.getHexProperty("ARROWUP"), InputHandler.AXIS_NONE, false);		
        this.addAction( new DownAction(), InputHandler.DEVICE_KEYBOARD, 
        		gs.getHexProperty("ARROWDOWN"), InputHandler.AXIS_NONE, false);		
        this.addAction( new LeftAction(), InputHandler.DEVICE_KEYBOARD, 
        		gs.getHexProperty("ARROWLEFT"), InputHandler.AXIS_NONE, false);		
        this.addAction( new RightAction(), InputHandler.DEVICE_KEYBOARD, 
        		gs.getHexProperty("ARROWRIGHT"), InputHandler.AXIS_NONE, false);		
        this.addAction( new EnterAction(), InputHandler.DEVICE_KEYBOARD, 
        		gs.getHexProperty("SELECT"), InputHandler.AXIS_NONE, false);		

	}
	
    private class UpAction implements InputActionInterface {
    	
        public void performAction( final InputActionEvent e ) {
        	if ( e.getTriggerPressed() ) {
        		panel.setNextActiveOption(true);
        	}
		}
    }	   
    private class DownAction implements InputActionInterface {
        public void performAction( final InputActionEvent e ) {
        	if ( e.getTriggerPressed() ) {
        		panel.setNextActiveOption(false);
        	}
		}
    }
    private class EnterAction implements InputActionInterface {
        public void performAction( final InputActionEvent e) {
        	if ( e.getTriggerPressed() ) {
        		int activeOption = panel.getActiveOption();
        		List<RallyMenuItem<?>> items = panel.getItems();        	
        		performActions(items.get(activeOption).getEnterAction());
        	}
        }
    }        
	
    private class LeftAction implements InputActionInterface {
        public void performAction( final InputActionEvent e) {
        	if ( e.getTriggerPressed() ) {
        		int activeOption = panel.getActiveOption();
        		List<RallyMenuItem<?>> items = panel.getItems();        	
        		performActions(items.get(activeOption).getLeftAction());
        	}
        }
    }     
    private class RightAction implements InputActionInterface {
        public void performAction( final InputActionEvent e) {
        	if ( e.getTriggerPressed() ) {
        		int activeOption = panel.getActiveOption();
        		List<RallyMenuItem<?>> items = panel.getItems();        	
        		performActions(items.get(activeOption).getRightAction());
        	}
        }
    }         
    
	private void performActions(List<IAction> actions){
		for(IAction action:actions)
			action.performAction();
	}
}
