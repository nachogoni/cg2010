package ar.edu.itba.cg.tpe2.core.shader.procedural;

import java.awt.Color;
import java.util.List;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;

import ar.edu.itba.cg.tpe2.core.colors.Diffuse;
import ar.edu.itba.cg.tpe2.core.geometry.Primitive;
import ar.edu.itba.cg.tpe2.core.geometry.Ray;
import ar.edu.itba.cg.tpe2.core.light.Light;
import ar.edu.itba.cg.tpe2.core.scene.Scene;
import ar.edu.itba.cg.tpe2.core.shader.ProceduralShader;

public class Organic extends ProceduralShader {
	// TODO 2d
	public Organic(String name, String type, int depth, Diffuse initialColor, Diffuse finalColor) {
		super(name, type, depth, initialColor, finalColor);
	}

	@Override
	public Color getColorAt(Point3f aPoint, Primitive primitive, List<Light> lights, Ray viewRay, Scene scene) {
		Point3f relativePoint = new Point3f(aPoint);
		float noiseCoef = computeTurbulence(relativePoint, noise.getDepth(), 0.25f, true);
		noiseCoef = noiseCoef * 10;
		noiseCoef = noiseCoef - (int) noiseCoef;
		
		return getColor(noiseCoef);
	}

}
