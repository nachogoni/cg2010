package ar.edu.itba.cg.tpe2.core.scene;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import ar.edu.itba.cg.tpe2.core.geometry.Primitive;
import ar.edu.itba.cg.tpe2.core.geometry.Ray;

public class PrimitiveOctree {

	// Maxima cantidad de primitivas en la hoja
	public static int maxPrimitives = 1;
	
	// Nodo raiz del octree
	OctreeNode root;
	
	private double size=0;
	
	// Constante epsilon
	private final static double EPSILON = 0.000000001;
	
	/**
	 * Constructor default
	 * @param xMinimun Cota inferior para x 
	 * @param xMaximun Cota superior para x
	 * @param yMinimun Cota inferior para y 
	 * @param yMaximun Cota superior para y
	 * @param zMinimun Cota inferior para z
	 * @param zMaximun Cota superior para z
	 */
	public PrimitiveOctree(double xMinimun, double xMaximun, double yMinimun, double yMaximun, double zMinimun, double zMaximun) {
		 
		// We are giving the chance to the intersect functions in primitives to have a representation error
		// and they still be in the correct octree node
		this.size = max( max(Math.abs(xMinimun), Math.abs(xMaximun), 
					max( Math.abs(yMinimun), Math.abs(yMaximun) ) ),
					max(Math.abs(zMinimun), Math.abs(zMaximun) ) )*2 + EPSILON*2;
		System.out.println(this.size);
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
		List<OctreeNode> ret = new ArrayList<OctreeNode>(); 
		byte a = 0, b = 0;
		//TODO ver que onda si el origen esta metido dentro del octree
		
		Point3d rayOrig = new Point3d(ray.getOrigin().x, ray.getOrigin().y, ray.getOrigin().z);
		Point3d rayDir = new Point3d(ray.getDirection().x, ray.getDirection().y, ray.getDirection().z);
		
		//double tx0 ,tx1, ty0, ty1, tz0, tz1;
				
		/*if (rayDir.x == 0) {
			rayDir.set(EPSILON, rayDir.y, rayDir.z);
			b |= 4;
		} else */
		/*if (rayDir.x < 0) {
			double dist=0;
			/*if ( rayOrig.x < root.xMin ) {
				dist=root.xMin-rayOrig.x;
			} else if (rayOrig.x > root.xMax) {
				dist=rayOrig.x-root.xMax;
			}
			if (rayOrig.x < 0) {
				rayOrig.set(rayOrig.x+ dist*2+this.size, rayOrig.y , rayOrig.z);
			} else {
				rayOrig.set(rayOrig.x- (dist*2+this.size), rayOrig.y , rayOrig.z );
			}
			
			rayOrig.set(this.size-rayOrig.x + rayOrig.x, rayOrig.y, rayOrig.z);
			
			rayDir.set(-rayDir.x, rayDir.y, rayDir.z);
			a |= 0x4;
		}*/
		/*if (rayDir.y == 0) {
			rayDir.set(rayDir.x, EPSILON, rayDir.z);
			b |= 2;			
		} else */
		/*if (rayDir.y < 0) {
			/*double dist=0;
			if ( rayOrig.y < root.yMin ) {
				dist=root.yMin-rayOrig.y;
			} else if (rayOrig.y > root.yMax) {
				dist=rayOrig.y-root.yMax;
			}			
			if (rayOrig.y < 0) {
				rayOrig.set(rayOrig.x, rayOrig.y+ dist*2+this.size, rayOrig.z);
			} else {
				rayOrig.set(rayOrig.x, rayOrig.y - (dist*2+this.size), rayOrig.z );
			}
			
			rayOrig.set(rayOrig.x, this.size-rayOrig.y+rayOrig.y, rayOrig.z);
			rayDir.set(rayDir.x, -rayDir.y, rayDir.z);
			a |= 0x2;
		}*/
		/*if (rayDir.z == 0) {
			rayDir.set(rayDir.x, rayDir.y, EPSILON);
			b |= 1;			
		}*//*			
		} else */
		/*if (rayDir.z < 0) {
			/*double dist=0;
			if ( rayOrig.z < root.zMin ) {
				dist=root.zMin-rayOrig.z;
			} else if (rayOrig.z > root.zMax) {
				dist=rayOrig.z-root.zMax;
			}						
			
			if (rayOrig.z < 0) {
				rayOrig.set(rayOrig.x, rayOrig.y, rayOrig.z+ dist*2+this.size );
			} else {
				rayOrig.set(rayOrig.x, rayOrig.y, rayOrig.z- (dist*2+this.size) );
			}
			rayOrig.set(rayOrig.x, rayOrig.y, this.size-rayOrig.z+rayOrig.z);
			rayDir.set(rayDir.x, rayDir.y, -rayDir.z);
			a |= 0x1;
		}*/
		
		
		//System.out.println("a:"+a);
		//System.out.println("b:"+b);
		double tx0, tx1;
		tx0=(root.xMin - rayOrig.x) / rayDir.x;
		tx1=(root.xMax - rayOrig.x) / rayDir.x;
		
		if (tx0 > tx1) {
			double aux = tx0;
			tx0=tx1;
			tx1=aux;
		}
		
		double ty0, ty1;
		ty0=(root.yMin - rayOrig.y) / rayDir.y;
		ty1=(root.yMax - rayOrig.y) / rayDir.y;

		if (ty0 > ty1) {
			double aux = ty0;
			ty0=ty1;
			ty1=aux;
		}
		double tz0, tz1;
		tz0=(root.zMin - rayOrig.z) / rayDir.z;
		tz1=(root.zMax - rayOrig.z) / rayDir.z;
		
		if (tz0 > tz1) {
			double aux = tz0;
			tz0=tz1;
			tz1=aux;
		}
		
		double tmin = max(tx0,ty0,tz0);
		double tmax = min(tx1,ty1,tz1);
		
		// Check if there is an instersection with the root node
		if ( (tmin <= tmax) && (tmax > 0)  ) {
			procSubtree(tx0,ty0,tz0,tx1,ty1,tz1, root, ret, a, b, rayOrig, rayDir);
		} else {
			//System.out.println("no hay interseccion con root! :S");
		}
		
		return ret;
	}
	
	void procSubtree( double tx0, double ty0, double tz0,
			double tx1, double ty1, double tz1, OctreeNode node, List<OctreeNode> ret, byte a, byte b, Point3d orig, Point3d dir) {
		
		/*if ( max(tx0, ty0, tz0) <= min(tx1, ty1, tz1) ) {
			return;
		}*/
		
		/*if ( (tx1 <= 0.0 ) || (ty1 <= 0.0) || (tz1 <= 0.0) ) {
			return;
		}*/
		//if (node.isLeaf() && (node.primitives.size() > 0)) {
			/*double tmin = max(tx0,ty0,tz0);
			node.tMin=tmin;*/

			//ListIterator<OctreeNode> iterator = ret.listIterator();
			//ret.add(node);
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
			/*return;
		} else if (node.isLeaf()) {
			//System.out.println("soy hoja y termino la recursion");
			return;
		}*/
		/*double txM = 0.5 * (tx0 + tx1);
		double tyM = 0.5 * (ty0 + ty1);
		double tzM = 0.5 * (tz0 + tz1);	*/	
		
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
	   /* byte currNode = findFirstNode(tx0,ty0,tz0,txM,tyM,tzM); 		
		//System.out.println("crrnode="+currNode);
	    do {
	    	// next_Node() takes the t1 values for a child (which may or may not have tM's of the parent)
	    	// and determines the next node.  Rather than passing in the currNode value, we pass in possible values
	    	// for the next node.  A value of 8 refers to an exit from the parent.
	    	// While having more parameters does use more stack bandwidth, it allows for a smaller function
	        // with fewer branches and less redundant code.  The possibilities for the next node are passed in
	        // the same respective order as the t-values.  Hence if the first parameter is found as the greatest, the
	        // fourth parameter will be the return value.  If the 2nd parameter is the greatest, the 5th will be returned, etc.
	        switch(currNode) {
		        case 0x0 : 
		        	//System.out.println("case0");
		        	procSubtree(tx0,ty0,tz0,txM,tyM,tzM,node.childs.get(a), ret, a, b, orig);
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
		        case 0x7 : 
		        	//System.out.println("case7");
		        	procSubtree(txM,txM,tzM,tx1,ty1,tz1,node.childs.get(0x7^a), ret, a, b, orig);
		                    currNode = (byte) 0x8;
		                    break;
		    }
	    } while (currNode < 0x8);*/
		//double tx0, tx1;
		tx0=(node.xMin - orig.x) / dir.x;
		tx1=(node.xMax - orig.x) / dir.x;
		
		if (tx0 > tx1) {
			double aux = tx0;
			tx0=tx1;
			tx1=aux;
		}
		
		//double ty0, ty1;
		ty0=(node.yMin - orig.y) / dir.y;
		ty1=(node.yMax - orig.y) / dir.y;

		if (ty0 > ty1) {
			double aux = ty0;
			ty0=ty1;
			ty1=aux;
		}
		//double tz0, tz1;
		tz0=(node.zMin - orig.z) / dir.z;
		tz1=(node.zMax - orig.z) / dir.z;
		
		if (tz0 > tz1) {
			double aux = tz0;
			tz0=tz1;
			tz1=aux;
		}
		
		double tmin = max(tx0,ty0,tz0);
		double tmax = min(tx1,ty1,tz1);		 		
		
		
		if ( (tmin <= tmax) && (tmax > 0)  ) {
			
		} else {
			//System.out.println("no hay interseccion con este nodo");
			return; //System.out.println("no hay interseccion con root! :S");
		}
		
		if (node.isLeaf() && (node.primitives.size() > 0)) {
			ret.add(node);
			return;
		} else if (node.isLeaf()) {
			return;
		}
		
		double txM = 0.5 * (tx0 + tx1);
		double tyM = 0.5 * (ty0 + ty1);
		double tzM = 0.5 * (tz0 + tz1);				
		
		if (tx0 > txM || tx1 < txM) {
			return;
			//throw new RuntimeException("se cago fiero");
		}
		if (ty0 > tyM || ty1 < tyM) {
			return;
			//throw new RuntimeException("se cago fiero");
		}
		if (tz0 > tzM || tz1 < tzM) {
			return;
			//throw new RuntimeException("se cago fiero");
		}
	    procSubtree(tx0,ty0,tz0,txM,tyM,tzM,node.childs.get(0), ret, a, b, orig, dir);
	    
	    procSubtree(tx0,ty0,tzM,txM,tyM,tz1,node.childs.get(1), ret, a, b, orig, dir);
	    
	    procSubtree(tx0,tyM,tz0,txM,ty1,tzM,node.childs.get(2), ret, a, b, orig, dir);
	    
	    procSubtree(tx0,tyM,tzM,txM,ty1,tz1,node.childs.get(3), ret, a, b, orig, dir);
	    
	    procSubtree(txM,ty0,tz0,tx1,tyM,tzM,node.childs.get(4), ret, a, b, orig, dir);
	    
	    procSubtree(txM,ty0,tzM,tx1,tyM,tz1,node.childs.get(5), ret, a, b, orig, dir);
	    
	    procSubtree(txM,tyM,tz0,tx1,ty1,tzM,node.childs.get(6), ret, a, b, orig, dir);
	    
	    procSubtree(txM,tyM,tzM,tx1,ty1,tz1,node.childs.get(7), ret, a, b, orig, dir);
	    
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
			double txM, double tyM, double tzM) {
		byte ret=0;
		if(tx0 > ty0) {
			if(tx0 > tz0) {
				// max(tx0, ty0, tz0) is tx0. Entry plane is YZ.
				if(tyM < tx0) {
					ret |= 2;
				} else {
					//ret &= 0xD;
				}
				if(tzM < tx0) {
					ret |= 1;
				} else {
					//ret &= 0xE;
				}
			} else {
				// max(tx0, ty0, tz0) is tz0. Entry plane is XY.
				if(txM < tz0) { 
					ret |= 4;
				} else {
					//ret &= 0xB;
				}
				if(tyM < tz0) { 
					ret |= 2;
				} else {
					//ret &= 0xD;
				}
			}
		} else {
			if(ty0 > tz0) {
				// max(tx0, ty0, tz0) is ty0. Entry plane is XZ.
				if(txM < ty0) { 
					ret |= 4;
				} else {
					//ret &= 0xB;
				}
				if(tzM < ty0) { 
					ret |= 1;
				} else {
					//ret &= 0xE;
				}
			} else {
				// max(tx0, ty0, tz0) is tz0. Entry plane is XY.
				if(txM < tz0) { 
					ret |= 4;
				} else {
					//ret &= 0xB;
				}
				if(tyM < tz0) {
					ret |= 2;
				} else {
					//ret &= 0xD;
				}
			}
		}
		
		
		/*	
		byte ret=a;
		if (tz0 > ty0 ) {
			if (tz0 > tx0) {
			//tz0 es el mayor
				if (txM < tz0) {
					ret |= 4;
					//ret |= 1;
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
					//ret |= 4;
					ret |= 1;
				} else {
					//ret &= 0xB;
				}
			}
		} else {
			//ty0 es el maximo
			if (ty0 > tx0) {
				if (txM < ty0) {
					ret |= 4;
					//ret |= 1;
				} else {
					//ret &= 0xE;
				}
				if(tzM < ty0) {
					ret |= 1;
					//ret |= 4;
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
		}
		*/
		return ret;
	}
	
	/**
	 * Funcion add: Inserta recursivamente una primitiva a un nodo. 
	 * Si es hoja, lo agrega. Si supera la cantidad se expande(pasa a ser padre).
	 * Si es padre, verifica si en que hijos esta la primitiva y vuelve a llamarse recursivamente.
	 * 
	 * @param node Nodo a intertar la primitiva
	 * @param p primitiva
	 */
	
	private void add(OctreeNode node, Primitive p) {
		if (node.isLeaf()) {
			if (node.primitives.size() == PrimitiveOctree.maxPrimitives) {
				//throw new RuntimeException("Se entro en una recursion infinita :S");
				System.out.println("Se expande el nodo, tamanio del octree " + node.primitives.size());System.out.flush();
				
				// Crea hijos y les agrega mis primitivas.
				expand(node);
				node.primitives.clear();
				add(node,p);
			} else {
				node.primitives.add(p);
			}
			//node.primitives.add(p);
		} else {
			List<Integer> intersectedOctants = getIntersectedOctants(node, p);
			for ( Integer octant : intersectedOctants ) {
				System.out.println("Se caga aca");System.out.flush();
				add(node.childs.get(octant.intValue()),p);
			}
		}
	}
	
	/**
	 * Funcion add: Inserta una primitiva al nodo raiz
	 * @param p Primitiva
	 */
	public void add(Primitive p) {
		add(root, p);
		return;
	}
	
	/**
	 * Funcion que transforma un nodo de hoja a padre
	 * Creando sus hijos como octantes predefinidos contenidos en el padre
	 * 
	 * @param node Nodo a expandir
	 */
	private void expand(OctreeNode node) {
		
		
		double xMed = (node.xMin + node.xMax)*0.5;
		double yMed = (node.yMin + node.yMax)*0.5;
		double zMed = (node.zMin + node.zMax)*0.5;

		//Armo sus hijos
		node.childs.add(0, new OctreeNode(node.xMin, xMed,node.yMin,yMed, node.zMin, zMed));//ok
		node.childs.add(1, new OctreeNode(node.xMin, xMed,node.yMin,yMed, zMed, node.zMax));//ok
		node.childs.add(2, new OctreeNode(node.xMin, xMed,yMed,node.yMax, node.zMin, zMed));//ok
		node.childs.add(3, new OctreeNode(node.xMin, xMed,yMed,node.yMax, zMed, node.zMax));//ok
		
		node.childs.add(4, new OctreeNode(xMed, node.xMax,node.yMin,yMed, node.zMin, zMed));
		node.childs.add(5, new OctreeNode(xMed, node.xMax,node.yMin,yMed, zMed, node.zMax));
		node.childs.add(6, new OctreeNode(xMed, node.xMax,yMed,node.yMax, node.zMin, zMed));
		node.childs.add(7, new OctreeNode(xMed, node.xMax,yMed,node.yMax,zMed, node.zMax));//ok
		
		// Marca booleana para ver si todas las primitvas
		
		for (Primitive pr : node.primitives) {
			//En que octantes del nodo esta la primitiva
			List<Integer> intersectedOctants = getIntersectedOctants(node, pr);
			
			for ( Integer octant : intersectedOctants ) {
				//Busco en los hijos si hay alguno que tiene mi misma cantidad de primitivas,
				//Si es asi , agrando la capacidad maxima de un nodo.
				if (node.childs.get(octant.intValue()).primitives.size() + 1 == node.primitives.size()) {
					PrimitiveOctree.maxPrimitives+=node.primitives.size();	
				}
				
				add(node.childs.get(octant.intValue()),pr);
			}
			//add(node, p);
		}
		
		return;				

	}
	
	/**
	 * Funcion getOctants: Funcion que recibe un nodo padre y una primitiva 
	 * y se fija en que octantes esta la primitiva pasada como parametro.
	 * 
	 * @param parent Nodo padre
	 * @param p Primitiva a insertar
	 * @return Lista numera de octantes
	 */
	private List<Integer> getIntersectedOctants(OctreeNode parent, Primitive p) {
		/*double [] extremes = p.getBoundaryPoints();
		Point3d pMin = new Point3d(extremes[0],extremes[2], extremes[4]);
		Point3d pMax = new Point3d(extremes[1],extremes[3], extremes[5]);
		Point3d pMid = new Point3d((parent.xMax+parent.xMin)*0.5,
				(parent.yMax+parent.yMin)*0.5,
				(parent.zMax+parent.zMin)*0.5);

		boolean over[] = new boolean[8];
		over[0] = over[1] = over[2] = over[3] = (pMin.x <= pMid.x);
		over[4] = over[5] = over[6] = over[7] = (pMax.x > pMid.x);
		over[0] = over[0] && (pMin.y <= pMid.y);
		over[1] = over[1] && (pMin.y <= pMid.y);
		over[4] = over[4] && (pMin.y <= pMid.y);
		over[5] = over[5] && (pMin.y <= pMid.y);
		over[2] = over[2] && (pMax.y > pMid.y);
		over[3] = over[3] && (pMax.y > pMid.y);
		over[6] = over[6] && (pMax.y > pMid.y);
		over[7] = over[7] && (pMax.y > pMid.y);
		over[0] = over[0] && (pMin.z <= pMid.z);
		over[2] = over[2] && (pMin.z <= pMid.z);
		over[4] = over[4] && (pMin.z <= pMid.z);
		over[6] = over[6] && (pMin.z <= pMid.z);
		over[1] = over[1] && (pMax.z > pMid.z);
		over[3] = over[3] && (pMax.z > pMid.z);
		over[5] = over[5] && (pMax.z > pMid.z);
		over[7] = over[7] && (pMax.z > pMid.z);*/
		
		
		
		boolean posOctants[] = {true, true, true, true, true, true, true, true};
		
		//Punto intermedio
		double xMed = (parent.xMax+parent.xMin)*0.5;
		double yMed = (parent.yMax+parent.yMin)*0.5;
		double zMed = (parent.zMax+parent.zMin)*0.5;
		
		double [] extremes = p.getBoundaryPoints();
		
		if (extremes[0] > xMed){
			posOctants[0]=false;
			posOctants[1]=false;
			posOctants[2]=false;
			posOctants[3]=false;
		}
		if (extremes[1] < xMed) {
			posOctants[4]=false;
			posOctants[5]=false;
			posOctants[6]=false;
			posOctants[7]=false;				
		}
		if (extremes[2] > yMed) {
			posOctants[0]=false;
			posOctants[1]=false;
			posOctants[4]=false;
			posOctants[5]=false;			
		}
		if (extremes[3] < yMed) {
			posOctants[2]=false;
			posOctants[3]=false;
			posOctants[6]=false;
			posOctants[7]=false;			
		}		

		if (extremes[4] > zMed) {
			posOctants[0]=false;
			posOctants[2]=false;
			posOctants[4]=false;
			posOctants[6]=false;				
		} 
		if (extremes[5] < zMed){
			posOctants[1]=false;
			posOctants[3]=false;
			posOctants[5]=false;
			posOctants[7]=false;
		}	
		
		List<Integer> ret = new ArrayList<Integer>();
		for (int i=0; i< posOctants.length; i++) {
			if (posOctants[i]) {
				ret.add(i);
				
			}
		}
		System.out.println("octantes intersectados " + ret.size());
		return ret;
	}
	
	public void printOctree() {
		printOctree(root);
	}
	
	// Armado anda JOYA
	private void printOctree(OctreeNode root) {
		System.out.println("************************************");
		System.out.println("Este nodo es hoja?:" + root.isLeaf());
		System.out.println("Tiene #hijos: " + root.childs.size());
		System.out.println("Tiene #primitivas" + root.primitives.size());
		System.out.println("xMin:"+root.xMin + ", xMax:"+root.xMax);
		System.out.println("yMin:"+root.yMin + ", yMax:"+root.yMax);
		System.out.println("zMin:"+root.zMin + ", zMax:"+root.zMax);
		
		/*if (root.primitives.size()>1) {
			double [] p1 = root.primitives.get(0).getBoundaryPoints();
			double [] p2 = root.primitives.get(1).getBoundaryPoints();
			
		System.out.println("#primitiva1" + p1[0] + ", " + p1[1] + ", " + p1[2] + ", " + p1[3] + ", " + p1[4] + ", " + p1[5]);
		System.out.println("#primitiva2" + p2[0] + ", " + p2[1] + ", " + p2[2] + ", " + p2[3] + ", " + p2[4] + ", " + p2[5]);
		}*/
		for (OctreeNode child : root.childs) {
			printOctree(child);
		}
	}
	
	double max(double a, double b, double c) {
		double aux = a > b ? a : b;
		return aux > c ? aux : c;
	}
	double max(double a, double b) {
		return a > b ? a : b;
	}
	
	double min(double a, double b, double c) {
		double aux = a < b ? a : b;
		return aux < c ? aux : c;
	}
	double min(double a, double b) {
		return a < b ? a : b;
	}
		
}
