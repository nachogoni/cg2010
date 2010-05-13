package ar.edu.itba.cg.tpe2.core.geometry;

import java.awt.Color;

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
	
	public void translate(Point3d trans){
		Matrix4d m = new Matrix4d();
		m.setIdentity();
		m.m03 = trans.x;
		m.m13 = trans.y;
		m.m23 = trans.z;
		
		transformWith(m);
	}
	
	/**
	 * Funcion que retorna las cotas para los ejes 
	 * @return cotas en el orden: xmin, xmax, ymin, ymax, zmin, zmax
	 */
	public abstract double[] getBoundaryPoints();
	
	/**
	 * Get the intersection between the figure and a ray
	 */
	public abstract Point3d intersect(Ray ray);
	
	public abstract double [] getUV(Point3d point);
	
	public abstract Vector3 getNormalAt(Point3d p, Point3d from);
	
	public abstract void transformWith(Matrix4d m);
	
	public Color getColorAt(Point3d aPoint) {
		if (shader != null)
			return this.shader.getColorAt(aPoint, this);
		else
			return Color.WHITE;
	}
	
	public float getReflectionK(){
		if (shader != null)
			return this.shader.getReflectionK();
		else
			return 0.0f;
	}
	
	public float getRefractionK(){
		if (shader != null)
			return this.shader.getRefractionK();
		else
			return 0.0f;
	}
	
	
}