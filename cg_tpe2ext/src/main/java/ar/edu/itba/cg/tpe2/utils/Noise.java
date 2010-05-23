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

		// Create the sample random matrix
		int sampleSize = 16;
		float[][] sample = new float[sampleSize][sampleSize];
		for (int x = 0; x < sampleSize; x++) {
			for (int y = 0; y < sampleSize; y++) {
				sample[x][y] = rnd.nextFloat();
			}
		}
		
		// Create the noise matrix
//		for (int i = resolutionDeph; i > 1; i--) {
			// Number of pixels with the same value
			int pow = 16;//(int)Math.pow(2, 4);
			int box = 16;//size / pow;
			for (int xb = 0; xb < pow; xb++) {
				for (int yb = 0; yb < pow; yb++) {
					float value = sample[xb][yb];
					// Fill that little matrix with value
					for (int x = 0; x < box; x++) {
						for (int y = 0; y < box; y++) {
							texture[xb * box + x][yb * box + y] += value;
						}
					}
				}
			}
//			this.interpolate();
//		}

		resolutionDeph=0;
		
		// Change values from -1..1 to 0..1
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
//				texture[x][y] = (float)Math.abs(texture[x][y]);
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
				float corners = (texture[x+1][y+1] + texture[x-1][y-1] + texture[x-1][y+1] + texture[x+1][y-1])/16f;
				float sides = (texture[x][y+1] + texture[x][y-1] + texture[x-1][y] + texture[x+1][y])/8f;
				ntexture[x][y] = texture[x][y] / 4f + corners + sides;
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
