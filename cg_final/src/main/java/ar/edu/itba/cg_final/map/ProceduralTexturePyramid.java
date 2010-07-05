package ar.edu.itba.cg_final.map;

import ar.edu.itba.cg_final.RallyGame;
import ar.edu.itba.cg_final.textures.MarbleProceduralTexture;
import ar.edu.itba.cg_final.textures.StoneProceduralTexture;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.scene.Node;
import com.jme.scene.shape.Pyramid;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.terrain.util.MidPointHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;

public class ProceduralTexturePyramid extends Node {
	
	private static final long serialVersionUID = -2270974621034389670L;

	public static enum pyramidType {
		MARBLE, STONE
	}
	
	
	public ProceduralTexturePyramid(String name, pyramidType type) {
		this(type);
		this.setName(name);
	}
	
	public ProceduralTexturePyramid(pyramidType type) {
		RallyGame rg = RallyGame.getInstance();
    	
        Pyramid pyramid = new Pyramid("pyramid", 20, 20);
        pyramid.setModelBound(new BoundingBox());
        pyramid.updateModelBound();
        pyramid.setLocalScale(50);
        ProceduralTextureGenerator pt;
        if (type.equals(pyramidType.MARBLE)) {
        	pt = new MarbleProceduralTexture(new MidPointHeightMap(64, 1f));
        } else {
        	pt = new StoneProceduralTexture(new MidPointHeightMap(64, 1f));
        }
        pt.createTexture(512);
        
        // assign the texture to the terrain
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        Texture t1 = TextureManager.loadTexture(pt.getImageIcon().getImage(),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, true);
        ts.setTexture(t1, 0);
        
        t1.setApply(Texture.ApplyMode.Combine);
        t1.setCombineFuncRGB(Texture.CombinerFunctionRGB.Modulate);
        t1.setCombineSrc0RGB(Texture.CombinerSource.CurrentTexture);
        t1.setCombineOp0RGB(Texture.CombinerOperandRGB.SourceColor);
        t1.setCombineSrc1RGB(Texture.CombinerSource.PrimaryColor);
        t1.setCombineOp1RGB(Texture.CombinerOperandRGB.SourceColor);
        pyramid.setRenderState(ts);
        pyramid.updateRenderState();
        this.attachChild(pyramid);
        
        final StaticPhysicsNode staticNode = rg.getPhysicsSpace().createStaticNode();
        

        staticNode.attachChild( pyramid );
        
        this.attachChild( staticNode );
        
        staticNode.generatePhysicsGeometry();   
	}
    
    public void placePiramid(float x, float y, float z) {
    	localTranslation.x = x;
    	localTranslation.y = y;
    	localTranslation.z = z;
    }    
}
