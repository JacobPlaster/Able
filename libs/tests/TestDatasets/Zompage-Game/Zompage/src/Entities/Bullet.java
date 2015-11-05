package Entities;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class Bullet {
	
	/**
	 * Initialise the entitie and create all resources. This is called within the creation of the entitie
	 */
	public abstract void create();
	/**
	 * Render the entitie, this is used as a render loop
	 * 
	 * @param Pass in the ModelBatch from the super class
	 */
	public abstract void render(ModelBatch batch);
	/**
	 * Pause the indiviual entitie
	 */
	public abstract void pause();
	/**
	 * Resume the entitie
	 */
	public abstract void resume();
	/**
	 * Dispose the entitie
	 */
	public abstract void dispose();
	/**
	 * Retruns true if the bullet has reached maxed range and disposed
	 */
	public abstract Boolean isDead();
	/**
	 * Gets the name of the entitie in string.
	 */
	public abstract String getName();
	/**
	 * Gets the position of the entitie in Vector3 format
	 */
	public abstract Vector3 getPosition();

}
