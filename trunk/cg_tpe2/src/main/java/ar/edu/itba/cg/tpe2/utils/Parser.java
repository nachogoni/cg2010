package ar.edu.itba.cg.tpe2.utils;

import java.io.IOException;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

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
			System.out.println(current);
			if(current == null){
				return null;
			}
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
			}
		}
	}

	

	private void parseLightSettings() throws IOException {
		String current, type, light_color;
		Double __a, __b, __c, power;
		Point3d light_pos;
		do{
			current = aParser.getNextToken();
			if(current.equals("type")){
				type = aParser.getNextToken();
			} else if (current.equals("color")){
				// We discard the first {
				current = aParser.getNextToken();
				current = aParser.getNextToken() + aParser.getNextToken();
				int length = current.length();
				light_color = current.substring(1, length);
				__a = new Double(aParser.getNextToken());
				__b = new Double(aParser.getNextToken());
				__c = new Double(aParser.getNextToken());
				// Now we discard the last }
				current = aParser.getNextToken();
				current = light_color;
			}else if (current.equals("power")){
				power = new Double(aParser.getNextToken());
			}else if (current.equals("p")){
				light_pos = new Point3d(new Double(aParser.getNextToken()),
						new Double(aParser.getNextToken()),
						new Double(aParser.getNextToken()));
			}
		}while(!current.equals("}"));
	}


	private void parseShaderSettings() throws IOException {
		String current;
		String name, type, texture_path;
		String shader_color, abs_color;
		Double __a, __b, __c, eta, abs_dst, abs__a, abs__b, abs__c;
		Integer samples;
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
						if(current.equals("texture")){
							texture_path = aParser.getNextToken();
							int length = texture_path.length();
							texture_path = texture_path.substring(1, length);
						} else if(current.equals("spec")){
							// We discard the first {
							current = aParser.getNextToken();
							current = aParser.getNextToken() + aParser.getNextToken();
							int length = current.length();
							shader_color = current.substring(1, length);
							__a = new Double(aParser.getNextToken());
							__b = new Double(aParser.getNextToken());
							__c = new Double(aParser.getNextToken());
							// Now we discard the last }
							current = aParser.getNextToken();
							current = type;
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
							// We discard the first {
							current = aParser.getNextToken();
							current = aParser.getNextToken() + aParser.getNextToken();
							int length = current.length();
							shader_color = current.substring(1, length);
							__a = new Double(aParser.getNextToken());
							__b = new Double(aParser.getNextToken());
							__c = new Double(aParser.getNextToken());
							// Now we discard the last }
							current = aParser.getNextToken();
							current = type;
						} else if (current.equals("absorbtion.distance")){
							abs_dst = new Double(aParser.getNextToken());
						} else if (current.equals("absorbtion.color")){
							// We discard the first {
							current = aParser.getNextToken();
							current = aParser.getNextToken() + aParser.getNextToken();
							int length = current.length();
							abs_color = current.substring(1, length);
							abs__a = new Double(aParser.getNextToken());
							abs__b = new Double(aParser.getNextToken());
							abs__c = new Double(aParser.getNextToken());
							// Now we discard the last }
							current = aParser.getNextToken();
							current = type;
						}
					}while(!current.equals("}"));
				} else if (type.equals("mirror")){
					do{
						current = aParser.getNextToken();
						if(current.equals("refl")){
							// We discard the first {
							current = aParser.getNextToken();
							current = aParser.getNextToken() + aParser.getNextToken();
							int length = current.length();
							shader_color = current.substring(1, length);
							__a = new Double(aParser.getNextToken());
							__b = new Double(aParser.getNextToken());
							__c = new Double(aParser.getNextToken());
							// Now we discard the last }
							current = aParser.getNextToken();
							current = type;
						}
					}while(current.equals("}"));
				}
			}
		}while(!current.equals("}"));
		
		// Aca habria que armar el objeto
		
	}


	private void parseCameraSettings() throws IOException {
		String current;
		String type;
		Point3d eye, target;
		Vector3d up;
		Double fov, aspect;
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
		
		//Aca habria que crear el objeto.
		
	}


	private void parseImageSettings() throws NumberFormatException, IOException {
		String current;
		Integer width, height;
		Integer aa_min, aa_max;
		Integer samples;
		Integer bucket_size;
		String bucket_type;
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
		
		//Aca habria que crear el objeto.
		
	}
	
	
	
	
}
