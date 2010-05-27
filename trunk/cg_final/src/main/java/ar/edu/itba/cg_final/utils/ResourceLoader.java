package ar.edu.itba.cg_final.utils;

import java.net.MalformedURLException;
import java.net.URL;

public class ResourceLoader {

	public static final String RESOURCES_PATH = "src/main/resources/";
	
	public static URL getURL(String resourceName) throws MalformedURLException {
		return new URL("file:" + RESOURCES_PATH + resourceName);
	}
	
}
