package GameInstances;

import utility.DataManager;
import utility.GameAssets;
import utility.IActivityRequestHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.wockawocka.nether.GameInstance;
import com.wockawocka.nether.NetherMain;

public class MainMenu extends GameInstance{
	
	int screenWidth, screenHeight;
	NetherMain main;
	Texture autoFiller;
	TextureRegion bgMountain, bg, gameLogo, scoresButton, startButton, flatGrass;
	TextureRegion ws_title, ws_text, ws_action, ws_filler;
	Sprite flatGrassSprite;
	IActivityRequestHandler mainDeviceHandler;
	
	Sound buttonClick;
	boolean isClicking = false;
	boolean firstTime = false;
	
	public MainMenu(int screenWidth, int screenHeight, NetherMain main, IActivityRequestHandler mainDeviceHandler)
	{
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.main = main;	
		this.mainDeviceHandler = mainDeviceHandler;
	}
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		flatGrass = GameAssets.getTextureRegion(GameAssets.Tr_Segment_FlatGrass);
		autoFiller = GameAssets.manager.get(GameAssets.Segment_Auto_Filler);
		bgMountain = GameAssets.getTextureRegion(GameAssets.Tr_Background_Mountains_1);
		bg = GameAssets.getTextureRegion(GameAssets.Tr_Background_BackColor);
		gameLogo = GameAssets.getTextureRegion(GameAssets.Tr_MainMenu_GameLogo);
		scoresButton = GameAssets.getTextureRegion(GameAssets.Tr_MainMenu_Button_Scores);
		startButton = GameAssets.getTextureRegion(GameAssets.Tr_MainMenu_Button_Start);
		buttonClick = GameAssets.Sound_Button_Click;
		
		flatGrassSprite = new Sprite(flatGrass);
		flatGrassSprite.setFlip(false, true);
		flatGrassSprite.setSize(screenWidth/4, screenHeight/16);
		
		if(DataManager.getTimesPlayed() <= 0)
		{
			firstTime = true;
			ws_title = GameAssets.getTextureRegion(GameAssets.Tr_Welcome_Screen_Title);
			ws_text = GameAssets.getTextureRegion(GameAssets.Tr_Welcome_Screen_Text);
			ws_action = GameAssets.getTextureRegion(GameAssets.Tr_Welcome_Screen_Action);
			ws_filler = GameAssets.getTextureRegion(GameAssets.Tr_Welcome_Screen_Filler);
		}
	}

	@Override
	public void render(SpriteBatch bottomLayer, SpriteBatch mainLayer,
			SpriteBatch topLayer, SpriteBatch effectsLayer) {
		
		float fingerPosX = Gdx.input.getX();
		float fingerPosY = screenHeight - Gdx.input.getY();
		
		topLayer.begin();
		
		// bg
		topLayer.draw(bg, 0, 0, screenWidth+2, screenHeight);
		topLayer.draw(bgMountain, 0, 0, screenWidth/2, screenHeight);
		topLayer.draw(bgMountain, screenWidth/2, 0, screenWidth/2, screenHeight);
		
		// segment floor
		flatGrassSprite.setPosition(0, screenHeight/8);
		flatGrassSprite.draw(topLayer);
		flatGrassSprite.setPosition(screenWidth/4, screenHeight/8);
		flatGrassSprite.draw(topLayer);
		flatGrassSprite.setPosition((screenWidth/2), screenHeight/8);
		flatGrassSprite.draw(topLayer);
		flatGrassSprite.setPosition((screenWidth/4)*3, screenHeight/8);
		flatGrassSprite.draw(topLayer);
		topLayer.draw(autoFiller, 0, -screenHeight/8, screenWidth, screenHeight/4);
		
		
		// UI
		topLayer.draw(gameLogo, screenWidth/2 - screenWidth/4, screenHeight - screenHeight/4, screenWidth/2, screenHeight/6);
		topLayer.draw(startButton, screenWidth/2 - (screenWidth/5)/2 - screenWidth/7, screenHeight/5, screenWidth/5, screenHeight/8);
		topLayer.draw(scoresButton, screenWidth/2 - (screenWidth/5)/2 + screenWidth/7, screenHeight/5, screenWidth/5, screenHeight/8);
		
		if(ws_text != null)
		{
			if(screenWidth > ws_text.getRegionWidth())
			{
				if(ws_title != null && firstTime)
				{
					topLayer.draw(ws_filler, 0, 0, screenWidth, screenHeight);
					topLayer.draw(ws_title, screenWidth/2 - ws_title.getRegionWidth()/2,  screenHeight-screenHeight/10);
					topLayer.draw(ws_text, screenWidth/2 - ws_text.getRegionWidth()/2, screenHeight- screenHeight/2 - ws_text.getRegionHeight()/2);
					topLayer.draw(ws_action, screenWidth/2 - ws_action.getRegionWidth()/2, screenHeight/20 - ws_action.getRegionHeight()/2);
				}
			} else
			{
				if(ws_title != null && firstTime)
				{
					topLayer.draw(ws_filler, 0, 0, screenWidth, screenHeight);
					topLayer.draw(ws_title, screenWidth/2 - (screenWidth/3*2)/2,  screenHeight-screenHeight/8, screenWidth/3*2, screenHeight/12);
					topLayer.draw(ws_text, screenWidth/2 - (screenWidth/5*3)/2, screenHeight- screenHeight/2 - (screenHeight/5*3)/2, screenWidth/5*3, screenHeight/5*3);
					topLayer.draw(ws_action, screenWidth/2 - (screenWidth/5*3)/2, screenHeight/20, screenWidth/5*3, screenHeight/14);
				}
			}
		}
		
		if(Gdx.input.isTouched())
		{
			firstTime = false;
			// play button
			if(fingerPosX >= screenWidth/2 - (screenWidth/5)/2 - screenWidth/7 - screenWidth/10 && fingerPosX <= screenWidth/2 - (screenWidth/5)/2 - screenWidth/7 + screenWidth/5 + screenWidth/10)
			{
				if(fingerPosY >= screenHeight/5 - screenHeight/16 && fingerPosY <= screenHeight/5 + screenHeight/8 + screenHeight/16)
				{
					if(!isClicking)
						buttonClick.play(0.75f);
					isClicking = true;
					main.changeInstance(new MainGame(screenWidth, screenHeight, null, main, mainDeviceHandler));
				}
			}
			// scores button
			if(fingerPosX >= screenWidth/2 - (screenWidth/5)/2 + screenWidth/7 - screenWidth/10 && fingerPosX <= screenWidth/2 - (screenWidth/5)/2 + screenWidth/7 + screenWidth/5 + screenWidth/10)
			{
				if(fingerPosY >= screenHeight/5 - screenHeight/16 && fingerPosY <= screenHeight/5 + screenHeight/8 + screenHeight/16)
				{
					if(!isClicking)
					{
						buttonClick.play(0.75f);
					// if the user is signed in, then get the leaderboard. Else sign in
					if (mainDeviceHandler.getSignedInGPGS() )
						mainDeviceHandler.getLeaderboardGPGS();
					else
						mainDeviceHandler.loginGPGS();
					}
					
					isClicking = true;
				}
			}
		} else
			isClicking = false;
		topLayer.end();
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
		DataManager.addToTimesPlayed();
	}
}
