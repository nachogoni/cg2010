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
			for(Spatial c:children)
				c.setLocalTranslation((DisplaySystem.getDisplaySystem().getWidth() - maxwidth)/2, c.getLocalTranslation().getY(), c.getLocalTranslation().getZ());
			first = false;
		}
		return node;
	}

	public void update() {
		node.updateRenderState();
	}

	public void setActiveOption(int newOpt) {
		this.activeOption = newOpt;
	}
	
	public int getActiveOption() {
		return this.activeOption;
	}
	
	public List<RallyMenuItem<?>> getItems() {
		return this.items;
	}
	
}
