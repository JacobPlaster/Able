package com.wockawocka.nether;

import utility.IActivityRequestHandler;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


public class Main implements IActivityRequestHandler{
	private static Main application;
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Dino Sprint";
		cfg.useGL20 = false;
		
	// COMPATABLE
		
		// ldpi
		//cfg.width = 320;
		//cfg.height = 240;
		
		// mdpi
		//cfg.width = 480;
		//cfg.height = 320;
		
		// hdpi
	    //cfg.width = 800;
		//cfg.height = 480;
		
		// xhdpi
		//cfg.width = 960;
		//cfg.height = 720;
		
		// hdpi tablet
		cfg.width = 1280;
		cfg.height = 800;
		
		
		  if (application == null) {
	            application = new Main();
	        }
		  
		new LwjglApplication(new NetherMain(application), cfg);
	}

	@Override
	public void showAds(boolean show) {
		// Do nothing
	}

	@Override
	public boolean getSignedInGPGS() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void loginGPGS() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void submitScoreGPGS(int score) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getLeaderboardGPGS() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAchievementsGPGS() {
		// TODO Auto-generated method stub
		
	}
}
