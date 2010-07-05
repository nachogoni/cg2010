package ar.edu.itba.cg_final.textures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import com.jmex.terrain.util.AbstractHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;

public class MarbleProceduralTexture extends ProceduralTextureGenerator {
	private static final Logger logger = Logger
			.getLogger(MarbleProceduralTexture.class.getName());

	// collection of alpha maps
	protected List<BufferedImage> splatMaps;

	// collection of texture maps
	protected List<BufferedImage> splatTextures;

	/**
	 * Constructor instantiates a new <code>ProceduralSplatTexture</code> object
	 * initializing the list for textures and the height map.
	 * 
	 * @param heightMap
	 *            the height map to use for the texture generation.
	 */
	public MarbleProceduralTexture(AbstractHeightMap heightMap) {
		super(heightMap);

		splatMaps = new ArrayList<BufferedImage>();
		splatTextures = new ArrayList<BufferedImage>();
	}

	/**
	 * <code>addSplatTexture</code> adds an additional splat texture to the list
	 * of splat textures. Each texture has an alpha map and a texture map
	 * associated with it. The alpha map determines the amount of color from the
	 * texture map to add to the existing procedural texture.
	 * 
	 * @param map
	 *            the alpha map.
	 * 
	 * @param texture
	 *            the texture map.
	 */
	public void addSplatTexture(ImageIcon map, ImageIcon texture) {
		// create the texture data.
		BufferedImage img = new BufferedImage(map.getIconWidth(), map
				.getIconHeight(), BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = (Graphics2D) img.getGraphics();
		g.drawImage(map.getImage(), null, null);
		g.dispose();

		splatMaps.add(img);

		img = new BufferedImage(texture.getIconWidth(),
				texture.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) img.getGraphics();
		g.drawImage(texture.getImage(), null, null);
		g.dispose();

		splatTextures.add(img);
	}

	public void createTexture(int textureSize) {
		super.createTexture(textureSize);

		BufferedImage img = new BufferedImage(textureSize, textureSize,
				BufferedImage.TYPE_INT_RGB);

		img.getGraphics().drawImage(proceduralTexture.getImage(), 0, 0, null);

		for (int x = 0; x < textureSize; x++) {
			for (int y = 0; y < textureSize; y++) {
				img.setRGB(x, y, getColorAt(x, y).getRGB());
			}
		}

		// create the new image from the data.
		proceduralTexture = new ImageIcon(img);
		proceduralTexture.setDescription("TerrainTexture");

		logger.fine("Created splat texture successfully.");
	}
	
	public Color getColorAt(float x, float y) {
		float scale = 0.01f;
		int maxLevel = 4;
		
		float noiseCoef = 0;
		float freq;
		float xn = scale*x, yn = scale*y;
		
		for(float level = 0 ; level < maxLevel ; level++){
			freq = (float) Math.pow(2,level);
			noiseCoef +=  (1/freq) * Math.abs( noise(freq * xn, freq * yn, 0f));
		}
		noiseCoef = (float) Math.cos(xn + noiseCoef);
		return getColor(noiseCoef);
		
	}
	protected Color clamp(float [] rgbs){
		return new Color(clamp(rgbs[0]),clamp(rgbs[1]),clamp(rgbs[2]));
	}
	
	protected float clamp(float channel){
		if ( channel > 1.0 )
			return 1;
		if ( channel < 0 )
			return 0;
		return channel;
	}	
	protected Color getColor(float noiseCoef) {
		
		
		float[] initialComponents = {0.2f, 0.2f, 0.2f};
		float[] finalComponents = {1,1,1};
		float[] resultComponents = {0,0,0};
		
		float coef = noiseCoef;
		for(int i = 0; i < 3 ; i++)
			resultComponents[i] = coef * initialComponents[i] + (1.0f - coef) * finalComponents[i];
		return clamp(resultComponents);
	}	
	

	public float noise(float x, float y, float z) {
		int X = (int) Math.floor(x) & 255, // FIND UNIT CUBE THAT
		Y = (int) Math.floor(y) & 255, // CONTAINS POINT.
		Z = (int) Math.floor(z) & 255;
		x -= Math.floor(x); // FIND RELATIVE X,Y,Z
		y -= Math.floor(y); // OF POINT IN CUBE.
		z -= Math.floor(z);
		float u = fade(x), // COMPUTE FADE CURVES
		v = fade(y), // FOR EACH OF X,Y,Z.
		w = fade(z);
		int A = p[X] + Y, AA = p[A] + Z, AB = p[A + 1] + Z, // HASH COORDINATES
															// OF
		B = p[X + 1] + Y, BA = p[B] + Z, BB = p[B + 1] + Z; // THE 8 CUBE
															// CORNERS,
		
		return lerp(w, lerp(v, lerp(u, grad(p[AA], x, y, z), // AND ADD
				grad(p[BA], x - 1, y, z)), // BLENDED
				lerp(u, grad(p[AB], x, y - 1, z), // RESULTS
						grad(p[BB], x - 1, y - 1, z))),// FROM 8
				lerp(v, lerp(u, grad(p[AA + 1], x, y, z - 1), // CORNERS
						grad(p[BA + 1], x - 1, y, z - 1)), // OF CUBE
						lerp(u, grad(p[AB + 1], x, y - 1, z - 1), grad(
								p[BB + 1], x - 1, y - 1, z - 1))));
	}
	static float fade(float t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}

	static float lerp(float t, float a, float b) {
		return a + t * (b - a);
	}

	static float grad(int hash, float x, float y, float z) {
		int h = hash & 15; // CONVERT LO 4 BITS OF HASH CODE
		float u = h < 8 ? x : y, // INTO 12 GRADIENT DIRECTIONS.
		v = h < 4 ? y : h == 12 || h == 14 ? x : z;
		return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
	}
	static final int p[] = new int[512], permutation[] = new int[256];
	static {
		Random r = new Random();
		for (int i = 0; i < 256; i++){
			permutation[i] = r.nextInt(256);
		//	System.out.println(permutation[i]);
		}
		for (int i = 0; i < 256; i++)
			p[256 + i] = p[i] = permutation[i];
		
	}	
	
	/**
	 * @return Returns the number of splat maps currently defined.
	 */
	public int getSplatSize() {
		return (splatMaps.size());
	}

	/**
	 * @return Returns the splat alpha map at the specified index.
	 */
	public BufferedImage getSplatMap(int index) {
		return splatMaps.get(index);
	}

	/**
	 * @return Returns the splat texture map at the specified index.
	 */
	public BufferedImage getSplatTexture(int index) {
		return splatTextures.get(index);
	}

	public void clearTextures() {
		super.clearTextures();
		splatMaps.clear();
		splatTextures.clear();
	}
}
