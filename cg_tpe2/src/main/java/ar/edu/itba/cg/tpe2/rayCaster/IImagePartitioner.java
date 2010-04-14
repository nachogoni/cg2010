package ar.edu.itba.cg.tpe1.rayCaster;

import java.awt.Rectangle;
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
	 * @return List of @size number of portions
	 */
	List<Rectangle> getPortions(int size, int width, int height);

}
