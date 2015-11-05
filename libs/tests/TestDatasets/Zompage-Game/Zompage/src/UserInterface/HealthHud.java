package UserInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class HealthHud 
{	
	// health bar
	Texture healthFullTexture, healthDamagedTexture, healthDeadTexture;
	Sprite hudSprite;
	float x, y;
	int health = 2;
	double hitTimer;
	public String resolution;
	private int money = 500;
	// money
	BitmapFont font = new BitmapFont();
	
	/**
	 * Creates a health bar in the top right hand corner of the screen.
	 * This should be created in the instance and passed into the layer class.
	 * 
	 * This also controls the players health.
	 * 
	 * @param Decides which resolution files to load
	 */
	public HealthHud(String res)
	{
		this.resolution = res;
	}

	public void create()
	{
		
		//font = new BitmapFont(Gdx.files.internal("data/fonts/welshes.fnt"),
		  //       Gdx.files.internal("data/fonts/welshes.png"), false);
		 
		 
		// ************************************************************* //
		// ********************* health images ************************* //
		// ************************************************************* //
		healthFullTexture = new Texture(resolution + "health_bar.png");
		healthDamagedTexture = new Texture(resolution + "health_bar_2.png");
		healthDeadTexture = new Texture(resolution + "health_bar_3.png");
		
		hudSprite = new Sprite(healthFullTexture);
		
		x = Gdx.graphics.getWidth() - hudSprite.getWidth() - 5;
		y = Gdx.graphics.getHeight() - hudSprite.getHeight() -5f;
		
	}
	
	public void render(SpriteBatch batch)
	{
		batch.draw(hudSprite, x, y);
		font.draw(batch, "" + money, 200, Gdx.graphics.getHeight() - 10);
		
		// acts ass a timer, this stops the zombie from killing the player instantly
		if(hitTimer > 0)
		{
			hitTimer -= (0.2 * Gdx.graphics.getDeltaTime());
		}
		
	}
	
	/**
	 * Applies damage to the health bar hud. damage subtracts 1 from total health (3)
	 * and changes the health bar hud image according to the total health.
	 */
	public void damage()
	{
		if(hitTimer <= 0)
		{
			health --;
			
			if(health == 1)
				hudSprite = new Sprite(healthDamagedTexture);
			if(health <= 0)
				hudSprite = new Sprite(healthDeadTexture);
			
			hitTimer = 0.2;
		}
	}
	
	/**
	 * Resets the health to full and changes the hud according to this.
	 */
	public void heal()
	{
		hudSprite = new Sprite(healthFullTexture);
		health = 2;
	}

	/**
	 * Returns the overall health of the player
	 * @returns int health
	 */
	public int getHealth()
	{
		return health;
	}
	
	/**
	 * Adds money to the players wallet
	 * @param amount
	 */
	public void addMoney(int amount)
	{
		this.money += amount;
	}
	
	/**
	 * Subtracts money from the players wallet
	 * @param amount
	 */
	public void subMoney(int amount)
	{
		this.money -= amount;
	}
	
	/**
	 * Retrieves the total amount of money in the players wallet
	 * @return int amount
	 */
	public int getMoney()
	{
		return this.money;
	}
	
	public void pause()
	{
		
	}
	
	public void resume()
	{
		
	}
	
	public void dispose()
	{
		healthFullTexture.dispose();
		healthDamagedTexture.dispose();
	}
}
