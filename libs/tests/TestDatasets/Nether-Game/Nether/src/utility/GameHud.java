package utility;

import Entities.Player;
import GameInstances.MainGame;
import GameInstances.MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.wockawocka.nether.NetherMain;

public class GameHud {
	
	boolean isGameOver = false, isPaused = false;
	public int score = 0;
	BitmapFont font;
	NetherMain main;
	boolean scoreUploaded = false;
	
	// sounds
	Sound buttonClick, punchSound;
	boolean isClicking = false, deadPlayed = false;
		
	// score board
	float gameOverEndPosY, scoreBoardEndPosY, buttonEndPosY;
	float gameOverPosY, scoreBoardPosY, buttonPosY;

	TextureRegion gameOverTexture, scoreContainerTexture, buttonReplayTexture, buttonMenuTexture, buttonSignInTexture,
	playButtonSmall, pauseButtonSmall, menuButtonSmall;
	
	MainGame mainGame;
	IActivityRequestHandler mainDeviceHandler;
	
	int screenWidth, screenHeight;
	String screenResolution;
	
	public GameHud(int screenWidth, int screenHeight, String screenResolution, NetherMain main, MainGame mainGame, IActivityRequestHandler mainDeviceHandler)
	{
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.screenResolution = screenResolution;
		this.main = main;
		
		// START POSITION
		gameOverPosY = screenHeight*2;
		scoreBoardPosY = - screenHeight/2;
		buttonPosY =  - screenHeight/6;
		
		// END POSITIONS
		gameOverEndPosY = screenHeight - (screenHeight/9)*2;
		scoreBoardEndPosY = screenHeight - (screenHeight/4) - screenHeight/2;
		buttonEndPosY = screenHeight/25;
		
		this.mainGame = mainGame;
		this.mainDeviceHandler = mainDeviceHandler;
	}
	
	public void create()
	{
		// create the font
		font = GameAssets.walshes;
		font.setScale(1, 1);
		
		// GRAB TEXTURES
		scoreContainerTexture = GameAssets.getTextureRegion(GameAssets.Tr_Hud_ScoreBoard);	
		gameOverTexture = GameAssets.getTextureRegion(GameAssets.Tr_Hud_GameOverLogo);
		buttonReplayTexture = GameAssets.getTextureRegion(GameAssets.Tr_Hud_Button_Replay);
		buttonMenuTexture = GameAssets.getTextureRegion(GameAssets.Tr_Hud_Button_Menu);
		playButtonSmall =  GameAssets.getTextureRegion(GameAssets.Tr_Hud_Button_Play_Small);
		pauseButtonSmall =  GameAssets.getTextureRegion(GameAssets.Tr_Hud_Button_Pause_Small);
		menuButtonSmall =  GameAssets.getTextureRegion(GameAssets.Tr_Hud_Button_Menu_Small);
		buttonSignInTexture = GameAssets.getTextureRegion(GameAssets.Tr_Hud_Button_SignIn);
		buttonClick = GameAssets.Sound_Button_Click;
		punchSound = GameAssets.Sound_Punch;
	}
	
	public void render(SpriteBatch hudBatch)
	{
		float delta = Gdx.graphics.getDeltaTime();
		float fingerPosX = Gdx.input.getX(0);
		float fingerPosY = screenHeight - Gdx.input.getY(0);
		
		if(!scoreUploaded && mainDeviceHandler.getSignedInGPGS())
		{
			mainDeviceHandler.submitScoreGPGS(score);
			scoreUploaded = true;
			unlockAchievements();
		}
		
		// PAUSE 
		if(!isGameOver)
		{
			if(!isPaused)
			{
				hudBatch.draw(pauseButtonSmall, screenWidth - screenWidth/12 - screenWidth/40, screenHeight - screenHeight/8 - screenWidth/40, screenWidth/12, screenHeight/8);
				if(Gdx.input.isTouched())
				{
					// IF THE FINGER TOUCHES THE BUTTON
					if(fingerPosX >= screenWidth - screenWidth/12 - screenWidth/40 && fingerPosX <= (screenWidth - screenWidth/12 - screenWidth/40) + screenWidth/12)
						if(fingerPosY >= screenHeight - screenHeight/8 - screenWidth/80 && fingerPosY <= (screenHeight - screenHeight/8 - screenWidth/40) + screenHeight/8)
						{
							if(!isClicking)
								buttonClick.play(0.75f);
							isClicking = true;
							main.pause();
							isPaused = true;
						}
				}
			}
			
			if(isPaused){
				hudBatch.draw(playButtonSmall, screenWidth - screenWidth/6 - screenWidth/30, screenHeight - screenHeight/8 - screenWidth/40, screenWidth/12, screenHeight/8);
				hudBatch.draw(menuButtonSmall, screenWidth - (screenWidth/6)*2 + screenWidth/60, screenHeight - screenHeight/8 - screenWidth/40,  screenWidth/12, screenHeight/8);
				font.draw(hudBatch, "Paused", screenWidth/2 - font.getSpaceWidth()*6, screenHeight/2 + font.getCapHeight());
				if(Gdx.input.isTouched())
				{
					// IF THE FINGER TOUCHES THE RESUME BUTTON
					if(fingerPosX >= screenWidth - screenWidth/6 - screenWidth/30 && fingerPosX <= (screenWidth - screenWidth/6 - screenWidth/30) + screenWidth/12)
						if(fingerPosY >= screenHeight - screenHeight/8 - screenWidth/40 && fingerPosY <= (screenHeight - screenHeight/8 - 20)  - screenWidth/40 + screenHeight/8)
						{
							if(!isClicking)
								buttonClick.play(0.75f);
							isClicking = true;
							mainGame.resumeGame();
							isPaused = false;
						}
					// IF THE FINGER TOUCHES THE MENU BUTTON
					if(fingerPosX >= screenWidth - (screenWidth/6)*2 + screenWidth/60 && fingerPosX <= screenWidth - (screenWidth/6)*2 + screenWidth/60 + screenWidth/12)
						if(fingerPosY >= screenHeight - screenHeight/8 - screenWidth/40 && fingerPosY <= (screenHeight - screenHeight/8 - 20) + screenHeight/8)
						{
							if(!isClicking)
								buttonClick.play(0.75f);
							isClicking = true;
							main.changeInstance(new MainMenu(screenWidth, screenHeight, main, mainDeviceHandler));
						}
				}
			}
			if(!Gdx.input.isTouched())
				isClicking = false;
		}
		
		
		
		
		if(!isGameOver)
			font.draw(hudBatch, "" + score, screenWidth/40, screenHeight - screenWidth/40);
		
		// SOCRE SCREEN ANIMATION
		if(isGameOver)
		{
			// TEST STUFF
			hudBatch.draw(scoreContainerTexture, screenWidth/2 - (screenWidth/2)/2, scoreBoardPosY, screenWidth/2, screenHeight/2);
			hudBatch.draw(gameOverTexture, screenWidth/2 - (screenWidth/2)/2, gameOverPosY, screenWidth/2, screenHeight/6);
			// buttons
			hudBatch.draw(buttonReplayTexture,(float)(screenWidth/2 - (screenWidth/4)/2.5 - (screenWidth/12) - screenWidth/20), buttonPosY, screenWidth/5, screenHeight/8);
			hudBatch.draw(buttonMenuTexture,(float)(screenWidth/2 - (screenWidth/4)/2.5 + (screenWidth/12) + screenWidth/20), buttonPosY, screenWidth/5, screenHeight/8);
			// sign in
			if(!mainDeviceHandler.getSignedInGPGS())
			{
				hudBatch.draw(buttonSignInTexture, screenWidth/40, screenHeight - screenHeight/10 - screenWidth/40, screenWidth/8, screenHeight/10);
			}
			
			if(gameOverPosY > gameOverEndPosY)
				gameOverPosY -= 600 * delta;
			else
			{
				gameOverPosY = gameOverEndPosY;
				// once the game over logo has reahced its destination
				if(scoreBoardPosY < scoreBoardEndPosY)
					scoreBoardPosY += 1000 * delta;
				else
				{
					scoreBoardPosY = scoreBoardEndPosY;
					// score board stats
					font.draw(hudBatch, "" + score, screenWidth/2 - (screenWidth/5) - (screenWidth/60), screenHeight - scoreBoardPosY - screenHeight/8);
					font.draw(hudBatch, "" + DataManager.getHighScore(), screenWidth/2 - (screenWidth/5) - (screenWidth/60), screenHeight - scoreBoardPosY - screenHeight/4 - screenHeight/11);
					
					// once the score board has reached its destination
					if(buttonPosY < buttonEndPosY)
						buttonPosY += 600 * delta;
					else
						buttonPosY = buttonEndPosY;
				}
			}
			if(Gdx.input.isTouched())
			{
				// REPLAY BUTTON
				if(fingerPosX >= (screenWidth/2 - (screenWidth/4)/2.5 - (screenWidth/12) - screenWidth/20) && fingerPosX <= 
						(screenWidth/2 - (screenWidth/4)/2.5 - (screenWidth/12) - screenWidth/20) + screenWidth/5)
				{
					if(fingerPosY >= buttonPosY && fingerPosY <= buttonPosY + screenHeight/6)
					{
						if(!isClicking)
							buttonClick.play(0.75f);
						isClicking = true;
						main.changeInstance(new MainGame(screenWidth, screenHeight, screenResolution, main, mainDeviceHandler));
					}
				}
				// Main Menu Button
				if(fingerPosX >= (screenWidth/2 - (screenWidth/4)/2.5 + (screenWidth/12) + screenWidth/20) && fingerPosX <= 
						(screenWidth/2 - (screenWidth/4)/2.5 + (screenWidth/12) + screenWidth/20) + screenWidth/5)
				{
					if(fingerPosY >= buttonPosY && fingerPosY <= buttonPosY + screenHeight/6)
					{
						if(!isClicking)
							buttonClick.play(0.75f);
						isClicking = true;
						main.changeInstance(new MainMenu(screenWidth, screenHeight, main, mainDeviceHandler));	
					}
				}
				// sign in
				if(!mainDeviceHandler.getSignedInGPGS())
				{
					if(fingerPosX >= screenWidth/40 && fingerPosX <= screenWidth/40 + screenWidth/8)
					{
						if(fingerPosY <=  screenHeight - (screenWidth/40)  && fingerPosY >= screenHeight - (screenWidth/40 + screenHeight/10))
						{
							if(!isClicking)
								buttonClick.play(0.75f);
							isClicking = true;
							// if the user is signed in, then get the leaderboard. Else sign in
							mainDeviceHandler.loginGPGS();
							// score will be uploaded in the render method
						}
					}
				}
			}
		}
	}
	
	public void pause()
	{
		this.isPaused = true;
	}
	
	public void resume()
	{
		this.isPaused = false;
	}
	
	/**
	 * Initiates the score screen
	 */
	public void gameOver()
	{
		if(!deadPlayed)
		{
			punchSound.play();
			deadPlayed = true;
		}
		
		if(score > DataManager.getHighScore())
			DataManager.saveHighScore(score);
		this.isGameOver = true;
		mainDeviceHandler.showAds(true);
		
		// if the user is signed in, then upload score
		if (mainDeviceHandler.getSignedInGPGS())
		{
			mainDeviceHandler.submitScoreGPGS(score);
			scoreUploaded = true;
		}
		unlockAchievements();
	}
	
	private void unlockAchievements()
	{
		if (mainDeviceHandler.getSignedInGPGS()) 
		{
			mainDeviceHandler.unlockAchievementGPGS("CgkI-9i8lsQbEAIQAQ");
			if(score >= 100)
				mainDeviceHandler.unlockAchievementGPGS("CgkI-9i8lsQbEAIQAg");
			if(score >= 250)
				mainDeviceHandler.unlockAchievementGPGS("CgkI-9i8lsQbEAIQAw");
			if(score >= 400)
				mainDeviceHandler.unlockAchievementGPGS("CgkI-9i8lsQbEAIQBA");
			if(score >= 600)
				mainDeviceHandler.unlockAchievementGPGS("CgkI-9i8lsQbEAIQBQ");
			if(score >= 1000)
				mainDeviceHandler.unlockAchievementGPGS("CgkI-9i8lsQbEAIQBg");
			if(score >= 1500)
				mainDeviceHandler.unlockAchievementGPGS("CgkI-9i8lsQbEAIQBw");
		}
	}
	
	public void addPoint()
	{
		this.score ++;
	}
	
	public int getScore()
	{
		return this.score;
	}
	
	public  void dispose()
	{
	}
}
