package ar.edu.itba.cg.tpe2.core.camera;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Thinlens extends Camera {

	// Type: Thinlens
	// fdist
	// lensr
	
	double fdist, lensr;

	public Thinlens(String type, Point3d eye, Point3d target, Vector3d up,
			double fov, double aspect, double fdist, double lensr) {
		super(type, eye, target, up, fov, aspect);
		this.fdist = fdist;
		this.lensr = lensr;
	}

	public double getFdist() {
		return fdist;
	}

	public double getLensr() {
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
