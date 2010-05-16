package ar.edu.itba.cg.tpe2.rayTracer;

import java.util.List;

/**
 * Basic Interface for different ways of dividing an image to be processed
 *
 */
public interface IImagePartitioner {

	
	/**
	 * @param size Number of pieces or portions in which the image should be divided
	 * @param width Width of image
	 * @param height Height of image
	 * @param imageType 
	 * @return List of @size number of portions
	 */
	List<Task> getPortions(int size, int width, int height, int imageType);

}
