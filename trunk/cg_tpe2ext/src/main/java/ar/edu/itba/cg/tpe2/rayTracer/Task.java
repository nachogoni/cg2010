package ar.edu.itba.cg.tpe2.rayTracer;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Task {

	private Rectangle region;

	private BufferedImage image;

	public Task(Rectangle region, int imgType) {
		this.region = region;
		image = new BufferedImage(region.width, region.height, imgType);
	}
	
	public Rectangle getRegion() {
		return region;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
}
