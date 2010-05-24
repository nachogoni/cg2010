package ar.edu.itba.cg.tpe2.core.shader;

import java.awt.Color;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;

import ar.edu.itba.cg.tpe2.core.colors.Diffuse;
import ar.edu.itba.cg.tpe2.utils.noise.ImprovedNoise;
import ar.edu.itba.cg.tpe2.utils.noise.INoise;

public abstract class ProceduralShader extends Shader {

	protected INoise noise;
	protected Diffuse finalColor;
	protected Diffuse initialColor;
	
	public ProceduralShader(String name, String type, int depth, Diffuse initialColor, Diffuse finalColor) {
		super(name, type);
		noise = new ImprovedNoise(depth);
		this.initialColor = initialColor;
		this.finalColor = finalColor;
	}
	
	protected Color getColor(float noiseCoef) {
		float[] initialComponents = initialColor.getColorAt(null, null).getRGBColorComponents(null);
		float[] finalComponents = finalColor.getColorAt(null, null).getRGBColorComponents(null);
		float[] resultComponents = {0,0,0};
		
		for(int i = 0; i < 3 ; i++)
			resultComponents[i] = noiseCoef * initialComponents[i] + (1.0f - noiseCoef) * finalComponents[i];
		return clamp(resultComponents);
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

	protected float computeTurbulence(Point2f p, int maxLevel, float persistance){
		float noiseCoef = 0;
		float freq, amp;
		for(float level = 0 ; level < maxLevel ; level++){
			freq = (float) Math.pow(2,level);
			amp = (float) Math.pow(persistance,level);
			noiseCoef +=  amp * Math.abs( noise.noise(freq * p.x, freq * p.y));
		}
		return noiseCoef;
	}
	
	protected float computeTurbulence(Point3f p, int maxLevel, float persistance){
		float noiseCoef = 0;
		float freq, amp;
		for(float level = 0 ; level < maxLevel ; level++){
			freq = (float) Math.pow(2,level);
			amp = (float) Math.pow(persistance,level);
			noiseCoef +=  amp * Math.abs( noise.noise(freq * p.x, freq * p.y, freq * p.z ));
		}
		return noiseCoef;
	}
}
