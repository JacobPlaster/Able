package Entities.world_entites;

import utility.GameAssets;
import utility.MathsLibrary;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Entities.Player;
import Entities.entitie;

public class Rock_large extends entitie{
	
	private int screenHeight;
	private int screenWidth;
	private String screenResolution;
	private Player player;
	private float xPos, yPos, angle;
	private Boolean isActive = false;
	private Boolean aboveRock = false;
	
	private float width;
	private float height;
	private float playerPosY, playerPosX;
	
	private float spawnHeight;
	
	TextureRegion rockTexture;
	Sprite rock;

	public Rock_large(int screenWidth, int screenHeight, String screenResolution, float xPos, float yPos, float angle, Player player)
	{
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.screenResolution = screenResolution;
		this.player = player;
		this.yPos = yPos;
		this.xPos = xPos;
		this.angle = angle;
		
		this.width = screenWidth/4;
		this.height = screenHeight/3;
	}

	@Override
	public void create() {
		
		rockTexture = GameAssets.getTextureRegion(GameAssets.Tr_Entitie_Rock_Large_1);
		rockTexture.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		rock = new Sprite(rockTexture);
		rock.setSize(width, height);
		
		spawnHeight = MathsLibrary.randomNumber(yPos, yPos+rock.getHeight()/2);
		// the screenHeihgt/100 thing is to get past the small gap left at the top of the flat_grass.png
		rock.setPosition(xPos, ((screenHeight - (spawnHeight) -(screenHeight/3)*2) + (rock.getHeight()/3)*2) + screenHeight/100);
		rock.setRotation(angle);
		
		if(MathsLibrary.randomNumber(0, 1) == 1)
			rock.setFlip(true, false);
		}

	@Override
	public void render(SpriteBatch batch) {
		
		rock.draw(batch);
		
		if(isActive)
		{
			playerPosX = player.getPosFrontX();
			playerPosY = player.getPosBottomY();
			
			if(playerPosX >= xPos && playerPosX < xPos+(rock.getWidth() - player.getWidth()/2))
			{
				aboveRock = true;
				if(playerPosY < spawnHeight + (height/2- player.getHeight()/4))
					player.killPlayer();
			} else
				aboveRock = false;
		}
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void activate() {
		isActive = true;
	}

	@Override
	public void deActivate() {
		isActive = false;
	}

	@Override
	public void dispose() {
	}

	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return spawnHeight - yPos;
	}

	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return spawnHeight + height/3;
	}
	/**
	 * @param input x position
	 * @return true if player is above the rock or interacting with the rock
	 */
	public boolean isAbove(float posX)
	{
		return this.aboveRock;
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return xPos;
	}

}