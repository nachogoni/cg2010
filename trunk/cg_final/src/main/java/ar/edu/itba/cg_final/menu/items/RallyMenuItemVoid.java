package ar.edu.itba.cg_final.menu.items;

import ar.edu.itba.cg_final.menu.RallyMenuItem;

import com.jme.renderer.ColorRGBA;

public class RallyMenuItemVoid extends RallyMenuItem<Void> {

	public RallyMenuItemVoid(String text) {
		super(text);
	}

	public RallyMenuItemVoid(String text, ColorRGBA color, ColorRGBA onSelectedColor) {
		super(text, color, onSelectedColor);
	}

	
	public RallyMenuItemVoid(String text, ColorRGBA color, ColorRGBA onSelectedColor, boolean selected) {
		super(text, color, onSelectedColor, selected);
	}

	@Override
	public void changeValue(Void newValue) {
	}

}
