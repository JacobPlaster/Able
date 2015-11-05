package UserInterface;

import Entities.Entitie;
import Entities.Player;
import Entities.TurretStandard;
import Utils.MathsLibrary;
import Zombies.Zombie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class TurretHud 
{
	private int turretCost = 250;
	
	// Drawing turretButton
	Texture turretButtonTexture, turretButtonPressedTexture;
	Sprite turretButton;
	float tbPosX, tbPosY;
	float distFromEdge  = 5;
	float tbInnerPosX = distFromEdge + distFromEdge;
	float tbInnerPosY = distFromEdge + distFromEdge;
	Sprite tbSprite, tbSpritePressed;
	Boolean isTbPressed = false;
	Boolean inValidTurret = false;
	Boolean active = true;
	// Utilities
	MathsLibrary maths = new MathsLibrary();
	Array<Entitie> entities;
	Player player;
	Array<Zombie> zombies;
	public String resolution;
	HealthHud hud;
	
	/**
	 * Spawns an image icon at the top of the screen which spawns a turret when pressed, this is only way
	 * to spawn a turret in-game.
	 * 
	 * @param Decides which resolution files to load
	 * @param Array containing all existing entities
	 * @param The currently active player
	 * @param Array containing all existing zombies
	 * @param hud to allow access to the wallet
	 */

	public TurretHud(String res, Array<Entitie> entities, Player player, Array<Zombie> zombies, HealthHud hud)
	{
		this.entities = entities;
		this.player = player;
		this.zombies = zombies;
		this.resolution = res;
		this.hud = hud;
	}
	
	public void create()
	{
		turretButtonTexture = new Texture(resolution + "turretButton.png");
		turretButtonPressedTexture = new Texture(resolution + "turretButtonPressed.png");
		tbSprite = new Sprite(turretButtonTexture);
		tbSpritePressed = new Sprite(turretButtonPressedTexture);
		turretButton = tbSprite;
		
		tbPosX = distFromEdge;
		tbPosY = Gdx.graphics.getHeight() - tbSprite.getHeight() - distFromEdge;
		
	}
	
	public void render(SpriteBatch batch)
	{	 
		
		if(hud.getMoney() < turretCost)
		{
			active = false;
			turretButton = tbSpritePressed;
		}
		else 
		{
			active = true;
			turretButton = tbSprite;
		}
	
		if(active)
		{
			// if the co-ordinates of the buttona re pressed then switch omage to the pressed image texture and spawn a turret
			 if(Gdx.input.isTouched(0) || Gdx.input.isTouched(1))
	         {
				 if((Gdx.input.getX(0) > distFromEdge && Gdx.input.getX(0) < distFromEdge + tbSprite.getWidth()) && (Gdx.input.getY(0) > distFromEdge && Gdx.input.getY(0) < distFromEdge + tbSprite.getHeight()))
				 {
					 isTbPressed = true;
					 turretButton = tbSpritePressed;
				 } else
				 {
					 turretButton = tbSprite;
					 isTbPressed = false;
				 }
	         } else
	         {
	        	 isTbPressed = false;
	        	 turretButton = tbSprite;
	         }
			 
			 
			 if(isTbPressed() && active)
				{
					if(!inValidTurret)
						spawnTurret(player.getPosition());
					inValidTurret = true;
				} else
					inValidTurret = false;
		}
		                
		 batch.draw(turretButton, tbPosX, tbPosY); 
	}
	
	public void spawnTurret(Vector3 pos)
	{
		entities.add(new TurretStandard(pos, zombies));
		hud.subMoney(turretCost);
	}

	public void pause()
	{
		
	}
	
	public void resume()
	{
		
	}
	
	public Boolean isTbPressed()
	{
		return isTbPressed;
	}
	
	public void dispose()
	{
		turretButtonTexture.dispose();
		turretButtonPressedTexture.dispose();
	}
}
