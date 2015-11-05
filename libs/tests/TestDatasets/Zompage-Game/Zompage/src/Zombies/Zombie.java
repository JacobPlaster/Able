package Zombies;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class Zombie 
{
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
	
	public abstract void applyHit(float angle, float distance, int damage);
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
	 * Returns true if the Zombies health is below 0
	 */
	public abstract Boolean isDead();
	/**
	 * Gets the angle of the zombie
	 */
	public abstract float getAngle();
	/**
	 * Gets the name of the entitie in string.
	 */
	public abstract String getName();
	/**
	 * Gets the position of the entitie in Vector3 format
	 */
	public abstract Vector3 getPosition();
}
