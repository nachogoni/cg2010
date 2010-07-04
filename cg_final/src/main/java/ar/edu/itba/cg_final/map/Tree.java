package ar.edu.itba.cg_final.map;

import ar.edu.itba.cg_final.RallyGame;

import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.scene.BillboardNode;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class Tree extends Node {
	
	private static final long serialVersionUID = 1130692595359578525L;
	public Tree(String name) {
		this();
		this.setName(name);
	}
	
	public Tree() {
        //SE AGREGA ARBOLITO
        //TODO sacar esto de aca y meterlo en una funcion que cargue el bosque.
        //Ponerlo despues de que se cargue el auto
        BlendState blendState = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        blendState.setBlendEnabled( true );
        blendState.setSourceFunction( BlendState.SourceFunction.SourceAlpha );
        blendState.setDestinationFunction( BlendState.DestinationFunction.OneMinusSourceAlpha );
        blendState.setTestEnabled( true );
        blendState.setTestFunction( BlendState.TestFunction.GreaterThanOrEqualTo );
        blendState.setEnabled( true );                
        
        Quad q = new Quad("Quad");
        TextureState ts2 = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        ts2.setEnabled(true);
        Texture t4 = TextureManager.loadTexture(
            RallyGame.class.getClassLoader().getResource(
            "images/tree1.png"), 
            MinificationFilter.Trilinear,
            MagnificationFilter.Bilinear );
        
        ts2.setTexture(t4);
        
        q.setRenderState(ts2);
        q.setRenderState(blendState);
        q.updateRenderState();
     
        BillboardNode billboard = new BillboardNode("Billboard");
        billboard.setAlignment(BillboardNode.AXIAL);
        billboard.attachChild(q);   
        billboard.setLocalScale(100f);

        this.attachChild(billboard);        
		
	}
    public void placeTree(float x, float y, float z) {
    	localTranslation.x = x;
    	localTranslation.y = y;
    	localTranslation.z = z;
    }	
	
}
