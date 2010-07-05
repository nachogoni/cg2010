package ar.edu.itba.cg_final.map;

import ar.edu.itba.cg_final.settings.GlobalSettings;
import ar.edu.itba.cg_final.terrain.ForceFieldFence;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.SharedMesh;
import com.jme.scene.shape.Box;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class CheckPoint extends Node {

	private static final long serialVersionUID = 4783284580069565288L;
	Node forceFieldNode;
    
    public CheckPoint(String name, Vector2f position, float rotation, boolean startingGrid, GlobalSettings gs) {
        super(name);
        buildCheckpoint(name, position, rotation, startingGrid, gs);
    }
    
    public Vector2f get2DPosition() {
    	Vector3f pos = forceFieldNode.getLocalTranslation();
    	return new Vector2f(pos.x, pos.z);
    }
    
    public Vector3f get3DPosition() {
    	return forceFieldNode.getLocalTranslation();
    }
    
    public void setPosition(Vector3f newPosition) {
    	forceFieldNode.setLocalTranslation(newPosition);
    }
    
	private void buildCheckpoint(String name, Vector2f position, float rotation, boolean startingGrid, GlobalSettings gs) {
        //Create the actual forcefield 
        //The first box handles the X-axis, the second handles the z-axis.
        //We don't rotate the box as a demonstration on how boxes can be 
        //created differently.
        Box forceFieldX = new Box(name+".checkPoint", 
        		new Vector3f(0, 0, 0), 100, 60, 0.1f);
        
        forceFieldX.setModelBound(new BoundingBox());
        forceFieldX.updateModelBound();

        //We are going to share these boxes as well
        SharedMesh forceFieldX1 = new SharedMesh(name+".forceFieldX1",forceFieldX);
        
        //add all the force fields to a single node and make this node part of
        //the transparent queue.
        forceFieldNode = new Node(name);

        forceFieldNode.attachChild(forceFieldX1);
        
        float [] angles = new float[]{0f,rotation,0f}; 
        forceFieldNode.setLocalRotation(new Quaternion(angles));
        forceFieldNode.setLocalTranslation(position.x, 30f, position.y);
        
        String texture;
        if (startingGrid == true) {
        	texture = gs.getProperty("STARTPOINT.TEXTURE");
        } else {
        	texture = gs.getProperty("CHECKPOINT.TEXTURE");
        }
        
        //load a texture for the force field elements
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        Texture t = TextureManager.loadTexture(ForceFieldFence.class.getClassLoader()
                  .getResource(texture),
                  Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
        ts.setTexture(t);
                
        BlendState blendState = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        blendState.setBlendEnabled( true );
        blendState.setSourceFunction( BlendState.SourceFunction.SourceAlpha );
        blendState.setDestinationFunction( BlendState.DestinationFunction.OneMinusSourceAlpha );
        blendState.setTestEnabled( true );
        blendState.setTestFunction( BlendState.TestFunction.GreaterThanOrEqualTo );
        blendState.setEnabled( true );   
        
        forceFieldNode.setRenderState(ts);
        forceFieldNode.setRenderState(blendState);
        
        //Attach all the pieces to the main fence node
        this.attachChild(forceFieldNode);
	
	}
}
