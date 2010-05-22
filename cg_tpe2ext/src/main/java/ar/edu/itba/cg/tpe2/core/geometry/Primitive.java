package ar.edu.itba.cg.tpe2.core.geometry;

import java.awt.Color;
import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;

import ar.edu.itba.cg.tpe2.core.light.Light;
import ar.edu.itba.cg.tpe2.core.scene.Scene;
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
	public void rotatex(float angle){
		Matrix4f m = new Matrix4f();
		m.setIdentity();
		m.m11 = (float)Math.cos(angle);
		m.m12 = (float)-Math.sin(angle);
		m.m21 = (float)Math.sin(angle);
		m.m22 = (float)Math.cos(angle);
		transformWith(m);
	}
	
	/**
	 * Rotate object in Y axis 
	 * 
	 */
	public void rotatey(float angle){
		Matrix4f m = new Matrix4f();
		m.setIdentity();
		m.m00 = (float)Math.cos(angle);
		m.m02 = (float)Math.sin(angle);
		m.m20 = (float)-Math.sin(angle);
		m.m22 = (float)Math.cos(angle);
		transformWith(m);
	}
	
	/**
	 * Rotate object in Z axis 
	 * 
	 */
	public void rotatez(float angle){
		Matrix4f m = new Matrix4f();
		m.setIdentity();
		m.m00 = (float)Math.cos(angle);
		m.m01 = (float)-Math.sin(angle);
		m.m10 = (float)Math.sin(angle);
		m.m11 = (float)Math.cos(angle);
		transformWith(m);
	}
	
	/**
	 * Scale primitive in X axis
	 * 
	 * 
	 */
	public void scalex(float scale){
		Matrix4f m = new Matrix4f();
		m.setIdentity();
		m.m00 = scale;
		transformWith(m);
	}
	
	/**
	 * Scale primitive in Y axis
	 * 
	 */
	public void scaley(float scale){
		Matrix4f m = new Matrix4f();
		m.setIdentity();
		m.m11 = scale;
		transformWith(m);
	}
	
	/**
	 * Scale primitive in Z axis
	 * 
	 */
	public void scalez(float scale){
		Matrix4f m = new Matrix4f();
		m.setIdentity();
		m.m22 = scale;
		transformWith(m);
	}
	
	/**
	 * Scale primitive in U
	 * 
	 */
	public void scaleu(float scale){
		Matrix4f m = new Matrix4f();
		m.setIdentity();
		m.m00 = scale;
		m.m11 = scale;
		m.m22 = scale;
		// TODO Verificar que esto es asi
		transformWith(m);
	}
	
	public void translate(Point3f trans){
		Matrix4f m = new Matrix4f();
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
	public abstract float[] getBoundaryPoints();
	
	/**
	 * Get the intersection between the figure and a ray
	 */
	public abstract Point3f intersect(Ray ray);
	
	public abstract float [] getUV(Point3f point);
	
	public abstract Vector3 getNormalAt(Point3f p, Point3f from);
	
	public abstract void transformWith(Matrix4f m);
	
	public Color getColorAt(Point3f aPoint, List<Light> lights, Ray viewRay, Scene scene) {
		if (shader != null)
			return this.shader.getColorAt(aPoint, this, lights, viewRay, scene);
		else
			return Color.WHITE;//FIXME
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
