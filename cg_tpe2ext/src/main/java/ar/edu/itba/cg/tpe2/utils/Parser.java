package ar.edu.itba.cg.tpe2.utils;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Point3i;
import javax.vecmath.Vector3f;

import ar.edu.itba.cg.tpe2.core.camera.Camera;
import ar.edu.itba.cg.tpe2.core.camera.Pinhole;
import ar.edu.itba.cg.tpe2.core.camera.Thinlens;
import ar.edu.itba.cg.tpe2.core.colors.Diffuse;
import ar.edu.itba.cg.tpe2.core.colors.Specular;
import ar.edu.itba.cg.tpe2.core.geometry.Plane;
import ar.edu.itba.cg.tpe2.core.geometry.Primitive;
import ar.edu.itba.cg.tpe2.core.geometry.Sphere;
import ar.edu.itba.cg.tpe2.core.geometry.Transform;
import ar.edu.itba.cg.tpe2.core.geometry.Triangle;
import ar.edu.itba.cg.tpe2.core.light.Light;
import ar.edu.itba.cg.tpe2.core.light.PointLight;
import ar.edu.itba.cg.tpe2.core.scene.Image;
import ar.edu.itba.cg.tpe2.core.scene.Scene;
import ar.edu.itba.cg.tpe2.core.shader.Constant;
import ar.edu.itba.cg.tpe2.core.shader.Glass;
import ar.edu.itba.cg.tpe2.core.shader.Mirror;
import ar.edu.itba.cg.tpe2.core.shader.Phong;
import ar.edu.itba.cg.tpe2.core.shader.Shader;
import ar.edu.itba.cg.tpe2.core.shader.procedural.Fire;
import ar.edu.itba.cg.tpe2.core.shader.procedural.Marble;
import ar.edu.itba.cg.tpe2.core.shader.procedural.Organic;
import ar.edu.itba.cg.tpe2.core.shader.procedural.Stone;
import ar.edu.itba.cg.tpe2.core.shader.procedural.Water;
import ar.edu.itba.cg.tpe2.core.shader.procedural.Wood;

public class Parser {

	private static final float LIGHT_SPACING = 0.1f;
	public static final boolean SOFT_SHADOWS = true;
	public static final float LIGHT_COUNT = 27;
	private static final int DEFAULT_RESOLUTION = 9;
	private String filename = null;
	private FileParser aParser;
	private Scene scene;
	private HashMap<String, Shader> shaders;
	
	public Parser(String filename) {
		if (filename == null)
			throw new IllegalArgumentException("Filename or scene cant be null");
		this.filename = filename;
	}
	
	
	/**
	 * Start parsing the file
	 * 
	 * @return
	 * @throws IOException 
	 */
	public Scene parse() throws IOException {
		
		this.scene = new Scene();
		this.shaders = new HashMap<String, Shader>();
		String current;
		aParser = new FileParser(this.filename);

		System.out.println("Parsing...");
		
		while(true){
			current = aParser.getNextToken();
			String back = current;
			if(current == null){
				// se termino el archivo
				return scene;
			}
			//System.out.println(current);
			if(current.equalsIgnoreCase("image")){
//				System.out.println("Reading the image settings...");
				this.parseImageSettings();
			} else if (current.equalsIgnoreCase("camera")){
//				System.out.println("Reading the camera settings...");
				this.parseCameraSettings();
			} else if (current.equalsIgnoreCase("shader")){
//				System.out.println("Reading Shader settings...");
				this.parseShaderSettings();
			} else if (current.equalsIgnoreCase("light")){
//				System.out.println("Reading Light Settings...");
				this.parseLightSettings();
			} else if (current.equalsIgnoreCase("object")){
//				System.out.println("Reading Object Settings...");
				this.parseObjectSettings();
			} else if(current.equals("transform")){
				Transform aTrans = this.parseTransform();
			} else if(current.equals("trace-depths")){
				do {
					current = aParser.getNextToken();
				}while (!current.equals("}"));
			} else if (!back.equals("trace-depths") && (current.equals("spec") || current.equals("diff") || current.equals("color"))){
				Specular aSpec = this.parseSpec(current);
			}
		}
	}

	

	private void parseObjectSettings() throws IOException {
		String current;
		String shader_name = "";
		String type = "";
		String object_name = "";
		List<Point3f> points = new LinkedList<Point3f>();
		List<Point3i> triangles = new LinkedList<Point3i>();
		List<Vector3f> normals = new LinkedList<Vector3f>();
		List<Point2f> uvs = new LinkedList<Point2f>();
		Point3f center = null;
		Vector3f normal = null;
		Float radious = null;
		Integer pts_qty = 0, tri_qty = 0;
		Transform aTrans = null;
		do{
			current = aParser.getNextToken();
			if( current.equals("shader")){
				shader_name = aParser.getNextToken();
			} else if (current.equals("type")){
				type = aParser.getNextToken();
				if(type.equals("plane")){
					do{
						current = aParser.getNextToken();
						if(current.equals("p")){
							points.add(new Point3f(new Float(aParser.getNextToken()), 
									new Float(aParser.getNextToken()), 
									new Float(aParser.getNextToken())));
						} else if(current.equals("n")){
							normal = new Vector3f(new Float(aParser.getNextToken()), 
									new Float(aParser.getNextToken()), 
									new Float(aParser.getNextToken()));
						} else if (current.equals("transform")){
							aTrans = this.parseTransform();
						}
					}while(!current.equals("}"));
				} else if (type.equals("sphere")){
					do{
						current = aParser.getNextToken();
						if(current.equals("name")){
							object_name = aParser.getNextToken();
						} else if(current.equals("c")){
							center = new Point3f(new Float(aParser.getNextToken()), 
									new Float(aParser.getNextToken()), 
									new Float(aParser.getNextToken()));
						} else if(current.equals("r")){
							radious = new Float(aParser.getNextToken());
						} else if (current.equals("transform")){
							aTrans = this.parseTransform();
						}
					}while (!current.equals("}"));
				} else if (type.equals("generic-mesh")){
					do{
						current = aParser.getNextToken();
						if(current.equals("name")){
							object_name = aParser.getNextToken();
						} else if(current.equals("points")){
							pts_qty = new Integer(aParser.getNextToken());
							for(int i = 0; i < pts_qty; i++){
								points.add(new Point3f(new Float(aParser.getNextToken()), 
										new Float(aParser.getNextToken()), 
										new Float(aParser.getNextToken())));
							}
						} else if(current.equals("triangles")){
							tri_qty = new Integer(aParser.getNextToken());
							for(int i = 0; i < tri_qty; i++){
								// TODO Get point according to the index
								triangles.add(new Point3i(new Integer(aParser.getNextToken()),
										new Integer(aParser.getNextToken()),
										new Integer(aParser.getNextToken())));
							}
							
						} else if (current.equals("transform")){
							aTrans = this.parseTransform();
						} else if (current.equals("normals") && !aParser.peekNextToken().equals("none")){
							String normals_type = aParser.getNextToken();
							for(int i = 0; i < pts_qty; i++){
								normals.add(new Vector3f(new Float(aParser.getNextToken()), 
										new Float(aParser.getNextToken()), 
										new Float(aParser.getNextToken())));
							}
						} else if (current.equals("uvs") && !aParser.peekNextToken().equals("none")){
							String uvs_type = aParser.getNextToken();
							for(int i = 0; i < pts_qty; i++){
								uvs.add(new Point2f(new Float(aParser.getNextToken()), 
										new Float(aParser.getNextToken())));
							}
						}
					}while(!current.equals("}"));
				} else if (type.equals("mesh")){
					// Es un MESH
					do {
						current = aParser.getNextToken();
						if(current.equals("name")){
							object_name = aParser.getNextToken();
						} else if(!current.equals("}")){
							pts_qty = new Integer(current);
							tri_qty = new Integer(aParser.getNextToken());
							for(int i = 0; i < pts_qty; i++){
								if(aParser.getNextToken().equals("v")){
									points.add(new Point3f(new Float(aParser.getNextToken()), 
											new Float(aParser.getNextToken()), 
											new Float(aParser.getNextToken())));
									normals.add(new Vector3f(new Float(aParser.getNextToken()), 
											new Float(aParser.getNextToken()), 
											new Float(aParser.getNextToken())));
									uvs.add(new Point2f(new Float(aParser.getNextToken()), 
											new Float(aParser.getNextToken())));
								}
							}
							for(int i = 0; i < tri_qty; i++){
								// TODO Get point according to the index
								if(aParser.getNextToken().equals("t")){
									triangles.add(new Point3i(new Integer(aParser.getNextToken()),
											new Integer(aParser.getNextToken()),
											new Integer(aParser.getNextToken())));
								}
							}
							
						}
					} while(!current.equals("}"));
				} else if (type.equals("box")){
						do{
						current = aParser.getNextToken();
						if(current.equals("name")){
							object_name = aParser.getNextToken();
						} else if(current.equals("shader")){
							shader_name = aParser.getNextToken();
						} else if (current.equals("transform")){
							aTrans = this.parseTransform();
						}
					}while (!current.equals("}"));
				} else {
					// It's none of the accepted types of Shader
					do{
						current = aParser.getNextToken();
						// We consume all the discarded tokens.
						if(current.equals("transform")){
							aTrans = this.parseTransform();
						}
					}while(!current.equals("}"));
					return;
				}
			} else if (current.equals("transform")){
				aTrans = this.parseTransform();
			}
		}while(!current.equals("}"));
		
		
		//Aca armamos los objetos
		
		if(type.equals("generic-mesh")  || type.equals("mesh")){
			//Armo el generic-mesh, un listado de triangulos.
			LinkedList<Primitive> triangleList = new LinkedList<Primitive>();
			for(int i = 0; i < triangles.size(); i++){
				if(normals.size()>0){
					if(uvs.size()>0){
						//Tengo Normals y UVs
						triangleList.add(new Triangle(object_name, shaders.get(shader_name), points.get(triangles.get(i).x),
								points.get(triangles.get(i).y), points.get(triangles.get(i).z), normals.get(triangles.get(i).x),
								normals.get(triangles.get(i).y), normals.get(triangles.get(i).z), uvs.get(triangles.get(i).x), uvs.get(triangles.get(i).y), uvs.get(triangles.get(i).z), aTrans));
					} else {
						//Tengo solo Normals
						triangleList.add(new Triangle(object_name, shaders.get(shader_name), points.get(triangles.get(i).x),
								points.get(triangles.get(i).y), points.get(triangles.get(i).z), normals.get(triangles.get(i).x),
								normals.get(triangles.get(i).y), normals.get(triangles.get(i).z), aTrans));
					}
				} else if(uvs.size()>0){
					//Tengo solo UVs
					triangleList.add(new Triangle(object_name, shaders.get(shader_name), points.get(triangles.get(i).x),
							points.get(triangles.get(i).y), points.get(triangles.get(i).z), 
							uvs.get(triangles.get(i).x), uvs.get(triangles.get(i).y), uvs.get(triangles.get(i).z), aTrans));
				} else {
					//No tengo ninguno de los dos
					triangleList.add(new Triangle(object_name, shaders.get(shader_name), points.get(triangles.get(i).x),
							points.get(triangles.get(i).y), points.get(triangles.get(i).z), aTrans));
				}
			}
			scene.add(triangleList);
//			for(int i = 0; i < triangleList.size(); i++){
//				System.out.println(triangleList.get(i).toString());
//			}
		} else if (type.equals("sphere")){
			//Aca armamos la esfera
			LinkedList<Primitive> sphereList = new LinkedList<Primitive>();
			sphereList.add(new Sphere(object_name, shaders.get(shader_name), center, radious, aTrans));
			scene.add(sphereList);
//			for(int i = 0; i < sphereList.size(); i++){
//				System.out.println(sphereList.get(i).toString());
//			}
		} else if(type.equals("plane")){
			LinkedList<Primitive> planeList = new LinkedList<Primitive>();
			if(points.size()>1){
				//Es la sintaxis de 3 puntos
				planeList.add(new Plane(object_name, shaders.get(shader_name), points.get(0), points.get(1), points.get(2), aTrans));
			} else {
				planeList.add(new Plane(object_name, shaders.get(shader_name), points.get(0), normal, aTrans));
			}
			scene.addPlanes(planeList);
//			for(int i = 0; i < planeList.size(); i++)
//				System.out.println(planeList.get(i).toString());
		} else if (type.equals("box")){
			//Creo los 12 triangulos
			LinkedList<Primitive> triangleList = new LinkedList<Primitive>();
			
			triangleList.add(new Triangle(object_name, shaders.get(shader_name), new Point3f(-0.5f,+0.5f,-0.5f), new Point3f(+0.5f,+0.5f,-0.5f), new Point3f(+0.5f,+0.5f,+0.5f), new Point2f(0.0001f,0.0001f), new Point2f(0.0001f,1), new Point2f(1,1), aTrans));
			triangleList.add(new Triangle(object_name, shaders.get(shader_name), new Point3f(-0.5f,+0.5f,-0.5f), new Point3f(-0.5f,+0.5f,+0.5f), new Point3f(+0.5f,+0.5f,+0.5f), new Point2f(0.0001f,0.0001f), new Point2f(1,0.0001f), new Point2f(1,1), aTrans));

			triangleList.add(new Triangle(object_name, shaders.get(shader_name), new Point3f(+0.5f,+0.5f,+0.5f), new Point3f(+0.5f,+0.5f,-0.5f), new Point3f(+0.5f,-0.5f,-0.5f), new Point2f(0.0001f,0.0001f), new Point2f(0.0001f,1), new Point2f(1,1), aTrans));
			triangleList.add(new Triangle(object_name, shaders.get(shader_name), new Point3f(+0.5f,+0.5f,+0.5f), new Point3f(+0.5f,-0.5f,+0.5f), new Point3f(+0.5f,-0.5f,-0.5f), new Point2f(0.0001f,0.0001f), new Point2f(1,0.0001f), new Point2f(1,1), aTrans));
			
			triangleList.add(new Triangle(object_name, shaders.get(shader_name), new Point3f(-0.5f,+0.5f,+0.5f), new Point3f(+0.5f,+0.5f,+0.5f), new Point3f(+0.5f,-0.5f,+0.5f), new Point2f(0.0001f,0.0001f), new Point2f(0.0001f,1), new Point2f(1,1), aTrans));
			triangleList.add(new Triangle(object_name, shaders.get(shader_name), new Point3f(-0.5f,+0.5f,+0.5f), new Point3f(-0.5f,-0.5f,+0.5f), new Point3f(+0.5f,-0.5f,+0.5f), new Point2f(0.0001f,0.0001f), new Point2f(1,0.0001f), new Point2f(1,1), aTrans));
			
			triangleList.add(new Triangle(object_name, shaders.get(shader_name), new Point3f(-0.5f,+0.5f,-0.5f), new Point3f(-0.5f,+0.5f,+0.5f), new Point3f(-0.5f,-0.5f,+0.5f), new Point2f(0.0001f,0.0001f), new Point2f(0.0001f,1), new Point2f(1,1), aTrans));
			triangleList.add(new Triangle(object_name, shaders.get(shader_name), new Point3f(-0.5f,+0.5f,-0.5f), new Point3f(-0.5f,-0.5f,-0.5f), new Point3f(-0.5f,-0.5f,+0.5f), new Point2f(0.0001f,0.0001f), new Point2f(1,0.0001f), new Point2f(1,1), aTrans));
			
			triangleList.add(new Triangle(object_name, shaders.get(shader_name), new Point3f(+0.5f,-0.5f,-0.5f), new Point3f(-0.5f,-0.5f,-0.5f), new Point3f(-0.5f,-0.5f,+0.5f), new Point2f(0.0001f,0.0001f), new Point2f(0.0001f,1), new Point2f(1,1), aTrans));
			triangleList.add(new Triangle(object_name, shaders.get(shader_name), new Point3f(+0.5f,-0.5f,-0.5f), new Point3f(+0.5f,-0.5f,+0.5f), new Point3f(-0.5f,-0.5f,+0.5f), new Point2f(0.0001f,0.0001f), new Point2f(1,0.0001f), new Point2f(1,1), aTrans));
			
			triangleList.add(new Triangle(object_name, shaders.get(shader_name), new Point3f(+0.5f,+0.5f,-0.5f), new Point3f(-0.5f,+0.5f,-0.5f), new Point3f(-0.5f,-0.5f,-0.5f), new Point2f(0.0001f,0.0001f), new Point2f(0.0001f,1), new Point2f(1,1), aTrans));
			triangleList.add(new Triangle(object_name, shaders.get(shader_name), new Point3f(+0.5f,+0.5f,-0.5f), new Point3f(+0.5f,-0.5f,-0.5f), new Point3f(-0.5f,-0.5f,-0.5f), new Point2f(0.0001f,0.0001f), new Point2f(1,0.0001f), new Point2f(1,1), aTrans));
			
			scene.add(triangleList);
		}
		
	}
	
	private Specular parseSpec( String type ) throws IOException{
		String current, color = null;
		Specular aSpec = new Specular();
		float __a, __b, __c;
		int flag = 0;
		// We discard the first {
		if(aParser.peekNextToken().equals("{")){
			current = aParser.getNextToken();
			current = aParser.getNextToken() + aParser.getNextToken();
			int length = current.length();
			color = current.substring(1, length-1);
		} else flag = 1;

		__a = new Float(aParser.getNextToken());
		__b = new Float(aParser.getNextToken());
		__c = new Float(aParser.getNextToken());
		// Now we discard the last }
		if(aParser.peekNextToken().equals("}") && flag == 0)
			current = aParser.getNextToken();
		// We check if there's Specularity
		if(type.equals("spec")){
			aSpec.setSpecularity(new Integer(aParser.getNextToken()));
		}
		
		aSpec.setColor(new Color(__a, __b, __c));
		aSpec.setColorType(color);
		
		//System.out.println(aSpec.toString());

		
		return aSpec;
	}
	
	private Transform parseTransform() throws IOException{
		String current;
		Transform aTrans = new Transform();
		Point3f translate;
		do{
			current = aParser.getNextToken();
			
			if(current.equals("translate")){
				translate = new Point3f(new Float(aParser.getNextToken()),
						new Float(aParser.getNextToken()),
						new Float(aParser.getNextToken()));
				aTrans.setTranslate(translate);
			} else if (current.equals("rotatex")){
				aTrans.setRotatex(new Float(aParser.getNextToken()));
			} else if (current.equals("rotatey")){
				aTrans.setRotatey(new Float(aParser.getNextToken()));
			} else if (current.equals("rotatez")){
				aTrans.setRotatez(new Float(aParser.getNextToken()));
			} else if (current.equals("scalex")) {
				aTrans.setScalex(new Float(aParser.getNextToken()));
			} else if (current.equals("scaley")) {
				aTrans.setScaley(new Float(aParser.getNextToken()));
			} else if (current.equals("scalez")) {
				aTrans.setScalez(new Float(aParser.getNextToken()));
			} else if (current.equals("scaleu")) {
				aTrans.setScaleu(new Float(aParser.getNextToken()));
			}
			
		}while(!current.equals("}"));
		
		//System.out.println(aTrans.toString());
		
		return aTrans;
		
	}


	private void parseLightSettings() throws IOException {
		String current, type = null, light_color;
		Float __a, __b, __c, power = -1f;
		Point3f light_pos = null;
		Specular aSpec = null;
		do{
			current = aParser.getNextToken();
			if(current.equals("type")){
				type = aParser.getNextToken();
			} else if (current.equals("color")){
				aSpec = this.parseSpec(current);
			}else if (current.equals("power")){
				power = new Float(aParser.getNextToken());
			}else if (current.equals("p")){
				light_pos = new Point3f(new Float(aParser.getNextToken()),
						new Float(aParser.getNextToken()),
						new Float(aParser.getNextToken()));
			}
		}while(!current.equals("}"));
		
		if(!type.equals("point")){
			return;
		}
//		
//		System.out.println("Type of Light: " + type);
//		System.out.println("Color: " + aSpec.getColorType() + " " + aSpec.getColor());
//		System.out.println("Power: " + power);
//		System.out.println("Light Position: " + light_pos.toString());
//		// Build Object Light
		PointLight pl = new PointLight(type,aSpec,power,light_pos);
		if ( SOFT_SHADOWS ){
			List<Light> softShadowLights = getSoftShadowLights(pl);
			for (Light l:softShadowLights)
				scene.addLight(l);
		}else
			scene.addLight(pl);
	}

	private List<Light> getSoftShadowLights(PointLight pl){
		List<Light> lights = new ArrayList<Light>();
		float totalPower = pl.getPower();
		Point3f p = pl.getP();
		p.sub(new Point3f(-LIGHT_SPACING,-LIGHT_SPACING,-LIGHT_SPACING));
		Color color = divideColor(pl.getASpec().getColor(),LIGHT_COUNT);
		Specular specular = new Specular("rgb",color);
		for(int i = 0; i < 3 ; i++){
			Point3f px = new Point3f(p);
				px.add(new Point3f(i*LIGHT_SPACING,0,0));
				for(int j = 0; j < 3 ; j++){
					Point3f py = new Point3f(px);
					py.add(new Point3f(0,j*LIGHT_SPACING,0));
					for(int k = 0; k < 3 ; k++){
						Point3f pz = new Point3f(py);
						pz.add(new Point3f(0,0,k*LIGHT_SPACING));
						lights.add(new PointLight("light "+i+" "+j+" "+k,specular,totalPower,pz));
					}
				}
		}
		return lights;
	}

	private Color divideColor(Color color,float n) {
		float[] colorComponents = color.getRGBColorComponents(null);
		return new Color(colorComponents[0]/n,colorComponents[1]/n,colorComponents[2]/n);
	}
	
	private void parseShaderSettings() throws IOException {
		String current;
		String name = null, type = null, texture_path = null;
		String shader_color = null, abs_color = null;
		Float __a = -1f, __b = -1f, __c = -1f, eta = -1f, abs_dst = -1f, abs__a = -1f, abs__b = -1f, abs__c = -1f;
		Integer samples = 1;
		Specular aSpec = null, absSpec = null;
		Diffuse aDiff = null;
		Color aColor = null;
		int depth = DEFAULT_RESOLUTION;
		Diffuse initDiff = new Diffuse(0, 0, 0);
		Diffuse finalDiff = new Diffuse(1, 1, 1);
		
		do{
			current = aParser.getNextToken();
			if(current.equals("name")){
				name = aParser.getNextToken();
			} else if (current.equals("type")){
				type = aParser.getNextToken();
				// Depending on the type of Shader...
				if(type.equals("phong")){
					do{
						current = aParser.getNextToken();
						// TODO what if current is diff { "sRGB nonlinear" 0.800 0.800 0.800 } ??
						if(current.equals("texture")){
							texture_path = aParser.getNextToken();
							int length = texture_path.length();
							texture_path = texture_path.substring(1, length-1);
							aDiff = new Diffuse(texture_path);
						} else if(current.equals("spec")){
							aSpec = this.parseSpec(current);
						} else if (current.equals("samples")){
							samples = new Integer(aParser.getNextToken());
						} else if (current.equals("diff")){
							aDiff = this.parseDiff(current);
						}
					}while(!current.equals("}"));
				} else if (type.equals("constant")){
					do{
						current = aParser.getNextToken();
						if (current.equals("color")){
							aColor = this.parseColor(current);
						}
					}while(!current.equals("}"));
				} else if (type.equals("glass")){
					do{
						current = aParser.getNextToken();
						if(current.equals("eta")){
							eta = new Float(aParser.getNextToken());
						} else if(current.equals("color")){
							aSpec = this.parseSpec(current);
						} else if (current.equals("absorbtion.distance")){
							abs_dst = new Float(aParser.getNextToken());
						} else if (current.equals("absorbtion.color")){
							absSpec = this.parseSpec(current);
						}
					}while(!current.equals("}"));
				} else if (type.equals("mirror")){
					do{
						current = aParser.getNextToken();
						if(current.equals("refl")){
							aSpec = this.parseSpec(current);
						}
					}while(!current.equals("}"));
				} else if (type.equals("organic") ){
					do{
						current = aParser.getNextToken();
						if(current.equals("depth")){
							depth = new Integer(aParser.getNextToken());
						} else if (current.equals("diffuse_initial")){
							initDiff = this.parseDiff(current);
						} else if (current.equals("diffuse_final")){
							finalDiff = this.parseDiff(current);
						}
					}while(!current.equals("}"));
				} else if (type.equals("water") ){
					do{
						current = aParser.getNextToken();
						if(current.equals("depth")){
							depth = new Integer(aParser.getNextToken());
						} else if (current.equals("diffuse_initial")){
							initDiff = this.parseDiff(current);
						} else if (current.equals("diffuse_final")){
							finalDiff = this.parseDiff(current);
						}
					}while(!current.equals("}"));
				} else if (type.equals("wood") ){
					do{
						current = aParser.getNextToken();
						if(current.equals("depth")){
							depth = new Integer(aParser.getNextToken());
						} else if (current.equals("diffuse_initial")){
							initDiff = this.parseDiff(current);
						} else if (current.equals("diffuse_final")){
							finalDiff = this.parseDiff(current);
						}
					}while(!current.equals("}"));
				} else if (type.equals("marble") ){
					do{
						current = aParser.getNextToken();
						if(current.equals("depth")){
							depth = new Integer(aParser.getNextToken());
						} else if (current.equals("diffuse_initial")){
							initDiff = this.parseDiff(current);
						} else if (current.equals("diffuse_final")){
							finalDiff = this.parseDiff(current);
						}
					}while(!current.equals("}"));
				} else if (type.equals("fire") ){
					do{
						current = aParser.getNextToken();
						if(current.equals("depth")){
							depth = new Integer(aParser.getNextToken());
						} else if (current.equals("diffuse_initial")){
							initDiff = this.parseDiff(current);
						} else if (current.equals("diffuse_final")){
							finalDiff = this.parseDiff(current);
						}
					}while(!current.equals("}"));
				} else if (type.equals("stone") ){
					do{
						current = aParser.getNextToken();
						if(current.equals("depth")){
							depth = new Integer(aParser.getNextToken());
						} else if (current.equals("diffuse_initial")){
							initDiff = this.parseDiff(current);
						} else if (current.equals("diffuse_final")){
							finalDiff = this.parseDiff(current);
						}
					}while(!current.equals("}"));
				} else {
					// It's none of the accepted types of Shader
					do{
						current = aParser.getNextToken();
						if(current.equals("color") || current.equals("spec") || current.equals("diff")){
							aSpec = this.parseSpec(current);
						}
					}while(!current.equals("}"));
					return;
				}
			}
		}while(!current.equals("}"));
		
		Shader sh = null; 
		
		if(type.equals("mirror")){
			sh = new Mirror(name, type, aSpec);
		} else if (type.equals("phong")){
			sh = new Phong(name, type, aDiff, samples, aSpec);
		} else if (type.equals("constant")){
			sh = new Constant(name, type, aColor);
		} else if (type.equals("glass")){
			sh = new Glass(name, type, eta.floatValue(), abs_dst.floatValue(), aSpec, absSpec);
		} else if (type.equals("organic")){
			sh = new Organic(name, type, depth, initDiff, finalDiff);
		} else if (type.equals("stone")){
			sh = new Stone(name, type, depth, initDiff, finalDiff);
		} else if (type.equals("fire")){
			sh = new Fire(name, type, depth, initDiff, finalDiff);
		} else if (type.equals("water")){
			sh = new Water(name, type, depth, initDiff, finalDiff);
		} else if (type.equals("wood")){
			sh = new Wood(name, type, depth, initDiff, finalDiff);
		} else if (type.equals("marble")){
			sh = new Marble(name, type, depth, initDiff, finalDiff);
		}

		if ( sh != null )
			shaders.put(sh.getName(), sh);
	}


	private Diffuse parseDiff(String type) throws IOException {
		String current, color = null;
		Diffuse aDiff = null;
		float __a, __b, __c;
		int flag = 0;
		// We discard the first {
		if(aParser.peekNextToken().equals("{")){
			current = aParser.getNextToken();
			current = aParser.getNextToken() + aParser.getNextToken();
			int length = current.length();
			color = current.substring(1, length-1);
		} else flag = 1;

		__a = new Float(aParser.getNextToken());
		__b = new Float(aParser.getNextToken());
		__c = new Float(aParser.getNextToken());
		// Now we discard the last }
		if(aParser.peekNextToken().equals("}") && flag == 0)
			current = aParser.getNextToken();
		
		return new Diffuse(__a, __b, __c);
	}

	
	private Color parseColor(String type) throws IOException {
		String current, color = null;
		Diffuse aDiff = null;
		float __a, __b, __c;
		int flag = 0;
		// We discard the first {
		if(aParser.peekNextToken().equals("{")){
			current = aParser.getNextToken();
			current = aParser.getNextToken() + aParser.getNextToken();
			int length = current.length();
			color = current.substring(1, length-1);
		} else flag = 1;
		
		__a = new Float(aParser.getNextToken());
		__b = new Float(aParser.getNextToken());
		__c = new Float(aParser.getNextToken());
		// Now we discard the last }
		if(aParser.peekNextToken().equals("}") && flag == 0)
			current = aParser.getNextToken();
		
		return new Color(__a, __b, __c);
	}
	

	private void parseCameraSettings() throws IOException {
		Camera aCamera = null;
		String current;
		String type = null;
		Point3f eye = null, target = null;
		Vector3f up = null;
		Float fov = -1f, aspect = -1f;
		Float tl_fdist = null, tl_lensr = null;
		Point2f shift = null;
		do {
			current = aParser.getNextToken();
			if(current.equals("type")){
				type = aParser.getNextToken();
			} else if (current.equals("eye")){
				eye = new Point3f(new Float(aParser.getNextToken()), 
						new Float(aParser.getNextToken()), 
						new Float(aParser.getNextToken()));
			} else if (current.equals("target")){
				target = new Point3f(new Float(aParser.getNextToken()), 
						new Float(aParser.getNextToken()), 
						new Float(aParser.getNextToken()));
			} else if (current.equals("up")){
				up = new Vector3f(new Float(aParser.getNextToken()), 
						new Float(aParser.getNextToken()), 
						new Float(aParser.getNextToken()));
			} else if (current.equals("fov")){
				fov = new Float(aParser.getNextToken());
			} else if (current.equals("aspect")){
				aspect = new Float(aParser.getNextToken());
			} else if (current.equals("fdist")){
				tl_fdist = new Float(aParser.getNextToken());
			} else if (current.equals("lensr")){
				tl_lensr = new Float(aParser.getNextToken());
			} else if (current.equals("shift")){
				shift = new Point2f(new Float(aParser.getNextToken()),
						new Float(aParser.getNextToken()));
			}
		}while(!current.equals("}"));
		
		if(type.equals("pinhole")){
			//Armo una camara del tipo pinhole
			if(shift != null)
				aCamera = new Pinhole(type, eye, target, up, fov, aspect, shift);
			else aCamera = new Pinhole(type, eye, target, up, fov, aspect);
		} else if(type.equals("thinlens")){
			aCamera = new Thinlens(type, eye, target, up, fov.floatValue(), aspect.floatValue(), tl_fdist.floatValue(), tl_lensr.floatValue());
		}
		
		scene.setCamera(aCamera);
		
//		System.out.println(aCamera.toString());

		
	}


	private void parseImageSettings() throws NumberFormatException, IOException {
		String current;
		Integer width = -1, height = -1;
		Integer aa_min = -1, aa_max = -1;
		Integer samples = 1;
		Integer bucket_size = -1;
		String bucket_type = null;
		do{
			current = aParser.getNextToken();
			if(current.equals("resolution")){
				width = new Integer(aParser.getNextToken());
				height = new Integer(aParser.getNextToken());
			}else if(current.equals("aa")){
				aa_min = new Integer(aParser.getNextToken());
				aa_max = new Integer(aParser.getNextToken());
			}else if (current.equals("samples")){
				samples = new Integer(aParser.getNextToken());
			}else if (current.equals("bucket")){
				bucket_size = new Integer(aParser.getNextToken());
				bucket_type = aParser.getNextToken();
			}
		}while(!current.equals("}"));
		

		Image anImage;
		if(bucket_type == null){
			anImage = new Image(width, height, aa_min, aa_max, samples);
		} else {
			anImage = new Image(width, height, aa_min, aa_max, samples, bucket_size, bucket_type);
		}
		scene.setImage(anImage);
//		System.out.println(anImage.toString());
		
		
	}
	
	
	
	
}
