package utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameAssets {
	
	private static String filePath;
	
	public static boolean isLoaded = false;
	
	private final static String segments = "segments/";
	private final static String entities = "entities/";
	private final static String miscellaneous = "miscellaneous/";
	private final static String spritesheets = "spritesheets/";
	
	// Segments
	public static String Segment_Auto_Filler;
	public static String Segment_Flat_Grass;
	public static String Segment_Right_1;
	public static String Segment_Right_2;
	public static String Segment_Wall_1;

	public static String Segment_Hole_Corner_1;
	public static String Segment_Hole_Lining_1;
	
	// Entities
	public static String Tr_Entitie_Rock_Medium_1 = "Tr_Entite_Rock_Medium_1";
	public static String Entitie_Character_Running_SpriteSheet;
	public static String Tr_Entitie_WoodBlock_1 = "Tr_Entitie_WoodBlock_1";
	public static String Tr_Entitie_WoodBlockTop_1 = "Tr_Entitie_WoodBlockTop_1";
	public static String Tr_Entitie_Rock_Large_1 = "Tr_Entitie_Rock_Large_1";
	
	// FONTS
	public static BitmapFont walshes;
	
	// miscellaneous
	private static Texture Background_64x_Misc_SpriteSheet_Texture;
	private static String Background_64x_Misc_SpriteSheet;
	
	public static String Tr_Background_Mountains_1 = "Tr_Background_Mountain_1";
	public static String Tr_Background_BackColor = "Tr_Background_BackColor";
	
	// HUD
	public static String Tr_Hud_GameOverLogo = "Tr_Hud_GameOverLogo";
	public static String Tr_Hud_ScoreBoard = "Tr_Hud_ScoreBoard";
	public static String Tr_Hud_Button_Replay = "Tr_Hud_Button_Replayy";
	public static String Tr_Hud_Button_SignIn = "Tr_Hud_Button_SignIn";
	public static String Tr_Hud_Button_Menu = "Tr_Hud_Button_Menu";
	public static String Tr_Hud_Button_Pause_Small = "Tr_Hud_Button_Pause_Small";
	public static String Tr_Hud_Button_Play_Small = "Tr_Hud_Button_Play_Small";
	public static String Tr_Hud_Button_Menu_Small = "Tr_Hud_Button_Menu_Small";
	
	// Misc Main Menu
	public static String Tr_MainMenu_GameLogo = "Tr_MainMenu_GameLogo";
	public static String Tr_MainMenu_Button_Start = "Tr_MainMenu_Button_Start";
	public static String Tr_MainMenu_Button_Scores = "Tr_MainMenu_Button_Scores";
	
	// Welcome screen
	private static String Welcome_Screen_SpriteSheet;
	private static Texture Welcome_Screen_SpriteSheet_Texture;
	public static String Tr_Welcome_Screen_Title = "Welcome_Screen_Title";
	public static String Tr_Welcome_Screen_Text = "Welcome_Screen_Text";
	public static String Tr_Welcome_Screen_Action = "Welcome_Screen_Action";
	public static String Tr_Welcome_Screen_Filler = "Tr_Welcome_Screen_Filler";
	
	// Characters
	public static String Tr_Player_Running_1 = "Tr_Player_Running_1";
	public static String Tr_Player_Running_2 = "Tr_Player_Running_2";
	public static String Tr_Player_Running_3 = "Tr_Player_Running_3";
	public static String Tr_Player_Running_4 = "Tr_Player_Running_4";
	public static String Tr_Player_Running_5 = "Tr_Player_Running_5";
	public static String Tr_Player_Running_6 = "Tr_Player_Running_6";
	public static String Tr_Player_Running_7 = "Tr_Player_Running_7";
	public static String Tr_Player_Running_8 = "Tr_Player_Running_8";
	
	// Segments
	public static String Tr_Segment_FlatGrass = "Tr_Segment_FlatGrass";
	public static String Tr_Segment_Ramp = "Tr_Segment_Ramp";
	
	// Main spritesheet
	private static String Main_SpriteSheet;
	private static Texture Main_SpriteSheet_Texture;
	
	// sounds 
	public static Sound Sound_Button_Click;
	public static Sound Sound_Punch;
	public static Sound Sound_Swoosh;
	
	// Other
	private static String Company_SpriteSheet;
	private static Texture Company_SpriteSheet_Texture;
	
	public static String Tr_Company_Logo = "Tr_Company_Logo";
	public static String Tr_Company_LogoText_1 = "Tr_Company_LogoText_1";
	public static String Tr_Company_LogoText_2 = "Tr_Company_LogoText_2";
	public static String Tr_Company_Filler = "Tr_Company_Filler";
	
	public static AssetManager manager;
	
	public static void load(String resolution)
	{	
		manager = new AssetManager();
		
		// make any changes to the file directory, they will need to go here
		filePath = "data/img/";
		
		// load the fonts
		if(resolution == "xhdpi")
			walshes = new BitmapFont(Gdx.files.internal(filePath + "fonts/86x/walshes.fnt"), false);
		else if(resolution == "tablet" || resolution == "tablet-hdpi")
			walshes = new BitmapFont(Gdx.files.internal(filePath + "fonts/128x/walshes.fnt"), false);
		else if(resolution == "hdpi" || resolution == "ldpi")
		walshes = new BitmapFont(Gdx.files.internal(filePath + "fonts/64x/walshes.fnt"), false);
				
		// load the segment textures
		Segment_Auto_Filler = filePath+segments+"auto_filler.png";
		manager.load(Segment_Auto_Filler, Texture.class);
		// miscellaneous
		Background_64x_Misc_SpriteSheet = filePath+miscellaneous+"64x_background_spritesheet.png";
		manager.load(Background_64x_Misc_SpriteSheet, Texture.class);
		// main spritesheet
		Main_SpriteSheet = filePath+spritesheets+"main_spritesheet.png";
		manager.load(Main_SpriteSheet, Texture.class);
		// companu Spritesheet
		Company_SpriteSheet = filePath+spritesheets+"company_spritesheet.png";
		manager.load(Company_SpriteSheet, Texture.class);
		// sounnds
		manager.load("data/sounds/Punch.mp3", Sound.class);
		manager.load("data/sounds/Button_Click.wav", Sound.class);
		manager.load("data/sounds/Swoosh.wav", Sound.class);
		
		if(DataManager.getTimesPlayed() <= 0)
		{
			Welcome_Screen_SpriteSheet = filePath+miscellaneous+"welcome_screen.png";
			manager.load(Welcome_Screen_SpriteSheet, Texture.class);
		}
		
		while(!manager.update())
		{
			System.out.println("Loaded: " + manager.getProgress() *100 + "%");
		}
		
		// Background spritesheet
		Background_64x_Misc_SpriteSheet_Texture = manager.get(Background_64x_Misc_SpriteSheet);
		// main Spritesheet
		Main_SpriteSheet_Texture = manager.get(Main_SpriteSheet);
		// company Spritesheet
		Company_SpriteSheet_Texture = manager.get(Company_SpriteSheet);
		// get sounds
		Sound_Punch = manager.get("data/sounds/Punch.mp3");
		Sound_Button_Click = manager.get("data/sounds/Button_Click.wav");
		Sound_Swoosh = manager.get("data/sounds/Swoosh.wav");
		
		if(DataManager.getTimesPlayed() <= 0)
			Welcome_Screen_SpriteSheet_Texture = manager.get(Welcome_Screen_SpriteSheet);
		
		isLoaded = true;
		// loading screen goes there ^
		
	}
	
	/**
	 * Sorts through the collection of texturee regions which have been compiled from the various spritesheets.
	 * It returns the texure which the same name as the parsed in one.
	 * @param textureRegionName
	 * @return TextureRegion
	 */
	public static TextureRegion getTextureRegion(String textureRegionName)
	{
		// ENTITIES
		if(textureRegionName == GameAssets.Tr_Entitie_WoodBlock_1)
			return new TextureRegion(Main_SpriteSheet_Texture, 320, 0, 64, 63);
		if(textureRegionName == GameAssets.Tr_Entitie_Rock_Medium_1)
			return new TextureRegion(Main_SpriteSheet_Texture, 0, 128, 127, 127);
		if(textureRegionName == GameAssets.Tr_Entitie_WoodBlockTop_1)
			return new TextureRegion(Main_SpriteSheet_Texture, 385, 0, 64, 63);
		if(textureRegionName == GameAssets.Tr_Entitie_Rock_Large_1)
			return new TextureRegion(Main_SpriteSheet_Texture, 0, 256, 255, 256);
		// SEGMENTS
		if(textureRegionName == GameAssets.Segment_Wall_1)
			return new TextureRegion(Main_SpriteSheet_Texture, 448, 128, 32, 128);
		if(textureRegionName == GameAssets.Segment_Hole_Corner_1)
			return new TextureRegion(Main_SpriteSheet_Texture, 257, 64, 64, 64);
		if(textureRegionName == GameAssets.Segment_Hole_Lining_1)
			return new TextureRegion(Main_SpriteSheet_Texture, 384, 128, 16, 128);
		if(textureRegionName == GameAssets.Tr_Segment_FlatGrass)
			return new TextureRegion(Main_SpriteSheet_Texture, 0, 0, 256, 32);
		if(textureRegionName == GameAssets.Tr_Segment_Ramp)
			return new TextureRegion(Main_SpriteSheet_Texture, 0, 64, 511, 63);
		// MISCELLANEOUS
		if(textureRegionName == GameAssets.Tr_Background_Mountains_1)
			return new TextureRegion(Background_64x_Misc_SpriteSheet_Texture , 0, 0, 400, 480);
		if(textureRegionName == GameAssets.Tr_Background_BackColor)
			return new TextureRegion(Background_64x_Misc_SpriteSheet_Texture , 400, 0, 1, 480);
		// Hud Misc
		if(textureRegionName == GameAssets.Tr_Hud_GameOverLogo)
			return new TextureRegion(Main_SpriteSheet_Texture, 512, 0, 400, 63);
		if(textureRegionName == GameAssets.Tr_Hud_ScoreBoard)
			return new TextureRegion(Main_SpriteSheet_Texture, 512, 64, 400, 240);
		if(textureRegionName == GameAssets.Tr_Hud_Button_Replay)
			return new TextureRegion(Main_SpriteSheet_Texture, 256, 320, 180, 80);
		if(textureRegionName == GameAssets.Tr_Hud_Button_SignIn)
			return new TextureRegion(Main_SpriteSheet_Texture, 256, 400, 180, 80);
		if(textureRegionName == GameAssets.Tr_Hud_Button_Menu)
			return new TextureRegion(Main_SpriteSheet_Texture, 436, 320, 180, 80);
		if(textureRegionName == GameAssets.Tr_Hud_Button_Pause_Small)
			return new TextureRegion(Main_SpriteSheet_Texture, 256, 256, 64, 64);
		if(textureRegionName == GameAssets.Tr_Hud_Button_Play_Small)
			return new TextureRegion(Main_SpriteSheet_Texture, 320, 256, 64, 64);
		if(textureRegionName == GameAssets.Tr_Hud_Button_Menu_Small)
			return new TextureRegion(Main_SpriteSheet_Texture, 384, 256, 64, 64);
		// Main Menu
		if(textureRegionName == GameAssets.Tr_MainMenu_GameLogo)
			return new TextureRegion(Main_SpriteSheet_Texture, 448, 448, 405, 80);
		if(textureRegionName == GameAssets.Tr_MainMenu_Button_Start)
			return new TextureRegion(Main_SpriteSheet_Texture, 616, 319, 180, 80);
		if(textureRegionName == GameAssets.Tr_MainMenu_Button_Scores)
			return new TextureRegion(Main_SpriteSheet_Texture, 796, 319, 180, 80);
		// Characters
		if(textureRegionName == GameAssets.Tr_Player_Running_1)
			return new TextureRegion(Main_SpriteSheet_Texture, 129, 128, 63, 63);//
		if(textureRegionName == GameAssets.Tr_Player_Running_2)
			return new TextureRegion(Main_SpriteSheet_Texture, 192, 128, 64, 63);
		if(textureRegionName == GameAssets.Tr_Player_Running_3)
			return new TextureRegion(Main_SpriteSheet_Texture, 255, 128, 64, 63);
		if(textureRegionName == GameAssets.Tr_Player_Running_4)
			return new TextureRegion(Main_SpriteSheet_Texture, 320, 128, 62, 63);
		if(textureRegionName == GameAssets.Tr_Player_Running_5)
			return new TextureRegion(Main_SpriteSheet_Texture, 129, 192, 63, 63);//
		if(textureRegionName == GameAssets.Tr_Player_Running_6)
			return new TextureRegion(Main_SpriteSheet_Texture, 192, 192, 64, 63);
		if(textureRegionName == GameAssets.Tr_Player_Running_7)
			return new TextureRegion(Main_SpriteSheet_Texture, 255, 192, 64, 63);
		if(textureRegionName == GameAssets.Tr_Player_Running_8)
			return new TextureRegion(Main_SpriteSheet_Texture, 320, 192, 62, 63);
		// Company texture regions
		if(textureRegionName == GameAssets.Tr_Company_Logo)
			return new TextureRegion(Company_SpriteSheet_Texture, 0, 0, 200, 200);
		if(textureRegionName == GameAssets.Tr_Company_LogoText_1)
			return new TextureRegion(Company_SpriteSheet_Texture, 0, 200, 250, 100);
		if(textureRegionName == GameAssets.Tr_Company_LogoText_2)
			return new TextureRegion(Company_SpriteSheet_Texture, 0, 300, 250, 100);
		if(textureRegionName == GameAssets.Tr_Company_Filler)
			return new TextureRegion(Company_SpriteSheet_Texture, 0, 0, 1, 1);
		// Welcome screen Texture regions
		if(textureRegionName == GameAssets.Tr_Welcome_Screen_Title)
			return new TextureRegion(Welcome_Screen_SpriteSheet_Texture, 0, 401, 714, 49);
		if(textureRegionName == GameAssets.Tr_Welcome_Screen_Text)
			return new TextureRegion(Welcome_Screen_SpriteSheet_Texture, 0, 0, 898, 379);
		if(textureRegionName == GameAssets.Tr_Welcome_Screen_Action)
			return new TextureRegion(Welcome_Screen_SpriteSheet_Texture, 0, 475, 553, 30);
		if(textureRegionName == GameAssets.Tr_Welcome_Screen_Filler)
			return new TextureRegion(Welcome_Screen_SpriteSheet_Texture, 1004, 495, 1, 1);
		
			
		return null;
	}
	
	public static Boolean isLoaded()
	{
		if(manager.getProgress() >= 1)
			return true;
		return false;
	}
	
	public static void resume()
	{
		
	}
	
	public static void dispose()
	{
		manager.dispose();
		manager = null;
		walshes.dispose();
		Sound_Button_Click.dispose();
		Sound_Punch.dispose();
		Sound_Swoosh.dispose();
		if(Welcome_Screen_SpriteSheet_Texture != null)
			Welcome_Screen_SpriteSheet_Texture.dispose();
			
	}

}
