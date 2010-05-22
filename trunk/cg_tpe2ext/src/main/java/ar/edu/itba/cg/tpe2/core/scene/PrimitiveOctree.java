package ar.edu.itba.cg.tpe2.core.scene;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3f;

import ar.edu.itba.cg.tpe2.core.geometry.Plane;
import ar.edu.itba.cg.tpe2.core.geometry.Primitive;
import ar.edu.itba.cg.tpe2.core.geometry.Ray;

public class PrimitiveOctree {

	// Maxima cantidad de primitivas en la hoja
	public static int maxPrimitives = 1;
	
	public final static float DEFAULT_OCTREE_SIZE= 50.0f;
	public final static float EPSILON= 0.00000001f;
	
	// Nodo raiz del octree
	OctreeNode root;
	
	private float size=0;
	
	/**
	 * Constructor default
	 * @param xMinimun Cota inferior para x 
	 * @param xMaximun Cota superior para x
	 * @param yMinimun Cota inferior para y 
	 * @param yMaximun Cota superior para y
	 * @param zMinimun Cota inferior para z
	 * @param zMaximun Cota superior para z
	 */
	public PrimitiveOctree(float xMinimun, float xMaximun, float yMinimun, float yMaximun, float zMinimun, float zMaximun) {
		 
		// We are giving the chance to the intersect functions in primitives to have a representation error
		// and they still be in the correct octree node
		
		this.size = max( max(Math.abs(xMinimun), Math.abs(xMaximun), 
				max( Math.abs(yMinimun), Math.abs(yMaximun) ) ),
				max(Math.abs(zMinimun), Math.abs(zMaximun) ) )*2 + EPSILON*2;
	
		this.root = new OctreeNode(-size*0.5f, size*0.5f, 
				-size*0.5f, size*0.5f, 
				-size*0.5f, size*0.5f);
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
		
		Point3f rayOrig = new Point3f(ray.getOrigin().x, ray.getOrigin().y, ray.getOrigin().z);
		Point3f rayDir = new Point3f(ray.getDirection().x, ray.getDirection().y, ray.getDirection().z);
		
		//float tx0 ,tx1, ty0, ty1, tz0, tz1;
		float tx0, tx1;
		tx0=(root.xMin - rayOrig.x) / rayDir.x;
		tx1=(root.xMax - rayOrig.x) / rayDir.x;
		
		if (tx0 > tx1) {
			float aux = tx0;
			tx0=tx1;
			tx1=aux;
		}
		
		float ty0, ty1;
		ty0=(root.yMin - rayOrig.y) / rayDir.y;
		ty1=(root.yMax - rayOrig.y) / rayDir.y;

		if (ty0 > ty1) {
			float aux = ty0;
			ty0=ty1;
			ty1=aux;
		}
		float tz0, tz1;
		tz0=(root.zMin - rayOrig.z) / rayDir.z;
		tz1=(root.zMax - rayOrig.z) / rayDir.z;
		
		if (tz0 > tz1) {
			float aux = tz0;
			tz0=tz1;
			tz1=aux;
		}
		
		float tmin = max(tx0,ty0,tz0);
		float tmax = min(tx1,ty1,tz1);
		
		// Check if there is an instersection with the root node
		if ( (tmin <= tmax) && (tmax > 0)  ) {
			procSubtree(tx0,ty0,tz0,tx1,ty1,tz1, root, ret, a, b, rayOrig, rayDir);
		} else {
			//System.out.println("no hay interseccion con root! :S");
		}
		
		return ret;
	}
	
	void procSubtree( float tx0, float ty0, float tz0,
			float tx1, float ty1, float tz1, OctreeNode node, List<OctreeNode> ret, byte a, byte b, Point3f orig, Point3f dir) {
		
		/*if ( max(tx0, ty0, tz0) <= min(tx1, ty1, tz1) ) {
			return;
		}*/
		
		/*if ( (tx1 <= 0.0 ) || (ty1 <= 0.0) || (tz1 <= 0.0) ) {
			return;
		}*/
		//if (node.isLeaf() && (node.primitives.size() > 0)) {
			/*float tmin = max(tx0,ty0,tz0);
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
		/*float txM = 0.5 * (tx0 + tx1);
		float tyM = 0.5 * (ty0 + ty1);
		float tzM = 0.5 * (tz0 + tz1);	*/	
		
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
		//float tx0, tx1;
		tx0=(node.xMin - orig.x) / dir.x;
		tx1=(node.xMax - orig.x) / dir.x;
		
		if (tx0 > tx1) {
			float aux = tx0;
			tx0=tx1;
			tx1=aux;
		}
		
		//float ty0, ty1;
		ty0=(node.yMin - orig.y) / dir.y;
		ty1=(node.yMax - orig.y) / dir.y;

		if (ty0 > ty1) {
			float aux = ty0;
			ty0=ty1;
			ty1=aux;
		}
		//float tz0, tz1;
		tz0=(node.zMin - orig.z) / dir.z;
		tz1=(node.zMax - orig.z) / dir.z;
		
		if (tz0 > tz1) {
			float aux = tz0;
			tz0=tz1;
			tz1=aux;
		}
		
		float tmin = max(tx0,ty0,tz0);
		float tmax = min(tx1,ty1,tz1);		 		
		
		
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
		
		float txM = 0.5f * (tx0 + tx1);
		float tyM = 0.5f * (ty0 + ty1);
		float tzM = 0.5f * (tz0 + tz1);				
		
		if (tx0 > txM || tx1 < txM) {
			return;
		}
		if (ty0 > tyM || ty1 < tyM) {
			return;
		}
		if (tz0 > tzM || tz1 < tzM) {
			return;
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
				//System.out.println("Se expande el nodo, tamanio del octree " + node.primitives.size());System.out.flush();
				
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
				//System.out.println("Se caga aca");System.out.flush();
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
		
		
		float xMed = (node.xMin + node.xMax)*0.5f;
		float yMed = (node.yMin + node.yMax)*0.5f;
		float zMed = (node.zMin + node.zMax)*0.5f;

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
		
		boolean posOctants[] = {true, true, true, true, true, true, true, true};
		
		//Punto intermedio
		float xMed = (parent.xMax+parent.xMin)*0.5f;
		float yMed = (parent.yMax+parent.yMin)*0.5f;
		float zMed = (parent.zMax+parent.zMin)*0.5f;
		
		float [] extremes = p.getBoundaryPoints();
		
		if (p instanceof Plane) {
			List<Integer> ret = new ArrayList<Integer>();
			for (int i=0; i< posOctants.length; i++) {
				if (posOctants[i]) {
					ret.add(i);
					
				}
			}			
			return ret;
		}
		
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
		
		for (OctreeNode child : root.childs) {
			printOctree(child);
		}
	}
	
	float max(float a, float b, float c) {
		float aux = a > b ? a : b;
		return aux > c ? aux : c;
	}
	float max(float a, float b) {
		return a > b ? a : b;
	}
	
	float min(float a, float b, float c) {
		float aux = a < b ? a : b;
		return aux < c ? aux : c;
	}
	float min(float a, float b) {
		return a < b ? a : b;
	}
		
}
