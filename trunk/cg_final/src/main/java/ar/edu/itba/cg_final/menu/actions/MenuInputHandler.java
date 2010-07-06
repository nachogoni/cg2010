package ar.edu.itba.cg_final.menu.actions;

import java.util.List;

import ar.edu.itba.cg_final.menu.RallyMenuItem;
import ar.edu.itba.cg_final.menu.RallyMenuPanel;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;

public class MenuInputHandler extends InputHandler {
	
	RallyMenuPanel panel;
	
	
	public void setPanel(RallyMenuPanel panel) {
		this.panel = panel;
	}
	
	public MenuInputHandler(RallyMenuPanel panel) {
		this.panel = panel;
		
        this.addAction( new UpAction(), InputHandler.DEVICE_KEYBOARD, 
        		KeyInput.KEY_UP, InputHandler.AXIS_NONE, false);		
        this.addAction( new DownAction(), InputHandler.DEVICE_KEYBOARD, 
        		KeyInput.KEY_DOWN, InputHandler.AXIS_NONE, false);		
        this.addAction( new LeftAction(), InputHandler.DEVICE_KEYBOARD, 
        		KeyInput.KEY_LEFT, InputHandler.AXIS_NONE, false);		
        this.addAction( new RightAction(), InputHandler.DEVICE_KEYBOARD, 
        		KeyInput.KEY_RIGHT, InputHandler.AXIS_NONE, false);		
        this.addAction( new EnterAction(), InputHandler.DEVICE_KEYBOARD, 
        		KeyInput.KEY_RETURN, InputHandler.AXIS_NONE, false);		

	}
	
    private class UpAction implements InputActionInterface {
    	
        public void performAction( final InputActionEvent e ) {
        	if ( e.getTriggerPressed() ) {
	        	int activeOption = panel.getActiveOption();
	        	List<RallyMenuItem<?>> items = panel.getItems();
	        	
				activeOption++;
				if ( activeOption >= items.size() )
					activeOption--;
				else{
					items.get(activeOption-1).toggleSelect();
					items.get(activeOption).toggleSelect();
				}
				panel.setActiveOption(activeOption);
        	}
		}
    }	   
    private class DownAction implements InputActionInterface {
        public void performAction( final InputActionEvent e ) {
        	if ( e.getTriggerPressed() ) {
        		int activeOption = panel.getActiveOption();
        		List<RallyMenuItem<?>> items = panel.getItems();        	
        		activeOption--;
        		if ( activeOption < 0 )
        			activeOption++;
        		else{
        			items.get(activeOption+1).toggleSelect();
        			items.get(activeOption).toggleSelect();
        		}
        		panel.setActiveOption(activeOption);
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
