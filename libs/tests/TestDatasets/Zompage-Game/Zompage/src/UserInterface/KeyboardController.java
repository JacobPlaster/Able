package UserInterface;

import Entities.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class KeyboardController 
{
	public Player player;
	
	public KeyboardController(Player player)
	{
		this.player = player;
	}
	
	public void render()
	{
		 if(Gdx.input.isKeyPressed(Keys.D))
		 {
			 player.rotateLowerBody(270);
			 player.movePlayer(32f);
		 }
		 if(Gdx.input.isKeyPressed(Keys.A))
		 {
			 player.rotateLowerBody(90);
			 player.movePlayer(32f);
		 }
		 if(Gdx.input.isKeyPressed(Keys.S))
		 {
			 player.rotateLowerBody(180);
			 player.movePlayer(32f);
		 }
		 if(Gdx.input.isKeyPressed(Keys.W))
		 {
			 player.rotateLowerBody(0);
			 player.movePlayer(32f);
		 }
	}
}
