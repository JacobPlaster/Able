package utility;

public interface IActivityRequestHandler {
	   public void showAds(boolean show);
	   public boolean getSignedInGPGS();
	   public void loginGPGS();
	   public void submitScoreGPGS(int score);
	   public void unlockAchievementGPGS(String achievementId);
	   public void getLeaderboardGPGS();
	   public void getAchievementsGPGS();
	}
