package ar.edu.itba.cg.tpe2.rayTracer;

import java.awt.Color;

/**
 * Basic Interface to obtain a new color for a Primitive
 *
 */
public interface IColorProvider {

	Color getNextColor();
}
