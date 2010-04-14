package ar.edu.itba.cg.tpe1.geometry;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.vecmath.Point3d;

public class Star implements Primitive{

	private Color c = Color.ORANGE;
	// It'd be better if a point has it's color i.e. ColorPoint3d
	private Map<Double,Map<Double,Map<Double,Color> > > pointColors;

	private List<Triangle> triangles;
	
	/**
	 * Makes a 10, 20 or 40 points star centered at origin. The amount of points of
	 * the star is determined by 2^(sideslength.length-1) * 5, so the sideslength
	 * and depths parameter should be equal and take values between 2 and 4. Also,
	 * it is required that the values in sideslength and depths are ordered from
	 * lowest to highest
	 * @param origin Center of star
	 * @param angle Initial rotation of the star
	 * @param sideslength Lengths for each edge, array size must be between 2 and 4
	 * @param depths Depths for each edge, array size must be between 2 and 4
	 * @param colors Colors to be used in each part of the star
	 * @throws IllegalArgumentException
	 */
	public Star(Point3d origin, double angle, double sideslength [], double depths[], List<Color> colors) throws IllegalArgumentException{
		if ( sideslength.length < 2 || sideslength.length > 4 )
			throw new IllegalArgumentException("Star should have between two and four side lengths");
		
		if ( depths.length < 2 || depths.length > 4 )
			throw new IllegalArgumentException("Star should have between two and four depths");
		
		if ( depths.length != sideslength.length )
			throw new IllegalArgumentException("Star depths and sides should be the same length");
		
		if ( !isIncreasing(sideslength) )
			throw new IllegalArgumentException("Star side lengths should be increasing");
		
		if ( !isIncreasing(depths) )
			throw new IllegalArgumentException("Star depths should be increasing");

		List<Point3d> points = generatePoints(sideslength,depths,angle);
		
		if ( colors.isEmpty() )
			colors.add(Color.ORANGE);
		
		Iterator<Color> iterator = colors.iterator();
		pointColors = new HashMap<Double, Map<Double,Map<Double,Color>>>();
		triangles = new ArrayList<Triangle>();
		
		for ( int i = 0 ; i < points.size()-1 ; i ++){
			if ( ! iterator.hasNext() )
				iterator = colors.iterator();
			
			c = iterator.next();
			triangles.add(new Triangle(origin, points.get(i), points.get(i+1), c));
		}
		if ( ! iterator.hasNext() )
			iterator = colors.iterator();
		
		c = iterator.next();
		triangles.add(new Triangle(origin, points.get(points.size()-1), points.get(0), c));
	}
	
	/**
	 * Generates the list of points for the star. The size of the list depends
	 * on the size of the sideslength array. See constructor.
	 * @param sideslength Lengths for each edge
	 * @param depths Depths for each edge
	 * @param angle Initial rotation of the star 
	 * @return List of points for the star
	 */
	private List<Point3d> generatePoints(double[] sideslength, double[] depths, double angle) {
		/*
		 * 2^(sideslength.length-1) * 5 = 1 * 5 = 5
		 * 2^(sideslength.length-1) * 5 = 2 * 5 = 10
		 * 2^(sideslength.length-1) * 5 = 4 * 5 = 20
		 * 2^(sideslength.length-1) * 5 = 8 * 5 = 40
		 * 2^(sideslength.length-1) * 5 = number of points
		 */
		List<Point3d> points = new ArrayList<Point3d>();
		int numberOfPoints = (int) (Math.pow(2.0, sideslength.length-1) * 5);
		double minimumAngle = 2*Math.PI/numberOfPoints;
		
		List<Double> distanceList = generateOrderedList(sideslength);
		List<Double> depthsList = generateOrderedList(depths);
		
		Iterator<Double> diterator = distanceList.iterator();
		Iterator<Double> depthsiterator = depthsList.iterator();
		
		for(int i=0 ; i < numberOfPoints ; i++){
			
			double currentAngle = i*minimumAngle + angle;
			double distance = diterator.next();
			double depth = depthsiterator.next();
			
			points.add(new Point3d(distance*Math.cos(currentAngle),distance*Math.sin(currentAngle),depth));
			
			if ( !diterator.hasNext() ){
				diterator = distanceList.iterator();
				depthsiterator = depthsList.iterator();
			}
		}
		return points;
	}

	/**
	 * Given an array x = {x0, x1, x2, x3} it returns
	 * x3, x0, x1, x0, x2, x0, x1, x0 
	 * @param sideslength
	 * @return
	 */
	private List<Double> generateOrderedList(double[] sideslength) {
		List<Double> a = new ArrayList<Double>();
		for (double d:sideslength)
			a.add(d);
		return generateOrderedList(a);
	}

	/**
	 * Given an array x = {x0, x1, x2, x3} it returns
	 * x3, x0, x1, x0, x2, x0, x1, x0 
	 * @param sideslength
	 * @return
	 */
	private List<Double> generateOrderedList(List<Double> sidesLengths) {
		if ( sidesLengths.size() == 1 )
			return sidesLengths;
		
		List<Double> first = new ArrayList<Double>();
		List<Double> second = new ArrayList<Double>();
		for(int i=0 ; i < sidesLengths.size()-2 ; i++){
			first.add(sidesLengths.get(i));
			second.add(sidesLengths.get(i));
		}
		first.add(sidesLengths.get(sidesLengths.size()-1));
		second.add(sidesLengths.get(sidesLengths.size()-2));
		
		List<Double> out = generateOrderedList(first);
		out.addAll(generateOrderedList(second));
		return out;
	}

	/**
	 * Tests whether values in array are in increasing order
	 * @param array to be tested
	 * @return whether values in array are in increasing order
	 */
	private boolean isIncreasing(double[] array) {
		for(int i=0; i<array.length-1 ; i++)
			if ( array[i] >= array[i+1] )
				return false;
		return true;
	}

	@Override
	public Point3d intersect(Ray ray) {
		for ( int i = 0 ; i < triangles.size() ; i++ ){
			Point3d intersect = triangles.get(i).intersect(ray);
			if ( intersect != null ){
				synchronized (this) {
		
				Map<Double, Map<Double, Color>> map = pointColors.get(intersect.x);
				if ( map == null ){
					map = new HashMap<Double, Map<Double,Color>>();
					pointColors.put(intersect.x, map);
				}
				
				Map<Double, Color> map1 = map.get(intersect.y);
				if ( map1 == null ){
					map1 = new HashMap<Double,Color>();
					map.put(intersect.y, map1);
				}
				
				map1.put(intersect.z, triangles.get(i).color);
				}
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
		Color color = Color.BLACK;
		synchronized (this) {
			
		if ( pointColors.get(point.x) == null )
			return Color.BLACK;
		
		if ( pointColors.get(point.x).get(point.y) == null )
			return Color.BLACK;
		
		if ( pointColors.get(point.x).get(point.y).get(point.z) == null )
			return Color.BLACK;
		
		color = pointColors.get(point.x).get(point.y).get(point.z);
		}		
		return color;
	}
}
