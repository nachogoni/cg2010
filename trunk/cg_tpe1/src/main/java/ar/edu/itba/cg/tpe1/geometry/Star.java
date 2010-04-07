package ar.edu.itba.cg.tpe1.geometry;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Point3d;

public class Star implements Primitive{

	Color c = Color.ORANGE;
	Triangle ts [];


	/**
	 * Creates a star with center at (0,0,0)
	 * @param longSide
	 * @param shortSide
	 * @param depth
	 */
	public Star(double longSide, double shortSide, double depth, List<Color> colors) {
		ts = new Triangle[10];
		
		Point3d points [] = getPoints(longSide, shortSide, depth);
		Point3d origin = new Point3d();
		
		if ( colors.isEmpty() )
			colors.add(Color.ORANGE);
		
		Iterator<Color> iterator = colors.iterator();
		
		for ( int i = 0 ; i < points.length-1 ; i ++){
			if ( ! iterator.hasNext() )
				iterator = colors.iterator();
			
			c = iterator.next();
			ts[i] = new Triangle(origin, points[i], points[i+1], c);
		}
		if ( ! iterator.hasNext() )
			iterator = colors.iterator();
		
		c = iterator.next();
		ts[points.length-1] = new Triangle(origin, points[points.length-1], points[0], c);
	}

	private Point3d [] getPoints(double longSide, double shortSide, double depth) {
		Point3d points [] = new Point3d[10];
		double tens = Math.PI/10;
		double fives = Math.PI/5;
		double threeTens = tens*3;
		
		points[0] = new Point3d(0,-longSide,0); 
		points[1] = new Point3d(shortSide*Math.sin(fives),-shortSide*Math.cos(fives),-depth);
		points[2] = new Point3d(longSide*Math.cos(tens),-longSide*Math.sin(tens),0);
		points[3] = new Point3d(shortSide*Math.cos(tens),shortSide*Math.sin(tens),-depth);
		points[4] = new Point3d(longSide*Math.cos(threeTens),longSide*Math.sin(threeTens),0);
		points[5] = new Point3d(0,shortSide,-depth);
		points[6] = new Point3d(-longSide*Math.cos(threeTens),longSide*Math.sin(threeTens),0);
		points[7] = new Point3d(-shortSide*Math.cos(tens),shortSide*Math.sin(tens),-depth);
		points[8] = new Point3d(-longSide*Math.cos(tens),-longSide*Math.sin(tens),0);
		points[9] = new Point3d(-shortSide*Math.sin(fives),-shortSide*Math.cos(fives),-depth);
		
		return points;
	}

	@Override
	public Point3d intersect(Ray ray) {
		for ( int i = 0 ; i < ts.length ; i++ ){
			Point3d intersect = ts[i].intersect(ray);
			if ( intersect != null ){
				c = ts[i].color;
				return intersect;
			}
		}
			
		return null;
	}

	@Override
	public void setColor(Color color) {
		c = color;
	}

	public Color getColor(Point3d point) {
		return c;
	}
}
