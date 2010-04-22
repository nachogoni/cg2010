package ar.edu.itba.cg.tpe2.utils;

import ar.edu.itba.cg.tpe2.core.Scene;

public class Parser {

	private String filename = null;
	
	public Parser(String filename) {
		if (filename == null)
			throw new IllegalArgumentException("Filename or scene cant be null");
		this.filename = filename;
	}
	
	
	/**
	 * Start parsing the file
	 * 
	 * @return
	 */
	public Scene parse() {
		
		Scene scene = new Scene();
		
		// Parsear el archivo filename
		
		return scene;
	}
	
	
	
}
