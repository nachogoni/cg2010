package ar.edu.itba.cg.tpe1.rayCaster;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe1.geometry.Primitive;
import ar.edu.itba.cg.tpe1.geometry.Ray;

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
		Point3d intersection = null, aux = null;
		Color color;
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				// Set infinite color
				color = Color.BLACK;
				
				// Create a new Ray from camera, i, j
				Ray ray = new Ray(origin, new Point3d());// TODO: (origin, i, j);
				
				// Find intersection in scene with ray
				for (Primitive p : scene.getList()) {
					aux = p.intersect(ray);
					if (aux != null && intersection != null &&
						aux.distance(origin) < intersection.distance(origin)) {
						intersection = aux;
						color = p.getColor();
					}
				}
				
				// Set color to image(i, j)
				image.setRGB(i, j, color.getRGB());
			}
		}
		
		return image;
	}
	
}
