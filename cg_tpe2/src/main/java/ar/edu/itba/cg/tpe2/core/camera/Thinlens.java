package ar.edu.itba.cg.tpe2.core.camera;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

public class Thinlens extends Camera {

	float fdist, lensr;

	public Thinlens(String type, Point3f eye, Point3f target, Vector3f up,
			float fov, float aspect, float fdist, float lensr) {
		super(type, eye, target, up, fov, aspect);
		this.fdist = fdist;
		this.lensr = lensr;
	}

	public float getFdist() {
		return fdist;
	}

	public float getLensr() {
		return lensr;
	}

	@Override
	public String toString() {
		return "Thinlens [fdist=" + fdist + ", lensr=" + lensr
				+ ", getAspect()=" + getAspect() + ", getEye()=" + getEye()
				+ ", getFov()=" + getFov() + ", getTarget()=" + getTarget()
				+ ", getType()=" + getType() + ", getUp()=" + getUp() + "]";
	}
	
	
	
}
