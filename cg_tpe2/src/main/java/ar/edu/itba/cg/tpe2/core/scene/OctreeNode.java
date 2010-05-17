package ar.edu.itba.cg.tpe2.core.scene;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3f;

import ar.edu.itba.cg.tpe2.core.geometry.Primitive;

public class OctreeNode {
	float xMin;
	float xMax;
	float yMin;
	float yMax;
	float zMin;
	float zMax;
	
	float tMin;
	
	// Node's children
	List<OctreeNode> childs = new ArrayList<OctreeNode>(8);
	List<Primitive> primitives = new ArrayList<Primitive>();
	
	/**
	 * Default Constructor
	 * 
	 */
	public OctreeNode(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
		this.xMin=xMin;
		this.yMin=yMin;
		this.zMin=zMin;
		this.xMax=xMax;
		this.yMax=yMax;
		this.zMax=zMax;			
	}
	
	public boolean isLeaf() {
		return childs.isEmpty();
	}
	
	public boolean contains(Point3f p) {
		if (p.x >= this.xMin && p.x < this.xMax && p.y >= this.yMin && p.y < this.yMax && p.z >= this.zMin && p.z < this.zMax) {
			return true;
		}
		return false;
	}

}
