package ar.edu.itba.cg.tpe2.rayCaster.ColorProviders;

import java.awt.Color;
import java.util.List;

import ar.edu.itba.cg.tpe2.rayCaster.IColorProvider;

/**
 * Given the color list, it returns the colors in order.
 * Once it has reached the end, it starts from the beginning again
 */
public class CyclicColorProvider implements IColorProvider{

	private List<Color> colors;
	private int nextColor;

	public CyclicColorProvider(List<Color> colors) throws IllegalArgumentException {
		if ( colors == null || colors.isEmpty() )
			throw new IllegalArgumentException("List of Colors is either null or empty");
		this.colors = colors;
		nextColor = 0;
	}
	
	@Override
	synchronized public Color getNextColor() {
		if ( nextColor >= colors.size() )
			nextColor = 0;
		Color c = colors.get(nextColor++);
		return c;
	}

}
