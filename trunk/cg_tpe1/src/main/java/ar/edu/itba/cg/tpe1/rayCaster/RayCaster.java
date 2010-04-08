package ar.edu.itba.cg.tpe1.rayCaster;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import ar.edu.itba.cg.tpe1.rayCaster.ImagePartitioners.HorizontalImagePartitioner;

/**
 * RayCaster creates an image form a scene viewed from a camera
 */
public class RayCaster {

	private List<RayCasterThread> threads;
	protected BufferedImage image = null;
	private CyclicBarrier cb;
	private IImagePartitioner imagePartitioner;
	
	/**
	 * RayCaster constructor
	 * 
	 * @param scene Scene representation to work with
	 * @param camera Actual camera where the viewer is   
	 */
	public RayCaster(Scene scene, Camera camera, int numThreads) {
		cb = new CyclicBarrier(numThreads+1);
		threads = new ArrayList<RayCasterThread>(numThreads);
		for (int i=0; i < numThreads; i++){
			RayCasterThread rayCasterThread = new RayCasterThread(scene, camera, this, cb);
			rayCasterThread.start();
			threads.add(rayCasterThread);
		}
		imagePartitioner = new HorizontalImagePartitioner();
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
		if ( thereAreDeadThreads() )
			return null;

		image = new BufferedImage(width, height, imageType);

		List<Rectangle> portions = imagePartitioner.getPortions(threads.size(),width,height);
		
		for(int i=0; i < threads.size() ; i++){
			threads.get(i).setPortion(portions.get(i), width, height);
		}

		try { cb.await(); } catch (InterruptedException e) { e.printStackTrace(); } catch (BrokenBarrierException e) { e.printStackTrace(); }

		try { cb.await(); } catch (InterruptedException e) { e.printStackTrace(); } catch (BrokenBarrierException e) { e.printStackTrace(); }
		return image;
	}

	private boolean thereAreDeadThreads() {
		for (RayCasterThread rct:threads){
			if ( !rct.isAlive() )
				return true;
		}
		return false;
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
		threads.get(0).interrupt();
	}
	
	/**
	 * Finalize method
	 */
	protected void finalize() throws Throwable {
		this.cleanup();
		super.finalize();
	}
	
}