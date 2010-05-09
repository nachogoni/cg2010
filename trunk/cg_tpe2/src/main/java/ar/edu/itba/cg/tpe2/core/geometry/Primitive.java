package ar.edu.itba.cg.tpe2.core.geometry;

import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import ar.edu.itba.cg.tpe2.core.shader.Shader;

/**
 * Primitives 
 */
public abstract class Primitive {

	// Name
	String name;
	
	// Shader
	Shader shader;
	
	public Primitive(String name, Shader shader) {
		this.name = name;
		this.shader = shader;
		if ( shader != null )
			shader.setPrimitive(this);
	}
	
	/**
	 * Get primitive's name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set primitive's name
	 * 
	 * @return
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get primitive's shader
	 * 
	 * @return Shader
	 */
	public Shader getShader() {
		return shader;
	}

	/**
	 * Set primitive's shader
	 * 
	 */
	public void setShader(Shader shader) {
		this.shader = shader;
	}

	/**
	 * Rotate object in X axis 
	 * 
	 */
	public void rotatex(double angle){
		Matrix4d m = new Matrix4d();
		m.setIdentity();
		m.m11 = Math.cos(angle);
		m.m12 = -Math.sin(angle);
		m.m21 = Math.sin(angle);
		m.m22 = Math.cos(angle);
		transformWith(m);
	}
	
	/**
	 * Rotate object in Y axis 
	 * 
	 */
	public void rotatey(double angle){
		Matrix4d m = new Matrix4d();
		m.setIdentity();
		m.m00 = Math.cos(angle);
		m.m02 = Math.sin(angle);
		m.m20 = -Math.sin(angle);
		m.m22 = Math.cos(angle);
		transformWith(m);
	}
	
	/**
	 * Rotate object in Z axis 
	 * 
	 */
	public void rotatez(double angle){
		Matrix4d m = new Matrix4d();
		m.setIdentity();
		m.m00 = Math.cos(angle);
		m.m01 = -Math.sin(angle);
		m.m10 = Math.sin(angle);
		m.m11 = Math.cos(angle);
		transformWith(m);
	}
	
	/**
	 * Scale primitive in X axis
	 * 
	 * 
	 */
	public void scalex(double scale){
		Matrix4d m = new Matrix4d();
		m.setIdentity();
		m.m00 = scale;
		transformWith(m);
	}
	
	/**
	 * Scale primitive in Y axis
	 * 
	 */
	public void scaley(double scale){
		Matrix4d m = new Matrix4d();
		m.setIdentity();
		m.m11 = scale;
		transformWith(m);
	}
	
	/**
	 * Scale primitive in Z axis
	 * 
	 */
	public void scalez(double scale){
		Matrix4d m = new Matrix4d();
		m.setIdentity();
		m.m22 = scale;
		transformWith(m);
	}
	
	/**
	 * Scale primitive in U
	 * 
	 */
	public void scaleu(double scale){
		Matrix4d m = new Matrix4d();
		m.setIdentity();
		m.m00 = scale;
		m.m11 = scale;
		m.m22 = scale;
		// TODO Verificar que esto es asi
		transformWith(m);
	}
	
	/**
	 * Get the intersection between the figure and a ray
	 */
	public abstract Point3d intersect(Ray ray);

	/**
	 * Get the list of boundary points of the primitive
	 * 
	 * @return List of boundary points
	 */
	public abstract List<Point3d> getBoundaryPoints();
	
	public abstract double [] getUV(Point3d p);
	
	public abstract Vector3 getNormalAt(Point3d p);
	
	public abstract void transformWith(Matrix4d m);
}
