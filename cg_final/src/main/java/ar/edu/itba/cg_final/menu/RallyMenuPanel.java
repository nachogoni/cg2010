package ar.edu.itba.cg_final.menu;

import java.util.ArrayList;
import java.util.List;

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
			if ( children != null && !children.isEmpty() ){
				for(Spatial c:children)
					c.setLocalTranslation((DisplaySystem.getDisplaySystem().getWidth() - maxwidth)/2, c.getLocalTranslation().getY(), c.getLocalTranslation().getZ());
			}
			first = false;
		}
		return node;
	}

	public void update() {
		node.updateRenderState();
		for(RallyMenuItem<?> item:items){
			item.getSpatial().updateRenderState();
		}
	}

	public void setActiveOption(int newOpt) {
		this.activeOption = newOpt;
	}
	
	public void setActiveOption(RallyMenuItem<?> option) {
		int i = 0;
		for(RallyMenuItem<?> item:items){
			if ( item.equals(option) ){
				item.setSelected(true);
				this.activeOption = i;
			}else{
				item.setSelected(false);
			}
			i++;
		}
		update();
	}

	public int getActiveOption() {
		return this.activeOption;
	}
	
	public List<RallyMenuItem<?>> getItems() {
		return this.items;
	}

	public void setNextActiveOption(boolean dirUp) {
		int activeOption = getActiveOption();
		List<RallyMenuItem<?>> items = getItems();
		int aux;
		if ( dirUp )
			aux = getNextActiveOption(activeOption, activeOption+1, dirUp);
		else
			aux = getNextActiveOption(activeOption, activeOption-1, dirUp);
		if ( aux == activeOption )
			return;
		setActiveOption(items.get(aux));
	}

	private int getNextActiveOption(int initial, int current, boolean dirUp) {
		System.out.println("getNextActiveOption : "+initial+" : "+current+" : "+dirUp);
		if ( outOfBounds(current) )
			return initial;

		if ( items.get(current).isEnabled() )
			return current;

		return getNextActiveOption(initial, dirUp ? current+1 : current-1, dirUp);
	}

	private boolean outOfBounds(int current) {
		return current < 0 || current >= items.size();
	}

	
}
