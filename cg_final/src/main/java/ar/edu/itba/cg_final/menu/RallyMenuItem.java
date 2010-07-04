package ar.edu.itba.cg_final.menu;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.cg_final.menu.actions.IAction;
import ar.edu.itba.cg_final.menu.actions.NoAction;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.Spatial.LightCombineMode;

public abstract class RallyMenuItem<T>{
	
	private ColorRGBA onSelectedColor;
	private ColorRGBA defaultColor;
	private String textToDisplay;
	private Text textSpatial;
	protected T value;
	private boolean selected;
	
	private List<IAction> enterAction;
	private List<IAction> leftAction;
	private List<IAction> rightAction;

	public RallyMenuItem(String text) {
		this(text, new ColorRGBA(70.0f/255.0f, 108.0f/255.0f, 234.0f/255.0f, 1.0f)
				, new ColorRGBA(140.0f/255.0f, 162.0f/255.0f, 234.0f/255.0f, 1.0f));
	}

	public RallyMenuItem(String text, ColorRGBA color, ColorRGBA onSelectedColor) {
		this(text, color, onSelectedColor, false);
	}
	
	public RallyMenuItem(String text, ColorRGBA color, ColorRGBA onSelectedColor, boolean selected) {
		enterAction = new ArrayList<IAction>();
		enterAction.add(NoAction.getInstance());
		leftAction = new ArrayList<IAction>();
		leftAction.add(NoAction.getInstance());
		rightAction = new ArrayList<IAction>();
		rightAction.add(NoAction.getInstance());
		
		textToDisplay = text;
		this.defaultColor = color;
		this.onSelectedColor = onSelectedColor;
		this.selected = selected;
		textSpatial = new Text(textToDisplay,textToDisplay);
		textSpatial.setCullHint( Spatial.CullHint.Never );
		textSpatial.setRenderState( Text.getDefaultFontTextureState() );
		textSpatial.setRenderState( Text.getFontBlend() );
		textSpatial.setTextColor(defaultColor);
		textSpatial.setLocalScale(3);
		textSpatial.setLightCombineMode(LightCombineMode.Off);
		textSpatial.updateRenderState();
	}

	public Text getSpatial(){
		return textSpatial;
	}
	
	public void toggleSelect(){
		selected = !selected;
		
		if ( selected )
			textSpatial.setTextColor(onSelectedColor);
		else
			textSpatial.setTextColor(defaultColor);
		textSpatial.updateRenderState();
	}
	
	public void changeValue(T newValue){
		value = newValue;
	}

	public T getValue(){
		return value;
	}
	
	
	protected void setText(String text) {
		StringBuffer text2 = textSpatial.getText();
		text2.replace(0, text2.length(), text);
	}

	protected String getTextToDisplay() {
		return textToDisplay;
	}

	public void setEnterAction(IAction enterAction) {
		this.enterAction.add(enterAction);
	}

	public List<IAction> getEnterAction() {
		return enterAction;
	}

	public void setLeftAction(IAction leftAction) {
		this.leftAction.add(leftAction);
	}

	public List<IAction> getLeftAction() {
		return leftAction;
	}

	public void setRightAction(IAction rightAction) {
		this.rightAction.add(rightAction);
	}

	public List<IAction> getRightAction() {
		return rightAction;
	}

}
