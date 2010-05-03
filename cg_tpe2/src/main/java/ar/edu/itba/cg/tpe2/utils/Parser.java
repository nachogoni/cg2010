package ar.edu.itba.cg.tpe2.utils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3i;
import javax.vecmath.Vector3d;

import ar.edu.itba.cg.tpe2.core.geometry.Specification;
import ar.edu.itba.cg.tpe2.core.geometry.Transform;
import ar.edu.itba.cg.tpe2.core.scene.Scene;

public class Parser {

	private String filename = null;
	private FileParser aParser;
	
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
		
		Scene scene = new Scene();
		String current;
		aParser = new FileParser(this.filename);
		while(true){
			current = aParser.getNextToken();
			if(current == null){
				return null;
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
				} while (!current.equals("}"));
			}
		}
	}

	

	private void parseObjectSettings() throws IOException {
		String current;
		String shader_name, type, object_name;
		List<Point3d> points = new LinkedList<Point3d>();
		List<Point3i> triangles = new LinkedList<Point3i>();
		Point3d normal, center;
		Double radious;
		Integer pts_qty, tri_qty;
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
							normal = new Point3d(new Double(aParser.getNextToken()), 
									new Double(aParser.getNextToken()), 
									new Double(aParser.getNextToken()));
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
						}
					}while (current.equals("}"));
				} else if (current.equals("generic-mesh")){
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
								triangles.add(new Point3i(new Integer(aParser.getNextToken()),
										new Integer(aParser.getNextToken()),
										new Integer(aParser.getNextToken())));
							}
						}
					}while(!current.equals("}"));
				} else if (current.equals("box")){
					
				} else {
					// It's none of the accepted types of Shader
					do{
						current = aParser.getNextToken();
						// We consume all the discarded tokens.
					}while(!current.equals("}"));
					return;
				}
			}
		}while(!current.equals("}"));
		
	}
	
	private Specification parseSpec() throws IOException{
		String current, color;
		Specification aSpec = new Specification();
		Double __a, __b, __c;
	
		// We discard the first {
		current = aParser.getNextToken();
		current = aParser.getNextToken() + aParser.getNextToken();
		int length = current.length();
		color = current.substring(1, length-1);
		System.out.println("color: " + color);
		__a = new Double(aParser.getNextToken());
		__b = new Double(aParser.getNextToken());
		__c = new Double(aParser.getNextToken());
		// Now we discard the last }
		current = aParser.getNextToken();
		System.out.println("current: " + current);
		
		return new Specification(color, new Point3d(__a, __b, __c));
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
		
		return aTrans;
		
	}


	private void parseLightSettings() throws IOException {
		String current, type = null, light_color;
		Double __a, __b, __c, power;
		Point3d light_pos;
		Specification aSpec;
		do{
			current = aParser.getNextToken();
			System.out.println("super current: " + current);
			if(current.equals("type")){
				type = aParser.getNextToken();
			} else if (current.equals("color")){
				aSpec = this.parseSpec();
			}else if (current.equals("power")){
				power = new Double(aParser.getNextToken());
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
		// Build Object Light
	}


	private void parseShaderSettings() throws IOException {
		String current;
		String name = null, type = null, texture_path = null;
		String shader_color = null, abs_color = null;
		Double __a = -1d, __b = -1d, __c = -1d, eta = -1d, abs_dst = -1d, abs__a = -1d, abs__b = -1d, abs__c = -1d;
		Integer samples = -1;
		Specification aSpec = null;
		do{
			current = aParser.getNextToken();
			System.out.println("Shader: " + current);
			if(current.equals("name")){
				name = aParser.getNextToken();
			} else if (current.equals("type")){
				type = aParser.getNextToken();
				// Depending on the type of Shader...
				if(type.equals("phong")){
					do{
						current = aParser.getNextToken();
						if(current.equals("texture")){
							texture_path = aParser.getNextToken();
							int length = texture_path.length();
							texture_path = texture_path.substring(1, length);
						} else if(current.equals("spec")){
							aSpec = this.parseSpec();
						} else if (current.equals("samples")){
							samples = new Integer(aParser.getNextToken());
						}
					}while(!current.equals("}"));
				} else if (type.equals("glass")){
					do{
						current = aParser.getNextToken();
						if(current.equals("eta")){
							eta = new Double(aParser.getNextToken());
						} else if(current.equals("color")){
							aSpec = this.parseSpec();
						} else if (current.equals("absorbtion.distance")){
							abs_dst = new Double(aParser.getNextToken());
						} else if (current.equals("absorbtion.color")){
							aSpec = this.parseSpec();
						}
					}while(!current.equals("}"));
				} else if (type.equals("mirror")){
					do{
						current = aParser.getNextToken();
						if(current.equals("refl")){
							aSpec = this.parseSpec();
						}
					}while(current.equals("}"));
				} else {
					// It's none of the accepted types of Shader
					do{
						current = aParser.getNextToken();
						if(current.equals("color")  || current.equals("diff") || current.equals("spec")){
							aSpec = this.parseSpec();
						}
					}while(!current.equals("}"));
					return;
				}
			}
		}while(!current.equals("}"));
		
		System.out.println("Nombre: " + name);
		System.out.println("Tipo: " + type);
		System.out.println("Textura: " + texture_path);
		System.out.println("Color/Spec: (" + __a + ", " + __b + ", " + __c + ")");
		// Aca habria que armar el objeto
		
	}


	private void parseCameraSettings() throws IOException {
		String current;
		String type = null;
		Point3d eye = null, target = null;
		Vector3d up = null;
		Double fov = -1d, aspect = -1d;
		Double tl_fdist, tl_lensr;
		Point2d shift;
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
		
		System.out.println("Tipo de Cámara: " + type);
		System.out.println("Ojo: " + eye.toString());
		System.out.println("Objetivo: " + target.toString());
		System.out.println("Up: " + up.toString());
		System.out.println("FOV: " + fov);
		System.out.println("Aspect: " + aspect);
		//Aca habria que crear el objeto.
		
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
		
		System.out.println("Resolución: (" + width + ", " + height + ")");
		System.out.println("Antialiasing: (" + aa_min + ", " + aa_max + ")");
		System.out.println("Samples: " + samples);
		
	}
	
	
	
	
}
