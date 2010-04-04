package ar.edu.itba.cg.tpe1;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe1.geometry.Fog;
import ar.edu.itba.cg.tpe1.geometry.Primitive;
import ar.edu.itba.cg.tpe1.geometry.Triangle;
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
    	
    	List<Primitive> list = new ArrayList<Primitive>();

    	/*list.add(new Triangle(new Point3d(-50,0,-50), new Point3d(0,-50,0), new Point3d(-50,0,50), Color.RED));
    	list.add(new Triangle(new Point3d(-50,0,-50), new Point3d(0,-50,0), new Point3d(50,0,-50), Color.BLUE));
    	list.add(new Triangle(new Point3d(50,0,-50), new Point3d(0,-50,0), new Point3d(50,0,50), Color.YELLOW));
    	list.add(new Triangle(new Point3d(50,0,50), new Point3d(0,-50,0), new Point3d(-50,0,50), Color.GREEN));*/
    	
    	list.add(new Triangle(new Point3d(-7,0,0), new Point3d(0,7,0), new Point3d(0,0,7), Color.RED));
    	list.add(new Triangle(new Point3d(-7,0,0), new Point3d(0,7,0), new Point3d(0,0,-7), Color.YELLOW));
    	list.add(new Triangle(new Point3d(0,0,-7), new Point3d(0,7,0), new Point3d(7,0,0), Color.GREEN));
    	list.add(new Triangle(new Point3d(0,0,7), new Point3d(0,7,0), new Point3d(7,0,0), Color.BLUE));
    	
    	//list.add(new Fog(Color.CYAN));
    	
    	// Create an object list of the scene
    	Scene scene = new Scene(list);
    	
    	// Create a camera
    	//Camera camera = new Camera(new Point3d(0d, 0d, 10d), new Point3d(0d, 0d ,0d), 65, 30);
    	Camera camera = new Camera(new Point3d(-1d, -1d, 10d), new Point3d(0d, 0d, 0d), 65, 30);
    	
    	// Create rayCaster
    	RayCaster raycaster = new RayCaster(scene, camera);
    	
    	// Get image from a viewPort
    	image = raycaster.getImage(1024, 768, BufferedImage.TYPE_INT_RGB);
	    	
    	// Save image
    	try {
			ImageIO.write(image, "PNG", new File(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		raycaster.cleanup();
		
		System.out.println("Done!");
		
    }
}
