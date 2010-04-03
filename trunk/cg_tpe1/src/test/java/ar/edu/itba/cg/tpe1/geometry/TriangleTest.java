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
		fail("Not yet implemented");
	}

}
