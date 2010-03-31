import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

public class Main {
	
	public static void convertImage(String inputFile, String outputFile, String format) {
		
		if (inputFile == null || outputFile == null || format == null)
			return;
		
		try {
		
			BufferedImage image = null;
			
			File input = new File(inputFile);
			
			File output = new File(outputFile);
		
			image = ImageIO.read(input);

			
			
			ImageIO.write(image, format, output);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
				
	}
	
	public static void convertImage(String inputFile, String outputFile, float compression) {
		
		if (inputFile == null || outputFile == null)
			return;
		
		try {
			
			Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
			
			ImageWriter writer = (ImageWriter)iter.next();
			
			ImageWriteParam iwp = writer.getDefaultWriteParam();
			
			iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			
			iwp.setCompressionQuality(compression);
			
			File file = new File(outputFile);
			
			FileImageOutputStream output = new FileImageOutputStream(file);
			
			writer.setOutput(output);
			
			File input = new File(inputFile);
			
			BufferedImage img = ImageIO.read(input);
			
			IIOImage image = new IIOImage(img, null, null);
			
			writer.write(null, image, iwp);
			
			writer.dispose();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
		
	}

	public static void RGBtoHSB(String inputFile, String outputFile) {
		
		if (inputFile == null || outputFile == null)
			return;
		
		try {
			
			File input = new File(inputFile);

			File output = new File(outputFile);
			
			BufferedImage img = ImageIO.read(input);
			
			BufferedImage imgTo = ImageIO.read(output);

			int[] rgb = new int[3];
			float[] hsb = new float[3];
			Color color;

			for (int x = 0; x < img.getWidth(); x++)
			{
				for (int y = 0; y < img.getHeight(); y++)
				{
					color = new Color(img.getRGB(x, y));
					
					rgb[0] = color.getRed();
					rgb[1] = color.getGreen();
					rgb[2] = color.getBlue();
					
					hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb); 
					
					imgTo.setRGB(x, y, Color.HSBtoRGB(hsb[0], hsb[1], hsb[2])); 
				}
			}
			
			ImageIO.write(imgTo, "png", output);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
		
	}
	
	public static void RGBtoHSBN(String inputFile, String outputFile, int N) {
		
		if (inputFile == null || outputFile == null)
			return;
		
		try {
			
			File input = new File(inputFile);
			
			File output = new File(outputFile);
			
			BufferedImage img = ImageIO.read(input);
			
			BufferedImage imgTo = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_BGR);

			BufferedImage imgAux;
			
			int[] rgb = new int[3];
			float[] hsb = new float[3];
			Color color;
			
			for (int n = 0; n < N; n++)
			{
				for (int x = 0; x < img.getWidth(); x++)
				{
					for (int y = 0; y < img.getHeight(); y++)
					{
						color = new Color(img.getRGB(x, y));
						
						rgb[0] = color.getRed();
						rgb[1] = color.getGreen();
						rgb[2] = color.getBlue();
						
						hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb); 
						
						imgTo.setRGB(x, y, Color.HSBtoRGB(hsb[0], hsb[1], hsb[2])); 
					}
				}
				imgAux = img;
				img = imgTo;
				imgTo = imgAux;
				System.out.printf("n: %d\n", n);
			}
			
			ImageIO.write(img, "png", output);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
		
	}
	
	public static void RGBtoHSBWriteN(String inputFile, String outputFile, int N) {

		String aux;
		
		if (inputFile == null || outputFile == null)
			return;

		try {
			
			File input = new File(inputFile);
			File output = new File(outputFile);
			
			int[] rgb = new int[3];
			float[] hsb = new float[3];
			Color color;
			
			for (int n = 0; n < N; n++)
			{
				BufferedImage img = ImageIO.read(input);
				BufferedImage imgTo = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_BGR);

				for (int x = 0; x < img.getWidth(); x++)
				{
					for (int y = 0; y < img.getHeight(); y++)
					{
						color = new Color(img.getRGB(x, y));
						
						rgb[0] = color.getRed();
						rgb[1] = color.getGreen();
						rgb[2] = color.getBlue();
						
						hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb); 
						
						imgTo.setRGB(x, y, Color.HSBtoRGB(hsb[0], hsb[1], hsb[2])); 
					}
				}
				
				ImageIO.write(img, "png", output);
				
				System.out.printf("n: %d\n", n);
				
				aux = inputFile;
				inputFile = outputFile;
				outputFile = aux;
				
				input = new File(inputFile);
				output = new File(outputFile);
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
		
	}
	
	public static void createDiff(String file1, String file2, String diffFile) {
		
		if (file1 == null || file2 == null || diffFile == null)
			return;
		
		try {
			
			File input1 = new File(file1);
			File input2 = new File(file2);
			
			File output = new File(diffFile);
			
			BufferedImage img1 = ImageIO.read(input1);
			BufferedImage img2 = ImageIO.read(input2);
			
			if(img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight())
				return;
			
			BufferedImage imgTo = new BufferedImage(img1.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_BGR);
			
			
			int[] rgb1 = new int[3];
			int[] rgb2 = new int[3];
			Color color;
			int diff;
			
			for (int x = 0; x < img1.getWidth(); x++)
			{
				for (int y = 0; y < img1.getHeight(); y++)
				{
					color = new Color(img1.getRGB(x, y));
					
					rgb1[0] = color.getRed();
					rgb1[1] = color.getGreen();
					rgb1[2] = color.getBlue();
					
					color = new Color(img2.getRGB(x, y));
					
					rgb2[0] = color.getRed();
					rgb2[1] = color.getGreen();
					rgb2[2] = color.getBlue();
					
					diff = (Math.abs(rgb1[0]-rgb2[0]) + Math.abs(rgb1[1]-rgb2[1]) + Math.abs(rgb1[2]-rgb2[2])) / 3;
					
					color = new Color(diff, diff, diff);

					imgTo.setRGB(x, y, color.getRGB()); 
				}
			}
			
			ImageIO.write(imgTo, "png", output);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
		
	}
	
	public static void createDiffBright(String file1, String file2, String diffFile) {
		
		if (file1 == null || file2 == null || diffFile == null)
			return;
		
		try {
			
			File input1 = new File(file1);
			File input2 = new File(file2);
			
			File output = new File(diffFile);
			
			BufferedImage img1 = ImageIO.read(input1);
			BufferedImage img2 = ImageIO.read(input2);
			
			if(img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight())
				return;
			
			BufferedImage imgTo = new BufferedImage(img1.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_BGR);
			
			
			int[] rgb1 = new int[3];
			int[] rgb2 = new int[3];
			float[] hsb1 = new float[3];
			float[] hsb2 = new float[3];
			Color color;
			float diff;

			for (int x = 0; x < img1.getWidth(); x++)
			{
				for (int y = 0; y < img1.getHeight(); y++)
				{
					color = new Color(img1.getRGB(x, y));
					
					rgb1[0] = color.getRed();
					rgb1[1] = color.getGreen();
					rgb1[2] = color.getBlue();

					hsb1 = Color.RGBtoHSB(rgb1[0], rgb1[1], rgb1[2], hsb1); 
					
					color = new Color(img2.getRGB(x, y));
					
					rgb2[0] = color.getRed();
					rgb2[1] = color.getGreen();
					rgb2[2] = color.getBlue();
					
					hsb1 = Color.RGBtoHSB(rgb2[0], rgb2[1], rgb2[2], hsb2); 

					diff = Math.abs(hsb1[2]-hsb2[2]);
					
					color = new Color(diff, diff, diff);
					
					imgTo.setRGB(x, y, color.getRGB()); 
				}
			}
			
			ImageIO.write(imgTo, "png", output);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
		
	}
	
	public static void main(String[] args) {
		
		/*// Ejercicio 1
		convertImage("arboles.jpg", "arboles.gif", "gif");
		convertImage("arboles.jpg", "arboles.png", "png");

		convertImage("aerosol.gif", "aerosol.png", "png");
		convertImage("aerosol.gif", "aerosol.jpg", "jpg");

		convertImage("bandera_pirata.png", "bandera_pirata.jpg", "jpg");
		convertImage("bandera_pirata.png", "bandera_pirata.gif", "gif");
		
		convertImage("cornell_box.png", "cornell_box.jpg", "jpg");
		convertImage("cornell_box.png", "cornell_box.gif", "gif");
		
		convertImage("manzanas.jpg", "manzanas.png", "png");
		convertImage("manzanas.jpg", "manzanas.gif", "gif");
		
		convertImage("naciones_unidas.png", "naciones_unidas.jpg", "jpg");
		convertImage("naciones_unidas.png", "naciones_unidas.gif", "gif");
		
		convertImage("vaca.jpg", "vaca.png", "png");
		convertImage("vaca.jpg", "vaca.gif", "gif");
		*/
		
		/*// Ejercicio 2
		convertImage("arboles.jpg", "arboles_075.jpg", 0.75f);

		convertImage("aerosol.gif", "aerosol_075.jpg", 0.75f);

		convertImage("bandera_pirata.png", "bandera_pirata_075.jpg", 0.75f);
		
		convertImage("cornell_box.png", "cornell_box_075.jpg", 0.75f);
		
		convertImage("manzanas.jpg", "manzanas_075.jpg", 0.75f);
		
		convertImage("naciones_unidas.png", "naciones_unidas_075.jpg", 0.75f);
		
		convertImage("vaca.jpg", "vaca_075.jpg", 0.75f);
		*/
		
		/*// Ejercicio 3
		int N = 500;
		String str = "arbolesN.jpg";
		String str_b = "arbolesN2.jpg";
		String aux;
		
		convertImage("arboles.jpg", str, 0.75f);
		for (int i = 0; i < N; i++)
		{
			convertImage(str, str_b, 0.75f);
			aux = str;
			str = str_b;
			str_b = aux;
			System.out.printf("i: %d\n", i);
		}
		
		System.out.printf("Ultimo archivo: %s\n", str);
		*/
		
		/*// Ejercicio 4
		RGBtoHSB("cornell_box.png", "cornell_box_RGB-HSB.png");
		*/
		
		/*// Ejercicio 5
		RGBtoHSBN("cornell_box_copy.png", "cornell_box_RGB-HSBN.png", 100);
		*/

		/*// Ejercicio 6
		createDiff("bandera_pirata.png", "bandera_pirata2.png", "bandera_pirata_DIFF.png");
		*/
		
		/*// Ejercicio 7
		createDiff("cornell_box.png", "cornell_box_RGB-HSBN.png", "cornell_box_DIFF.png");
		createDiffBright("cornell_box.png", "cornell_box_RGB-HSBN.png", "cornell_box_BDIFF.png");
		*/
		
	}
}
