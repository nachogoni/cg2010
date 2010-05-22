package ar.edu.itba.cg.tpe2.core.geometry;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * Wrapper class for Vector3f
 *
 */
public class Vector3 extends Vector3f{

	private static final long serialVersionUID = 3038920878180500671L;

	public Vector3() {}
	
	public Vector3(Point3f origin, Point3f destiny) {
		super(destiny.x - origin.x,destiny.y - origin.y,destiny.z - origin.z);
	}
	
	public Vector3(float x, float y, float z){
		super(x,y,z);
	}
	
	public Vector3(Vector3f v){
		super(v);
	}
}
