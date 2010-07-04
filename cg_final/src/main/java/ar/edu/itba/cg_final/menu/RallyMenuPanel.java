package ar.edu.itba.cg_final.menu;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.cg_final.menu.actions.IAction;

import com.jme.input.KeyBindingManager;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.system.DisplaySystem;

public class RallyMenuPanel {
	List<RallyMenuItem<?>> items;
	private Node node;
	private int activeOption;
	
	public RallyMenuPanel() {
		items = new ArrayList<RallyMenuItem<?>>();
		node = new Node();
	}
	
	private float maxwidth = 0.0f;
	
	public void addItem(RallyMenuItem<?> rmi){
		Text itemSpatial = rmi.getSpatial();
		float width = itemSpatial.getWidth();
		maxwidth = maxwidth < width ? width : maxwidth;
		float height = itemSpatial.getHeight();
		itemSpatial.setLocalTranslation(0, items.size() * height, 0);
		items.add(rmi);
		activeOption = items.size()-1;
		node.attachChild(itemSpatial);
	}

	boolean first = true;
	
	public Node getNode(){
		if ( first ){
			List<Spatial> children = node.getChildren();
			for(Spatial c:children)
				c.setLocalTranslation((DisplaySystem.getDisplaySystem().getWidth() - maxwidth)/2, c.getLocalTranslation().getY(), c.getLocalTranslation().getZ());
			first = false;
		}
		return node;
	}

	public void update() {
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("up", false)) {
			activeOption++;
			if ( activeOption >= items.size() )
				activeOption--;
			else{
				items.get(activeOption-1).toggleSelect();
				items.get(activeOption).toggleSelect();
			}
		}
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("down", false)) {
			activeOption--;
			if ( activeOption < 0 )
				activeOption++;
			else{
				items.get(activeOption+1).toggleSelect();
				items.get(activeOption).toggleSelect();
			}		
		}
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("left", false)) {
			performActions(items.get(activeOption).getLeftAction());
		}
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("right", false)) {
			performActions(items.get(activeOption).getRightAction());
		}
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("enter", false)) {
			performActions(items.get(activeOption).getEnterAction());
		}
		node.updateRenderState();
	}

	private void performActions(List<IAction> actions){
		for(IAction action:actions)
			action.performAction();
	}
	
}
