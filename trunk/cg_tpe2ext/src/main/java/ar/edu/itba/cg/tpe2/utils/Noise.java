package ar.edu.itba.cg.tpe2.utils;

import java.awt.Color;

import javax.vecmath.Point2f;

public class Noise {

	private float[][] texture;
	
	
	public Noise(int resolutionDeph) {
		
		if (resolutionDeph < 0 || resolutionDeph >= 32)
			throw new IllegalArgumentException("Resolution deph must be from 0 (2^0) to 32 (2^32)");
		
		int size = (int)Math.pow(2, resolutionDeph);
		this.texture = new float[size][size];
		
	}
	
	public float getValue(Point2f p) {
		return getValue(p.x, p.y);
	}

	public float getValue(float u, float v) {
		
		
		return 0.0f;
	}
}
