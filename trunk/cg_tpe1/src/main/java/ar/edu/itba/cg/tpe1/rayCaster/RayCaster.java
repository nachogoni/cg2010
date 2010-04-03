package ar.edu.itba.cg.tpe1.rayCaster;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe1.geometry.Primitive;
import ar.edu.itba.cg.tpe1.geometry.Ray;

public class RayCaster {

	private CasterThread t1;
	private CasterThread t2;
	private CasterThread t3;
	private CasterThread t4;
	protected BufferedImage image;
	private CyclicBarrier cb;
	
	public RayCaster(Scene scene, Camera camera) {
		cb = new CyclicBarrier(5);
		this.t1 = new CasterThread(scene, camera, this, cb);
		this.t1.start();
		this.t2 = new CasterThread(scene, camera, this, cb);
		this.t2.start();
		this.t3 = new CasterThread(scene, camera, this, cb);
		this.t3.start();
		this.t4 = new CasterThread(scene, camera, this, cb);
		this.t4.start();
	}

	/*
	 * Get the image viewed through a viewport
	 */
	public BufferedImage getImage(int width, int height, int imageType) {
		if ( ! t1.isAlive() || ! t2.isAlive() || ! t3.isAlive() || ! t4.isAlive() )
			return null;

		image = new BufferedImage(width, height, imageType);

		t1.setPortion(0, width/2, 0, height/2);
		t2.setPortion(width/2, width, 0, height/2);
		t3.setPortion(0, width/2, height/2, height);
		t4.setPortion(width/2, width, height/2, height);

		try { cb.await(); } catch (InterruptedException e) { e.printStackTrace(); } catch (BrokenBarrierException e) { e.printStackTrace(); }

		try { cb.await(); } catch (InterruptedException e) { e.printStackTrace(); } catch (BrokenBarrierException e) { e.printStackTrace(); }
		return image;
	}

	synchronized protected BufferedImage getImage() {
		return image;
	}

	public void cleanup() {
		// Only need to interrupt one thread. Then the Barrier is broken and they all finish.
		t1.interrupt();
	}
}

class CasterThread extends Thread{

	private Scene scene;
	private Camera camera;
	private int fromX;
	private int toX;
	private int fromY;
	private int toY;
	private RayCaster rayCaster;
	private CyclicBarrier cb;

	public CasterThread(Scene scene, Camera camera, RayCaster rayCaster, CyclicBarrier cb) {
		this.cb = cb;
		this.scene = scene;
		this.camera = camera;
		this.rayCaster = rayCaster;
	}

	public void setPortion(int fromX, int toX, int fromY, int toY) {
		this.fromX = fromX;
		this.toX = toX;
		this.fromY = fromY;
		this.toY = toY;
	}

	public void run() {
		while ( true ){
			System.out.println(cb.getNumberWaiting()+" threads are waiting");
			try { cb.await(); } catch (InterruptedException e) { 
				System.out.println("Someone told me to die.");
				return;
			} catch (BrokenBarrierException e) { 
				System.out.println("Someone broke the barrier.");
				return;
			}

			Point3d origin = camera.getOrigin();
			Point3d intersection = null, aux = null;
			Color color;
			for (int i = fromX; i < toX; i++) {
				for (int j = fromY; j < toY; j++) {
					// Set infinite color
					color = Color.BLUE;
					
					// Create a new Ray from camera, i, j
					Ray ray = new Ray(origin, new Point3d());// TODO: (origin, i, j);
					
					// Find intersection in scene with ray
					for (Primitive p : scene.getList()) {
						aux = p.intersect(ray);
						if (aux != null && (intersection == null || (intersection != null &&
							aux.distance(origin) < intersection.distance(origin)))) {
							intersection = aux;
							color = p.getColor();
						}
					}
						
					// Set color to image(i, j)
					synchronized (rayCaster.image) {
						rayCaster.getImage().setRGB(i, j, color.getRGB());
					}
				}
			}
//			System.out.println(cb.getNumberWaiting()+" threads are waiting");
			try { cb.await(); } catch (InterruptedException e) { e.printStackTrace(); } catch (BrokenBarrierException e) { e.printStackTrace(); }
		}
	}
}