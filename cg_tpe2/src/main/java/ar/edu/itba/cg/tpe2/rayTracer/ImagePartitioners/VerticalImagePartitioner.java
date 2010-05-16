package ar.edu.itba.cg.tpe2.rayTracer.ImagePartitioners;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.cg.tpe2.rayTracer.IImagePartitioner;
import ar.edu.itba.cg.tpe2.rayTracer.Task;

/**
 * Splits the image in vertical stripes ( along the width )
 *
 */
public class VerticalImagePartitioner implements IImagePartitioner {

	@Override
	public List<Task> getPortions(int size, int width, int height, int imgType) {
		if ( width < 1 || height < 1 || size < 1 )
			return null;
		
		int portionWidth = width/size;
		int remainder = width%size;
		
		List<Task> out = new ArrayList<Task>();
		int currentWidth = 0;
		
		if ( remainder != 0 ){
			Rectangle rectangle = new Rectangle(0, 0, portionWidth+remainder, height ); 
			out.add(new Task(rectangle, imgType));
			currentWidth += portionWidth+remainder;
		}
		while( currentWidth < width){
			Rectangle rectangle = new Rectangle(currentWidth, 0, portionWidth, height);
			out.add(new Task(rectangle, imgType));
			currentWidth += portionWidth;
		}
		
		return out;
	}

}
