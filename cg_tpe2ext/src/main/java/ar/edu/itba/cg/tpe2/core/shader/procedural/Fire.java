package ar.edu.itba.cg.tpe2.core.shader.procedural;

import java.awt.Color;
import java.util.List;

import javax.vecmath.Point3f;

import ar.edu.itba.cg.tpe2.core.colors.Diffuse;
import ar.edu.itba.cg.tpe2.core.geometry.Primitive;
import ar.edu.itba.cg.tpe2.core.geometry.Ray;
import ar.edu.itba.cg.tpe2.core.light.Light;
import ar.edu.itba.cg.tpe2.core.scene.Scene;
import ar.edu.itba.cg.tpe2.core.shader.ProceduralShader;

public class Fire extends ProceduralShader {

	private static final int LEVELS = 9;

	public Fire(String name, String type, int depth, Diffuse initialColor, Diffuse finalColor) {
		super(name, type, depth, initialColor, finalColor);
	}

	@Override
	public Color getColorAt(Point3f aPoint, Primitive primitive, List<Light> lights, Ray viewRay, Scene scene) {
		Point3f relativePoint = new Point3f(aPoint);
		if ( primitive != null )
			relativePoint.sub(primitive.getReferencePoint());
		float noiseCoef = 0;
		noiseCoef = computeTurbulence(relativePoint, LEVELS,0.75f);
		
		return getColor(noiseCoef);
	}

}