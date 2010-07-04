package ar.edu.itba.cg_final.map;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.cg_final.RallyGame;
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
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.state.CullState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.CullState.Face;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.FaultFractalHeightMap;

public class RallyTrack extends Node {
	private static final long serialVersionUID = 1L;
	private TerrainPage terrain;
	private ForceFieldFence fence;
	
	private ArrayList<Tree> forestList = new ArrayList<Tree>();
	private Node forest;

	public RallyTrack(GlobalSettings gs) {
		
		RallyGame rg = RallyGame.getInstance();
		buildLights();
		
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
        if (gs.getProperty("GAME.GRAPHICS.QUALITY").equals(GraphicsQualityUtils.High.toString())) {
        	texture = "texture/autodromo2.png";
            minF = MinificationFilter.Trilinear; 
            maxF = MagnificationFilter.Bilinear;
        } else {
        	texture = "texture/autodromo2low.jpg";
            minF = MinificationFilter.NearestNeighborNoMipMaps; 
            maxF = MagnificationFilter.NearestNeighbor;        	
        }
        
        Texture t1 = TextureManager.loadTexture( RallyGame.class.getClassLoader().
                getResource(texture), minF, maxF);
        ts.setTexture( t1, 0 );

        Texture t3 = TextureManager.loadTexture( RallyGame.class.getClassLoader().
                getResource(
                "texture/Detail.jpg" ),
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
        
        buildFence();
        
        buildForest(gs);
        
        //initialize OBBTree of terrain
        this.findPick( new Ray( new Vector3f(), new Vector3f( 1, 0, 0 ) ), new BoundingPickResults() ); 
	}
	
	private void buildFence() {
		RallyGame rg = RallyGame.getInstance();
	       //This is the main node of our fence
		ForceFieldFence fence = new ForceFieldFence("fence");
        
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
		
		for (int i = 1; i < treeCount; i++) {
			Tree tree = new Tree("tree"+i);
			Vector2f pos = gs.get2DVectorProperty("TRACK1.TREE" + i + ".POS"); 
			tree.placeTree(pos.x, terrain.getHeight(pos) + treeHeight, pos.y);
			forestList.add(tree);
		}
		
	}
	
	public void initForest(Node inGameState) {
		Node forestNode = new Node("forest");
		/*TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
		.createTextureState();
		ZBufferState zs = DisplaySystem.getDisplaySystem().getRenderer()
		.createZBufferState();
		zs.setFunction(TestFunction.LessThan);
		
		zs.setWritable(false);
		zs.setEnabled(true);	*/	
		//forestNode.setTextureCombineMode(TextureCombineMode);
		//forestNode.setRenderState(ts);		
		for (Tree tree : forestList) {
			//tree.setRenderState(zs);
			//tree.updateRenderState();
			forestNode.attachChild(tree);
		}

		//forestNode.updateRenderState();		
		inGameState.attachChild(forestNode);
		
		this.forest = forestNode;
		
		
		
		


		
		
		
	}
	
    private void buildLights() {
    	RallyGame rg = RallyGame.getInstance();
    	
        DirectionalLight dl = new DirectionalLight();
        dl.setDiffuse( new ColorRGBA( 1.0f, 1.0f, 1.0f, 1.0f ) );
        dl.setDirection( new Vector3f( 1, -0.5f, 1 ) );
        dl.setEnabled( true );
        rg.getLightState().attach( dl );

        DirectionalLight dr = new DirectionalLight();
        dr.setEnabled( true );
        dr.setDiffuse( new ColorRGBA( 1.0f, 1.0f, 1.0f, 1.0f ) );
        dr.setAmbient( new ColorRGBA( 0.5f, 0.5f, 0.5f, 1.0f ) );
        dr.setDirection( new Vector3f( 0.5f, -0.5f, 0 ) );

        rg.getLightState().attach( dr );
	}
   
   public TerrainPage getTerrain() {
	   return terrain;
   }

   public List<Checkpoint> createCheckPoints(GlobalSettings gs) {
	   
	   List<Checkpoint> cpList = new ArrayList<Checkpoint>();

	   int count = gs.getIntProperty("TRACK1.CHECKPOINT.COUNT");
	   
	   for (int i = 1; i <= count; i++) {
		   Checkpoint cp = new Checkpoint("CP"+String.valueOf(i),
				   gs.get2DVectorProperty("TRACK1.CHECKPOINT"+i+".POS"),
				   gs.getFloatProperty("TRACK1.CHECKPOINT"+i+".ROT"), (i==1));
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

}
