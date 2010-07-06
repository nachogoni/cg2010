package ar.edu.itba.cg_final.states.utils;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.system.DisplaySystem;
import com.jmex.effects.transients.Transient;

public class RallyFadeOutIn extends Transient {

	private static final long serialVersionUID = 1L;
	private Geometry fadeQ;
	private Node fadeInNode, fadeOutNode;
	private ColorRGBA fadeColor;
	private float speed;
	private Quad fadeQuad;
	private Node stateNode;
	private boolean finished;

	/**
	 * Creates a new FadeInOut node. The speed is by default .01f
	 * 
	 * @param name
	 *            The name of the node.
	 * @param fade
	 *            The geometry whos per vertex color will fade over time.
	 * @param out
	 *            The begining node that will fade out.
	 * @param in
	 *            The ending node that will fade in.
	 * @param c
	 *            The begining color of the fade Geometry.
	 */
	public RallyFadeOutIn(String name, Geometry fade, Node out, Node in, ColorRGBA c) {
		super(name);
		initialise(fade, out, in, c, 0.01f);
	}

	public RallyFadeOutIn(String name, Node rNode, Node out, Node in, ColorRGBA c) {
		this(name,rNode,out,in,c,0.01f);
	}

	public RallyFadeOutIn(String name, Node rNode, Node out, Node in, ColorRGBA c, float spd) {
		super(name);
	    fadeQuad = new Quad("Fade Quad");
	    fadeQuad.updateGeometry(DisplaySystem.getDisplaySystem().getWidth(), DisplaySystem.getDisplaySystem().getHeight());
//	    fadeQuad.updateGeometry(5,5);
	    fadeQuad.setColorBuffer(null);
	    fadeQuad.getLocalTranslation().z = 1;
	    fadeQuad.setLightCombineMode(LightCombineMode.Off);
	    initialise(fadeQuad, out, in, c, spd);
	}
	
	/**
	 * Creates a new FadeInOut node.
	 * 
	 * @param name
	 *            The name of the node.
	 * @param fade
	 *            The geometry whos per vertex color will fade over time.
	 * @param out
	 *            The begining node that will fade out.
	 * @param in
	 *            The ending node that will fade in.
	 * @param c
	 *            The begining color of the fade geometry.
	 * @param s
	 *            The speed at which the fade will take place.
	 */
	public RallyFadeOutIn(String name, Geometry fade, Node out, Node in,
			ColorRGBA c, float s) {
		super(name);
		initialise(fade, out, in, c, s);
	}

	private void initialise(Geometry fade, Node out, Node in, ColorRGBA c, float speed) {
		finished = false;
		setMaxNumOfStages(2);
		setCurrentStage(0);
		setSpeed(speed);

		fadeColor = (ColorRGBA) c.clone();
		fadeColor.a = 0;

		fadeInNode = in;
		fadeOutNode = out;
		fadeQ = fade;

		BlendState fadeAS = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		fadeAS.setBlendEnabled(true);
		fadeAS.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		fadeAS.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		fadeAS.setTestEnabled(false);
		fadeAS.setTestFunction(BlendState.TestFunction.GreaterThanOrEqualTo);
		fadeAS.setEnabled(true);

		fadeQ.setRenderState(fadeAS);

		this.attachChild(fadeOutNode);
	}

	/**
	 * Returns the Geometry that is fading.
	 * 
	 * @return The currently fading geometry.
	 */
	public Geometry getFadeQuad() {
		return fadeQ;
	}

	/**
	 * Sets the geometry that will fade.
	 * 
	 * @param f
	 *            The new geometry that will fade.
	 */
	public void setFadeQuad(Geometry f) {
		fadeQ = f;
	}

	/**
	 * Returns the node this object is fading into.
	 * 
	 * @return The current fade in node.
	 */
	public Node getFadeInNode() {
		return fadeInNode;
	}

	/**
	 * Sets the node that this object will fade into.
	 * 
	 * @param fade
	 *            The node to fade into.
	 */
	public void setFadeInNode(Node fade) {
		fadeInNode = fade;
	}

	/**
	 * Returns the node this object is fading from.
	 * 
	 * @return The current fade out node.
	 */
	public Node getFadeOutNode() {
		return fadeOutNode;
	}

	/**
	 * Sets the node this object will fade from.
	 * 
	 * @param fade
	 *            The new fade out node.
	 */
	public void setFadeOutNode(Node fade) {
		fadeOutNode = fade;
	}

	/**
	 * Returns the current color being applied to the fade quad.
	 * 
	 * @return The current fade color.
	 */
	public ColorRGBA getFadeColor() {
		return fadeColor;
	}

	/**
	 * Sets the current per vertex color of the fade quad, and updates the
	 * current fade color to c.
	 * 
	 * @param c
	 *            The new color to set the fade quad too.
	 */
	public void setFadeColor(ColorRGBA c) {
		fadeColor = (ColorRGBA) c.clone();
		fadeQ.setDefaultColor(fadeColor);
	}

	/**
	 * Returns the speed that this object should fade at.
	 * 
	 * @return The current speed.
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * Sets the speed this object should fade at.
	 * 
	 * @param s
	 *            The new speed.
	 */
	public void setSpeed(float s) {
		speed = s;
	}

	/**
	 * Ignoring children, this only updates all the controllers of this
	 * FadeInOut
	 * 
	 * @param time
	 *            the time to pass to update.
	 */
//	public void updateWorldData(float time) {
//		if (getControllers().size() != 0) {
//			for (int i = 0; i < getControllers().size(); i++) {
//				(getController(i)).update(time);
//			}
//		}
//	}

	public void attachTo(Node stateNode) {
		this.stateNode = stateNode;
		stateNode.attachChild(this);
		stateNode.attachChild(fadeQuad);
	}

	public void detachFromNode() {
		stateNode.detachChild(fadeQuad);
		stateNode.detachChild(this);
//		stateNode.attachChild(getFadeInNode());
	}

	public void setFinished() {
		finished = true;
	}

	public boolean hasFinished() {
		return finished;
	}
}
