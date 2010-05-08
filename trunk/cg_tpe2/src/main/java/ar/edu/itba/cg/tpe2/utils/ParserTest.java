package ar.edu.itba.cg.tpe2.utils;

import java.io.IOException;
import java.util.List;

import ar.edu.itba.cg.tpe2.core.geometry.Primitive;
import ar.edu.itba.cg.tpe2.core.scene.Scene;

public class ParserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String curr_dir = System.getProperty("user.dir");
		System.out.println(curr_dir);

		Parser aParser = new Parser(curr_dir + "/escenas/prueba2.sc");
		try {
			Scene aScene = aParser.parse();
			List<Primitive> aList = aScene.getList();
			for(int i = 0; i < aList.size(); i++){
				System.out.println(aList.get(i).toString());
			}
			System.out.println(aScene);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
