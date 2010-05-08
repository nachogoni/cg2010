package ar.edu.itba.cg.tpe2.utils;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3i;
import javax.vecmath.Vector3d;

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
import ar.edu.itba.cg.tpe2.core.scene.Image;
import ar.edu.itba.cg.tpe2.core.scene.Scene;
import ar.edu.itba.cg.tpe2.core.shader.Glass;
import ar.edu.itba.cg.tpe2.core.shader.Mirror;
import ar.edu.itba.cg.tpe2.core.shader.Phong;
import ar.edu.itba.cg.tpe2.core.shader.Shader;

public class Parser {

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
		while(true){
			current = aParser.getNextToken();
			if(current == null){
				// se termino el archivo
				return scene;
			}
			System.out.println(current);
			if(current.equalsIgnoreCase("image")){
				System.out.println("Reading the image settings...");
				this.parseImageSettings();
			} else if (current.equalsIgnoreCase("camera")){
				System.out.println("Reading the camera settings...");
				this.parseCameraSettings();
			} else if (current.equalsIgnoreCase("shader")){
				System.out.println("Reading Shader settings...");
				this.parseShaderSettings();
			} else if (current.equalsIgnoreCase("light")){
				System.out.println("Reading Light Settings...");
				this.parseLightSettings();
			} else if (current.equalsIgnoreCase("object")){
				System.out.println("Reading Object Settings...");
				this.parseObjectSettings();
			} else {
				do{
					current = aParser.getNextToken();
					if(current.equals("transform")){
						Transform aTrans = this.parseTransform();
					} else if (current.equals("spec") || current.equals("diff") || current.equals("color")){
						Specular aSpec = this.parseSpec(current);
					}
				} while (!current.equals("}"));
			}
		}
	}

	

	private void parseObjectSettings() throws IOException {
		String current;
		String shader_name = "";
		String type = "";
		String object_name = "";
		List<Point3d> points = new LinkedList<Point3d>();
		List<Point3i> triangles = new LinkedList<Point3i>();
		List<Vector3d> normals = new LinkedList<Vector3d>();
		List<Point2d> uvs = new LinkedList<Point2d>();
		Point3d center = null;
		Vector3d normal = null;
		Double radious = null;
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
							points.add(new Point3d(new Double(aParser.getNextToken()), 
									new Double(aParser.getNextToken()), 
									new Double(aParser.getNextToken())));
						} else if(current.equals("n")){
							normal = new Vector3d(new Double(aParser.getNextToken()), 
									new Double(aParser.getNextToken()), 
									new Double(aParser.getNextToken()));
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
							center = new Point3d(new Double(aParser.getNextToken()), 
									new Double(aParser.getNextToken()), 
									new Double(aParser.getNextToken()));
						} else if(current.equals("r")){
							radious = new Double(aParser.getNextToken());
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
								points.add(new Point3d(new Double(aParser.getNextToken()), 
										new Double(aParser.getNextToken()), 
										new Double(aParser.getNextToken())));
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
								normals.add(new Vector3d(new Double(aParser.getNextToken()), 
										new Double(aParser.getNextToken()), 
										new Double(aParser.getNextToken())));
							}
						} else if (current.equals("uvs") && !aParser.peekNextToken().equals("none")){
							String uvs_type = aParser.getNextToken();
							for(int i = 0; i < pts_qty; i++){
								uvs.add(new Point2d(new Double(aParser.getNextToken()), 
										new Double(aParser.getNextToken())));
							}
						}
					}while(!current.equals("}"));
				} else if (current.equals("box")){
					
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
		
		if(type.equals("generic-mesh")){
			//Armo el generic-mesh, un listado de triangulos.
			LinkedList<Primitive> triangleList = new LinkedList<Primitive>();
			for(int i = 0; i < triangles.size(); i++){
				if(normals.size()>0){
					if(uvs.size()>0){
						//Tengo Normals y UVs
						triangleList.add(new Triangle(object_name, shaders.get(shader_name), points.get(triangles.get(i).x-1),
								points.get(triangles.get(i).y-1), points.get(triangles.get(i).z-1), normals.get(triangles.get(i).x-1),
								normals.get(triangles.get(i).y-1), normals.get(triangles.get(i).z-1), uvs.get(triangles.get(i).x-1), uvs.get(triangles.get(i).y-1), uvs.get(triangles.get(i).z-1)));
					} else {
						//Tengo solo Normals
						triangleList.add(new Triangle(object_name, shaders.get(shader_name), points.get(triangles.get(i).x-1),
								points.get(triangles.get(i).y-1), points.get(triangles.get(i).z-1), normals.get(triangles.get(i).x-1),
								normals.get(triangles.get(i).y-1), normals.get(triangles.get(i).z-1)));
					}
				} else if(uvs.size()>0){
					//Tengo solo UVs
					triangleList.add(new Triangle(object_name, shaders.get(shader_name), points.get(triangles.get(i).x-1),
							points.get(triangles.get(i).y-1), points.get(triangles.get(i).z-1), 
							uvs.get(triangles.get(i).x-1), uvs.get(triangles.get(i).y-1), uvs.get(triangles.get(i).z-1)));
				} else {
					//No tengo ninguno de los dos
					triangleList.add(new Triangle(object_name, shaders.get(shader_name), points.get(triangles.get(i).x-1),
							points.get(triangles.get(i).y-1), points.get(triangles.get(i).z-1)));
				}
			}
			scene.add(triangleList);
//			for(int i = 0; i < triangleList.size(); i++){
//				System.out.println(triangleList.get(i).toString());
//			}
		} else if (type.equals("sphere")){
			//Aca armamos la esfera
			LinkedList<Primitive> sphereList = new LinkedList<Primitive>();
			sphereList.add(new Sphere(object_name, shaders.get(shader_name), center, radious));
			scene.add(sphereList);
//			for(int i = 0; i < sphereList.size(); i++){
//				System.out.println(sphereList.get(i).toString());
//			}
		} else if(type.equals("plane")){
			LinkedList<Primitive> planeList = new LinkedList<Primitive>();
			if(points.size()>1){
				//Es la sintaxis de 3 puntos
				planeList.add(new Plane(object_name, shaders.get(shader_name), points.get(0), points.get(1), points.get(2)));
			} else {
				planeList.add(new Plane(object_name, shaders.get(shader_name), points.get(0), normal));
			}
			scene.add(planeList);
//			for(int i = 0; i < planeList.size(); i++)
//				System.out.println(planeList.get(i).toString());
		} else if (type.equals("box")){
			
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
		
		System.out.println(aSpec.toString());

		
		return aSpec;
	}
	
	private Transform parseTransform() throws IOException{
		String current;
		Transform aTrans = new Transform();
		Point3d translate;
		do{
			current = aParser.getNextToken();
			
			if(current.equals("translate")){
				translate = new Point3d(new Double(aParser.getNextToken()),
						new Double(aParser.getNextToken()),
						new Double(aParser.getNextToken()));
				aTrans.setTranslate(translate);
			} else if (current.equals("rotatex")){
				aTrans.setRotatex(new Integer(aParser.getNextToken()));
			} else if (current.equals("rotatey")){
				aTrans.setRotatey(new Integer(aParser.getNextToken()));
			} else if (current.equals("rotatez")){
				aTrans.setRotatez(new Integer(aParser.getNextToken()));
			} else if (current.equals("scalex")) {
				aTrans.setScalex(new Double(aParser.getNextToken()));
			} else if (current.equals("scaley")) {
				aTrans.setScaley(new Double(aParser.getNextToken()));
			} else if (current.equals("scalez")) {
				aTrans.setScalez(new Double(aParser.getNextToken()));
			} else if (current.equals("scaleu")) {
				aTrans.setScaleu(new Double(aParser.getNextToken()));
			}
			
		}while(!current.equals("}"));
		
		System.out.println(aTrans.toString());
		
		return aTrans;
		
	}


	private void parseLightSettings() throws IOException {
		String current, type = null, light_color;
		Float __a, __b, __c, power = -1f;
		Point3d light_pos = null;
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
				light_pos = new Point3d(new Double(aParser.getNextToken()),
						new Double(aParser.getNextToken()),
						new Double(aParser.getNextToken()));
			}
		}while(!current.equals("}"));
		
		if(!type.equals("point")){
			return;
		}
		
		System.out.println("Type of Light: " + type);
		System.out.println("Color: " + aSpec.getColorType() + " " + aSpec.getColor());
		System.out.println("Power: " + power);
		System.out.println("Light Position: " + light_pos.toString());
		// Build Object Light
	}


	private void parseShaderSettings() throws IOException {
		String current;
		String name = null, type = null, texture_path = null;
		String shader_color = null, abs_color = null;
		Double __a = -1d, __b = -1d, __c = -1d, eta = -1d, abs_dst = -1d, abs__a = -1d, abs__b = -1d, abs__c = -1d;
		Integer samples = -1;
		Specular aSpec = null, absSpec = null;
		Diffuse aDiff = null;
		
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
							texture_path = texture_path.substring(1, length);
							aDiff = new Diffuse(texture_path);
						} else if(current.equals("spec")){
							aSpec = this.parseSpec(current);
						} else if (current.equals("samples")){
							samples = new Integer(aParser.getNextToken());
						} else if (current.equals("diff")){
							aDiff = this.parseDiff(current);
						}
					}while(!current.equals("}"));
				} else if (type.equals("glass")){
					do{
						current = aParser.getNextToken();
						if(current.equals("eta")){
							eta = new Double(aParser.getNextToken());
						} else if(current.equals("color")){
							aSpec = this.parseSpec(current);
						} else if (current.equals("absorbtion.distance")){
							abs_dst = new Double(aParser.getNextToken());
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
					}while(current.equals("}"));
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
		
		if(type.equals("mirror")){
			Shader aMirror = new Mirror(name, type, aSpec);
			shaders.put(aMirror.getName(), aMirror);
		} else if (type.equals("phong")){
			Shader aPhong = new Phong(name, type, aDiff, samples, aSpec);
			shaders.put(aPhong.getName(), aPhong);
		} else if (type.equals("glass")){
			Shader aGlass = new Glass(name, type, eta.doubleValue(), abs_dst.doubleValue(), aSpec, absSpec);
			shaders.put(aGlass.getName(), aGlass);
		}
		
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


	private void parseCameraSettings() throws IOException {
		Camera aCamera = null;
		String current;
		String type = null;
		Point3d eye = null, target = null;
		Vector3d up = null;
		Double fov = -1d, aspect = -1d;
		Double tl_fdist = null, tl_lensr = null;
		Point2d shift = null;
		do {
			current = aParser.getNextToken();
			if(current.equals("type")){
				type = aParser.getNextToken();
			} else if (current.equals("eye")){
				eye = new Point3d(new Double(aParser.getNextToken()), 
						new Double(aParser.getNextToken()), 
						new Double(aParser.getNextToken()));
			} else if (current.equals("target")){
				target = new Point3d(new Double(aParser.getNextToken()), 
						new Double(aParser.getNextToken()), 
						new Double(aParser.getNextToken()));
			} else if (current.equals("up")){
				up = new Vector3d(new Double(aParser.getNextToken()), 
						new Double(aParser.getNextToken()), 
						new Double(aParser.getNextToken()));
			} else if (current.equals("fov")){
				fov = new Double(aParser.getNextToken());
			} else if (current.equals("aspect")){
				aspect = new Double(aParser.getNextToken());
			} else if (current.equals("fdist")){
				tl_fdist = new Double(aParser.getNextToken());
			} else if (current.equals("lensr")){
				tl_lensr = new Double(aParser.getNextToken());
			} else if (current.equals("shift")){
				shift = new Point2d(new Double(aParser.getNextToken()),
						new Double(aParser.getNextToken()));
			}
		}while(!current.equals("}"));
		
		if(type.equals("pinhole")){
			//Armo una camara del tipo pinhole
			if(shift != null)
				aCamera = new Pinhole(type, eye, target, up, fov, aspect, shift);
			else aCamera = new Pinhole(type, eye, target, up, fov, aspect);
		} else if(type.equals("thinlens")){
			aCamera = new Thinlens(type, eye, target, up, fov.doubleValue(), aspect.doubleValue(), tl_fdist.doubleValue(), tl_lensr.doubleValue());
		}
		
		scene.setaCamera(aCamera);
		
//		System.out.println(aCamera.toString());

		
	}


	private void parseImageSettings() throws NumberFormatException, IOException {
		String current;
		Integer width = -1, height = -1;
		Integer aa_min = -1, aa_max = -1;
		Integer samples = -1;
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
		scene.setAnImage(anImage);
//		System.out.println(anImage.toString());
		
		
	}
	
	
	
	
}
