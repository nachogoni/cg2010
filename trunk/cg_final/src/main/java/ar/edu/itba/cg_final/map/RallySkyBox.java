package ar.edu.itba.cg_final.map;

import java.net.MalformedURLException;
import org.apache.log4j.Logger;
import ar.edu.itba.cg_final.utils.ResourceLoader;
import com.jme.image.Texture;
import com.jme.scene.Skybox;
import com.jme.scene.state.CullState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class RallySkyBox extends Skybox{

	private static final long serialVersionUID = -3777842691534981883L;
	private static final Logger logger = Logger.getLogger(RallySkyBox.class.getName());
	
	public static Skybox getRedSkyBox(DisplaySystem display, float xExtent, float yExtent, float zExtent) {
		return getSkyBox(display, xExtent, yExtent, zExtent, 
				"skyboxes/red/north.jpg", "skyboxes/red/west.jpg", 
				"skyboxes/red/south.jpg", "skyboxes/red/east.jpg", 
				"skyboxes/red/top.jpg", "skyboxes/red/bottom.jpg"); 
	}
	
	public static Skybox getDaySkyBox(DisplaySystem display, float xExtent, float yExtent, float zExtent) {
		return getSkyBox(display, xExtent, yExtent, zExtent, 
				"skyboxes/day/north.jpg", "skyboxes/day/west.jpg", 
				"skyboxes/day/south.jpg", "skyboxes/day/east.jpg", 
				"skyboxes/day/top.jpg", "skyboxes/day/bottom.jpg"); 
	}
	
	public static Skybox getNightSkyBox(DisplaySystem display, float xExtent, float yExtent, float zExtent) {
		return getSkyBox(display, xExtent, yExtent, zExtent, 
				"skyboxes/night/north.jpg", "skyboxes/night/west.jpg", 
				"skyboxes/night/south.jpg", "skyboxes/night/east.jpg", 
				"skyboxes/night/top.jpg", "skyboxes/night/bottom.jpg"); 
	}
	
	public static Skybox getSkyBox(DisplaySystem display, float xExtent, float yExtent, float zExtent,
			String north, String west, String south, String east, String top, String bottom) {
		Skybox skybox = new Skybox("skybox", xExtent, yExtent, zExtent);
		
		try {		
			skybox.setTexture(Skybox.Face.North, 
					TextureManager.loadTexture(ResourceLoader.getURL(north), 
					Texture.MinificationFilter.BilinearNearestMipMap,
					Texture.MagnificationFilter.Bilinear));
			skybox.setTexture(Skybox.Face.West, 
					TextureManager.loadTexture(ResourceLoader.getURL(west),
					Texture.MinificationFilter.BilinearNearestMipMap,
					Texture.MagnificationFilter.Bilinear));
			skybox.setTexture(Skybox.Face.South, 
					TextureManager.loadTexture(ResourceLoader.getURL(south), 
					Texture.MinificationFilter.BilinearNearestMipMap,
					Texture.MagnificationFilter.Bilinear));
			skybox.setTexture(Skybox.Face.East, 
					TextureManager.loadTexture(ResourceLoader.getURL(east),
					Texture.MinificationFilter.BilinearNearestMipMap,
					Texture.MagnificationFilter.Bilinear));
			skybox.setTexture(Skybox.Face.Up, 
					TextureManager.loadTexture(ResourceLoader.getURL(top),
					Texture.MinificationFilter.BilinearNearestMipMap,
					Texture.MagnificationFilter.Bilinear));
			skybox.setTexture(Skybox.Face.Down, 
					TextureManager.loadTexture(ResourceLoader.getURL(bottom), 
					Texture.MinificationFilter.BilinearNearestMipMap,
					Texture.MagnificationFilter.Bilinear));
			skybox.preloadTextures();
	 
			CullState cullState = display.getRenderer().createCullState();
			cullState.setCullFace(CullState.Face.None);
			cullState.setEnabled(true);
			
			skybox.setRenderState(cullState);
			skybox.updateRenderState();
			
		} catch (NullPointerException e) {
			logger.error("Error at the skybox creation");
		} catch (MalformedURLException e) {
			logger.error("Error while loading skybox texture");
		}		
		
		return skybox;
	}
	
}
