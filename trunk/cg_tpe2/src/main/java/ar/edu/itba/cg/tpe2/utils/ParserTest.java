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

		Parser aParser = new Parser(curr_dir + "/escenas/scene1.sc");
		try {
			Scene aScene = aParser.parse();
//			List<Primitive> aList = aScene.getList();
//			for(int i = 0; i < aList.size(); i++){
//				System.out.println(aList.get(i).toString());
//			}
			System.out.println(aScene);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int i = 0;
		double spheresPerFace=4.0;
		double distance=0.5;
		double radius=0.5;
		double interval=spheresPerFace*radius + (spheresPerFace/2-1)*distance + distance/2 - radius; 
		for (double x = -interval ; x <= interval; x += 2*radius + distance) {
			for (double y = -interval; y <= interval; y += 2*radius + distance) {
				for (double z = -interval; z <= interval; z += 2*radius + distance) {
					System.out.println("object { \n\t shader phong"+ (i % 2) + "\n\t type sphere\n\t name sphere" + i + "\n\t c " + x + " "+ y + " "+ z + "\n\t r 0.7\n} ");
					i++;
				}
			}
		}

	}

}
