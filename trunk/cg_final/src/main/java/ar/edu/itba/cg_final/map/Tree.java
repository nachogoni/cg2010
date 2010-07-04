package ar.edu.itba.cg_final.map;

import java.util.Random;

import ar.edu.itba.cg_final.RallyGame;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.BillboardNode;
import com.jme.scene.Node;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.geometry.PhysicsCylinder;

public class Tree extends Node {
	
	private static final long serialVersionUID = 1130692595359578525L;
	public Tree(String name) {
		this();
		this.setName(name);
	}
	
	public Tree() {

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
        
        Cylinder postGeometry = new Cylinder("treepost", 10, 10, 0.1f, 10);
        Quaternion q2 = new Quaternion();
        //rotate the cylinder to be vertical
        q2.fromAngleAxis(FastMath.PI/2, new Vector3f(1,0,0));
        postGeometry.setLocalRotation(q2);
        postGeometry.setModelBound(new BoundingBox());
        postGeometry.updateModelBound();     
        
        //Ponerlo despues de que se cargue el auto
        
        /*TextureState ts3 = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        Texture t3 = TextureManager.loadTexture(ForceFieldFence.class.getClassLoader()
                  .getResource("texture/post.jpg"),
                  Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
        ts3.setTexture(t3);*/
        
        /*Node post = new Node("treepost");
        post.attachChild(postGeometry);
        //post.setRenderState(ts3);
        post.setLocalScale(15);
        post.setLocalTranslation(0, -20, 0);*/
        
        final StaticPhysicsNode staticNode = rg.getPhysicsSpace().createStaticNode();
        PhysicsCylinder pc = staticNode.createCylinder((new Integer((new Random(100)).nextInt())).toString());
        pc.setLocalRotation(q2);
        
        //staticNode.attachChild(pc);
        staticNode.setLocalTranslation(2, 0, 0);
        staticNode.setLocalScale(new Vector3f(1,500,100));
        staticNode.updateGeometricState(0, true);
        //staticNode.updateRenderState();

        //staticNode.attachChild( post );
        
        this.attachChild( staticNode );
        
        staticNode.generatePhysicsGeometry();     
		
	}
    public void placeTree(float x, float y, float z) {
    	localTranslation.x = x;
    	localTranslation.y = y;
    	localTranslation.z = z;
    }	
	
}
