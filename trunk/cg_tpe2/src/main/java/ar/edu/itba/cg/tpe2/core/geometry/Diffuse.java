package ar.edu.itba.cg.tpe2.core.geometry;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Diffuse {

	
	private Color c;
	private BufferedImage img = null;
	

	public Diffuse(float r, float g, float b) {
		this.c = new Color(r,g,b);
	}
	
	public Diffuse(String textureFilename){
		File input = new File(textureFilename);
		try {
			img = ImageIO.read(input);
		} catch (IOException e) {
			throw new IllegalArgumentException("Texture "+textureFilename+" could not be loaded");
		}
	}
	
	public Color getColorAt(int x, int y){
		if ( img == null )
			return c;
		
		return new Color(img.getRGB(x, y));
	}
}
