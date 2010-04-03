package ar.edu.itba.cg.tpe1.geometry;

import java.awt.Color;

import javax.vecmath.Point3d;

import junit.framework.TestCase;

public class TriangleTest extends TestCase {

	public void testTriangleExceptionAsPoint() {
		try {
			Point3d p = new Point3d(2,3,4);
			new Triangle(p,p,p, new Color(2));
            fail("This should've thrown an exception!");
        } catch (IllegalArgumentException expected) {}
	}

	public void testTriangleExceptionAsSegment() {
		try {
			Point3d p = new Point3d(0,0,0);
			Point3d p1 = new Point3d(1,1,1);
			Point3d p2 = new Point3d(2,2,2);
			new Triangle(p,p1,p2, new Color(2));
            fail("This should've thrown an exception!");
        } catch (IllegalArgumentException expected) {}
	}
	
	public void testIntersect() {
		Color c = Color.BLACK;
		double a = 0.5;
		
		while ( a < 17 ){
			Triangle tPositive = new Triangle(new Point3d(a,0,0), new Point3d(0,a,0), new Point3d(0,0,a), c);
			Triangle tNegative = new Triangle(new Point3d(-a,0,0), new Point3d(0,-a,0), new Point3d(0,0,-a), c);

			Ray r = new Ray(new Point3d(), new Point3d(a/10,a/10,a/10));
			assertNotNull(tPositive.intersect(r));
			assertNull(tNegative.intersect(r));
			
			r = new Ray(new Point3d(), new Point3d(a,a,a));
			assertNotNull(tPositive.intersect(r));
			assertNull(tNegative.intersect(r));
			
			r = new Ray(new Point3d(), new Point3d(-a/10,-a/10,-a/10));
			assertNull(tPositive.intersect(r));
			assertNotNull(tNegative.intersect(r));
			
			r = new Ray(new Point3d(), new Point3d(-a,-a,-a));
			assertNull(tPositive.intersect(r));
			assertNotNull(tNegative.intersect(r));
			a = a * 2;
		}

		Triangle tOverXY = new Triangle(new Point3d(a/2,a,0), new Point3d(0,0,0), new Point3d(a,0,0), c);
		Ray rToNegative = new Ray(new Point3d(a/2,0,a), new Point3d(0,0,-1));
		Ray rToPositive = new Ray(new Point3d(a/2,0,-a), new Point3d(0,0,1));
		assertEquals(tOverXY.intersect(rToPositive), new Point3d(a/2,0,0));
		assertEquals(tOverXY.intersect(rToNegative), new Point3d(a/2,0,0));
		
		Triangle tOverXYOrigin = new Triangle(new Point3d(0,a,0), new Point3d(-a/2,0,0), new Point3d(a/2,0,0), c);
		Ray rToNegativeZ = new Ray(new Point3d(0,0,a), new Point3d(0,0,-1));
		Ray rToPositiveZ = new Ray(new Point3d(0,0,-a), new Point3d(0,0,1));
		assertEquals(tOverXYOrigin.intersect(rToPositiveZ), new Point3d(0,0,0));
		assertEquals(tOverXYOrigin.intersect(rToNegativeZ), new Point3d(0,0,0));
		
		Ray parallelRay = new Ray(new Point3d(0,0,a), new Point3d(-1,0,0));
		assertNull(tOverXY.intersect(parallelRay));
		assertNull(tOverXYOrigin.intersect(parallelRay));
		
		Ray samePlaneRay = new Ray(new Point3d(a,0,0), new Point3d(-1,0,0));
		assertNull(tOverXY.intersect(samePlaneRay));
		assertNull(tOverXYOrigin.intersect(samePlaneRay));
	}

}
