package Entities.world_entites;

import utility.GameAssets;
import utility.MathsLibrary;

import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Entities.Player;
import Entities.entitie;

public class Rock_medium extends entitie{
	
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
	Sprite rockSprite;

	public Rock_medium(int screenWidth, int screenHeight, String screenResolution, float xPos, float yPos, float angle, Player player)
	{
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.screenResolution = screenResolution;
		this.player = player;
		this.yPos = yPos;
		this.xPos = xPos;
		this.angle = angle;
		
		this.width = screenWidth/6;
		this.height = screenHeight/5;
	}

	@Override
	public void create() {
		rockTexture = GameAssets.getTextureRegion(GameAssets.Tr_Entitie_Rock_Medium_1);
		rockTexture.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		rockSprite = new Sprite(rockTexture);
		rockSprite.setSize(width, height);
		
		spawnHeight = MathsLibrary.randomNumber(yPos, yPos-rockSprite.getHeight()/3);
		// the screenHeihgt/100 thing is to get past the small gap left at the top of the flat_grass.png
		rockSprite.setPosition(xPos, ((screenHeight - (spawnHeight) -(screenHeight/3)*2) + (rockSprite.getHeight()/3)*2) + screenHeight/100);
		rockSprite.setRotation(angle);
		if(MathsLibrary.randomNumber(0, 1) == 1)
			rockSprite.setFlip(true, false);
		}

	@Override
	public void render(SpriteBatch batch) {
		
		rockSprite.draw(batch);
		
		if(isActive)
		{
			playerPosX = player.getPosFrontX();
			playerPosY = player.getPosBottomY();
			
			if(playerPosX >= xPos && playerPosX < xPos+(rockSprite.getWidth() + player.getWidth()/2))
			{
				aboveRock = true;
				if(playerPosY < spawnHeight+(rockSprite.getHeight()) - player.getHeight()/6)
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
		return spawnHeight+height;
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
