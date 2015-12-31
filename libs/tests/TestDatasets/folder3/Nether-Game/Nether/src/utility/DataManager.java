package utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class DataManager {
	
	// use prefs since it is cross platform and easy
	static Preferences prefs;
	
	public DataManager()
	{
		
	}

	public static void create()
	{
		// creates new reference to preferences file
		prefs = Gdx.app.getPreferences("My Preferences");
	}
	
	/**
	 * Updates the prefs file with the inputed high score, this overrides any other previous highscore
	 * @param Score
	 */
	public static void saveHighScore(int Score)
	{
		prefs.putInteger("HighScore", Score);
		prefs.flush();
	}
	
	/**
	 * Adds to the number of times played
	 */
	public static void addToTimesPlayed()
	{
		prefs.putInteger("TimesPlayed", getTimesPlayed()+1);
		prefs.flush();
	}
	
	/**
	 * returns the amount of times played
	 * @return
	 */
	public static int getTimesPlayed()
	{
		return prefs.getInteger("TimesPlayed", 0);
	}
	
	/**
	 * return the highscore from the prefs file
	 * @return
	 */
	public static int getHighScore()
	{
		return prefs.getInteger("HighScore", 0);
	}
	
	public static void dispose()
	{
		
	}
}
