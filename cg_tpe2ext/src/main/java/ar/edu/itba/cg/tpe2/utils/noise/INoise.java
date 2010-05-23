package ar.edu.itba.cg.tpe2.utils.noise;

public interface INoise {

	public abstract float noise(float x, float y, float z);

	public abstract float noise(float x, float y);

	public abstract int getDepth();
}
