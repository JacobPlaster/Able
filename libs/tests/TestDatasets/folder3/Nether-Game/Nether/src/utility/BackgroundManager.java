package utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BackgroundManager {
	
	int screenWidth, screenHeight;
	
	Sprite backgroundSprite, mountainSprite_1, mountainSprite_2, mountainSprite_3;
	TextureRegion backgroundImage, mountainTexture;
	Boolean isActive = true;
	
	public BackgroundManager(int screenWidth, int screenHeight)
	{
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}
	
	public void create()
	{	
		  // create backdrop
        backgroundImage = GameAssets.getTextureRegion(GameAssets.Tr_Background_BackColor);
        backgroundSprite = new Sprite(backgroundImage);
        backgroundSprite.setSize(screenWidth+2, screenHeight);
        
        // create mountains
        mountainTexture = GameAssets.getTextureRegion(GameAssets.Tr_Background_Mountains_1);
        mountainSprite_1 = new Sprite(mountainTexture);
        mountainSprite_1.setSize(screenWidth/2, screenHeight);
        
        mountainSprite_2 = new Sprite(mountainTexture);
        mountainSprite_2.setSize(screenWidth/2, screenHeight);
        mountainSprite_2.setPosition(screenWidth/2, 0);
        
        mountainSprite_3 = new Sprite(mountainTexture);
        mountainSprite_3.setSize(screenWidth/2, screenHeight);
        mountainSprite_3.setPosition(screenWidth, 0);
	}
	
	public void render(SpriteBatch backgroundBatch)
	{
		backgroundSprite.draw(backgroundBatch);
		mountainSprite_1.draw(backgroundBatch);
		mountainSprite_2.draw(backgroundBatch);
		mountainSprite_3.draw(backgroundBatch);
		
		if(isActive)
		{
			mountainSprite_1.translateX(-100 * Gdx.graphics.getDeltaTime()/5);
			mountainSprite_2.translateX(-100 *Gdx.graphics.getDeltaTime()/5);
			mountainSprite_3.translateX(-100 *Gdx.graphics.getDeltaTime()/5);
			//backgroundBatch.draw(mountainsTexture, 0, 0, screenWidth, screenHeight);
			
			if(mountainSprite_1.getX() + mountainSprite_1.getWidth() <= 0)
				mountainSprite_1.setPosition(screenWidth, 0);
			if(mountainSprite_2.getX() +  mountainSprite_2.getWidth() <= 0)
				mountainSprite_2.setPosition(screenWidth, 0);
			if(mountainSprite_3.getX() +  mountainSprite_3.getWidth() <= 0)
				mountainSprite_3.setPosition(screenWidth, 0);
		}
	}
	
	public void pause()
	{
		isActive = false;
	}
	
	public void resume()
	{
		isActive = true;
	}
	
	public void dispose()
	{
	}
}
