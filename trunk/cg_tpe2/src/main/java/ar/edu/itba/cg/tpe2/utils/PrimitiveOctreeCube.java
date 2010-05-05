package ar.edu.itba.cg.tpe2.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.core.geometry.Primitive;
import ar.edu.itba.cg.tpe2.core.geometry.Quadrilateral;
import ar.edu.itba.cg.tpe2.core.geometry.Ray;

public class PrimitiveOctreeCube {

	List<Primitive> list = new ArrayList<Primitive>();
	
	public PrimitiveOctreeCube(double xMinimun, double xMaximun, double yMinimun, double yMaximun, double zMinimun, double zMaximun) {

		List<Primitive> list = new ArrayList<Primitive>();

		Point3d a = new Point3d(xMinimun, yMaximun, zMaximun);
		Point3d b = new Point3d(xMaximun, yMaximun, zMaximun);
		Point3d c = new Point3d(xMinimun, yMinimun, zMaximun);
		Point3d d = new Point3d(xMaximun, yMinimun, zMaximun);
		Point3d e = new Point3d(xMinimun, yMaximun, zMinimun);
		Point3d f = new Point3d(xMaximun, yMaximun, zMinimun);
		Point3d g = new Point3d(xMaximun, yMinimun, zMinimun);
		Point3d h = new Point3d(xMinimun, yMinimun, zMinimun);
		
		list.add(new Quadrilateral(null, null, a, b, f, e, Color.BLACK));
		list.add(new Quadrilateral(null, null, a, d, h, e, Color.BLACK));
		list.add(new Quadrilateral(null, null, c, b, f, g, Color.BLACK));
		list.add(new Quadrilateral(null, null, d, c, g, h, Color.BLACK));
		list.add(new Quadrilateral(null, null, e, f, g, h, Color.BLACK));
		list.add(new Quadrilateral(null, null, a, b, c, d, Color.BLACK));
		
	}

	public boolean intersect(Ray ray) {
		
		//TODO: recorrer los elementos de las lista y devolver si da != null
		
		return true;
	}	
	
}
