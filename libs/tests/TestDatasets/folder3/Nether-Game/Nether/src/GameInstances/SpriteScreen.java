package GameInstances;

import utility.GameAssets;
import utility.IActivityRequestHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.wockawocka.nether.GameInstance;
import com.wockawocka.nether.NetherMain;

public class SpriteScreen extends GameInstance{
	private final int MAX_TIME = 4;
	private float timer = 0;
	
	int screenWidth, screenHeight;
	NetherMain main;

	TextureRegion companyLogo, companyText_1, companyText_2, bg;
	IActivityRequestHandler mainDeviceHandler;
	
	public SpriteScreen(int screenWidth, int screenHeight, NetherMain main, IActivityRequestHandler mainDeviceHandler)
	{
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.main = main;
		this.mainDeviceHandler = mainDeviceHandler;
	}

	@Override
	public void create() {
		
		companyLogo = GameAssets.getTextureRegion(GameAssets.Tr_Company_Logo);
		companyText_1 = GameAssets.getTextureRegion(GameAssets.Tr_Company_LogoText_1);
		companyText_2 = GameAssets.getTextureRegion(GameAssets.Tr_Company_LogoText_2);
		bg = GameAssets.getTextureRegion(GameAssets.Tr_Company_Filler);
	}

	@Override
	public void render(SpriteBatch bottomLayer, SpriteBatch mainLayer,
			SpriteBatch topLayer, SpriteBatch effectsLayer) {
		
		
		topLayer.begin();
	
		topLayer.draw(bg, 0, 0, screenWidth, screenHeight);
		topLayer.draw(companyLogo, screenWidth/2 - 100, screenHeight - screenHeight/3 - 100);
		topLayer.draw(companyText_1, screenWidth/2 - 250, screenHeight - screenHeight/3 - 200);
		topLayer.draw(companyText_2, screenWidth/2 , screenHeight - screenHeight/3 - 200);

		topLayer.end();
		
		// increments 1 second at a time
		timer += 1*Gdx.graphics.getDeltaTime();
		
		// timer counts the life of the sprite screen
		if(timer >= MAX_TIME)
		{
			main.changeInstance(new MainMenu(screenWidth, screenHeight, main, mainDeviceHandler));
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
	public void dispose() {
		// THE RESOURCES ARE DISPOSED
		// THEY ARE GONE FROM MEMORY UNTIL THE GAME IS RELAUNCHED
		companyLogo.getTexture().dispose();
		companyText_1.getTexture().dispose();
		companyText_2.getTexture().dispose();
		bg.getTexture().dispose();
	}

}
