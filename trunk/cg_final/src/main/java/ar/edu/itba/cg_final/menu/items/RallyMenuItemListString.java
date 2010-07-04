package ar.edu.itba.cg_final.menu.items;

import java.util.List;

import ar.edu.itba.cg_final.menu.RallyMenuItem;
import ar.edu.itba.cg_final.menu.actions.IAction;

public class RallyMenuItemListString extends RallyMenuItem<List<String>> {
	
	private List<String> possibleValues;
	int currentIdx = 0;

	public RallyMenuItemListString(String text) {
		super(text);
	}

	
	public RallyMenuItemListString(String text, final List<String> values) {
		this(text);
		this.possibleValues = values;
		changeValue(possibleValues.get(0));
		IAction decreaseAction = new IAction() {
			public void performAction() {
				if ( currentIdx > 0 )
					currentIdx--;
				changeValue(values.get(currentIdx));
			}
		};
		IAction increaseAction = new IAction() {
			public void performAction() {
				if ( currentIdx < values.size()-1 )
					currentIdx++;
				changeValue(values.get(currentIdx));
			}
		};
		setLeftAction(decreaseAction);
		setRightAction(increaseAction);
	}
	
	
	public void changeValue(String newValue) {
		setText(getTextToDisplay() + " " + newValue);
	}

	public String getSelectedValue(){
		return possibleValues.get(currentIdx);
	}
}
