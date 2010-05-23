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

public class Marble extends ProceduralShader {
	// TODO 3d
	public Marble(String name, String type, int depth, Diffuse initialColor, Diffuse finalColor) {
		super(name, type, depth, initialColor, finalColor);
	}

	@Override
	public Color getColorAt(Point3f aPoint, Primitive primitive, List<Light> lights, Ray viewRay, Scene scene) {
		Point3f relativePoint = new Point3f(aPoint);
		if ( primitive != null )
			relativePoint.sub(primitive.getReferencePoint());
		float noiseCoef = computeTurbulence(relativePoint, noise.getDepth(),0.25f);
//		noiseCoef = (float) Math.sin( relativePoint.y + noiseCoef + 1) * 0.5f;
		noiseCoef = (float) Math.sin( relativePoint.y + relativePoint.x + noiseCoef);
		
		return getColor(noiseCoef);
	}

}
