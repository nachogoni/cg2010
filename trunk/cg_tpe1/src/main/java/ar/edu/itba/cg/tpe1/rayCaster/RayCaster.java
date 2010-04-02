package ar.edu.itba.cg.tpe1.rayCaster;

import java.awt.image.BufferedImage;

import javax.vecmath.Point3d;

public class RayCaster {

	private Scene scene;
	private Camera camera;
	
	public RayCaster(Scene scene, Camera camera) {
		this.scene = scene;
		this.camera = camera;
	}

	/*
	 * Get the image viewed through a viewport
	 */
	public BufferedImage getImage(int width, int height, int imageType) {

		BufferedImage image = new BufferedImage(width, height, imageType);
		
		Point3d origin = camera.getOrigin();
		
		// TODO Tirar rayos locos 
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				// Create a new Ray from camera, i, j
				//Ray ray = new Ray(origin, i, j);
				
				// Find intersection in scene with ray
				//for (Primitive p = )
				
				// Set color to image(i, j)
			}
		}
		
		
		return image;
	}
	
}
