package Entities.world_entites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Entities.Player;
import Entities.entitie;
import utility.GameAssets;

public class Half_Tree_1 extends entitie
{
	private int screenHeight;
	private int screenWidth;
	private String screenResolution;
	private Player player;
	private float xPos, yPos, width, height;
	
	private boolean isActive = false;
	private boolean isAbove = false;
	
	TextureRegion bottomSeg, topSeg;
	Sprite topLeftBlock;

	public Half_Tree_1(int screenWidth, int screenHeight, String screenResolution, float xPos, float yPos, float angle, Player player)
	{
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.screenResolution = screenResolution;
		this.player = player;
		this.yPos = yPos;
		this.xPos = xPos;
		this.width = screenWidth/7;
		this.height = screenHeight/3;
	}

	@Override
	public void create() {
		// havent implemented the graphics for the rocks8
		bottomSeg = GameAssets.getTextureRegion(GameAssets.Tr_Entitie_WoodBlock_1);
		topSeg = GameAssets.getTextureRegion(GameAssets.Tr_Entitie_WoodBlockTop_1);
		
		topLeftBlock = new Sprite(topSeg);
		topLeftBlock.setSize(width/2, screenHeight/8);
		topLeftBlock.setFlip(true, false);
		topLeftBlock.setPosition(xPos, -yPos + (screenHeight/3)*2);
	}

	@Override
	public void render(SpriteBatch batch) {
		
		// top layer
		topLeftBlock.draw(batch);	
		batch.draw(topSeg, xPos+width/2 -1, yPos - (yPos - screenHeight/3)*2, width/2, screenHeight/8);
		// 2nd layer
		batch.draw(bottomSeg, xPos+1, (yPos + screenHeight/8) - (yPos - screenHeight/3)*2-1, width/2, screenHeight/8);	
		batch.draw(bottomSeg, xPos+width/2 -2, (yPos + screenHeight/8) - (yPos - screenHeight/3)*2-1, width/2, screenHeight/8);
		// 3rd layer
		batch.draw(bottomSeg, xPos+1, (yPos + (screenHeight/8)*2) - (yPos - screenHeight/3)*2-2, width/2, screenHeight/8);	
		batch.draw(bottomSeg, xPos+width/2 -2,  (yPos + (screenHeight/8)*2) - (yPos - screenHeight/3)*2-2, width/2, screenHeight/8);
		// 4th layer
		batch.draw(bottomSeg, xPos+1, (yPos + (screenHeight/8)*3) - (yPos - screenHeight/3)*2-3, width/2, screenHeight/8);	
		batch.draw(bottomSeg, xPos+width/2 -2,  (yPos + (screenHeight/8)*3) - (yPos - screenHeight/3)*2-3, width/2, screenHeight/8);
		// 5th layer
		batch.draw(bottomSeg, xPos+1, (yPos + (screenHeight/8)*4) - (yPos - screenHeight/3)*2 -3, width/2, screenHeight/8);	
		batch.draw(bottomSeg, xPos+width/2 -2,  (yPos + (screenHeight/8)*4) - (yPos - screenHeight/3)*2-3, width/2, screenHeight/8);
		
		if (isActive)
		{
			float playerX = player.getPosFrontX();
			float playerY = player.getPosBottomY() - player.getHeight()/3;
			
			if(playerX > xPos && playerX < xPos + width + player.getWidth()/2)
			{
				isAbove = true;
				if(playerY < yPos)
					player.killPlayer();
			} else
				isAbove = false;
		}
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void activate() {
		this.isActive = true;
	}

	@Override
	public void deActivate() {
		this.isActive = false;
	}

	@Override
	public void dispose() {	
	}
	
	/**
	 * Chechs tot see if the player is currently above the half tree
	 * @return
	 */
	public Boolean isAbove()
	{
		return this.isAbove;
	}

	/**
	 * Returns the height of the first rock in the gap
	 */
	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return yPos;
	}

	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return width;
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return xPos;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return yPos;
	}

}
