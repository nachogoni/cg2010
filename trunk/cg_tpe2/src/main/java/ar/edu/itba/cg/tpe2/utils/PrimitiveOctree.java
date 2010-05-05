package ar.edu.itba.cg.tpe2.utils;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.core.geometry.Primitive;
import ar.edu.itba.cg.tpe2.core.geometry.Ray;

public class PrimitiveOctree {

	static final int MAX_PRIMITIVES = 1000;
	
	PrimitiveOctree[] nodes = null;
	
	List<Primitive> primitives = new ArrayList<Primitive>();
	
	private double xMin;
	private double xMax;
	private double yMin;
	private double yMax;
	private double zMin;
	private double zMax;
	
	public PrimitiveOctree(double xMinimun, double xMaximun, double yMinimun, double yMaximun, double zMinimun, double zMaximun) {
		this.xMax = xMaximun;
		this.xMin = xMinimun;
		this.yMax = yMaximun;
		this.yMin = yMinimun;
		this.zMax = zMaximun;
		this.zMin = zMinimun;
	}
	
	public boolean contains(Point3d point) {
		//INFO: Ver tema de que un punto pueda estar en 2 cubos...
		if ((point.x >= this.xMin) && (point.x <= this.xMax) &&
			(point.y >= this.yMin) && (point.y <= this.yMax) &&
			(point.z >= this.zMin) && (point.z <= this.zMax))
			return true;
		else
			return false;
	}
	
	public void add(Primitive p) {
		//TODO:

		// agrega la primitiva y se divide si es necesario (MAX_PRIMITIVES)
		
		
		// Soy hoja? si-> supere el maximo de elementos? si -> me rompo; no-> me agrego p
		//divide(); //FIXME

		// Soy nodo? si-> chequeo si lo tengo que meter en que hijos y se los doy
		
		primitives.add(p);
		
		return;
	}
	
	public void add(List<Primitive> list) {
		for (Primitive p : list) {
			add(p);
		}
	}
	
	private void divide() {
		//TODO:
		
		// Crea 8 hijos nuevos y les asigna las primitivas segun las coordenadas de cada una
		
/*	PrimitiveOctree[] nodes = {new PrimitiveOctree(), new PrimitiveOctree(), 
 * 							   new PrimitiveOctree(), new PrimitiveOctree(), 
					   		   new PrimitiveOctree(), new PrimitiveOctree(), 
					   		   new PrimitiveOctree(), new PrimitiveOctree()};
	
*/		
		// This is not a leaf anymore
		primitives.clear();
		return;
	}
	
	public boolean isLeaf() {
		return primitives.isEmpty();
	}
	
	public boolean isEmpty() {
		return (primitives.isEmpty()) && (nodes == null);
	}
	
	/**
	 * Get the possible primitive list
	 */
	public List<Primitive> intersect(Ray ray) {
		//TODO:
		
		// chequea contra todos los hijos y si colisiona se mete ahi adentro y devuelve el merge de todas las listas
		
		return primitives;
	}

	public double getxMin() {
		return xMin;
	}

	public double getxMax() {
		return xMax;
	}

	public double getyMin() {
		return yMin;
	}

	public double getyMax() {
		return yMax;
	}

	public double getzMin() {
		return zMin;
	}

	public double getzMax() {
		return zMax;
	}
	
}
