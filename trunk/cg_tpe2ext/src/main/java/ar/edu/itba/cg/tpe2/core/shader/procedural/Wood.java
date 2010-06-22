package ar.edu.itba.cg.tpe2.core.shader.procedural;

import java.awt.Color;
import java.util.List;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import ar.edu.itba.cg.tpe2.core.colors.Diffuse;
import ar.edu.itba.cg.tpe2.core.geometry.Primitive;
import ar.edu.itba.cg.tpe2.core.geometry.Ray;
import ar.edu.itba.cg.tpe2.core.light.Light;
import ar.edu.itba.cg.tpe2.core.scene.Scene;
import ar.edu.itba.cg.tpe2.core.shader.ProceduralShader;
import ar.edu.itba.cg.tpe2.utils.noise.ImprovedNoise;
import ar.edu.itba.cg.tpe2.utils.noise.RandomNoise;

public class Wood extends ProceduralShader {
	// TODO 3d
	public Wood(String name, String type, int depth, Diffuse initialColor, Diffuse finalColor) {
		super(name, type, depth, initialColor, finalColor);
	}

	@Override
	public Color getColorAt(Point3f aPoint, Primitive primitive, List<Light> lights, Ray viewRay, Scene scene) {
		Vector3f vector = new Vector3f(aPoint);
		vector.sub(primitive.getReferencePoint());
		vector.normalize();
		vector.scale(0.3f);
		
			
		Point3f relativePoint = new Point3f(vector);
		float noiseCoef = computeTurbulence(relativePoint, noise.getDepth(), 0.25f, true);
		noiseCoef = noiseCoef * 15;
		noiseCoef = noiseCoef - (int) noiseCoef;
		
		return getColor(noiseCoef);
		
	}

	@Override
	public float getReflectionK() {
		return 0.1f;
	}
}
