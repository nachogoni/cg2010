package ar.edu.itba.cg.tpe2.rayCaster;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.cg.tpe2.rayCaster.ImagePartitioners.HorizontalImagePartitioner;

/**
 * RayCaster creates an image form a scene viewed from a camera
 */
public class RayCaster {

	//TODO: pasarlo a los enums
	public static final int COLOR_MODE_RANDOM = 0;
	public static final int COLOR_MODE_ORDERED = 1;
	//TODO: pasarlo a los enums
	public static final int COLOR_VARIATION_LINEAR = 0;
	public static final int COLOR_VARIATION_LOG = 1;
	
	private List<RayCasterThread> threads;
	protected BufferedImage image = null;
	private IImagePartitioner imagePartitioner;
	private int bucketsSize;
	
	/**
	 * RayCaster constructor
	 * 
	 * @param scene Scene representation to work with
	 * @param camera Actual camera where the viewer is   
	 * @param numberOfThreads Number of threads to create
	 * @param numberOfBuckets Number of buckets to make 
	 * @param colorProvider Color mode: Random or Ordered
	 * @param colorVariation Color variation type: Linear or Log
	 */
	public RayCaster(Scene scene, Camera camera, int numberOfThreads, int numberOfBuckets, IColorProvider colorProvider, int colorVariation) {
		threads = new ArrayList<RayCasterThread>(numberOfThreads);
		this.bucketsSize = numberOfBuckets;
		for (int i=0; i < numberOfThreads; i++){
			RayCasterThread rayCasterThread = new RayCasterThread(scene, camera, this);
			rayCasterThread.setColorMode(colorProvider);
			rayCasterThread.setColorVariation(colorVariation);
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

		List<Rectangle> tasks = imagePartitioner.getPortions(bucketsSize,width,height);
//		cb = new CyclicBarrier(2);
		synchronized (RayCasterThread.class) {
			RayCasterThread.setImage(image);
			RayCasterThread.setTasks(tasks);
		}
		
//		while ( ! RayCasterThread.allTasksFinished() );
		try {
			synchronized (this) {
				this.wait();	
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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