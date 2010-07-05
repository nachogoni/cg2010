package ar.edu.itba.cg_final.textures;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import com.jmex.terrain.util.AbstractHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;

public class GrassProceduralTexture extends ProceduralTextureGenerator {
	private static final Logger logger = Logger
			.getLogger(GrassProceduralTexture.class.getName());

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
	public GrassProceduralTexture(AbstractHeightMap heightMap) {
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

	/**
	 * <code>createTexture</code> overrides the method in
	 * <code>ProcduralTextureGenerator</code> to provide the capability to
	 * overlay the existing procedural texture with one or more 'splat' maps.
	 */
	public void createTexture(int textureSize) {
		super.createTexture(textureSize);

		BufferedImage img = new BufferedImage(textureSize, textureSize,
				BufferedImage.TYPE_INT_RGB);
		BufferedImage splatTexture;
		BufferedImage splatMap;

		float alpha;
		int scaledX;
		int scaledY;

		int rgb;
		int red;
		int green;
		int blue;

		int splatSize = splatTextures.size();

		img.getGraphics().drawImage(proceduralTexture.getImage(), 0, 0, null);

		for (int x = 0; x < textureSize; x++) {
			for (int y = 0; y < textureSize; y++) {
				rgb = img.getRGB(x, y);
				red = (rgb & 0x00FF0000) >> 16;
				green = (rgb & 0x0000FF00) >> 8;
				blue = (rgb & 0x000000FF);

				for (int i = 0; i < splatSize; i++) {
					splatMap = splatMaps.get(i);
					splatTexture = splatTextures.get(i);

					// Retrieve the amount of the color to use for this texture.
					scaledX = (int) (x * (splatMap.getWidth() / (float) textureSize));
					scaledY = (int) (splatMap.getHeight() - ((y * (splatMap
							.getHeight() / (float) textureSize)) + 1));

					alpha = ((splatMap.getRGB(scaledX, scaledY) >> 24) & 0x000000FF) / 255.0f;

					// We may have to tile the texture if the terrain is larger
					// than the texture.
					scaledX = x % splatTexture.getWidth();
					scaledY = y % splatTexture.getHeight();

					// perform alpha composite
					if (alpha > 0) {
						red = (int) ((red * (1.0f - alpha)) + (((splatTexture
								.getRGB(scaledX, scaledY) & 0x00FF0000) >> 16) * alpha));
						green = (int) ((green * (1.0f - alpha)) + (((splatTexture
								.getRGB(scaledX, scaledY) & 0x0000FF00) >> 8) * alpha));
						blue = (int) ((blue * (1.0f - alpha)) + (((splatTexture
								.getRGB(scaledX, scaledY) & 0x000000FF)) * alpha));
					}
				}

				// set the color for the final texture.
				rgb = red << 16 | green << 8 | blue;
				img.setRGB(x, y, rgb);

				red = 0;
				green = 0;
				blue = 0;
			}
		}

		// create the new image from the data.
		proceduralTexture = new ImageIcon(img);
		proceduralTexture.setDescription("TerrainTexture");

		logger.fine("Created splat texture successfully.");
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
