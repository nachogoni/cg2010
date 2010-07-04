package ar.edu.itba.cg_final.menu.items;

import ar.edu.itba.cg_final.menu.RallyMenuItem;
import ar.edu.itba.cg_final.menu.actions.IAction;


public class RallyMenuItemInteger extends RallyMenuItem<Integer> {

	public RallyMenuItemInteger(String text) {
		super(text);
		IAction decreaseAction = new IAction() {
			public void performAction() {
				changeValue(value-1);
			}
		};
		IAction increaseAction = new IAction() {
			public void performAction() {
				changeValue(value+1);
			}
		};
		setLeftAction(decreaseAction);
		setRightAction(increaseAction);
	}
	
	@Override
	public void changeValue(Integer newValue) {
		super.changeValue(newValue);
		
		if ( newValue < 0 )
			super.changeValue(0);
		else if ( newValue > 100 )
			super.changeValue(100);
		
		setText(getTextToDisplay() + " " + value);
	}
	
}
