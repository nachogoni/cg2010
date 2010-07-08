package ar.edu.itba.cg_final.map;


import ar.edu.itba.cg_final.RallyGame;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.math.Vector3f;
import com.jme.scene.BillboardNode;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.physics.StaticPhysicsNode;

public class Tree extends Node {
	
	private static final long serialVersionUID = 1130692595359578525L;
	private boolean isBush;
	public Tree(String name) {
		this(name,false);
	}
	
	public void buildTree() {

		RallyGame rg = RallyGame.getInstance();
		
        //Ponerlo despues de que se cargue el auto
        BlendState blendState = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        blendState.setBlendEnabled( true );
        blendState.setSourceFunction( BlendState.SourceFunction.SourceAlpha );
        
        blendState.setDestinationFunction( BlendState.DestinationFunction.OneMinusSourceAlpha );
        
        blendState.setTestEnabled( true );
        blendState.setTestFunction( BlendState.TestFunction.GreaterThan );
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
        billboard.setAlignment(BillboardNode.AXIAL_Y);
        billboard.attachChild(q);   
        billboard.setLocalScale(100f);
        
        this.attachChild(billboard);        

        if ( ! isBush ){
        
			Box boxGeometry = new Box("post", new Vector3f(-1f, -10, -1f),
					new Vector3f(1f, 10, 1f));
			/*
			 * Cylinder postGeometry = new Cylinder("treepost", 10, 10, 0.1f,
			 * 10); Quaternion q2 = new Quaternion(); //rotate the cylinder to
			 * be vertical q2.fromAngleAxis(FastMath.PI/2, new Vector3f(1,0,0));
			 * postGeometry.setLocalRotation(q2);
			 */
			// postGeometry.setModelBound(new BoundingBox());
			// postGeometry.updateModelBound();
			boxGeometry.setModelBound(new BoundingBox());
			boxGeometry.updateModelBound();

			BlendState blendState2 = DisplaySystem.getDisplaySystem()
					.getRenderer().createBlendState();
			blendState2.setBlendEnabled(true);
			blendState2
					.setSourceFunction(BlendState.SourceFunction.DestinationAlpha);

			blendState2
					.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);

			blendState2.setTestEnabled(true);
			blendState2.setTestFunction(BlendState.TestFunction.Never);
			blendState2.setEnabled(true);

			boxGeometry.setRenderState(blendState2);
			boxGeometry.updateRenderState();

			// Ponerlo despues de que se cargue el auto

			/*
			 * TextureState ts3 =
			 * DisplaySystem.getDisplaySystem().getRenderer().
			 * createTextureState(); Texture t3 =
			 * TextureManager.loadTexture(ForceFieldFence.class.getClassLoader()
			 * .getResource("texture/post.jpg"),
			 * Texture.MinificationFilter.Trilinear,
			 * Texture.MagnificationFilter.Bilinear); ts3.setTexture(t3);
			 */

			/*
			 * Node post = new Node("treepost"); post.attachChild(postGeometry);
			 * //post.setRenderState(ts3); post.setLocalScale(15);
			 * post.setLocalTranslation(0, -20, 0);
			 */

	        final StaticPhysicsNode staticNode = rg.getPhysicsSpace().createStaticNode();
	        /*PhysicsCylinder pc = staticNode.createCylinder("c" + this.getName());
	        pc.setLocalRotation(q2);*/
	        staticNode.attachChild(boxGeometry);
	        
	        //staticNode.attachChild(pc);
	        staticNode.setLocalScale(new Vector3f(1,500,1));
	        staticNode.setLocalTranslation(-2.75f, 0, 3.25f);
	        staticNode.updateGeometricState(0, true);
	        //staticNode.updateRenderState();
	
	        //staticNode.attachChild( post );
	        
	        this.attachChild( staticNode );
	        
	        staticNode.generatePhysicsGeometry();
        }
		
	}
	
    public Tree(String name, boolean arbust) {
		this.setName(name);
		this.isBush = arbust;
		buildTree();
	}

	public void placeTree(float x, float y, float z) {
    	localTranslation.x = x;
    	localTranslation.y = y;
    	localTranslation.z = z;
    }	
	
}
