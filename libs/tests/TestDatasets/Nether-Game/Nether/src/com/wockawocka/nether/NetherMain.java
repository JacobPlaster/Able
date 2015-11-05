package com.wockawocka.nether;

import java.util.ArrayList;
import java.util.List;

import utility.DataManager;
import utility.GameAssets;
import utility.IActivityRequestHandler;
import GameInstances.MainMenu;
import GameInstances.SpriteScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class NetherMain extends Game {
	
	private int WIDTH, HEIGHT;
	private String RESOLUTION = "hdpi";
	private GameInstance currentInstance;
	public static List<GameInstance> GameInstances = new ArrayList<GameInstance>();
	SpriteBatch bottomLayer, mainLayer, topLayer, effectsLayer;
	private IActivityRequestHandler mainDeviceHandler;
	
	public NetherMain(IActivityRequestHandler mainDeviceHandler)
	{
		this.mainDeviceHandler = mainDeviceHandler;
	}
	
	@Override
	public void create() {		

		mainLayer = new SpriteBatch();
		bottomLayer = new SpriteBatch();
		topLayer = new SpriteBatch();
		effectsLayer = new SpriteBatch();
		
		DataManager.create();
		
		WIDTH  = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		
		// loads the mdpi images
		if(WIDTH  >= 480 && HEIGHT >= 320)
			RESOLUTION = "ldpi";
		// loads the hdpi images
		if(WIDTH >= 800 && HEIGHT >= 480)
			RESOLUTION = "hdpi";
		// loads the xhdpi images
		if(WIDTH >= 960 && HEIGHT >= 640)
			RESOLUTION = "xhdpi";
		// loads the tablet images
		if(WIDTH >= 600 && HEIGHT >= 1024)
			RESOLUTION = "tablet";
		// loads the tablet hdpi images
		if(WIDTH >= 800 && HEIGHT >= 1200)
			RESOLUTION = "tablet-hdpi";
		
		// load all of the assets on creation of the game
		 GameAssets.load(RESOLUTION);
		 System.out.println("RESOLUTION = " + RESOLUTION);
		
		 // create new mainGame
		//GameInstances.add(new MainGame(WIDTH, HEIGHT, RESOLUTION, this));
		 GameInstances.add(new SpriteScreen(WIDTH, HEIGHT, this, mainDeviceHandler));
		 
		// changes the game instance to the game view
		// change this to the sprite screen on deployment
		changeInstance(GameInstances.get(0));
	}

	@Override
	public void dispose() 
	{
		// should be the last thing disposed 
		GameAssets.dispose();
		DataManager.dispose();
		currentInstance.dispose();
		
		// dispose all of the batches
		topLayer.dispose();
		mainLayer.dispose();
		bottomLayer.dispose();
		effectsLayer.dispose();
	}

	@Override
	public void render() 
	{	
		super.render();
			
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		// render the current instance
		currentInstance.render(bottomLayer, mainLayer, topLayer, effectsLayer);	
		
	}
	
	/**
	 * Changes the game instance and calls the create method of the passed in instance. It also disposes
	 * of all of the resources of the past instance.
	 */
	public void changeInstance(GameInstance instance)
	{
		mainDeviceHandler.showAds(false);
		/* If there is already an instance running
		 safely destroy the current version before
		 changing to a new one */
		if(currentInstance != null)
			currentInstance.dispose();
		currentInstance = instance;
		// create the new instances resources
		currentInstance.create();
	}
	

	@Override
	public void resize(int width, int height) 
	{
	}

	@Override
	public void pause() 
	{
		currentInstance.pause();
	}

	@Override
	public void resume() 
	{
		currentInstance.resume();
		// re load all of the assets
		GameAssets.manager.finishLoading();
	}
}
