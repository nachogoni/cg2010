package ar.edu.itba.cg_final.map;

import ar.edu.itba.cg_final.RallyGame;
import ar.edu.itba.cg_final.settings.GameUserSettings;
import ar.edu.itba.cg_final.settings.GlobalSettings;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.math.Vector3f;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.material.Material;

public class Obstacles extends Node {

	private static final long serialVersionUID = -1725837107311137978L;
	public Obstacles(String name, GlobalSettings gs, GameUserSettings gus) {
		this(gs, gus);
		this.setName(name);
	}
	
	public Obstacles(GlobalSettings gs, GameUserSettings gus) {
        //createStuff( 10, 0, 10, 0, null, new Sphere( "", 20, 20, 5 ) );
        
        createStuff( -10, 0, -10, 0, null, new Box( "", new Vector3f(), 5, 5, 5 ),gs, gus);
        createStuff( 0, 0, -10, 0, null, new Box( "", new Vector3f(), 5, 5, 5 ),gs, gus);
        createStuff( 10, 0, -10, 0, null, new Box( "", new Vector3f(), 5, 5, 5 ),gs, gus);
        createStuff( -5, 10, -10, 0, null, new Box( "", new Vector3f(), 5, 5, 5 ),gs, gus);
        createStuff( 5, 10, -10, 0, null, new Box( "", new Vector3f(), 5, 5, 5 ),gs, gus);
        createStuff( 0, 20, -10, 0, null, new Box( "", new Vector3f(), 5, 5, 5 ),gs, gus);

	}
	
    private void createStuff( float x, float y, float z, float angle, Vector3f axis, Geometry geom, GlobalSettings gs, GameUserSettings gus) {
    	MinificationFilter minF;
    	MagnificationFilter maxF;
    	
		if ( gus.getHighRes() ) {
            minF = MinificationFilter.Trilinear; 
            maxF = MagnificationFilter.Bilinear;
        } else {
            minF = MinificationFilter.NearestNeighborNoMipMaps; 
            maxF = MagnificationFilter.NearestNeighbor;        	
        }
    	
        //Quad q = new Quad("Quad");
        TextureState ts2 = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        ts2.setEnabled(true);
        Texture t4 = TextureManager.loadTexture(
            RallyGame.class.getClassLoader().getResource(
            gs.getProperty("TRACK1.OBSTACLE.TEXTURE")), minF, maxF);
        
        ts2.setTexture(t4);
    	
    	RallyGame rg = RallyGame.getInstance();
    	
        final DynamicPhysicsNode node = rg.getPhysicsSpace().createDynamicNode();
        node.attachChild( geom );
        node.setMaterial( Material.WOOD );
        node.setMass(node.getMass());
        node.generatePhysicsGeometry();
        geom.setModelBound( new BoundingBox() );
        geom.updateModelBound();
        if ( axis != null ) {
            node.getLocalRotation().fromAngleNormalAxis( angle, axis );
        }
        node.getLocalTranslation().set( x, y, z );
        node.rest();
        
        node.setRenderState(ts2);
        
        this.attachChild( node );
    }	
    

    public void placeObstacles(float x, float y, float z) {
    	localTranslation.x = x;
    	localTranslation.y = y;
    	localTranslation.z = z;
    }	    
}
