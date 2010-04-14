package ar.edu.itba.cg.tpe1.rayCaster.ColorProviders;

import java.awt.Color;
import java.util.Random;

import ar.edu.itba.cg.tpe1.rayCaster.IColorProvider;

/**
 * Gives a random color each time it is called
 *
 */
public class RandomColorProvider implements IColorProvider{

	static Random rnd = new Random();
	
	@Override
	public Color getNextColor() {
		return new Color(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat());
	}

}
