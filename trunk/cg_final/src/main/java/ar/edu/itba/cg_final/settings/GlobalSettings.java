package ar.edu.itba.cg_final.settings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class GlobalSettings {
	
	Properties p = new Properties();
	
	public GlobalSettings(String filepath) {
		try {
			p.load(new FileInputStream(filepath));
		} catch (FileNotFoundException e) {
			System.out.println("Falta el archivo de configuracion");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error al cargar el archivo de configuracion");
			e.printStackTrace();
		}
	}
	

	public String getProperty(String property) {
		return p.getProperty(property);
	}
	
}
