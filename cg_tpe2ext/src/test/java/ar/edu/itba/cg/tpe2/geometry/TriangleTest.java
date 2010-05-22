package ar.edu.itba.cg.tpe2.geometry;

import java.awt.Color;

import javax.vecmath.Point3f;

import junit.framework.TestCase;
import ar.edu.itba.cg.tpe2.core.geometry.Ray;
import ar.edu.itba.cg.tpe2.core.geometry.Triangle;

public class TriangleTest extends TestCase {

	public void testTriangleExceptionAsPoint() {
		try {
			Point3f p = new Point3f(2,3,4);
			new Triangle("",null,p,p,p, null);
            fail("This should've thrown an exception!");
        } catch (IllegalArgumentException expected) {}
	}

	public void testTriangleExceptionAsSegment() {
		try {
			Point3f p = new Point3f(0,0,0);
			Point3f p1 = new Point3f(1,1,1);
			Point3f p2 = new Point3f(2,2,2);
			new Triangle("",null,p,p1,p2, null);
            fail("This should've thrown an exception!");
        } catch (IllegalArgumentException expected) {}
	}
	
	public void testIntersect() {
		Color c = Color.BLACK;
		float a = 0.5f;
		
		while ( a < 17 ){
			Triangle tPositive = new Triangle("",null,new Point3f(a,0,0), new Point3f(0,a,0), new Point3f(0,0,a), null);
			Triangle tNegative = new Triangle("",null,new Point3f(-a,0,0), new Point3f(0,-a,0), new Point3f(0,0,-a), null);

			Ray r = new Ray(new Point3f(), new Point3f(a/10,a/10,a/10));
			assertNotNull(tPositive.intersect(r));
			assertNull(tNegative.intersect(r));
			
			r = new Ray(new Point3f(), new Point3f(a,a,a));
			assertNotNull(tPositive.intersect(r));
			assertNull(tNegative.intersect(r));
			
			r = new Ray(new Point3f(), new Point3f(-a/10,-a/10,-a/10));
			assertNull(tPositive.intersect(r));
			assertNotNull(tNegative.intersect(r));
			
			r = new Ray(new Point3f(), new Point3f(-a,-a,-a));
			assertNull(tPositive.intersect(r));
			assertNotNull(tNegative.intersect(r));
			a = a * 2;
		}
		// FIXME Add a default shader in order fgor this not to break! :P
		Triangle tOverXY = new Triangle("",null,new Point3f(a/2,a,0), new Point3f(0,0,0), new Point3f(a,0,0), null);
		Ray rToNegative = new Ray(new Point3f(a/2,0,a), new Point3f(0,0,-1));
		Ray rToPositive = new Ray(new Point3f(a/2,0,-a), new Point3f(0,0,1));
		assertEquals(tOverXY.intersect(rToPositive), new Point3f(a/2,0,0));
		assertEquals(tOverXY.intersect(rToNegative), new Point3f(a/2,0,0));
		
		Triangle tOverXYOrigin = new Triangle("",null,new Point3f(0,a,0), new Point3f(-a/2,0,0), new Point3f(a/2,0,0), null);
		Ray rToNegativeZ = new Ray(new Point3f(0,0,a), new Point3f(0,0,-1));
		Ray rToPositiveZ = new Ray(new Point3f(0,0,-a), new Point3f(0,0,1));
		assertEquals(tOverXYOrigin.intersect(rToPositiveZ), new Point3f(0,0,0));
		assertEquals(tOverXYOrigin.intersect(rToNegativeZ), new Point3f(0,0,0));
		
		Ray parallelRay = new Ray(new Point3f(0,0,a), new Point3f(-1,0,0));
		assertNull(tOverXY.intersect(parallelRay));
		assertNull(tOverXYOrigin.intersect(parallelRay));
		
		Ray samePlaneRay = new Ray(new Point3f(a,0,0), new Point3f(-1,0,0));
		assertNull(tOverXY.intersect(samePlaneRay));
		assertNull(tOverXYOrigin.intersect(samePlaneRay));
	}

}
