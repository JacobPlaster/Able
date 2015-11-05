package com.wockawocka.nether;

import utility.IActivityRequestHandler;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.google.android.gms.games.Games;


public class MainActivity extends AndroidApplication implements IActivityRequestHandler, GameHelperListener{
	
	 private static final String PUBLISHER_ID = "pub-6953431910552954";
	 private static final String LEADERBOARD_ID = "CgkI-9i8lsQbEAIQCA";
	 private static final String AD_UNIT_ID_GAME_OVER_BANNER = "ca-app-pub-6953431910552954/8051800024";
	 protected AdView adView;
	 protected View gameView;
	 private GameHelper gameHelper;
	 
	 private final int SHOW_ADS = 1;
	 private final int HIDE_ADS = 0;
	 protected Handler handler = new Handler()
	    {
	        @Override
	        public void handleMessage(Message msg) {
	            switch(msg.what) {
	                case SHOW_ADS:
	                {
	                    adView.setVisibility(View.VISIBLE);
	                    break;
	                }
	                case HIDE_ADS:
	                {
	                    adView.setVisibility(View.GONE);
	                    break;
	                }
	            }
	        }
	    };
	  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        System.out.println("ANDROID MAIN ACTIVITY STARTED");
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        // only 52% of phones support openGL 2.0
        // 96% of phones support openGL 1.1
        cfg.useGL20 = false;
        adView = new AdView(this);
        
        // Do the stuff that initialize() would do for you
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        

        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);
        
        AdView admobView = createAdView(MainActivity.AD_UNIT_ID_GAME_OVER_BANNER);
        layout.addView(admobView);
        View gameView = createGameView(cfg);
        layout.addView(gameView);

        startAdvertising(admobView);
        setContentView(layout);
        
    	gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
    	gameHelper.enableDebugLog(true);
    	gameHelper.setMaxAutoSignInAttempts(0);
    	gameHelper.setup(this);
    }
    
    private AdView createAdView(String AD_UNIT_ID) {
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(AD_UNIT_ID);
        adView.setId(12345);// this is an arbitrary id, allows for relative positioning in createGameView()
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        adView.setLayoutParams(params);
        adView.setBackgroundColor(Color.BLACK);
        return adView;
      }
    
    private View createGameView(AndroidApplicationConfiguration cfg) {
        gameView = initializeForView(new NetherMain(this), cfg);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ABOVE, adView.getId());
        gameView.setLayoutParams(params);
        return gameView;
      }
    
    public void startAdvertising(AdView adView) {
        AdRequest adRequest = new AdRequest.Builder()
       // .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
        //.addTestDevice("AEB3AE31D227A4ED8E2B56BDD5A44066")  // My Moto g
        .build();
        adView.loadAd(adRequest);
      }

    @Override
    public void onResume() {
      super.onResume();
      if (adView != null) adView.resume();
    }

    @Override
    public void onPause() {
      if (adView != null) adView.pause();
      super.onPause();
    }
    
    @Override
	public void onStart(){
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	public void onStop(){
		super.onStop();
		gameHelper.onStop();
	}

    
    @Override
    public void onDestroy() {
      if (adView != null) adView.destroy();
      super.onDestroy();
    }

	@Override
	public void showAds(boolean show) {
		handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}
	
	@Override
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		gameHelper.onActivityResult(request, response, data);
	}

	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSignInSucceeded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getSignedInGPGS() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void loginGPGS() {
		try {
			runOnUiThread(new Runnable(){
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (final Exception ex) {
		}
	}

	@Override
	public void submitScoreGPGS(int score) {
		Games.Leaderboards.submitScore(gameHelper.getApiClient(), LEADERBOARD_ID, score);
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
		Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
	}

	@Override
	public void getLeaderboardGPGS() {
		startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), LEADERBOARD_ID), 100);
	}

	@Override
	public void getAchievementsGPGS() {
		startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 100);
	}
}