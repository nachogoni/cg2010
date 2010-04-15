package ar.edu.itba.cg.tpe2.rayCaster.ImagePartitioners;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.cg.tpe2.rayCaster.IImagePartitioner;

/**
 * Splits the image in vertical stripes ( along the width )
 *
 */
public class VerticalImagePartitioner implements IImagePartitioner {

	@Override
	public List<Rectangle> getPortions(int size, int width, int height) {
		if ( width < 1 || height < 1 || size < 1 )
			return null;
		
		int portionWidth = width/size;
		int remainder = width%size;
		
		List<Rectangle> out = new ArrayList<Rectangle>();
		int currentWidth = 0;
		
		if ( remainder != 0 ){
			out.add(new Rectangle(0, 0, portionWidth+remainder, height ));
			currentWidth += portionWidth+remainder;
		}
		while( currentWidth < width){
			out.add(new Rectangle(currentWidth, 0, portionWidth, height));
			currentWidth += portionWidth;
		}
		
		return out;
	}

}
