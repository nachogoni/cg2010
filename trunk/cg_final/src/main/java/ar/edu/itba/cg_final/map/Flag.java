package ar.edu.itba.cg_final.map;

import java.net.URL;

import com.jme.image.Texture;
import com.jme.light.LightNode;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.math.spring.SpringPoint;
import com.jme.math.spring.SpringPointForce;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.effects.cloth.ClothPatch;
import com.jmex.effects.cloth.ClothUtils;

public class Flag extends Node {

	private static final long serialVersionUID = 1L;
    //the cloth that makes up the flag.
    private ClothPatch cloth;
    //parameters for the wind
    private float windStrength = 15f;
    private Vector3f windDirection = new Vector3f(0.8f, 0, 0.2f);
    private SpringPointForce gravity, wind;
	
	public Flag() {
		this(0, 0, 0, 1);
	}
	
	public Flag(float x, float y, float z, float scale) {
		this(x, y, z, scale, Flag.class.getClassLoader().getResource(
		"images/Monkey.jpg"));
	}
	
	public Flag(float x, float y, float z, float scale, URL clothPath) {
        super();
        //create a cloth patch that will handle the flag part of our flag.
        cloth = new ClothPatch("cloth", 25, 25, 1f, 10);
        // Add our custom flag wind force to the cloth
        wind = new RandomFlagWindForce(windStrength, windDirection);
        cloth.addForce(wind);
        // Add a simple gravitational force:
        gravity = ClothUtils.createBasicGravity();
        cloth.addForce(gravity);
        
        //Create the flag pole
        Cylinder c = new Cylinder("pole", 10, 10, 0.5f, 50 );
        this.attachChild(c);
        Quaternion q = new Quaternion();
        //rotate the cylinder to be vertical
        q.fromAngleAxis(FastMath.PI/2, new Vector3f(1,0,0));
        c.setLocalRotation(q);
        c.setLocalTranslation(new Vector3f(-12.5f,-12.5f,0));

        //create a texture that the flag will display.
        //Let's promote jME! 
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        ts.setTexture(
            TextureManager.loadTexture(clothPath,
            Texture.MinificationFilter.Trilinear,
            Texture.MagnificationFilter.Bilinear));
        
        //We'll use a LightNode to give more lighting to the flag, we use the node because
        //it will allow it to move with the flag as it hops around.
        //first create the light
        PointLight dr = new PointLight();
        dr.setEnabled( true );
        dr.setDiffuse( new ColorRGBA( 1.0f, 1.0f, 1.0f, 1.0f ) );
        dr.setAmbient( new ColorRGBA( 0.5f, 0.5f, 0.5f, 1.0f ) );
        dr.setLocation( new Vector3f( 0.5f, -0.5f, 0 ) );
        //next the state
        LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
        lightState.setEnabled(true);
        lightState.setTwoSidedLighting( true );
        lightState.attach(dr);
        //last the node
        LightNode lightNode = new LightNode( "light" );
        lightNode.setLight( dr );
        lightNode.setLocalTranslation(new Vector3f(15,10,0));

        this.setRenderState(lightState);
        this.attachChild(lightNode);
        
        cloth.setRenderState(ts);
        //We want to see both sides of the flag, so we will turn back facing culling OFF.
        CullState cs = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
        cs.setCullFace(CullState.Face.None);
        cloth.setRenderState(cs);
        this.attachChild(cloth);
        
        //We need to attach a few points of the cloth to the poll. These points shouldn't
        //ever move. So, we'll attach five points at the top and 5 at the bottom. 
        //to make them not move the mass has to be high enough that no force can move it.
        //I also move the position of these points slightly to help bunch up the flag to
        //give it better realism.
        for (int i = 0; i < 5; i++) {
            cloth.getSystem().getNode(i*25).position.y *= .8f;
            cloth.getSystem().getNode(i*25).setMass(Float.POSITIVE_INFINITY);
            
        }
        
        for (int i = 24; i > 19; i--) {
            cloth.getSystem().getNode(i*25).position.y *= .8f;
            cloth.getSystem().getNode(i*25).setMass(Float.POSITIVE_INFINITY);
            
        }
        this.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
        this.setLocalScale(scale);
        placeFlag(x, y + c.getHeight(), z);
    }
	
    public void update(float time) {
    	
    }
	
    public void placeFlag(float x, float z) {
    	placeFlag(x, 0, z);
    }
    
    public void placeFlag(float x, float y, float z) {
    	localTranslation.x = x;
    	localTranslation.y = y;
    	localTranslation.z = z;
    }
	
    /**
     * RandomFlagWindForce defines a SpringPointForce that will slighly adjust the
     * direction of the wind and the force of the wind. This will cause the flag
     * to flap in the wind and rotate about the flag pole slightly, giving it a
     * realistic movement.
     * @author Mark Powell
     *
     */
    private class RandomFlagWindForce extends SpringPointForce{
        
        private final float strength;
        private final Vector3f windDirection;

        /**
         * Creates a new force with a defined max strength and a starting direction.
         * @param strength the maximum strength of the wind.
         * @param direction the starting direction of the wind.
         */
        public RandomFlagWindForce(float strength, Vector3f direction) {
            this.strength = strength;
            this.windDirection = direction;
        }
        
        /**
         * called during the update of the cloth. Will adjust the direction slightly
         * and adjust the strength slightly.
         */
        public void apply(float dt, SpringPoint node) {
            windDirection.x += dt * (FastMath.nextRandomFloat() - 0.5f);
            windDirection.z += dt * (FastMath.nextRandomFloat() - 0.5f);
            windDirection.normalize();
            float tStr = FastMath.nextRandomFloat() * strength;
            node.acceleration.addLocal(windDirection.x * tStr, windDirection.y
                    * tStr, windDirection.z * tStr);
        }
    };
    
}
