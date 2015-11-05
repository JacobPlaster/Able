package UserInterface;

import Entities.Player;
import Utils.MathsLibrary;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class AndroidJoystick {
	
	int posX, posY, innerPosX, innerPosY, tPosX, tPosY;
	String pos;
	float distFromEdge = 5;
	public Texture joystickOuter, joystickInner, turretButton;
	Array<Sprite> sprites = new Array<Sprite>();
	MathsLibrary maths = new MathsLibrary();
	int fingerId = 10;
	// stops the stick from dissapearing whne the user moves out of the radius
	Boolean originInBounds = false;
	Boolean originThis = false;
	Boolean fingerSet = false;
	// the player
	Player player;
	// utilities
	public String resolution;
	
	/**
	 * Analog stick which adds movement to players
	 * 
	 * @param Decides which resolution files to load
	 * @param Spawn location of the analog stick
	 * @param The current player
	 */
	public AndroidJoystick(String res, String pos, Player player)
	{
		this.pos = pos;
		this.player = player;
		this.resolution = res;
	}
	
	public void create()
	{
		
		// ****************************************************** //
		// ******************** IMAGE SIZES ********************* //
		// Outeside area = 128px (but the graphics is only 86)
		// Inside stick = 64px
		// ****************************************************** //
		// ****************************************************** //
		
		// loading a texture from image file
		 joystickOuter = new Texture(resolution + "outsideJoystick.png");
		 joystickInner = new Texture(resolution + "insideJoystick.png");
		 // add to sprites array
		 sprites.add(new Sprite(joystickOuter));
		 sprites.add(new Sprite(joystickInner));	 
		 // set posX and posY depending on their initiated position chosen
		 if(pos.toLowerCase() == "left")
		 {
			 posX = 0;
			 posY = 0;
		 } else
		 {
			 posX = Gdx.graphics.getWidth()-joystickOuter.getWidth();
			 posY = 0;
			 
		 }
		 
		 // original position
		 innerPosX = posX + joystickInner.getWidth()/2;
		 innerPosY = posY + joystickInner.getHeight()/2;
	}
	
	public void render(SpriteBatch batch, AndroidJoystick oppositeJoystick)
	{
			
         // If the screen is touched
         if(Gdx.input.isTouched())
         {
        	 float mouseX = 0;
        	 float mouseY = 0;
        	 
        	 if(!fingerSet) 
             {
     			if(Gdx.input.getX() >= Gdx.graphics.getWidth()/2 && pos == "right")
     		   	 {
     		   		 this.fingerId = 0;
     		   	 } 
     			if(Gdx.input.getX() < Gdx.graphics.getWidth()/2 && pos == "right")
     		   	 {
     		   		 this.fingerId = 1;
     		   	 }
     			if(Gdx.input.getX() >= Gdx.graphics.getWidth()/2 && pos == "left")
    		   	 {
    		   		 this.fingerId = 1;
    		   	 } 
     			if(Gdx.input.getX() < Gdx.graphics.getWidth()/2 && pos == "left")
   		   	 	{
     				this.fingerId = 0;
   		   	 	} 
     			
     			fingerSet = true;
             }
        	 
        	 
        	 mouseX = Gdx.input.getX(fingerId);
        	 mouseY = Gdx.input.getY(fingerId);
        	 
        	// if your finger is within the analog stick area, then set the dynamic analog inner equal to your finger pos (FOR FINGER 2)
        	 if(maths.getDistance2D(mouseX, Gdx.graphics.getHeight() - mouseY, posX + joystickOuter.getWidth()/2, posY + joystickOuter.getHeight()/2) <= joystickOuter.getHeight()/2)
        	 {
        		 originInBounds = true;
        		 innerPosX = (int)mouseX - joystickInner.getWidth()/2;
        		 innerPosY = (int)(Gdx.graphics.getHeight()-mouseY - joystickInner.getWidth()/2);
        	 }
        	// if your finger is outside of the analog area but was originally within the analog area then use math and sourcery to calculate the position of 
        	 // the dynamic inner analog stick (FOR FINGER 1)
        	 if(originInBounds && maths.getDistance2D(mouseX, Gdx.graphics.getHeight() - mouseY, posX + joystickOuter.getWidth()/2, posY + joystickOuter.getHeight()/2) > joystickOuter.getHeight()/4)
        	 {
        		 float[] stickPos = maths.get3rdPoint(posX + joystickOuter.getWidth()/2, posY + joystickOuter.getHeight()/2, mouseX, Gdx.graphics.getHeight() - mouseY, joystickOuter.getHeight()/4);
        		 innerPosX = (int) stickPos[0]- joystickInner.getWidth()/2;
        		 innerPosY = (int) stickPos[1]- joystickInner.getWidth()/2;
        	 }
 
         } else
         {
        	 // else draw the analog sticks to their original position
        	 innerPosX = posX + joystickInner.getWidth()/2;
    		 innerPosY = posY + joystickInner.getHeight()/2;
    		 originThis = false;
    		 fingerId = 10;
    		 fingerSet = false;
         }
         
         if(!Gdx.input.isTouched() && !Gdx.input.isTouched(1))
         {
        	 fingerId = 10;
        	 fingerSet = false;
         }
         
         // if the screen isnt touched anymore set originInBounds equal to false
         if(!Gdx.input.isTouched(0) && originInBounds)
        	 originInBounds = false;
         
         
         if(isTouched() && pos == "left")
 		{
 			player.rotateLowerBody(getAngle());
 			player.movePlayer(calculatedSpeed());
 		}
 		if(isTouched() && pos != "left")
 		{
 			player.rotateUpperBody(getAngle());
 			player.fireWeapon(30, 4, 500f,200f, 0);
 		}
 		
 		
         
         // render the images     
 		batch.draw(sprites.get(0), posX, posY); 
	    batch.draw(sprites.get(1), innerPosX, innerPosY);
	}
	
	/**
	 * Pause the joysticks
	 */
	public void pause()
	{
		
	}
	
	/**
	 * Resume the class
	 */
	public void resume()
	{
	}
	
	/**
	 * Returns if the finger x and y has been equal to the analog area since the user first touched the screen
	 *
	 * @return Boolean
	 */
	public Boolean isTouched()
	{
		// returns if the finger x and y has been equal to the analog area since the user first touched the screen
		return originInBounds;
	}
	  
	/**
	 * Returns the angle direction that the joystick is pointing in (degrees) North = 0... South = 180
	 *
	 * @return float angle in degrees
	 */
	public float getAngle()
	{
		// gets the angle direction that the joystick is pointing in (degrees) North = 0... South = 180
		float angle = (float) ((float) (Math.toDegrees( Math.atan2((innerPosX + joystickInner.getWidth()/2) - (posX + joystickOuter.getWidth()/2),
				(posY + joystickOuter.getWidth()/2) - (innerPosY+ joystickInner.getWidth()/2)) ) + 180.0) % 360.0);
	    return angle;
	}
	
	/**
	 * Returns the height of the to area covered by the joystick entitie
	 *
	 * @return float height
	 */
	public float getHeight()
	{
		return this.joystickOuter.getHeight();
	}
	
	/**
	 * Returns the width of the to area covered by the joystick entitie
	 *
	 * @return float width
	 */
	public float getWidth()
	{
		return this.joystickOuter.getWidth();
	}
	
	public int getFingerId()
	{
		return fingerId;
	}
	
	/**
	 * Returns the distance of the dynamic analog stick form the center of the main analog area.
	 *
	 * @return float distance
	 */
	public float distanceFromCenter()
	{
		// returns the distance of the dynamic analog stick form the center of the main analog area (use this to calculate speed)
		float distance = maths.getDistance2D(innerPosX + joystickInner.getWidth()/2, innerPosY + joystickInner.getHeight()/2, posX + joystickOuter.getWidth()/2, posY + joystickOuter.getHeight()/2);
		return distance;
	}
	

	/**
	 * Returns the calculated speed which is adjusted to different screen resolution. This means that 
	 * the max and min speed are the same throughout all resolutions.
	 *
	 * @return float speed
	 */
	public float calculatedSpeed()
	{
		// returns the distance of the dynamic analog stick form the center of the main analog area (use this to calculate speed)
		float distance = maths.getDistance2D(innerPosX + joystickInner.getWidth()/2, innerPosY + joystickInner.getHeight()/2, posX + joystickOuter.getWidth()/2, posY + joystickOuter.getHeight()/2);
		
		if(resolution == "data/mdpi/")
			distance = distance*3;
		if(resolution == "data/hdpi/")
			distance = distance*2;
		
		System.out.println("speed: " + distance);
		
		// stops the player from moving extremely fast on larger resolution devices
		if(distance >= 38f)
			return 38f;
		
		return distance;
	}
	
	/**
	 * Returns the position of the entire joystick (the outside image).
	 *
	 * @return Vector3 position
	 */
	public Vector2 getPosition()
	{
		return new Vector2(posX, posY);
	}
	
	/**
	 * Disposes the joystick class and all of its resources.
	 */
	public void dispose()
	{
		// dispose these when the class is called to dispose
		sprites.clear();
		joystickInner.dispose();
		joystickOuter.dispose();
	}
}
