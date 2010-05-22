package ar.edu.itba.cg.tpe2.utils;

import java.util.Random;

import javax.vecmath.Point2f;

public class Noise {

	private float[][] texture;
	
	public Noise(int resolutionDeph) {
		
		Random rnd = new Random(0);
		
		if (resolutionDeph < 0 || resolutionDeph >= 16)
			throw new IllegalArgumentException("Resolution deph must be from 0 (2^0) to 16 (2^16)");
		
		int size = (int)Math.pow(2, resolutionDeph);
		this.texture = new float[size][size];
		
		// Create the noise matrix
		for (int i = 0; i <= resolutionDeph; i++) {
			// Number of pixels with the same value
			int pow = (int)Math.pow(2, i);
			int box = size / pow;
			for (int xb = 0; xb < pow; xb++) {
				for (int yb = 0; yb < pow; yb++) {
					float value = rnd.nextFloat() * 2 - 1;
					// Fill that little matrix with value
					for (int x = 0; x < box; x++) {
						for (int y = 0; y < box; y++) {
							texture[xb * box + x][yb * box + y] += value;
						}
					}
				}
			}
			this.interpolate();
		}
	
		// Change values from -1..1 to 0..1
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				texture[x][y] += resolutionDeph + 1;
				texture[x][y] /= (resolutionDeph + 1) * 2f;
			}
		}
	}
	
	public void interpolate() {
		int size = texture.length;
		
		float[][] ntexture = new float[size][size];
		
		for (int x = 1; x < size-1; x++) {
			for (int y = 1; y < size-1; y++) {
				ntexture[x][y] = (texture[x][y] + texture[x+1][y+1] + texture[x+1][y] + texture[x][y+1] + texture[x-1][y-1] + texture[x-1][y] + texture[x][y-1])/9f;
			}
		}
		
		texture = ntexture;
	}
	
	public float getValue(Point2f p) {
		//TODO: why not -> return a average or something with near points...
		return getValue((int)p.x, (int)p.y);
	}

	public float getValue(int x, int y) {
		if (x < 0 || y < 0 || x > texture.length || y > texture.length)
			throw new IndexOutOfBoundsException("Index out of bounds. Max value is " + texture.length);
		
		return texture[x][y];
	}
}
