package ar.edu.itba.cg.tpe2.utils;

import java.io.FileNotFoundException;
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
			if(current.equalsIgnoreCase("image")){
				System.out.println("Reading the image settings...");
				this.parseImageSettings();
			} else if (current.equalsIgnoreCase("camera")){
				System.out.println("Reading the camera settings...");
				this.parseCameraSettings();
			} else if (current.equalsIgnoreCase("shader")){
				System.out.println("Reading Shader settings...");
				this.parseShaderSettings();
			}
		}
	}


	private void parseShaderSettings() {
		// TODO Auto-generated method stub
		
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
