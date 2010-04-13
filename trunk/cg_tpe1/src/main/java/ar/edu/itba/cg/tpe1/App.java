package ar.edu.itba.cg.tpe1;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe1.rayCaster.Camera;
import ar.edu.itba.cg.tpe1.rayCaster.RayCaster;
import ar.edu.itba.cg.tpe1.rayCaster.Scene;

/**
 * TPE1
 *
 */
public class App 
{
    public static void main( String[] args )
    {
		int i = 0;
		String arg;
		boolean failed = false;
		boolean time = false;
		int colorMode = RayCaster.COLOR_MODE_RANDOM;
		int colorVar = RayCaster.COLOR_VARIATION_LINEAR;
    	String sceneName = null;
    	String fileName = null;
    	String format = "PNG";
    	BufferedImage image;
    	File output = null;
    	int width = 800;
    	int height = 600;
    	long start = 0;
    	long stop = 0;
    	
        //Parameters parsing
        while (!failed && i < args.length && args[i].startsWith("-")) {
            arg = args[i++];

            if (arg.equals("-time")) {
            	// Time
            	time = true;
            } else if (arg.equals("-o")) {
            	// Output filename
                if (i < args.length) {
                	fileName = args[i++].toLowerCase();
                	// Check fileName
                	if (fileName.matches("[a-zA-Z0-9_]+.(png|bmp)") == false) {
                        System.out.println("'" + fileName + "' is an invalid output filename");
                        failed = true;
                    }
                } else {
                    System.out.println(arg + " requires a filename");
                    failed = true;
                }
            } else if (arg.equals("-i")) {
            	// Scene name
                if (i < args.length) {
                	sceneName = args[i++];
                	// Check sceneName
                	if (sceneName.matches("scene\\d.sc") == false) {
                        System.out.println("'" + sceneName + "' is an invalid scene name");
                        failed = true;
                    }
                } else {
                    System.out.println(arg + " requires a scene name");
                    failed = true;
                }
            } else if (arg.equals("-r")) {
            	// Resolution
            	if ((i+1) < args.length) {
            		arg = args[i++];
            		// Width
            		if (arg.matches("[0-9]+") == false) {
            			System.out.println("'" + arg + "' is an invalid image width");
            			failed = true;
            		} else {
            			width = (new Integer(arg)).intValue();
            		}
            		arg = args[i++];
            		// Height
            		if (arg.matches("[0-9]+") == false) {
            			System.out.println("'" + arg + "' is an invalid image height");
            			failed = true;
            		} else {
            			height = (new Integer(arg)).intValue();
            		}
            	} else {
            		System.out.println(arg + " requires width and heigh");
            		failed = true;
            	}
            } else if (arg.equals("-cm")) {
            	// Color mode
                if (i < args.length) {
                	arg = args[i++];
                	if (arg.equals("random")) {
                		colorMode = RayCaster.COLOR_MODE_RANDOM;
                	} else if (arg.equals("ordered")) {
	                	colorMode = RayCaster.COLOR_MODE_ORDERED;
                	} else {
                		System.out.println("Invalid color mode");
                	}
                } else {
                    System.out.println(arg + " requires color mode");
                    failed = true;
                }
            } else if (arg.equals("-cv")) {
            	// Color variation
                if (i < args.length) {
                	arg = args[i++];
                	if (arg.equals("linear")) {
                		colorVar = RayCaster.COLOR_VARIATION_LINEAR;
                	} else if (arg.equals("log")) {
	                	colorVar = RayCaster.COLOR_VARIATION_LOG;
                	} else {
                		System.out.println("Invalid color variation");
                	}
                } else {
                    System.out.println(arg + " requires a search function");
                    failed = true;
                }
	        } else if (arg.equals("-usage")) {
	        	// Usage
        		failed = true;
	        } else {
	        	i = -1;
        		failed = true;
	        }
        }
        
        if (i != args.length) {
        	System.out.println("Invalid arguments");
        	failed = true;
        }

        if (failed || sceneName == null) {
        	showUsage();
        	return;
        }
        
        // Create output file name
        if (fileName == null) {
        	fileName = sceneName.substring(0, sceneName.length() - 2).concat("png");
        }
        // Open output file
    	try {
    		output = new File(fileName);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Set output format
		format = fileName.substring(fileName.length() - 3, fileName.length()).toUpperCase();
    	
    	// Create an scene
    	Scene scene = new Scene(sceneName);
    	
    	// Create a camera
    	Camera camera = new Camera(new Point3d(0d, 0d, 10d), new Point3d(0d, 0d ,0d), new Point3d(0d, 10d ,10d), 65, width, height);
    	
    	// Create rayCaster
    	RayCaster raycaster = null;
    	
    	// Set colors for primitives
    	if (colorMode == RayCaster.COLOR_MODE_ORDERED) {
    		raycaster = new RayCaster(scene, camera, 4, colorMode, colorVar);
    	} else {
    		raycaster = new RayCaster(scene, camera, 4, colorMode, colorVar);
    	}
    	
    	// Take start time
    	if (time == true) {
    		start = Calendar.getInstance().getTimeInMillis();
    	}
    	
    	// Get image from a viewPort
    	image = raycaster.getImage(width, height, BufferedImage.TYPE_INT_RGB);

    	// Take stop time
    	if (time == true) {
    		stop = Calendar.getInstance().getTimeInMillis();
    	}

    	// Save image
    	try {
			ImageIO.write(image, format, output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		raycaster.cleanup();
		
		if (time == true) {
			System.out.println("Done in " + (stop - start) + " milliseconds!");
		} else {
			System.out.println("Done!");
		}
		
    }
    
	public static void showUsage() {
		System.out.println("Usage: App -i sceneX.sc [-o output] [-time] [-cm [random|ordered]] [-cm [linear|log]] [-r width height] [-usage]");
		return;
	}
}
