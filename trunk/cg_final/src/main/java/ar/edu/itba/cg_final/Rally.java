package ar.edu.itba.cg_final;

import ar.edu.itba.cg_final.map.SkyBox;

import com.jme.app.BaseGame;
import com.jme.scene.Skybox;
 
public class Rally extends BaseGame {

	Skybox skyBox;
	
	public static void main(String[] args) {
		Rally app = new Rally();
		app.setConfigShowMode(ConfigShowMode.ShowIfNoConfig);
		app.start();
	}
	
	@Override
	protected void cleanup() {
		
	}

	@Override
	protected void initGame() {
		//Create skybox
		skyBox = SkyBox.getNightSkyBox(display, 200f, 200f, 200f);
		
	}

	@Override
	protected void initSystem() {
		
	}

	@Override
	protected void reinit() {
		
	}

	@Override
	protected void render(float interpolation) {
//        Renderer r = display.getRenderer();
//
//        /** Draw the rootNode and all its children. */
//        r.draw(rootNode);
//        
//        /** Call simpleRender() in any derived classes. */
//        simpleRender();
//        
//        /** Draw the stats node to show our stat charts. */
//        r.draw(statNode);
//        
//        doDebug(r);
	}

	@Override
	protected void update(float interpolation) {
		
	}

	
}