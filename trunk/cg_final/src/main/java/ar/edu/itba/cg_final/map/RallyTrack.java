package ar.edu.itba.cg_final.map;

import java.util.ArrayList;

import ar.edu.itba.cg_final.RallyGame;
import ar.edu.itba.cg_final.map.ProceduralTexturePyramid.pyramidType;
import ar.edu.itba.cg_final.settings.GameUserSettings;
import ar.edu.itba.cg_final.settings.GlobalSettings;
import ar.edu.itba.cg_final.terrain.ForceFieldFence;
import ar.edu.itba.cg_final.utils.GraphicsQualityUtils;

import com.jme.image.Texture;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.CombinerFunctionRGB;
import com.jme.image.Texture.CombinerOperandRGB;
import com.jme.image.Texture.CombinerScale;
import com.jme.image.Texture.CombinerSource;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.image.Texture.WrapMode;
import com.jme.intersection.BoundingPickResults;
import com.jme.light.DirectionalLight;
import com.jme.light.PointLight;
import com.jme.light.SpotLight;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.CullState.Face;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.FaultFractalHeightMap;

public class RallyTrack extends Node {
	private static final long serialVersionUID = 1L;
	private static final int BUSH_QUOTA = 2;
	private TerrainPage terrain;
	private ForceFieldFence fence;
	
	private ArrayList<Tree> forestList = new ArrayList<Tree>();
	//private ArrayList<Tree> obstaclesList = new ArrayList<Tree>();
	private Node forest;
	private Node pyramids;
	private Node obstacles;

	public RallyTrack(GlobalSettings gs, GameUserSettings gus) {
		
		RallyGame rg = RallyGame.getInstance();
		
		
		DisplaySystem display = DisplaySystem.getDisplaySystem(); 
		
        display.getRenderer().setBackgroundColor( new ColorRGBA( 0.5f, 0.5f, 0.5f, 1 ) );

        FaultFractalHeightMap heightMap = new FaultFractalHeightMap( 1025, 32, 0, 20,
                0.75f, 3 );
        Vector3f terrainScale = new Vector3f( 10, 1, 10 );
        heightMap.setHeightScale( 0.001f );
        
        TerrainPage page = new TerrainPage( "Terrain", 33, heightMap.getSize(), terrainScale,
                heightMap.getHeightMap() );
        
        page.setDetailTexture( 1, 16 );
        
        CullState cs = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
        cs.setCullFace(Face.Back);
        cs.setEnabled( true );
        page.setRenderState( cs );
        
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        ts.setEnabled( true );
        
        String texture;
        MinificationFilter minF; 
        MagnificationFilter maxF;
        if ( gus.getHighRes() ) {
        	texture = gs.getProperty("TRACK1.TEXTURE.HIGH");
            minF = MinificationFilter.Trilinear; 
            maxF = MagnificationFilter.Bilinear;
        } else {
        	texture = gs.getProperty("TRACK1.TEXTURE.LOW");
            minF = MinificationFilter.NearestNeighborNoMipMaps; 
            maxF = MagnificationFilter.NearestNeighbor;        	
        }
        
        Texture t1 = TextureManager.loadTexture( RallyGame.class.getClassLoader().
                getResource(texture), minF, maxF);
        ts.setTexture( t1, 0 );

        Texture t3 = TextureManager.loadTexture( RallyGame.class.getClassLoader().
                getResource(gs.getProperty("TRACK1.TEXTURE.DETAIL")),
                MinificationFilter.Trilinear,
                MagnificationFilter.Bilinear );
        ts.setTexture( t3, 1 );
        t3.setWrap( WrapMode.Repeat );
        
        t1.setApply( ApplyMode.Combine );
        t1.setCombineFuncRGB( CombinerFunctionRGB.Modulate );
        t1.setCombineSrc0RGB( CombinerSource.CurrentTexture );
        t1.setCombineOp0RGB( CombinerOperandRGB.SourceColor );
        t1.setCombineSrc1RGB( CombinerSource.PrimaryColor );
        t1.setCombineOp1RGB( CombinerOperandRGB.SourceColor );
        t1.setCombineScaleRGB( CombinerScale.One );
        
        t3.setApply( ApplyMode.Combine );
        t3.setCombineFuncRGB( CombinerFunctionRGB.AddSigned);
        t3.setCombineSrc0RGB( CombinerSource.CurrentTexture );
        t3.setCombineOp0RGB( CombinerOperandRGB.SourceColor );
        t3.setCombineSrc1RGB( CombinerSource.Previous );
        t3.setCombineOp1RGB( CombinerOperandRGB.SourceColor );
        t3.setCombineScaleRGB( CombinerScale.One);
        
        page.setRenderState( ts );        
    	
        final StaticPhysicsNode staticNode = rg.getPhysicsSpace().createStaticNode();

        staticNode.attachChild( page );

        staticNode.getLocalTranslation().set( 0, -150, 0 );
        
        this.attachChild( staticNode );
        staticNode.generatePhysicsGeometry();
        //initialize OBBTree of terrain
        
        this.terrain = page;
        
        buildFence(gs);
        
        buildForest(gs);
        
        buildPyramids(gs);
        
        generateObstacles(gs);
        
        buildLights(rg);
        
        //initialize OBBTree of terrain
        this.findPick( new Ray( new Vector3f(), new Vector3f( 1, 0, 0 ) ), new BoundingPickResults() ); 
	}
	
	private void buildFence(GlobalSettings gs) {
		RallyGame rg = RallyGame.getInstance();
	       //This is the main node of our fence
		ForceFieldFence fence = new ForceFieldFence("fence", gs);
        
        //we will do a little 'tweaking' by hand to make it fit in the terrain a bit better.
        //first we'll scale the entire "model" by a factor of 5
        fence.setLocalScale(320);
        
        //now let's move the fence to to the height of the terrain and in a little bit.
        
        fence.setLocalTranslation(new Vector3f(0, 
        		terrain.getHeight(25,25)+10, 
        		0));
        
        fence.updateGeometricState(0, true);
        
        final StaticPhysicsNode staticNode = rg.getPhysicsSpace().createStaticNode();

        staticNode.attachChild( fence );
        
        staticNode.getLocalTranslation().set( terrain.getWorldBound().getCenter().x - fence.getWorldBound().getCenter().x, 
        		-150, terrain.getWorldBound().getCenter().z - fence.getWorldBound().getCenter().z);

        this.attachChild( staticNode );
        
        staticNode.generatePhysicsGeometry();
        
        this.fence = fence;
	}

	private void buildForest(GlobalSettings gs) {
		
		float treeHeight = -100;
		
		int treeCount = gs.getIntProperty("TRACK1.TREE.COUNT");
		
		for (int i = 1; i <= treeCount; i++) {
			boolean arbust = false;
			if ( i % BUSH_QUOTA == 0 )
				arbust = true;
			
			Tree tree = new Tree("tree"+i, arbust, gs);
			Vector2f pos = gs.get2DVectorProperty("TRACK1.TREE" + i + ".POS");
			float ypos = terrain.getHeight(pos) + treeHeight;
			
			if ( arbust )
				ypos -= 50;
			tree.placeTree(pos.x, ypos, pos.y);
			forestList.add(tree);
		}
		
	}
	
	public void initForest(Node inGameState) {

		Node forestNode = new Node("forest");

		for (Tree tree : forestList) {
			forestNode.attachChild(tree);
		}

		inGameState.attachChild(forestNode);
		
		forestNode.setIsCollidable(true);
		this.forest = forestNode;
		
	}
	
	private void buildPyramids(GlobalSettings gs) {
		
		int count = gs.getIntProperty("TRACK1.PIRAMID.COUNT");
		pyramids = new Node();
		
		
		for (int i = 1; i <= count; i++) {
			ProceduralTexturePyramid pyramid;
			Vector2f pos = gs.get2DVectorProperty("TRACK1.PIRAMID" + i + ".POS");
			String type = gs.getProperty("TRACK1.PIRAMID" + i + ".TYPE");
			
			if (type.equals("Marble")) {
				pyramid = new ProceduralTexturePyramid("pyramid", pyramidType.MARBLE);
			} else {
				pyramid = new ProceduralTexturePyramid("pyramid", pyramidType.STONE);
			}
			pyramid.placePiramid(pos.x, terrain.getHeight(pos) + 320, pos.y);
			pyramids.attachChild(pyramid);
		}
		
		this.attachChild(pyramids);
	}
	
    private void buildLights(RallyGame rg) {
//    	RallyGame rg = RallyGame.getInstance();
//    	
//        DirectionalLight dl = new DirectionalLight();
//        dl.setDiffuse( new ColorRGBA( 1.0f, 1.0f, 1.0f, 1.0f ) );
//        dl.setDirection( new Vector3f( 1, -0.5f, 1 ) );
//        dl.setEnabled( true );
//        rg.getLightState().attach( dl );
//
//        DirectionalLight dr = new DirectionalLight();
//        dr.setEnabled( true );
//        dr.setDiffuse( new ColorRGBA( 1.0f, 1.0f, 1.0f, 1.0f ) );
//        dr.setAmbient( new ColorRGBA( 0.5f, 0.5f, 0.5f, 1.0f ) );
//        dr.setDirection( new Vector3f( 0.5f, -0.5f, 0 ) );
//
//        rg.getLightState().attach( dr );
    	
    	GlobalSettings gs = new GlobalSettings();
		TerrainPage tp = this.getTerrain();
		Vector2f pos;
		
		LightState ls = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
		

		
		//Create a Basic Directional Light
		DirectionalLight dl = new DirectionalLight();
		dl.setDirection(new Vector3f(0,-1,0));
		dl.setDiffuse(new ColorRGBA(
				gs.getFloatProperty("TRACK1.AMBIENT.LIGHT.R"), 
				gs.getFloatProperty("TRACK1.AMBIENT.LIGHT.G"), 
				gs.getFloatProperty("TRACK1.AMBIENT.LIGHT.B"), 
				gs.getFloatProperty("TRACK1.AMBIENT.LIGHT.A")));
		dl.setAmbient(new ColorRGBA(0.2f, 0.2f, 0.2f, 1.0f));
		dl.setEnabled(true);
		
		ls.attach(dl);
		ls.setTwoSidedLighting(true);
		

		//If there is a first checkpoint it has a Spotlight on top.  Go through it and your car will turn red
		int i = gs.getIntProperty("TRACK1.CHECKPOINT.COUNT");
		if(i > 0){
			pos = gs.get2DVectorProperty("TRACK1.CHECKPOINT1.POS");
		   	SpotLight sl = new SpotLight();
			sl.setDiffuse(new ColorRGBA(
					gs.getFloatProperty("TRACK1.LIGHT.SPOTLIGHT.R"), 
					gs.getFloatProperty("TRACK1.LIGHT.SPOTLIGHT.G"), 
					gs.getFloatProperty("TRACK1.LIGHT.SPOTLIGHT.B"), 
					gs.getFloatProperty("TRACK1.LIGHT.SPOTLIGHT.A")));
			sl.setAmbient(new ColorRGBA(0.75f, 0.75f, 0.75f, 1.0f));
			sl.setDirection(new Vector3f(0, -1, 0));
			sl.setLocation(new Vector3f(pos.x, tp.getHeight(pos)-150+100, pos.y));
			sl.setAngle(60);
			sl.setEnabled(true);
			ls.attach(sl);
			ls.setTwoSidedLighting(true);
		}
		
		//Insert Point Lights up to 5. JME Only supports 8 lights.
		i = gs.getIntProperty("TRACK1.POINTLIGHT.COUNT");
		for(int j = 1; j <= i && j <= 5; j++){
			pos = gs.get2DVectorProperty("TRACK1.POINTLIGHT" + j + ".POS");
			PointLight pl = new PointLight();
			pl.setDiffuse(new ColorRGBA(
					gs.getFloatProperty("TRACK1.POINTLIGHT"+ j +".R"), 
					gs.getFloatProperty("TRACK1.POINTLIGHT"+ j +".G"), 
					gs.getFloatProperty("TRACK1.POINTLIGHT"+ j +".B"), 
					gs.getFloatProperty("TRACK1.POINTLIGHT"+ j +".A")));
//			pl.setAmbient(new ColorRGBA(0.75f, 0.75f, 0.75f, 1.0f));
			pl.setLocation(new Vector3f(pos.x, tp.getHeight(pos)-150+100, pos.y));
			pl.setEnabled(true);
			ls.attach(pl);
			ls.setTwoSidedLighting(true);
		}
		
		rg.getRootNode().setRenderState(ls);
    	
	}
    
   private void generateObstacles(GlobalSettings gs) {
		int count = gs.getIntProperty("TRACK1.OBSTACLES.COUNT");
		obstacles = new Node();
		
		
		for (int i = 1; i <= count; i++) {
			Obstacles obstacle = new Obstacles("Obstacle"+i, gs);
			Vector2f pos = gs.get2DVectorProperty("TRACK1.OBSTACLES" + i + ".POS");
			
			obstacle.placeObstacles(pos.x, terrain.getHeight(pos)-145, pos.y);
			obstacles.attachChild(obstacle);
		}
		
		this.attachChild(obstacles);	   
   }
   
   public TerrainPage getTerrain() {
	   return terrain;
   }

   public ArrayList<CheckPoint> createCheckPoints(GlobalSettings gs) {
	   
	   ArrayList<CheckPoint> cpList = new ArrayList<CheckPoint>();

	   int count = gs.getIntProperty("TRACK1.CHECKPOINT.COUNT");
	   
	   for (int i = 1; i <= count; i++) {
		   CheckPoint cp = new CheckPoint("CheckPoint"+String.valueOf(i),
				   gs.get2DVectorProperty("TRACK1.CHECKPOINT"+i+".POS"),
				   gs.getFloatProperty("TRACK1.CHECKPOINT"+i+".ROT"),
				   (i==1), gs);
		   cpList.add(cp);
		   this.attachChild(cp);
	   }
	   
	   return cpList;
   }

	public ForceFieldFence getFence() {
		return fence;
	}
	
	public Node getForest() {
		return forest;
	}
	
	public Node getPyramids() {
		return pyramids;
	}
	public Node getObstacles() {
		return obstacles;
	}

}
