package GameInstances;

import utility.BackgroundManager;
import utility.GameHud;
import utility.IActivityRequestHandler;
import utility.SegmentLoader;
import Entities.Player;
import Entities.WorldChunk;
import Entities.WorldSegment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wockawocka.nether.GameInstance;
import com.wockawocka.nether.NetherMain;

public class MainGame extends GameInstance{
	
	OrthographicCamera camera;
	private int screenWidth, screenHeight;
	private String screenResolution;
	Texture backgroundImage;
	
	private float scoreTimer = 0;
	
	// game effects
	Pixmap flashPM;
	Texture flashTexture;
	float dynamicFlash = 1f;
	
	// game logic
	private Boolean readyToCreateChunk = true;
	private Boolean isCameraFrozen = false;
	private Boolean isCameraFreezing = false;
	private float CAMERA_SPEED;
	private float dynamicSpeed;
	private Boolean isPaused = false;
	
	private int difficulty = 3;
	
	// chunk managing
	WorldChunk currentChunk;
	WorldSegment currentSegment;
	
	// HUD
	GameHud gameHud;
	
	WorldChunk pastChunk;
	WorldChunk nextChunk;
	SegmentLoader sLoader;
	
	private int chunksCreated = 0;
	
	// The character class
	Player player;
	
	// Background manager
	BackgroundManager bgManager;

	NetherMain main;
	IActivityRequestHandler mainDeviceHandler;
	
	
	/**
	 * Creates the main game instance.
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 * @param resolution
	 */
	public MainGame(int screenWidth, int screenHeight, String resolution, NetherMain main, IActivityRequestHandler mainDeviceHandler)
	{
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.screenResolution = resolution;
		this.main = main;
		CAMERA_SPEED = (float) ((float) screenWidth/1.4);
		dynamicSpeed = CAMERA_SPEED;
		this.mainDeviceHandler = mainDeviceHandler;
	}

	@Override
	public void create() 
	{       
		camera = new OrthographicCamera(screenWidth, screenHeight);
        camera.setToOrtho(true);
        camera.position.set(screenWidth/2, 0f, 0f);
        
        // parse in current segment because they need to communicate with eachother
        player = new Player(screenWidth, screenHeight, screenResolution, this);
        player.create();
        
        // initiates the segement loader
        sLoader = new SegmentLoader(screenWidth, screenHeight, screenResolution, player);
        // creates the game HUD
        gameHud = new GameHud(screenWidth, screenHeight, screenResolution, main, this, mainDeviceHandler);
        gameHud.create();
        
        // create the background manager
        bgManager = new BackgroundManager(screenWidth, screenHeight);
        bgManager.create();
        
        // create effects 
        flashPM = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        flashPM.setColor( 1, 1, 1, 1f );
        flashPM.fillRectangle(0, 0, screenWidth, screenHeight);
        flashTexture = new Texture (flashPM);
	}

	@Override
	public void render(SpriteBatch bottomLayer, SpriteBatch mainLayer, SpriteBatch topLayer, SpriteBatch effectLayer) 
	{
		camera.update();
		mainLayer.setProjectionMatrix(camera.combined);
		manageChunks();
		
		bottomLayer.begin(); // BOTTOM LAYER START
		bgManager.render(bottomLayer);
		bottomLayer.end(); // BOTTOM LAYER END
		
		mainLayer.begin(); // MAIN LAYER START
		if(Gdx.input.isKeyPressed(Keys.SPACE))
			main.changeInstance(new MainGame(screenWidth, screenHeight, screenResolution, main, mainDeviceHandler));
		
		currentChunk.render(mainLayer);
		player.render(mainLayer, camera.position.x, currentSegment);
		
		if(Gdx.input.isKeyPressed(Input.Keys.BACKSPACE))
		{
			if (!isCameraFrozen)
				isCameraFrozen = true;
			else
				isCameraFrozen = false;
		}
		
		if(nextChunk != null)
			nextChunk.render(mainLayer);
	
		// read comment above camera render update (below)
		mainLayer.end(); // MAIN LAYER END
		
		scoreTimer += Gdx.graphics.getDeltaTime()*10;
		if(scoreTimer > 1 && !player.isDead() && !isPaused)
		{
			addPoint();
			this.scoreTimer = 0;
		}
		if(player.isDead())
		{
			effectLayer.begin();
			effectLayer.draw(flashTexture, 0, 0, screenWidth, screenHeight);
			effectLayer.end();
			
			effectLayer.setColor(1, 1, 1, dynamicFlash);
			dynamicFlash -= 5f * Gdx.graphics.getDeltaTime();
			if (dynamicFlash <= 0f)
				dynamicFlash = 0f;
		}
		// PAUSE SCREEN TINT
		if(isPaused)
		{
			effectLayer.setColor(0, 0, 0, 0.30f);

			effectLayer.begin();
			effectLayer.draw(flashTexture, 0, 0, screenWidth, screenHeight);
			effectLayer.end();
		}
		
		topLayer.begin(); // TOP LAYER START
		gameHud.render(topLayer);
		topLayer.end(); // TOP LAYER END
		
		// we have to update the camera x co-ordinate and then parse it into the players render method
		// since updating the players x co-ordinate directly causes image crawl
		cameraAnimationRender();
		if(!isCameraFrozen && !isPaused)
			camera.position.add(dynamicSpeed * Gdx.graphics.getDeltaTime(), 0, 0);
	}
	
	/**
	 * Manage chunks method decides when to dispose, generate and activate world chunks.
	 * When the chunk is activated then that chunks segments are added to the render loop.
	 */
	public void manageChunks()
	{
		// if there are no currently created chunks, then simply generate one
		if(currentChunk == null)
		{
			currentChunk = new WorldChunk(screenWidth, screenHeight, screenResolution, (screenHeight/3), 0, player, 0, sLoader);
			chunksCreated+=1;
		}
		
		if(currentChunk.currentSegmentNumber(camera.position.x) == 10 && readyToCreateChunk == true)
		{
			nextChunk = new WorldChunk(screenWidth, screenHeight, screenResolution, currentChunk.getEndHeight(), (screenWidth * 10) * chunksCreated, player, difficulty, sLoader);
			chunksCreated+=1;
			readyToCreateChunk = false;
		}
		
		// if the player has left the current chunk then activate the new chunk and update that to the current chunk
		// the screenWidth/2 creates an offset so the if statement occurs when the camera switches to the new world chunk
		if((currentChunk.currentSegmentNumber(camera.position.x) >= 11))
		{
			pastChunk = currentChunk;
			currentChunk = nextChunk;
			// this keeps the chunk in the render loop (so it does'nt just disappear)
			nextChunk = pastChunk;
			nextChunk.deActivate();
		}
		
		// if the current segment is not active, then activate it and deaactivate the old one
		// the >>>>> 2.8 <<<< thing is weird. basically its making sure that the active states switch
		// perfectly as the player sprite glides onto the next segment
		if(currentChunk.currentSegmentNumber((float) (camera.position.x-player.getWidth()*2.8))-1 != -1)
		{
			if(!currentChunk.getStateOfSegment(currentChunk.currentSegmentNumber((float) (camera.position.x-player.getWidth()*2.8))-1))
			{
				// activate the segment thta the player is about to enter
				currentChunk.activateSegment(currentChunk.currentSegmentNumber((float) (camera.position.x-player.getWidth()*2.8))-1);
				currentSegment = currentChunk.getActiveSegment();
				
				// activate the previous segment, we have no need for it now
				// make sure that the sgement exists
				if(currentChunk.currentSegmentNumber((float) (camera.position.x-player.getWidth()*2.8))-2 != -1)
					currentChunk.deActivateSegment(currentChunk.currentSegmentNumber((float) (camera.position.x-player.getWidth()*2.8))-2);
			}
		}
		
		if(currentChunk.currentSegmentNumber(camera.position.x) == 2 && readyToCreateChunk == false)
		{
			readyToCreateChunk = true;
			// disposes the past chunk (Which at this point, is the same as nextChunk)
			pastChunk.dispose();
		}
	}
	
	/**
	 * Freezes the camera at the current position
	 */
	public void freezeCamera()
	{
		this.isCameraFreezing = true; 
		gameHud.gameOver();
		bgManager.pause();
	}
	/**
	 * THis creates an animation of slowing down when the camera is frozen. Make sthe game look smoother
	 */
	public void cameraAnimationRender()
	{
		if(isCameraFreezing)
		{
			this.dynamicSpeed -= (CAMERA_SPEED*4) * Gdx.graphics.getDeltaTime();
			if(dynamicSpeed <= 0)
			{
				this.isCameraFrozen = true;
				this.isCameraFreezing = false;
			}
		}
	}
	
	/**
	 * Adds a point to the overall score
	 */
	public void addPoint()
	{
		gameHud.addPoint();
	}
	/**
	 * Get the game score
	 * @return
	 */
	public int getScore()
	{
		return gameHud.getScore();
	}

	@Override
	public void pause() 
	{
		currentChunk.pause();
		bgManager.pause();
		gameHud.pause();
		this.isPaused = true;
		player.pause();
	}

	@Override
	public void resume() 
	{
		currentChunk.resume();
	}
	public void resumeGame()
	{
		bgManager.resume();
		gameHud.pause();
		this.isPaused = false;
		player.resume();
	}

	@Override
	public void dispose() 
	{
		if(currentChunk != null)
			currentChunk.dispose();
		if(nextChunk != null)
			currentChunk.dispose();
		if(pastChunk != null)
			pastChunk.dispose();
		
		// dispose flash effect
		flashTexture.dispose();
		
		bgManager.dispose();
		gameHud.dispose();
	}
}
