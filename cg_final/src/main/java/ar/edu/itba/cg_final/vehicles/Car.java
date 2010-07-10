package ar.edu.itba.cg_final.vehicles;

import ar.edu.itba.cg_final.controller.Audio;
import ar.edu.itba.cg_final.controller.Audio.soundsEffects;
import ar.edu.itba.cg_final.settings.GlobalSettings;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.material.Material;

public class Car extends Node {

    private static final long serialVersionUID = 1L;

    // The node to represent the car chassis
    private DynamicPhysicsNode chassisNode;

    // Two suspesion systems
    private Suspension rearSuspension, frontSuspension;

	private boolean locked = false;
    
	Audio audio;
    
    public Car( final PhysicsSpace pSpace, GlobalSettings gs, Audio audio ) {
        super( "car" );
        createChassi( pSpace );
        createSuspension( pSpace );
        this.audio = audio;
        //loadFancySmoke();
    }

    public void isLocked(boolean state) {
    	locked  = state;
    }

    public Vector3f getPosition() {
        return chassisNode.getLocalTranslation();
    }
    
    public void setPosition( float x, float y, float z ) {
        chassisNode.clearDynamics();
        chassisNode.getLocalTranslation().set( x, y, z );
        chassisNode.getLocalRotation().loadIdentity();
        frontSuspension.resetPosition();
        rearSuspension.resetPosition();
    }

    public void setRotation( float x, float y, float z, float w ) {
        chassisNode.clearDynamics();
        chassisNode.getLocalRotation().set( x, y, z, w );
        frontSuspension.resetPosition();
        rearSuspension.resetPosition();
    }

    public void setRotation( float x, float y, float z) {
        chassisNode.clearDynamics();
        chassisNode.getLocalRotation().set(new Quaternion(new float[]{x,y,z}));
        frontSuspension.resetPosition();
        rearSuspension.resetPosition();
    }

    /**
     * Each suspension is away from the chassis center by an offset.
     * The x value represents the axis distance from chassis center
     * The y value represents the suspension height
     * The z value represents the side offset of the suspension from the car center line
     *
     * @param pSpace physics space
     */
    private void createSuspension( final PhysicsSpace pSpace ) {   	
        frontSuspension = new Suspension( pSpace, chassisNode, CarData.FRONT_SUSPENSION_OFFSET, true );
        this.attachChild( frontSuspension );
        rearSuspension = new Suspension( pSpace, chassisNode, CarData.REAR_SUSPENSION_OFFSET, false );
        this.attachChild( rearSuspension );
    }

    private void createChassi( final PhysicsSpace pSpace ) {
        chassisNode = pSpace.createDynamicNode();
        chassisNode.setName( "chassiPhysicsNode" );

        // The model of the chassis can be changed at the CarData interface
        Node chassisModel = Util.loadModel( CarData.CHASSIS_MODEL );
        chassisModel.setLocalScale( CarData.CHASSIS_SCALE );

        chassisNode.attachChild( chassisModel );

        // use false if you're going to use many cars
        chassisNode.generatePhysicsGeometry( true );
        chassisNode.setMaterial( Material.IRON );
        chassisNode.setMass( CarData.CHASSIS_MASS );
        this.attachChild( chassisNode );
    }

    /**
     * Accelerates the car forward or backwards
     * Does it by accelerating both suspensions (4WD). If you want a front wheel drive, comment out
     * the rearSuspension.accelerate(direction) line. If you want a rear wheel drive car comment out the other one.
     *
     * @param direction 1 for ahead and -1 for backwards
     */
    public void accelerate( final int direction ) {
    	if (locked)
    		return;
        rearSuspension.accelerate( direction );
        frontSuspension.accelerate( direction );
        audio.playSound(soundsEffects.ACEL);
        //acelSound.fadeIn(2.5f,3f);
    }

    /**
     * Stops accelerating both suspensions
     */
    public void releaseAccel() {
    	if (locked)
    		return;
        rearSuspension.releaseAccel();
        frontSuspension.releaseAccel();
        audio.stopSound(soundsEffects.ACEL);
    }

    /**
     * Steers the front wheels.
     *
     * @param direction 1 for right and -1 for left
     */
    public void steer( final float direction ) {
        frontSuspension.steer( direction );
    }

    /**
     * Unsteer the front wheels
     */
    public void unsteer() {
        frontSuspension.unsteer();
    }

    private final Vector3f tmpVelocity = new Vector3f();
    
    /**
     * To get the car speed for using in a HUD
     *
     * @return velocity of the car
     */
    public float getLinearSpeed() {
        return chassisNode.getLinearVelocity( tmpVelocity ).length();
    }

    /**
     * Needed e.g. by the ChaseCamera.
     *
     * @return node which represents the chassis
     */
	public DynamicPhysicsNode getChassis() {
		return chassisNode;
	}


}
