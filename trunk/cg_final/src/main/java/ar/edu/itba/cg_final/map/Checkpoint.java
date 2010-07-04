package ar.edu.itba.cg_final.map;

import ar.edu.itba.cg_final.terrain.ForceFieldFence;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.SharedMesh;
import com.jme.scene.shape.Box;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class Checkpoint extends Node {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4783284580069565288L;
    //The texture that makes up the "force field", we will keep a reference to it
    // here to allow us to animate it.
    private Texture t;
    
    /**
     * create the fence, passing the name to the parent.
     * @param name the name of the fence
     */
    public Checkpoint(String name, Vector2f position, int rotation) {
        super(name);
        buildCheckpoint(name, position, rotation);
    }
    
    /**
     * we add an update method to allow the texture to animate. This will
     * be called from the main games update method.
     * @param interpolation the time between frames.
     */
    public void update(float interpolation) {
        //We will use the interpolation value to keep the speed
        //of the forcefield consistent between computers.
        //we update the Y have of the texture matrix to give
        //the appearance the forcefield is moving.
        t.getTranslation().y += 0.3f * interpolation;
        //if the translation is over 1, it's wrapped, so go ahead
        //and check for this (to keep the vector's y value from getting
        //too large.)
        if(t.getTranslation().y > 1) {
            t.getTranslation().y = 0;
        }
    }
    
	private void buildCheckpoint(String name, Vector2f position, float rotation) {
        //Create the actual forcefield 
        //The first box handles the X-axis, the second handles the z-axis.
        //We don't rotate the box as a demonstration on how boxes can be 
        //created differently.
        Box forceFieldX = new Box(name+".forceFieldX", 
        		new Vector3f(position.x, 30f, position.y), 100, 60, 1);
        forceFieldX.setModelBound(new BoundingBox());
        forceFieldX.updateModelBound();
        //We are going to share these boxes as well
        SharedMesh forceFieldX1 = new SharedMesh(name+".forceFieldX1",forceFieldX);
        forceFieldX1.setLocalTranslation(new Vector3f(position.x,0,position.y));
        float [] angles = new float[]{0f,rotation,0f}; 
        forceFieldX1.setLocalRotation(new Quaternion(angles));
        
        //add all the force fields to a single node and make this node part of
        //the transparent queue.
        Node forceFieldNode = new Node(name+".check");
        forceFieldNode.attachChild(forceFieldX1);
        
        //load a texture for the force field elements
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        t = TextureManager.loadTexture(ForceFieldFence.class.getClassLoader()
                  .getResource("texture/checkpoint.png"),
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
