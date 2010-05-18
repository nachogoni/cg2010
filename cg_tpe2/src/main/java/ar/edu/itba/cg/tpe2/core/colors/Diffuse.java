package ar.edu.itba.cg.tpe2.core.colors;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.vecmath.Point3f;

import ar.edu.itba.cg.tpe2.core.geometry.Primitive;

public class Diffuse {

	private static String curr_dir = System.getProperty("user.dir");
	private Color c;
	private BufferedImage img = null;
	

	public Diffuse(float r, float g, float b) {
		this.c = new Color(r,g,b);
	}
	
	public Diffuse(String textureFilename){
		File input = new File(curr_dir+"/textures/"+textureFilename);
		try {
			img = ImageIO.read(input);
		} catch (IOException e) {
			throw new IllegalArgumentException("Texture "+textureFilename+" could not be loaded");
		}
	}
	
	public Color getColorAt(Point3f aPoint, Primitive primitive){
		if ( img == null )
			return c;
		
		float[] uv = primitive.getUV(aPoint);

		return new Color(img.getRGB((int)(uv[0]*img.getWidth()), (int)(uv[1]*img.getHeight())));
	}

	@Override
	public String toString() {
		return "Diffuse [c=" + c + ", img=" + img + "]";
	}

}
