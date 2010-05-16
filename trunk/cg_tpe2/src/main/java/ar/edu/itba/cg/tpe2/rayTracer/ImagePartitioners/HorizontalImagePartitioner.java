package ar.edu.itba.cg.tpe2.rayTracer.ImagePartitioners;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.cg.tpe2.rayTracer.IImagePartitioner;
import ar.edu.itba.cg.tpe2.rayTracer.Task;

/**
 * Splits the image in horizontal stripes ( along the height )
 * 
 */
public class HorizontalImagePartitioner implements IImagePartitioner {

	@Override
	public List<Task> getPortions(int size, int width, int height, int imgType) {
		if ( width < 1 || height < 1 || size < 1 )
			return null;
		
		int portionHeight = height/size;
		int remainder = height%size;
		
		List<Task> out = new ArrayList<Task>();
		int currentHeight = 0;
		
		if ( remainder != 0 ){
			Rectangle rectangle = new Rectangle(0, 0, width, portionHeight+remainder);
			out.add(new Task(rectangle, imgType));
			currentHeight += portionHeight+remainder;
		}
		while( currentHeight < height){
			Rectangle rectangle = new Rectangle(0, currentHeight, width, portionHeight);
			out.add(new Task(rectangle, imgType));
			currentHeight += portionHeight;
		}
		
		return out;
	}

}
