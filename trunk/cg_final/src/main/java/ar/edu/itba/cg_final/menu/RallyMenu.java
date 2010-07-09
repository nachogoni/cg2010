package ar.edu.itba.cg_final.menu;

import java.util.ArrayList;
import java.util.List;

import com.jme.scene.Node;

public class RallyMenu {
	List<RallyMenuPanel> panels;
	private Node menuNode;
	private int activePanel;
	
	public RallyMenu() {
		panels = new ArrayList<RallyMenuPanel>();
		menuNode = new Node();
	}
	
	public void addPanel(RallyMenuPanel rmp){
		if( panels.isEmpty() ){
			menuNode.attachChild(rmp.getNode());
			activePanel = 0;
		}
		panels.add(rmp);
	}

	public void setActivePanel(RallyMenuPanel rmp){
		for(int i = 0 ; i < panels.size() ; i++ )
			if ( panels.get(i).equals(rmp) ){
				menuNode.detachChild(getActivePanel().getNode());
				activePanel = i;
				menuNode.attachChild(getActivePanel().getNode());
			}
		update();
	}

	public void replacePanel(RallyMenuPanel oldPanel, RallyMenuPanel newPanel){
		for(int i = 0 ; i < panels.size() ; i++ )
			if ( panels.get(i).equals(oldPanel) ){
				panels.add(i, newPanel);
				panels.remove(i+1);
				menuNode.detachChild(oldPanel.getNode());
				menuNode.attachChild(newPanel.getNode());
				return;
			}
	}
	
	public RallyMenuPanel getActivePanel(){
		return panels.get(activePanel);
	}

	public void update() {
		getActivePanel().update();
	}
	
	public Node getMenuNode(){
		return menuNode;
	}
}
