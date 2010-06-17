package ar.edu.itba.cg_final;

import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.geometry.PhysicsBox;
import com.jmex.physics.util.SimplePhysicsGame;

public class P extends SimplePhysicsGame{

	public static void main(String[] args) {
		P app = new P();
		app.setConfigShowMode(ConfigShowMode.ShowIfNoConfig);
		app.start();
	}
	
	@Override
	protected void simpleInitGame() {
		// TODO Auto-generated method stub
		
		StaticPhysicsNode staticNode = getPhysicsSpace().createStaticNode();
		rootNode.attachChild( staticNode );
		PhysicsBox floorBox = staticNode.createBox( "floor" );
		floorBox.getLocalScale().set( 10, 0.5f, 10 );
		DynamicPhysicsNode dynamicNode = getPhysicsSpace().createDynamicNode();
		rootNode.attachChild( dynamicNode );
		dynamicNode.createBox( "falling box" );  
		dynamicNode.getLocalTranslation().set( 0, 5, 0 );
		showPhysics = true;

		
	}

	
}
