package ar.edu.itba.cg.tpe2.rayCaster.ColorProviders;

import java.awt.Color;
import java.util.List;

import ar.edu.itba.cg.tpe2.rayCaster.IColorProvider;

/**
 * Given the color list, it returns the colors in order.
 * Once it has reached the end, it starts returning colors in order
 * from the end to the start and so on.
 */
public class WaveColorProvider implements IColorProvider {

	private List<Color> colors;
	private int nextColor;
	private enum Direction{UP, DOWN};
	Direction dir;

	public WaveColorProvider(List<Color> colors) throws IllegalArgumentException {
		if ( colors == null || colors.isEmpty() )
			throw new IllegalArgumentException("List of Colors is either null or empty");
		this.colors = colors;
		nextColor = 0;
		dir = Direction.UP;
	}
	
	@Override
	synchronized public Color getNextColor() {
		if ( nextColor >= colors.size() ){
			nextColor = colors.size()-1;
			dir = Direction.DOWN;
		}
		if ( nextColor < 0 ){
			nextColor = 0;
			dir = Direction.UP;
		}
		Color c;
		if ( dir.equals(Direction.UP) )
			c = colors.get(nextColor++);
		else
			c = colors.get(nextColor--);
		return c;
	}

}
