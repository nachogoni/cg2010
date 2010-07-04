package ar.edu.itba.cg_final.menu.items;

import ar.edu.itba.cg_final.menu.RallyMenuItem;
import ar.edu.itba.cg_final.menu.actions.IAction;

public class RallyMenuItemBoolean extends RallyMenuItem<Boolean> {
	
	private String onMessage;
	private String offMessage;

	public RallyMenuItemBoolean(String text) {
		super(text);
		this.onMessage = "ON";
		this.offMessage = "OFF";
		IAction iAction = new IAction() {
			public void performAction() {
				changeValue(!value);
			}
		};
		setLeftAction(iAction);
		setRightAction(iAction);
	}

	public void setMessages(String onMessage, String offMessage){
		this.onMessage = onMessage;
		this.offMessage = offMessage;
	}
	
	@Override
	public void changeValue(Boolean newValue) {
		super.changeValue(newValue);
		if ( newValue )
			setText(getTextToDisplay() + " " + onMessage);
		else
			setText(getTextToDisplay() + " " + offMessage);
		
	}

}
