package ar.edu.itba.cg.tpe1.geometry;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Wrapper class for Vector3d
 *
 */
public class Vector3 extends Vector3d{

	private static final long serialVersionUID = 3038920878180500671L;

	public Vector3() {}
	
	public Vector3(Point3d origin, Point3d destiny) {
		super(destiny.x - origin.x,destiny.y - origin.y,destiny.z - origin.z);
	}
}
