package ar.edu.itba.cg.tpe2.core.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.core.geometry.Primitive;
import ar.edu.itba.cg.tpe2.core.geometry.Ray;

public class PrimitiveOctree {

	// Maxima cantidad de primitivas en la hoja
	public static final int MAX_PRIMITIVES = 1;
	
	// Nodo raiz del octree
	OctreeNode root;
	
	// Constante epsilon
	private final static double EPSILON = 0.00000000000001;
	
	/**
	 * Constructor default
	 * @param xMinimun Cota inferior para x 
	 * @param xMaximun Cota superior para x
	 * @param yMinimun Cota inferior para y 
	 * @param yMaximun Cota superior para y
	 * @param zMinimun Cota inferior para z
	 * @param zMaximun Cota superior para z
	 */
	private double size;
	public PrimitiveOctree(double xMinimun, double xMaximun, double yMinimun, double yMaximun, double zMinimun, double zMaximun) {
		 
		// We are giving the chance to the intersect functions in primitives to have a representation error
		// and they still be in the correct octree node
		size = Math.max( Math.max( Math.max( Math.abs(xMinimun), Math.abs(xMaximun) ), 
					Math.max( Math.abs(yMinimun), Math.abs(yMaximun) ) ),
					Math.max(Math.abs(zMinimun), Math.abs(zMaximun) ) )*2 + EPSILON*2;
		//size=10;
		this.root = new OctreeNode(-size*0.5, size*0.5, 
				-size*0.5, size*0.5, 
				-size*0.5, size*0.5);
	}	
	
	
	/**
	 * Funcion que devuelve una lista de nodos del arbol intersectados por el rayo
	 * La lista tiene los nodos en orden de interseccion!
	 * @param rayParam Rayo invocado por el ray tracer
	 * @return Lista de nodos intersectados
	 */
	
	public List<OctreeNode> intersectedNodes(final Ray ray) {
		
		// Lista con los nodos intersectados
		List<OctreeNode> ret = Collections.synchronizedList(new ArrayList<OctreeNode>()); 
		byte a = 0, b = 0;
		
		Point3d rayOrig = new Point3d(ray.getOrigin().x, ray.getOrigin().y, ray.getOrigin().z);
		Point3d rayDir = new Point3d(ray.getDirection().x, ray.getDirection().y,ray.getDirection().z);
		
		//double tx0 ,tx1, ty0, ty1, tz0, tz1;
				
		/*if (rayDir.x == 0) {
			rayDir.set(EPSILON, rayDir.y, rayDir.z);
			b |= 4;
		} else */if (rayDir.x < 0) {
			rayOrig.set(root.xMax + root.xMin - rayOrig.x, rayOrig.y, rayDir.z);
			rayDir.set(-rayDir.x, rayDir.y, rayDir.z);
			a |= 4;
		}
		/*if (rayDir.y == 0) {
			rayDir.set(rayDir.x, EPSILON, rayDir.z);
			b |= 2;			
		} else */if (rayDir.y < 0) {
			rayOrig.set(rayOrig.x, root.xMax + root.xMin -rayOrig.y, rayOrig.z);
			rayDir.set(rayDir.x, -rayDir.y, rayDir.z);
			a |= 2;
		}
		/*if (rayDir.z == 0) {
			rayDir.set(rayDir.x, rayDir.y, EPSILON);
			b |= 1;					
		} else */if (rayDir.z < 0) {
			rayOrig.set(rayOrig.x, rayOrig.y, root.xMax + root.xMin - rayOrig.z);
			rayDir.set(rayDir.x, rayDir.y, -rayDir.z);
			a |= 1;
		}
		//System.out.println("a:"+a);
		//System.out.println("b:"+b);
		double tx0=(root.xMin - rayOrig.x) / (rayDir.x+EPSILON);
		double tx1=(root.xMax - rayOrig.x) / (rayDir.x+EPSILON);
		
		double ty0=(root.yMin - rayOrig.y) / (rayDir.y+EPSILON);
		double ty1=(root.yMax - rayOrig.y) / (rayDir.y+EPSILON);
		
		double tz0=(root.zMin - rayOrig.z) / (rayDir.z+EPSILON);
		double tz1=(root.zMax - rayOrig.z) / (rayDir.z+EPSILON);
		
		
		double tmin = Math.max(tx0,Math.max(ty0,tz0));
		double tmax = Math.min(tx1,Math.min(ty1,tz1));
		
		// Check if there is an instersection with the root node
		if ( (tmin < tmax)  && ( tmax > 0.0 ) ) {
			procSubtree(tx0,ty0,tz0,tx1,ty1,tz1, root, ret, a, b, rayOrig);
		} else {
			//System.out.println("no hay interseccion con root! :S");
		}
		
		return ret;
	}
	
	void procSubtree( double tx0, double ty0, double tz0,
			double tx1, double ty1, double tz1, OctreeNode node, List<OctreeNode> ret, byte a, byte b, Point3d orig) {
		
		if ( (tx1 <= 0.0 ) || (ty1 <= 0.0) || (tz1 <= 0.0) ) {
			//System.out.println("ERROR :S");
			return;
		} 
		
		if (node.isLeaf() && node.primitives.size() > 0) {
			double tmin = Math.max(tx0,Math.max(ty0,tz0));
			node.tMin=tmin;

			//ListIterator<OctreeNode> iterator = ret.listIterator();
			ret.add(node);
			/*if (ret.isEmpty()) {
				ret.add(node);
				System.out.println("Me inserto a la rta porque ta vacia :P");
			} else {
				boolean notInserted=true;
				ret.add(node);
				while(iterator.hasNext() && notInserted) {
					OctreeNode currNode = iterator.next();
					if (tmin < currNode.tMin) {
						ret.add(iterator.previousIndex(), currNode);
						notInserted=false;
						System.out.println("Me inserto a la rta :D");
					}
				}
			}*/
			//System.out.println("soy hoja, me aniadi a la lista y termino la recursion");
			return;
		} else if (node.isLeaf()) {
			//System.out.println("soy hoja y termino la recursion");
			return;
		}
		double txM = 0.5 * (tx0 + tx1);
		double tyM = 0.5 * (ty0 + ty1);
		double tzM = 0.5 * (tz0 + tz1);		
		
		/*switch(b) {
			case 0:
				txM = 0.5 * (tx0 + tx1);
				tyM = 0.5 * (ty0 + ty1);
				tzM = 0.5 * (tz0 + tz1);
				break;
			case 1:
				txM = 0.5 * (tx0 + tx1);
				tyM = 0.5 * (ty0 + ty1);
				tzM = orig.z < 0.5 * (node.zMin + node.zMax) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
				break;
			case 2:
				txM = 0.5 * (tx0 + tx1);
				tyM = orig.y < 0.5 * (node.yMin + node.yMax) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
				tzM = 0.5 * (tz0 + tz1);
				break;
			case 3:
				txM = 0.5 * (tx0 + tx1);
				tyM = orig.y < 0.5 * (node.yMin + node.yMax) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
				tzM = orig.z < 0.5 * (node.zMin + node.zMax) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
				break;
			case 4:
				txM = orig.x < 0.5 * (node.xMin + node.xMax) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
				tyM = 0.5 * (ty0 + ty1);
				tzM = 0.5 * (tz0 + tz1);
				break;
			case 5:
				txM = orig.x < 0.5 * (node.xMin + node.xMax) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
				tyM = 0.5 * (ty0 + ty1);
				tzM = orig.z < 0.5 * (node.zMin + node.zMax) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
				break;
			case 6:
				txM = orig.x < 0.5 * (node.xMin + node.xMax) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
				tyM = orig.y < 0.5 * (node.yMin + node.yMax) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
				tzM = 0.5 * (tz0 + tz1);
				break;
			case 7:
				txM = orig.x < 0.5 * (node.xMin + node.xMax) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
				tyM = orig.y < 0.5 * (node.yMin + node.yMax) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
				tzM = orig.z < 0.5 * (node.zMin + node.zMax) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
				break;
			default:
				throw new RuntimeException("se cagooo");
		}*/


		
	    // Determining the first node requires knowing which of the t0's is the largest...
	    // as well as comparing the tM's of the other axes against that largest t0.
	    // Hence, the function should only require the 3 t0-values and the 3 tM-values.
	    byte currNode = findFirstNode(tx0,ty0,tz0,txM,tyM,tzM, (byte)0x0); 		
		System.out.println("crrnode="+currNode);
	    do {
	    	// next_Node() takes the t1 values for a child (which may or may not have tM's of the parent)
	    	// and determines the next node.  Rather than passing in the currNode value, we pass in possible values
	    	// for the next node.  A value of 8 refers to an exit from the parent.
	    	// While having more parameters does use more stack bandwidth, it allows for a smaller function
	        // with fewer branches and less redundant code.  The possibilities for the next node are passed in
	        // the same respective order as the t-values.  Hence if the first parameter is found as the greatest, the
	        // fourth parameter will be the return value.  If the 2nd parameter is the greatest, the 5th will be returned, etc.
	        switch(currNode) {
		        case 0x0 : procSubtree(tx0,ty0,tz0,txM,tyM,tzM,node.childs.get(a), ret, a, b, orig);
		                    currNode = nextNode(txM,tyM,tzM,(byte)0x4,(byte)0x2,(byte)0x1);
		                    break;
		        case 0x1 : 
		        	//System.out.println("case1");
		        	procSubtree(tx0,ty0,tzM,txM,tyM,tz1,node.childs.get(0x1^a), ret, a, b, orig);
		                    currNode = nextNode(txM,tyM,tz1,(byte)0x5,(byte)0x3,(byte)0x8);
		                    break;
		        case 0x2 : 
		        	//System.out.println("case2");
		        	procSubtree(tx0,tyM,tz0,txM,ty1,tzM,node.childs.get(0x2^a), ret, a, b, orig);
		                    currNode = nextNode(txM,ty1,tzM,(byte)0x6,(byte)0x8,(byte)0x3);
		                    break;
		        case 0x3 : 
		        //System.out.println("case3");
		        procSubtree(tx0,tyM,tzM,txM,ty1,tz1,node.childs.get(0x3^a), ret, a, b, orig);
		                    currNode = nextNode(txM,ty1,tz1,(byte)0x7,(byte)0x8,(byte)0x8);
		                    break;
		        case 0x4 : 
		        //System.out.println("case4");
		        procSubtree(txM,ty0,tz0,tx1,tyM,tzM,node.childs.get(0x4^a), ret, a, b, orig);
		                    currNode = nextNode(tx1,tyM,tzM,(byte)0x8,(byte)0x6,(byte)0x5);
		                    break;
		        case 0x5 : 
		        	//System.out.println("case5");
		        	procSubtree(txM,ty0,tzM,tx1,tyM,tz1,node.childs.get(0x5^a), ret, a, b, orig);
		                    currNode = nextNode(tx1,tyM,tz1,(byte)0x8,(byte)0x7,(byte)0x8);
		                    break;
		        case 0x6 : 
		        	//System.out.println("case6");
		        	procSubtree(txM,tyM,tz0,tx1,ty1,tzM,node.childs.get(0x6^a), ret, a, b, orig);
		                    currNode = nextNode(tx1,ty1,tzM,(byte)0x8,(byte)0x8,(byte)0x7);
		                    break;
		        case 0x7 : procSubtree(txM,txM,tzM,tx1,ty1,tz1,node.childs.get(0x7^a), ret, a, b, orig);
		                    currNode = (byte) 0x8;
		                    break;
		    }
	    } while (currNode < 0x8);
		 		
		
	  /*  procSubtree(tx0,ty0,tz0,txM,tyM,tzM,node.childs.get(0), ret);
	    procSubtree(tx0,ty0,tzM,txM,tyM,tz1,node.childs.get(1), ret);
	    procSubtree(tx0,tyM,tz0,txM,ty1,tzM,node.childs.get(2), ret);
	    procSubtree(tx0,tyM,tzM,txM,ty1,tz1,node.childs.get(3), ret);
	    procSubtree(txM,ty0,tz0,tx1,tyM,tzM,node.childs.get(4), ret);
	    procSubtree(txM,ty0,tzM,tx1,tyM,tz1,node.childs.get(5), ret);
	    procSubtree(txM,tyM,tz0,tx1,ty1,tzM,node.childs.get(6), ret);
	    procSubtree(txM,txM,tzM,tx1,ty1,tz1,node.childs.get(7), ret);*/

	} 	
	
	
	private byte nextNode(double txM, double tyM, double tzM, byte i, byte j,
			byte k) {
		byte ret;
		if (txM < tyM) {
			//txM es el minimo
			if (txM < tzM) {
				ret= i;
			//tzM es el minimo
			} else {
				ret= k;
			}
		} else {
			//tyM es el minimo
			if (tyM < tzM) {
				ret= j;
			//tzM es el minimo
			} else {
				ret= k;
			}			
		}
		return ret;
	}

	private byte findFirstNode(double tx0, double ty0, double tz0,
			double txM, double tyM, double tzM, byte a) {
		byte ret=0;
		if(tx0 > ty0) {
			if(tx0 > tz0) {
				// max(tx0, ty0, tz0) is tx0. Entry plane is YZ.
				if(tyM < tx0) 
					ret |= 2;
				if(tzM < tx0) 
					ret |= 1;
			} else {
				// max(tx0, ty0, tz0) is tz0. Entry plane is XY.
				if(txM < tz0) 
					ret |= 4;
				if(tyM < tz0) 
					ret |= 2;
			}
		} else {
			if(ty0 > tz0) {
				// max(tx0, ty0, tz0) is ty0. Entry plane is XZ.
				if(txM < ty0) 
					ret |= 4;
				if(tzM < ty0) 
					ret |= 1;
			} else {
				// max(tx0, ty0, tz0) is tz0. Entry plane is XY.
				if(txM < tz0) 
					ret |= 4;
				if(tyM < tz0) 
					ret |= 2;
			}
		}
		
		
			/*
		byte ret=a;
		if (tz0 > ty0 ) {
			if (tz0 > tx0) {
			//tz0 es el mayor
				if (txM < tz0) {
					//ret |= 4;
					ret |= 1;
				} else {
					//ret &= 0xE;
				}
				if(tyM < tz0) {
					ret |= 2;
				} else {
					//ret &= 0xD;
				}
			//tx0 es el mayor
			} else {
				if (tyM < tx0) {
					ret |= 2;
				} else {
					//ret &= 0xD;
				}
				if(tzM < tx0) {
					ret |= 4;
					//ret |= 1;
				} else {
					//ret &= 0xB;
				}
			}
		} else {
			//ty0 es el maximo
			if (ty0 > tx0) {
				if (txM < ty0) {
					//ret |= 4;
					ret |= 1;
				} else {
					//ret &= 0xE;
				}
				if(tzM < ty0) {
					//ret |= 1;
					ret |= 4;
				} else {
					//ret &= 0xB;
				}
			//tx0 es el maximo
			} else {
				if (tyM < tx0) {
					ret |= 2;
				} else {
					//ret &= 0xD;
				}
				if(tzM < tx0) {
					ret |= 1;
				} else {
					//ret &= 0xB;
				}
			}			
		}*/
		
		return ret;
	}
	
	private void add(OctreeNode node, Primitive p) {
		if (node.isLeaf()) {
			node.primitives.add(p);
			if (node.primitives.size() > MAX_PRIMITIVES) {
				System.out.println("Se expande el nodo, tamanio del octree " + node.primitives.size());System.out.flush();
				expand(node);
			}
			
		} else {
			for (OctreeNode currNode : node.childs) {
				if (currNode.contains(p)){
					add(currNode, p);
				}
			}
		}
		
	}
	
	public void add(Primitive p) {
		add(root, p);
		return;
	}
	
	private void expand(OctreeNode node) {
		
		double xMed = (node.xMin + node.xMax)*0.5;
		double yMed = (node.yMin + node.yMax)*0.5;
		double zMed = (node.zMin + node.zMax)*0.5;
		/*
		node.childs.add(0, new OctreeNode(node.xMin, xMed,node.yMin,yMed, zMed, node.zMax));
		node.childs.add(1, new OctreeNode(node.xMin, xMed,node.yMin,yMed, node.zMin, zMed));
		node.childs.add(2, new OctreeNode(node.xMin, xMed,yMed,node.yMax, zMed, node.zMax));
		node.childs.add(3, new OctreeNode(node.xMin, xMed,yMed,node.yMax, node.zMin, zMed));
		node.childs.add(4, new OctreeNode(xMed, node.xMax,node.yMin,yMed, zMed, node.zMax));
		node.childs.add(5, new OctreeNode(xMed, node.xMax,node.yMin,yMed, node.zMin, zMed));
		node.childs.add(6, new OctreeNode(xMed, node.xMax,yMed,node.yMax, zMed, node.zMax));
		node.childs.add(7, new OctreeNode(xMed, node.xMax,yMed,node.yMax,node.zMin, zMed));*/		
		
		node.childs.add(0, new OctreeNode(node.xMin, xMed,node.yMin,yMed, node.zMin, zMed));//ok
		node.childs.add(1, new OctreeNode(node.xMin, xMed,node.yMin,yMed, zMed, node.zMax));//ok
		node.childs.add(2, new OctreeNode(node.xMin, xMed,yMed,node.yMax, node.zMin, zMed));//ok
		node.childs.add(3, new OctreeNode(node.xMin, xMed,yMed,node.yMax, zMed, node.zMax));//ok
		
		node.childs.add(4, new OctreeNode(xMed, node.xMax,node.yMin,yMed, node.zMin, zMed));
		node.childs.add(5, new OctreeNode(xMed, node.xMax,node.yMin,yMed, zMed, node.zMax));
		node.childs.add(6, new OctreeNode(xMed, node.xMax,yMed,node.yMax, node.zMin, zMed));
		node.childs.add(7, new OctreeNode(xMed, node.xMax,yMed,node.yMax,zMed, node.zMax));//ok

		// Por cada primitiva, me fijo en cada nodo si alguno de los puntos de ella esta contendido en el
		for (Primitive p : node.primitives) {
			
			List<Point3d> boundaryPoints = p.getBoundaryPoints();
			
			for ( OctreeNode currNode : node.childs ) {
				boolean inserted = false;
				for (Iterator<Point3d> iterator =  boundaryPoints.iterator(); iterator.hasNext() && !inserted;) {
					Point3d point = iterator.next();
						if (currNode.contains(point)) {
							add(currNode, p);
							inserted=true;
						}
				}
			}
		}
		
		// This is not a leaf anymore
		node.primitives.clear();
		return;
	}
	
	public void printOctree() {
		printOctree(root);
	}
	
	private void printOctree(OctreeNode root) {
		System.out.println("************************************");
		System.out.println("Este nodo es hoja?:" + root.isLeaf());
		System.out.println("Tiene #hijos: " + root.childs.size());
		System.out.println("Tiene #primitivas" + root.primitives.size());
		System.out.println("xMin:"+root.xMin + ", xMax:"+root.xMax);
		System.out.println("yMin:"+root.yMin + ", yMax:"+root.yMax);
		System.out.println("zMin:"+root.zMin + ", zMax:"+root.zMax);
		for (OctreeNode child : root.childs) {
			printOctree(child);
		}
	}
	
	
}
