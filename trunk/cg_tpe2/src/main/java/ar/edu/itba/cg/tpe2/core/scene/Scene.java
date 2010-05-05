package ar.edu.itba.cg.tpe2.core.scene;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.cg.tpe2.geometry.Primitive;
import ar.edu.itba.cg.tpe2.utils.PrimitiveOctree;

/**
 * Create a scene representation
 */
public class Scene {

	private List<Primitive> list = new ArrayList<Primitive>();
	
	private PrimitiveOctree octree = null;
	
	/**
	 * Constructor for the scene from a file name
	 * 
	 * @param scene Scene file name
	 */
	public Scene(String scene) {
		this.list = read(scene);
	}

	/**
	 * Constructor for the scene from a primitives list
	 * 
	 * @param list List of primitives in the scene
	 */
	public Scene(List<Primitive> list) {
		this.list = list;
	}
	
	/**
	 * Constructor for the scene
	 * 
	 */
	public Scene() {
	}

	/**
	 * Get the list of primitives in the scene
	 * 
	 * @return List of primitives
	 */
	public List<Primitive> getList() {
		return list;
	}
	
	/**
	 * Add a primitive in the scene
	 * 
	 * @param p New primitive
	 */
	public void add(Primitive p) {
    	list.add(p);
    
    	
    	
	}
	
	/**
	 * Add a list of primitives in the scene
	 * 
	 * @param primitives List of primitives to be added
	 */
	public void add(List<Primitive> primitives) {
		for (Primitive p : primitives) {
			list.add(p);
		}
	}
	
	/**
	 * Return a list of primitives into a file
	 * 
	 * @param scene File name of the scene
	 * 
	 * @return List of primitives
	 */
	public static List<Primitive> read(String scene) {
		//TODO: crear la escena en base a lo que el parser lea del archivo
    	return null;
	}
	
	public void optimize() {
		
		// TODO:
		
		// Recorre la lista y obtiene los maximos y minimos
		// crea el octree con los max y min
		// recorre la lista agregando las p al octree o se la damos directamente
		
		// tenemos el octree de la escena
		
		return;
	}
	
	
}
