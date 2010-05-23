package ar.edu.itba.cg.tpe2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ar.edu.itba.cg.tpe2.utils.Noise;
import ar.edu.itba.cg.tpe2.utils.PerlinNoise;
import ar.edu.itba.cg.tpe2.utils.SimplexNoise;

/**
 * TPE2
 * 
 */
public class CopyOfApp {

	private static BufferedImage image;
	
	public static void main(String[] args) {

		int pow = 10;
		
		Noise n = new Noise(pow);
		
		float v;
		
		int size = (int)Math.pow(2, pow);
		
		image = new BufferedImage(size, size, BufferedImage.TYPE_3BYTE_BGR);
		
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				v = n.getValue(x, y);
				image.setRGB(x, y, (new Color(v, v, v)).getRGB());
			}
		}

		try {
			ImageIO.write(image, "PNG", new File("noise.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		PerlinNoise p = new PerlinNoise();
		
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				v = (float)Math.abs(p.noise(x, y));
				image.setRGB(x, y, (new Color(v, v, v)).getRGB());
			}
		}
		
		try {
			ImageIO.write(image, "PNG", new File("noiseSimplex.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
	}

}
