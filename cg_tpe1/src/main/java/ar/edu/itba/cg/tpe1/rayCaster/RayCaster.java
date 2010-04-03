package ar.edu.itba.cg.tpe1.rayCaster;

import java.awt.image.BufferedImage;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * RayCaster creates an image form a scene viewed from a camera
 */
public class RayCaster {

	private RayCasterThread t1;
	private RayCasterThread t2;
	private RayCasterThread t3;
	private RayCasterThread t4;
	protected BufferedImage image = null;
	private CyclicBarrier cb;
	
	/**
	 * RayCaster constructor
	 * 
	 * @param scene Scene representation to work with
	 * @param camera Actual camera where the viewer is   
	 */
	public RayCaster(Scene scene, Camera camera) {
		cb = new CyclicBarrier(5);
		this.t1 = new RayCasterThread(scene, camera, this, cb);
		this.t1.start();
		this.t2 = new RayCasterThread(scene, camera, this, cb);
		this.t2.start();
		this.t3 = new RayCasterThread(scene, camera, this, cb);
		this.t3.start();
		this.t4 = new RayCasterThread(scene, camera, this, cb);
		this.t4.start();
	}

	/**
	 * Get the image viewed through a viewport
	 * 
	 * @param width Width for the new image viewed from the viewport
	 * @param height Height for the new image viewed from the viewport
	 * @param imageType Format for the new image
	 *
	 * @return A BufferedImage representation  viewed from the viewport
	 * 
	 * @see The BufferedImage documentation for correct imageTypes
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

	/**
	 * Synchronization for the BufferedImage
	 * 
	 * @return Actual working image
	 */
	synchronized protected BufferedImage getImage() {
		return image;
	}

	/**
	 * Cleanup for the RayCaster class
	 */
	public void cleanup() {
		// Only need to interrupt one thread. Then the Barrier is broken and they all finish.
		t1.interrupt();
	}
	
	/**
	 * Finalize method
	 */
	protected void finalize() throws Throwable {
		this.cleanup();
		super.finalize();
	}
	
}