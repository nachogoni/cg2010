package ar.edu.itba.cg.tpe1.rayCaster.ImagePartitioners;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.cg.tpe1.rayCaster.IImagePartitioner;

/**
 * Splits the image in horizontal stripes ( along the height )
 * 
 */
public class HorizontalImagePartitioner implements IImagePartitioner {

	@Override
	public List<Rectangle> getPortions(int size, int width, int height) {
		if ( width < 1 || height < 1 || size < 1 )
			return null;
		
		int portionHeight = height/size;
		int remainder = height%size;
		
		List<Rectangle> out = new ArrayList<Rectangle>();
		int currentHeight = 0;
		
		if ( remainder != 0 ){
			out.add(new Rectangle(0, 0, width, portionHeight+remainder));
			currentHeight += portionHeight+remainder;
		}
		while( currentHeight < height){
			out.add(new Rectangle(0, currentHeight, width, portionHeight));
			currentHeight += portionHeight;
		}
		
		return out;
	}

}
