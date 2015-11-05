package Entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class entitie {
	
	public float hheight;
	
	public abstract void create();
	public abstract void render(SpriteBatch batch);
	public abstract void pause();
	public abstract void resume();
	public abstract void activate();
	public abstract void deActivate();
	public abstract void dispose();
	
	public abstract float getHeight();
	public abstract float getWidth();
	public abstract float getX();
	public abstract float getY();
	
}
