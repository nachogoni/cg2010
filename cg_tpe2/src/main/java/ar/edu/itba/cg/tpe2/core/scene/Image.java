package ar.edu.itba.cg.tpe2.core.scene;

public class Image {

	// Width
	// Height
	// Antialiasing: 
		// Min 
		// Max
	// Sampĺes
	// Bucket
		// Size
		// Type
	
	private int width, height;
	private int aa_min = 1, aa_max = 1;
	private int samples = 1;
	private int buckets;
	private String bucketType;
	
	public Image(int width, int height, int aaMin, int aaMax, int samples) {
		super();
		this.width = width;
		this.height = height;
		aa_min = aaMin;
		aa_max = aaMax;
		this.samples = samples;
	}
	public Image(int width, int height, int aaMin, int aaMax, int samples,
			int buckets, String bucketType) {
		super();
		this.width = width;
		this.height = height;
		aa_min = aaMin;
		aa_max = aaMax;
		this.samples = samples;
		this.buckets = buckets;
		this.bucketType = bucketType;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getAa_min() {
		return aa_min;
	}
	public int getAa_max() {
		return aa_max;
	}
	public int getSamples() {
		return samples;
	}
	public int getBuckets() {
		return buckets;
	}
	public String getBucketType() {
		return bucketType;
	}
	@Override
	public String toString() {
		return "Image [aa_max=" + aa_max + ", aa_min=" + aa_min
				+ ", bucketType=" + bucketType + ", buckets=" + buckets
				+ ", height=" + height + ", samples=" + samples + ", width="
				+ width + "]";
	}
	
	
	
}
