package ar.edu.itba.cg.tpe1;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe1.geometry.Primitive;
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
    	BufferedImage image;
    	String fileName = "imagen.png";
    	
    	// Create an object list of the scene
    	Scene scene = new Scene(new ArrayList<Primitive>());
    	
    	// Create a camera
    	Camera camera = new Camera(new Point3d(0d, 0d, 10d), 
    			new Point3d(0d, 0d ,0d));
    	
    	// Create rayCaster
    	RayCaster raycaster = new RayCaster(scene, camera);
    	
    	// Get image from a viewPort
    	image = raycaster.getImage(800, 600, BufferedImage.TYPE_INT_RGB);
    	
    	// Save image
    	try {
			ImageIO.write(image, "PNG", new File(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
