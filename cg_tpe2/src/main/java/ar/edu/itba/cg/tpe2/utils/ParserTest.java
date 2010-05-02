package ar.edu.itba.cg.tpe2.utils;

import java.io.IOException;

public class ParserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String curr_dir = System.getProperty("user.dir");
		System.out.println(curr_dir);
		Parser aParser = new Parser(curr_dir + "/external/examples/julia.sc");
		try {
			aParser.parse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
