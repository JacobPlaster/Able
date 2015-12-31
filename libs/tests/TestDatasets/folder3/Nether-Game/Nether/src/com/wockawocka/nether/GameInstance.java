package com.wockawocka.nether;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameInstance {
	
	/**
	 * Create method initiates the game class and crates all of the
	 * resources that the instance will use. Always call this method when creating a 
	 * new instance and DO NOT call the dispose method before all resources have been
	 * created.
	 */
	public abstract void create();
	/**
	 * Initiates the render cycle once. This method should be called constantly whilst 
	 * this instant is active. The SpriteBatch should be parsed into the method this
	 * means that the sprite batch doesn't need to be created every time this instance
	 * is created.
	 * 
	 * Sprite batches:
	 * bottomLayer controls the background (such as the mountains)
	 * mainLayer controls the player view (such as the rocks and player)
	 * topLayer controls the HUD
	 * 
	 * @param All ready created spritebatch
	 */
	public abstract void render(SpriteBatch bottomLayer, SpriteBatch mainLayer, SpriteBatch topLayer, SpriteBatch effectsLayer);
	/**
	 * Pauses the entire instance and causes for the render loop to stop.
	 * In order to resume the instance the resume method needs to be called
	 */
	public abstract void pause();
	/**
	 * Resume method should only be called once the game has been paused and
	 * the instance is ready to resume the render cycle. This method will not
	 * have anny affect if the instance is not paused
	 */
	public abstract void resume();
	/**
	 * Disposes method disposes of any created resources within the instance, this should
	 * always be called when the instance is deleted and a new instance is created. This method
	 * should no the called if the instance has'nt been created().
	 */
	public abstract void dispose();
}
