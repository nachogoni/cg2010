package ar.edu.itba.cg_final;

import ar.edu.itba.cg_final.controller.GameController;
import ar.edu.itba.cg_final.map.SkyBox;

import com.jme.app.BaseSimpleGame;
import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.renderer.Renderer;
import com.jme.scene.Skybox;
import com.jme.util.geom.Debugger;
 
public class Rally extends BaseSimpleGame {

	// SkyBox
	Skybox skyBox;
	// GameController
	GameController gc;
	
	public static void main(String[] args) {
		Rally app = new Rally();
		app.setConfigShowMode(ConfigShowMode.ShowIfNoConfig);
		app.start();
	}

	@Override
	protected void simpleInitGame() {
		// Our KeyBinding
		keyBinding();
		
		skyBox = SkyBox.getNightSkyBox(display, 200, 200, 200);
		
		rootNode.attachChild(skyBox);
		//rootNode.setCullHint(Spatial.CullHint.Never);
		
		
		
	}

    protected final void update(float interpolation) {
        super.update(interpolation);

        if ( !pause ) {
            /** Call simpleUpdate in any derived classes of SimpleGame. */
            simpleUpdate();

            /** Update controllers/render states/transforms/bounds for rootNode. */
            rootNode.updateGeometricState(tpf, true);
            statNode.updateGeometricState(tpf, true);
        }
    }
	
    protected final void render(float interpolation) {
        super.render(interpolation);
        
        Renderer r = display.getRenderer();

        /** Draw the rootNode and all its children. */
        r.draw(rootNode);
        
        /** Call simpleRender() in any derived classes. */
        simpleRender();
        
        /** Draw the stats node to show our stat charts. */
        r.draw(statNode);
        
        doDebug(r);
    }
    
    protected void doDebug(Renderer r) {
        super.doDebug(r);

        if (showDepth) {
            r.renderQueue();
            Debugger.drawBuffer(Texture.RenderToTextureType.Depth, Debugger.NORTHEAST, r);
        }
    }
    
	private void keyBinding() {
		// Remove all the KeyBindings for the KeyBindingManager
		KeyBindingManager.getKeyBindingManager().removeAll();
		
        KeyBindingManager.getKeyBindingManager().set( "exit",
                KeyInput.KEY_ESCAPE );
		
	}
	
	
}