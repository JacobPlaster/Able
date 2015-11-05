package instances;

public abstract class Instance 
{
	// Abstract class to act as a template for the other instances
	// Create all of the resources for the instance, use with a loading screen
	public abstract void create();
	// renders the instance
	public abstract void render();
	// Pause the instance, this is mainly for android
	public abstract void pause();
	// Resume the game, also mainly for android
	public abstract void resume();
	// Resize the instances of the screen
	public abstract void resize(int width, int height);
	// Destroys the game and all of its resources
	public abstract void dispose();
	
	// Return the name of the instance
	public abstract String getName();

}
