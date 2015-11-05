package com.WockaWocka.Zompage;

import instances.Instance;
import instances.MenuScreen;
import instances.SpriteScreen;
import instances.Test;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;


public class ZompageGame implements ApplicationListener {
	
	List<Instance> instances = new ArrayList<Instance>();
	Instance currentInstance;
	
	private int VIRTUAL_WIDTH;
	private int VIRTUAL_HEIGHT;
	private float ASPECT_RATIO;
	private String RESOLUTION_FOLDER;
	
	Rectangle viewport;
	
	@Override
	public void create() {		
		
		VIRTUAL_WIDTH = Gdx.graphics.getWidth();
		VIRTUAL_HEIGHT = Gdx.graphics.getHeight();
		ASPECT_RATIO = (float)VIRTUAL_WIDTH/(float)VIRTUAL_HEIGHT;
		
		// determines which image sizes are loaded in for the device
		RESOLUTION_FOLDER = "data/mdpi/";
		
		if(VIRTUAL_WIDTH >= 480 && VIRTUAL_HEIGHT >= 320)
			RESOLUTION_FOLDER = "data/mdpi/";
		
		if(VIRTUAL_WIDTH >= 800 && VIRTUAL_HEIGHT >= 480)
			RESOLUTION_FOLDER = "data/hdpi/";
		
		if(VIRTUAL_WIDTH >= 960 && VIRTUAL_HEIGHT >= 640)
			RESOLUTION_FOLDER = "data/xhdpi/";
		
		if(VIRTUAL_WIDTH >= 600 && VIRTUAL_HEIGHT >= 1024)
			RESOLUTION_FOLDER = "data/tablet-mdpi/";
		
		if(VIRTUAL_WIDTH >= 800 && VIRTUAL_HEIGHT >= 1200)
			RESOLUTION_FOLDER = "data/tablet-hdpi/";

		// Adds instances to the load method
		instances.add(new Test(this, VIRTUAL_WIDTH, VIRTUAL_HEIGHT, RESOLUTION_FOLDER, ASPECT_RATIO));
		instances.add(new SpriteScreen());
		instances.add(new MenuScreen());
		// set the current instance (this should be sprite when deployed)
		// set to test for now 
		changeInstance(instances.get(0));
	}
	
	public void changeInstance(Instance instance)
	{
		if(currentInstance != null)
		{
			currentInstance.dispose();
			currentInstance = instance;
		} else
		{
			currentInstance = instance;
		}
		System.out.println("Current instance = " + currentInstance.getName());
		currentInstance.create();	
	}

	@Override
	public void dispose() {
		currentInstance.dispose();
	}

	@Override
	public void render() {		
    	currentInstance.render();
    	if(Gdx.input.isKeyPressed(Keys.ESCAPE))
    		changeInstance(instances.get(0));
	}

	@Override
	public void resize(int width, int height) {
		currentInstance.resize(width, height);
	}

	@Override
	public void pause() {
		currentInstance.pause();
	}

	@Override
	public void resume() {
		currentInstance.resume();
	}
}
