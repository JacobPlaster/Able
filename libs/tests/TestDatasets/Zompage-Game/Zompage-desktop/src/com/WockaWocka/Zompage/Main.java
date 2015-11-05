package com.WockaWocka.Zompage;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main 
{
	public static void main(String[] args) 
	{
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Zom-page";
		cfg.useGL20 = false;
		
		// NON-COMPATABLE
		
		// ldpi
		/*cfg.width = 320;
		cfg.height = 240;*/
		
		// mdpi
		/*cfg.width = 480;
		cfg.height = 320;*/
		
		
		// COMPATABLE
		
		// hdpi
		cfg.width = 800;
		cfg.height = 480;
		
		// xhdpi
		/*cfg.width = 960;
		cfg.height = 640;*/
		
		new LwjglApplication(new ZompageGame(), cfg);
	}
}
