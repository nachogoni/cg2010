package ar.edu.itba.cg_final.map;

import java.net.MalformedURLException;

import org.apache.log4j.Logger;

import ar.edu.itba.cg_final.settings.GlobalSettings;
import ar.edu.itba.cg_final.utils.ResourceLoader;

import com.jme.image.Texture;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.state.CullState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class RallySkyBox extends Skybox{

	private static final long serialVersionUID = -3777842691534981883L;
	private static final Logger logger = Logger.getLogger(RallySkyBox.class.getName());
	
	public static Skybox getSkyBox(GlobalSettings gs, String skybox, DisplaySystem display) {
		if (skybox.equals("Night")) {
			return getNightSkyBox(display, gs);
		} else if (skybox.equals("Afternoon")) {
			return getRedSkyBox(display, gs);
		} else {
			return getDaySkyBox(display, gs);
		}
	}
	
	public static Skybox getRedSkyBox(DisplaySystem display, GlobalSettings gs) {
		return getSkyBox(display, 
				gs.getIntProperty("SKYBOX.XEXTENT"), 
				gs.getIntProperty("SKYBOX.YEXTENT"), 
				gs.getIntProperty("SKYBOX.ZEXTENT"), 
				gs.getProperty("SKYBOX.AFTERNOON.NORTH"),
				gs.getProperty("SKYBOX.AFTERNOON.WEST"), 
				gs.getProperty("SKYBOX.AFTERNOON.SOUTH"),
				gs.getProperty("SKYBOX.AFTERNOON.EAST"), 
				gs.getProperty("SKYBOX.AFTERNOON.TOP"),
				gs.getProperty("SKYBOX.AFTERNOON.BOTTOM")); 
	}
	
	public static Skybox getDaySkyBox(DisplaySystem display, GlobalSettings gs) {
		return getSkyBox(display, 
				gs.getIntProperty("SKYBOX.XEXTENT"), 
				gs.getIntProperty("SKYBOX.YEXTENT"), 
				gs.getIntProperty("SKYBOX.ZEXTENT"), 
				gs.getProperty("SKYBOX.DAY.NORTH"),
				gs.getProperty("SKYBOX.DAY.WEST"), 
				gs.getProperty("SKYBOX.DAY.SOUTH"),
				gs.getProperty("SKYBOX.DAY.EAST"), 
				gs.getProperty("SKYBOX.DAY.TOP"),
				gs.getProperty("SKYBOX.DAY.BOTTOM")); 
	}
	
	public static Skybox getNightSkyBox(DisplaySystem display, GlobalSettings gs) {
		return getSkyBox(display, 
				gs.getIntProperty("SKYBOX.XEXTENT"), 
				gs.getIntProperty("SKYBOX.YEXTENT"), 
				gs.getIntProperty("SKYBOX.ZEXTENT"), 
				gs.getProperty("SKYBOX.NIGHT.NORTH"),
				gs.getProperty("SKYBOX.NIGHT.WEST"), 
				gs.getProperty("SKYBOX.NIGHT.SOUTH"),
				gs.getProperty("SKYBOX.NIGHT.EAST"), 
				gs.getProperty("SKYBOX.NIGHT.TOP"),
				gs.getProperty("SKYBOX.NIGHT.BOTTOM")); 
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

			ZBufferState zState = display.getRenderer().createZBufferState();
			zState.setEnabled(false);
			skybox.setRenderState(zState);

			/*FogState fs = display.getRenderer().createFogState();
			fs.setEnabled(false);
			skybox.setRenderState(fs);*/

			skybox.setLightCombineMode(Spatial.LightCombineMode.Off);
			skybox.setCullHint(Spatial.CullHint.Never);
			skybox.setTextureCombineMode(TextureCombineMode.Replace);
			skybox.updateRenderState();
			skybox.lockBounds();
			skybox.lockMeshes();

			
			
		} catch (NullPointerException e) {
			logger.error("Error at the skybox creation");
		} catch (MalformedURLException e) {
			logger.error("Error while loading skybox texture");
		}		
		
		return skybox;
	}
	
}
