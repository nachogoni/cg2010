package ar.edu.itba.cg.tpe2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import ar.edu.itba.cg.tpe2.core.camera.Camera;
import ar.edu.itba.cg.tpe2.core.scene.Scene;
import ar.edu.itba.cg.tpe2.rayCaster.IColorProvider;
import ar.edu.itba.cg.tpe2.rayCaster.RayCaster;
import ar.edu.itba.cg.tpe2.rayCaster.ColorProviders.CyclicColorProvider;
import ar.edu.itba.cg.tpe2.rayCaster.ColorProviders.RandomColorProvider;
import ar.edu.itba.cg.tpe2.utils.Parser;

/**
 * TPE2
 *
 */
public class App 
{

	
    public static void main( String[] args ) throws IOException
    {
    	try {
    		
    		BufferedImage image;

    		long start = 0;
    		long stop = 0;
    		
    		//Parameters parsing
    		CommandLine cl= parseCommands(args);
    		
    		if (cl.hasOption("gui")) {
    			cl = showCommandsGUI();
    		}
    		
    		// Validate and get the sceneName
    		String sceneName = getSceneName(cl);
    		
    		// Validate and get the outputFile
    		File output = getOutputFile(cl);
    		
    		// Validate and get image resolution
    		int [] imageResolution = getImageResolution(cl);
    		
    		// Validate and get fov
    		int fov = getFov(cl);
    		
    		// Validate and get the color provider
    		IColorProvider colorProvider = getColorProvider(cl);
    		
    		// Validate and get Color variation
    		int colorVar = getColorVariation(cl);    		
    		
    		// Set output format
    		String format = output.getName().substring(output.getName().length() - 3, output.getName().length()).toUpperCase();
    		
    		// Create an scene
//    		Scene scene = new Scene(sceneName);
    		String curr_dir = System.getProperty("user.dir");
    		Parser aParser = new Parser(curr_dir + "/escenas/" + sceneName);
    		Scene scene = aParser.parse();
    		System.out.println(scene.toString());
    		
    		// Create a camera
//    		Camera camera = new Camera(new Point3d(0d, 0d, 6d), new Point3d(0d, 0d ,0d), 
//    				new Point3d(0d, 10d ,6d), fov, imageResolution[0], imageResolution[1]);
    		Camera camera = scene.getaCamera();
    		camera.setWidth(scene.getAnImage().getWidth());
    		
    		// Create rayCaster
    		RayCaster raycaster = null;
    		
    		int numberOfBuckets = 128;

    		// Set colors for primitives
    		if ( colorProvider instanceof CyclicColorProvider) {
    			raycaster = new RayCaster(scene, camera, 1, numberOfBuckets, colorProvider, colorVar);
    		} else {
    			raycaster = new RayCaster(scene, camera, 1, numberOfBuckets, colorProvider, colorVar);
    		}
    		
    		// Take start time
    		if (cl.hasOption("time")) {
    			start = Calendar.getInstance().getTimeInMillis();
    		}
    		
    		// Get image from a viewPort
    		image = raycaster.getImage(imageResolution[0], imageResolution[1], 
    				BufferedImage.TYPE_INT_RGB);
    		
    		// Take stop time
    		if (cl.hasOption("time")) {
    			stop = Calendar.getInstance().getTimeInMillis();
    		}
    		
    		if (cl.hasOption("show")) {
    			showImage(image, sceneName);
    		}
    		
    		// Save image
    		try {
    			ImageIO.write(image, format, output);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		
    		if (cl.hasOption("time")) {
    			System.out.println("Done in " + (stop - start) + " milliseconds!");
    		} else {
    			System.out.println("Done!");
    		}
    		
    		raycaster.cleanup();
    		
    		
    	} catch (ParseException pe) {
    		showUsage();
    		System.out.println(pe.getMessage());
    		return;
    	}
		
    }
    
	private static CommandLine showCommandsGUI() {
		// TODO Funcion que abre una ventana con todos los comandos disponibles y retorna un CommandLine con las opciones seleccionadas
		return null;
	}

	private static void showImage(BufferedImage image, String sceneName) {
		ImagePanel contentPane = new ImagePanel(image);
        JScrollPane jsp = new JScrollPane(contentPane);
		jsp.setVisible(true);
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setContentPane(jsp);
        f.setTitle("G2 - "+sceneName+" rendered");
        f.setSize(image.getWidth()+40,image.getHeight()+40);
        f.setLocation(200,200);
        f.setVisible(true);
	}

	private static int getColorVariation(CommandLine cl) throws IllegalArgumentException{
		
		int colorVar  = RayCaster.COLOR_VARIATION_LINEAR;
		
		if (cl.hasOption("cv")) {
			String colorVariation = cl.getOptionValue("cv");
			if (colorVariation.equals("linear")) {
				colorVar = RayCaster.COLOR_VARIATION_LINEAR;
			} else if (colorVariation.equals("log")) {
				colorVar = RayCaster.COLOR_VARIATION_LOG;
			} else {
				throw new IllegalArgumentException("Invalid color variation");
			}
		}
		return colorVar;
	}

	private static IColorProvider getColorProvider(CommandLine cl) throws IllegalArgumentException{
		List<Color> colors = new ArrayList<Color>();
		colors.add(new Color(143, 0, 255));
		colors.add(Color.BLUE);
		colors.add(Color.GREEN);
		colors.add(Color.YELLOW);
		colors.add(new Color(255, 140, 0));
		colors.add(Color.RED);		
		
		//Default value
		IColorProvider colorProvider = new RandomColorProvider();
		
		if (cl.hasOption("cm")) {
			String colorMode = cl.getOptionValue("cm");
			if (colorMode.equals("ordered")) {
				//Change the default value
				colorProvider = new CyclicColorProvider(colors);
			} else if ( ! colorMode.equals("random")) {
				// Other case, error...
				throw new IllegalArgumentException("Invalid color mode");
			}
		}  		
		
		return colorProvider;
	}

	private static int getFov(CommandLine cl) throws IllegalArgumentException{
		//default value
		int fov=60;
		
		if (cl.hasOption("fov")) {
			
			String cv = cl.getOptionValue("fov");
			// Angle
			Pattern anglePattern = Pattern.compile("(\\d+)");
			Matcher m = anglePattern.matcher(cv);
			if ( ! m.find() ) {
				throw new IllegalArgumentException("'" + cv + "' is an invalid aperture angle");
			} else {
				fov = Integer.parseInt(m.group(1));
			}
		}
		return fov;
	}

	private static int[] getImageResolution(CommandLine cl) throws IllegalArgumentException {
		int [] res = new int [2];
		
		// Default values
		res[0] = 640;
		res[1] = 480;
		if (cl.hasOption("size")) {
			Pattern dimensionsPattern = Pattern.compile("(\\d+)x(\\d+)");
			String sizeValue = cl.getOptionValue("size");
			Matcher m = dimensionsPattern.matcher(sizeValue);
			if ( ! m.find() ) {
				throw new IllegalArgumentException("'" + sizeValue + "' is an invalid image width");
			} else {
				res[0] = Integer.parseInt(m.group(1));
				res[1] = Integer.parseInt(m.group(2));
			}
		}
		return res;
	}

	private static String getSceneName(CommandLine cl) {
		String sceneName=null;
		if (cl.hasOption("i")){
			// Scene name
			sceneName=cl.getOptionValue("i");
			// Check sceneName
			if (sceneName.matches("scene\\d.sc") == false) {
				throw new IllegalArgumentException("'" + sceneName + "' is an invalid scene name");
			}
		}
		return sceneName;
	}

	private static CommandLine parseCommands(String[] args) throws ParseException{
    	Options options = new Options();
    	
    	// -i <scene file>
    	addInputFileCommand(options);
    	
       	// [ -time ]
    	addTimeCommand(options);
       	
       	// [ -o <output file> ]
    	addOutputFileCommand(options);

       	// [ -cm random|ordered ]
    	addColorAssignmentCommand(options);
       	
       	// [ -cv lineal|log ]
    	addColorVariationCommand(options);

       	// [-size widthxheight]
    	addSizeCommand(options);

       	// [-fov angle]
    	addFovCommand(options);

       	// [-usage]
    	addUsageCommand(options);

       	//NEW COMMANDS -> TP2
    	
       	// [ -progress ]
    	addProgressCommand(options);
       	
       	// [ -gui ]
    	addGUICommand(options);
    	
    	// [ -dof <T>]
    	addDofCommand(options);
    	
    	// [ -show ]
       	addShowCommand(options);
    	
       	CommandLineParser parser = new PosixParser();
       	CommandLine line =  null;
       	
       	// parse the command line arguments
	    line = parser.parse( options, args );
       	
	    return line;
	}
	
	private static void addShowCommand(Options options) {
    	OptionBuilder.withDescription( "display the image after processing" );
       	Option show   = OptionBuilder.create( "show" );
       	
       	options.addOption(show);
		
	}

	private static void addDofCommand(Options options) {
    	OptionBuilder.withArgName( "depth of field" );
    	OptionBuilder.hasArg();
    	OptionBuilder.withDescription( "sets the depth of field" );
       	Option dof   = OptionBuilder.create( "dof" );
   	
       	options.addOption(dof);
		
	}

	private static void addGUICommand(Options options) {
    	OptionBuilder.withDescription( "open a graphic interface ignoring all the other parameters" );
       	Option gui   = OptionBuilder.create( "gui" );
       	
       	options.addOption(gui);
		
	}

	private static void addProgressCommand(Options options) {
    	OptionBuilder.withDescription( "show rendering progress" );
       	Option progress   = OptionBuilder.create( "progress" );
       	
       	options.addOption(progress);
		
	}

	private static void addUsageCommand(Options options) {
    	OptionBuilder.withDescription( "display the list of options available" );
       	Option usage   = OptionBuilder.create( "usage" );

       	options.addOption(usage);       	
		
	}

	@Deprecated
	private static void addFovCommand(Options options) {
    	OptionBuilder.withArgName( "angle" );
    	OptionBuilder.hasArg();
    	OptionBuilder.withDescription( "color variation mode" );
       	Option fov   = OptionBuilder.create( "fov" );

       	options.addOption(fov);
		
	}

	@Deprecated
	private static void addSizeCommand(Options options) {
    	OptionBuilder.withArgName( "widthxheight" );
    	OptionBuilder.hasArg();
    	OptionBuilder.withDescription( "color variation mode" );
       	Option size   = OptionBuilder.create( "size" );
   	
       	options.addOption(size);
		
	}

	@Deprecated
	private static void addColorVariationCommand(Options options) {
    	OptionBuilder.withArgName( "lineal|log" );
    	OptionBuilder.hasArg();
    	OptionBuilder.withDescription( "color variation mode" );
       	Option cv   = OptionBuilder.create( "cv" );
   	
       	options.addOption(cv);
		
	}
	@Deprecated
	private static void addColorAssignmentCommand(Options options) {
    	OptionBuilder.withArgName( "random|ordered" );
    	OptionBuilder.hasArg();
    	OptionBuilder.withDescription( "color assignment order" );
       	Option cm   = OptionBuilder.create( "cm" );
   	
       	options.addOption(cm); 
		
	}

	private static void addOutputFileCommand(Options options) {
    	OptionBuilder.withArgName( "output" );
    	OptionBuilder.hasArg();
    	OptionBuilder.withDescription( "output file" );
       	Option outputFile   = OptionBuilder.create( "o" );
   	
       	options.addOption(outputFile);
	}

	private static void addTimeCommand(Options options) {
    	OptionBuilder.withDescription( "show processing time" );
       	Option time   = OptionBuilder.create( "time" );
       	
       	options.addOption(time);
	}

	private static void addInputFileCommand(Options options) {
    	OptionBuilder.withArgName( "sceneX.sc" );
    	OptionBuilder.hasArg();
    	OptionBuilder.withDescription( "use scene file for render" );
    	OptionBuilder.isRequired();
       	Option sceneFile   = OptionBuilder.create( "i" );
   	
       	options.addOption(sceneFile);
	}
	
	public static File getOutputFile(CommandLine cl) throws IllegalArgumentException {
		String fileName = null;
		File output;

    	if (cl.hasOption("o")) {
			fileName = cl.getOptionValue("o");
			// Check fileName
			if ( fileName.matches( "[a-zA-Z0-9_]+.(png|bmp)" ) == false ) {
				throw new IllegalArgumentException("'" + fileName + "' is an invalid output filename");
			}
    	} else { 
    		// Making the default value with the input filename, in case the option was not set
    		String sceneName = cl.getOptionValue("i");
    		
			fileName = sceneName.substring(0, sceneName.length() - 2).concat("png");
		}
    	
		// Open output file
		output = new File(fileName);
    	
    	return output;
	}
	
	public static void showUsage() {
		System.out.println("Usage: App -i sceneX.sc [-o output] [-time] [-cm [random|ordered]] [-cm [linear|log]] [-size widthxheight] [-fov angle] [-usage]");
		return;
	}
}
