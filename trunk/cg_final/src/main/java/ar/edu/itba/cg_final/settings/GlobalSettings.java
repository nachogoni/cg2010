package ar.edu.itba.cg_final.settings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
	
	public String toString(){
		return p.toString();
	}
	
	public void setProperty(String property, String value){
		p.setProperty(property, value);
		return;
	}
	
	public void store(String filepath){
		try {
			p.store(new FileOutputStream(filepath), "Archivos de Configuracion de Rally\n#Las teclas son hexadecimales");
		} catch (FileNotFoundException e) {
			System.out.println("Falta el archivo de configuracion");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error al guardar el archivo de configuracion");
			e.printStackTrace();
		}
	}
	
	public int getKey(String property){
		return Integer.parseInt(p.getProperty(property), 16);
	}
	
}
