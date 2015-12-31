package Entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class WorldSegment {
	
	/**
	 * Creates all of the resources for the World segment
	 */
	public abstract void create();
	/**
	 * Adds the World segemnt to the render loop
	 */
	public abstract void render(SpriteBatch batch);
	/**
	 * pauses the world segment
	 */
	public abstract void pause();
	/**
	 * Resumes the world segment from is paused state
	 */
	public abstract void resume();
	/**
	 * Disposes the world segment
	 */
	public abstract void dispose();
	/**
	 * Activates the segments logic, such as collision detection
	 */
	public abstract void activate();
	/**
	 * Returns true if the segment is active
	 * @return Boolean isActive
	 */
	public abstract Boolean isActive();
	/**
	 * Disables the segments logic
	 */
	public abstract void deActivate();
	/**
	 * Sets the start pos to the desired height post creation
	 */
	public abstract void setStartHeight(int position);
	/**
	 * Sets the position of the world segment in the chunk (post creation)
	 */
	public abstract void setPosInChunk(int position);
	/**
	 * Sets the where the segments spawns on the x co-ordinate
	 * @param position
	 */
	public abstract void setWidth(int position);
	/**
	 * Get the position that the segment ends at.
	 * @return End position
	 */
	public abstract int getEndHeight();
	/**
	 * Get the position that the segments starts at.
	 * @return Start position
	 */
	public abstract int getStartPos();
	/**
	 * Returns the actual height of the segment (excluding the auto fill).
	 * @return sizeS
	 */
	public abstract int size();
	/**
	 * get the slope angle of the segment
	 * @return
	 */
	public abstract float slopeAngle();
	/**
	 * Returns the of the parsed in value from the floor
	 * @return boolean true if is above floor
	 */
	public abstract float heightOfFloorAtPosX(float x);
}
