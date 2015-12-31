package Entities;

import utility.GameAssets;
import GameInstances.MainGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {
	
	private int screenWidth, screenHeight;
	private String screenResolution;
	private float posX, posY, angle;
	private float playerWidth, playerHeight;
	
	private float currentFloorHeight = 0;
	
	private float jumpHeight = 0;
	private Boolean isDead = false;
	private Boolean onFloor = false;
	private Boolean jumpClimb = false;
	private Boolean jumpDecline = false;
	private Boolean isPaused = false;
	// delyas the user from being able to jump (after pause resume)
	private float jumpDelayTimer = 0;
	
	private float GRAVITY;
	private float JUMP_SPEED;
	
	float dynamicGravity = GRAVITY;
	float dynamicJumpSpeed = JUMP_SPEED;
	MainGame main;
	Sound swooshSound;
	boolean isSwooshing = true;

	Texture t1;
	private float ANIMATION_FRAME_SPEED = 1/18f;
	Animation runningAnimation;
	
	/** 
	 * Creates a new player object, however the create method needs to be called before all of the resources are generated.
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 * @param screenResolution
	 */
	public Player(int screenWidth, int screenHeight, String screenResolution, MainGame main)
	{
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.screenResolution = screenResolution;
		this.main = main;
		
		this.GRAVITY = screenHeight/20;
		this.JUMP_SPEED = (float) ((screenHeight) / 65);
		this.dynamicGravity = GRAVITY;
		this.dynamicJumpSpeed = JUMP_SPEED;

		this.playerWidth = screenWidth/11;
		this.playerHeight = screenHeight/7;
	}
	
	/**
	 * Creates all of the resources for the player
	 */
	public void create()
	{
		posY = currentFloorHeight;
		
		// cut out the regions for the running sprite animatiom
		// Cutting 4 picesl off the hiegt to stop the textures from interfearing with eachother
		TextureRegion[] runningRegions = new TextureRegion[8];
		runningRegions[0] = GameAssets.getTextureRegion(GameAssets.Tr_Player_Running_1);
		runningRegions[1] = GameAssets.getTextureRegion(GameAssets.Tr_Player_Running_2);
		runningRegions[2] = GameAssets.getTextureRegion(GameAssets.Tr_Player_Running_3);
		runningRegions[3] = GameAssets.getTextureRegion(GameAssets.Tr_Player_Running_4);
		runningRegions[4] = GameAssets.getTextureRegion(GameAssets.Tr_Player_Running_5);
		runningRegions[5] = GameAssets.getTextureRegion(GameAssets.Tr_Player_Running_6);
		runningRegions[6] = GameAssets.getTextureRegion(GameAssets.Tr_Player_Running_7);
		runningRegions[7] = GameAssets.getTextureRegion(GameAssets.Tr_Player_Running_8);
		swooshSound = GameAssets.Sound_Swoosh;
	
		for(int i = 0; i < 7; i++)
			runningRegions[i].getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		runningAnimation = new Animation(ANIMATION_FRAME_SPEED, runningRegions[0], runningRegions[1], runningRegions[2], runningRegions[3],
				runningRegions[4], runningRegions[5], runningRegions[6], runningRegions[7]);
		runningAnimation.setPlayMode(Animation.LOOP);
		
		
		posX = screenWidth/2;
		posY = 0;
	}
	
	/**
	 * Draws all of the players objects to the screen
	 * 
	 * @param batch
	 * @param posX
	 */
	public void render(SpriteBatch batch, float cameraPosX, WorldSegment currentSegment)
	{	  
		float fingerPosY = Gdx.input.getY();
		float fingerPosX = Gdx.input.getX();
		
		float delta = Gdx.graphics.getDeltaTime();
		float currentHeight = currentSegment.heightOfFloorAtPosX(posX)-playerHeight/3;
		jumpDelayTimer -= 50 * delta;
		
			// if the player is airborn the apply gravity
			//**** GRAVITY ****/
			if(!onFloor && !jumpClimb && !isPaused)
			{
				posY -= dynamicGravity;
				dynamicGravity += GRAVITY * delta;
				//setPlayerRotation(0);
				ANIMATION_FRAME_SPEED = 0;
				isSwooshing = false;
			}
			
			if(!isDead)
			{	
				//**** JUMP METHOD ****//
				if(Gdx.input.isTouched() && jumpDelayTimer <= 0)
				{
					// IF THE FINGER TOUCHES THE BUTTON
					if(fingerPosX <= screenWidth - screenWidth/12 - 20 || fingerPosX >= (screenWidth - screenWidth/12 - 20) + screenWidth/12)
						if(fingerPosY <= screenHeight - screenHeight/8 - 20 || fingerPosY >= (screenHeight - screenHeight/8 - 20) + screenHeight/8)
						{
							// make sure that it doesnt jump when the pause button is pressed
							jump((screenHeight - Gdx.input.getY()));
							jumpDelayTimer = 10;
						}
				}
				// cast the input height to -1 so the jumpheight is not affected in this render cycle
				jumpRender(delta);
				
				
				// if the player is not in the air, then pin the player to the floor
				if (posY < currentHeight+playerHeight/8 && !jumpClimb)
				{
					// For the first cycle of the render code, the current segment will not have been set
					if(currentSegment != null)
					{
						posY = currentHeight;
						slowRotatePlayer(currentSegment.slopeAngle(), 60);
						onFloor = true;
						jumpDecline = false;
						dynamicGravity = 0;
					}
				} else
					onFloor = false;
			}
			if(!isDead)
			{	
				this.posX = cameraPosX-playerWidth;
			} else 
			{
				// IF THE PLAYER IS DEAD
				slowRotatePlayer(angle - 10, 80);
				posX -= 100 * delta/2;
				onFloor = false;
				jumpClimb = false;
				ANIMATION_FRAME_SPEED = 0;
			}
	

			batch.draw(runningAnimation.getKeyFrame(ANIMATION_FRAME_SPEED += delta), posX, (screenHeight - posY - (screenHeight/3)*2 + playerHeight)+ screenHeight/75,
					playerWidth/2, playerHeight/2, playerWidth, playerHeight, 1, 1, angle-90, false);
			if(jumpClimb || jumpDecline)
				ANIMATION_FRAME_SPEED = 0;
			
			if(isPaused)
				ANIMATION_FRAME_SPEED = 0;
			
	}
	
	/**
	 * Causes the player to jump to the set height
	 * @param height
	 */
	public void jump(float height)
	{			
		// player has to click a certain height above the player for it to jum
		// if we dont have this feature then the player just spams up and down
		if(height < (posY + playerHeight/2))
			height = posY+screenHeight/8;
		
		if(onFloor && height != 0 && !isPaused)
		{
			jumpHeight = height;
			this.onFloor = false;
			setPlayerRotation(0);
			jumpClimb = true;
			dynamicJumpSpeed = JUMP_SPEED*150;
			jumpDecline = false;
			
			if(!isSwooshing)
			{
				swooshSound.play();
				isSwooshing = true;
			}
		}
	}
	private void jumpRender(float delta)
	{
		if(!onFloor && jumpClimb && !isPaused)
		{
			// get the total height of the jump (we want the sprites waste to be at the top of the jumps 
			// so minus half of the sprite)
			float targetHeight = jumpHeight+playerHeight/2;
			// get the delta distance, but then divide the entire dela by 2, this means that halfway
			// into the jump the dino will start to decline since the distance will return negative
			float distance = (targetHeight - posY)/3;
			// once the dino reaches the top of the jump, then let the gravity take place
			if(posY > targetHeight)
			{
				jumpClimb = false;
				jumpDecline = true;
			}
		
			// update the y pos. x2.5 to make it stand out more
			this.dynamicJumpSpeed -= (JUMP_SPEED);
			
			posY += (dynamicJumpSpeed) * delta;
		}
	}
	
	
	/**
	 * Runs the command to kill the player, also stops the camera at the current xPosition
	 */
	public void killPlayer()
	{
		this.isDead = true;
		main.freezeCamera();
	}
	
	/** 
	 * Pause the player object
	 */
	public void pause()
	{
		jumpDelayTimer = 10;
		this.isPaused = true;
	}
	/**
	 * Resumes the player object from the pause state
	 */
	public void resume()
	{
		jumpDelayTimer = 10;
		this.isPaused = false;
	}
	/**
	 * Returns the players current x position
	 * @return
	 */
	public float  getPosX()
	{
		return this.posX;
	}
	/**
	 * returns the players current y position
	 * @return
	 */
	public float getPosY()
	{
		return this.posY;
	}
	/**
	 * Returns the y position of the player at the bottom of its sprite
	 * @return
	 */
	public float getPosBottomY()
	{
		return this.posY + this.playerWidth;
	}
	/** 
	 * Returns the x position at the front of the sprite
	 */
	public float getPosFrontX()
	{
		return this.posX + this.playerWidth;
	}
	
	/**
	 * Edit the boolean which determines whether the player is touching the floor
	 * @param dToFloor
	 */
	public void setCurrentFloorHeight(float floorHeight)
	{
		this.currentFloorHeight = floorHeight;
	}
	/**
	 * returns the height of the player sprite
	 * @return
	 */
	public float getHeight()
	{
		return this.playerHeight;
	}
	/** 
	 * returns the width of the player sprite
	 * @return
	 */
	public float getWidth()
	{
		return this.playerWidth;
	}
	/** 
	 * rotates the angle of the player.
	 * Parsed in data must be in radians
	 * @param angle
	 */
	public void setPlayerRotation(float angleD)
	{
		this.angle = angleD;
	}
	/** Get the players score */
	public int getScore()
	{
		return main.getScore();
	}
	/**
	 * Rotates the player in a slower and more realistic manner
	 * @param angleD
	 */
	public void slowRotatePlayer(float angleD, int speed)
	{
			if(this.angle < angleD)
				setPlayerRotation((float) ((float) angle+(speed)* Gdx.graphics.getDeltaTime()));
			if(this.angle > angleD)
				setPlayerRotation((float) ((float)angle-(speed)* Gdx.graphics.getDeltaTime()));
	}
	public boolean isDead()
	{
		return this.isDead;
	}
	/**
	 * Dispose the player class
	 */
	public void dispose()
	{
		this.t1.dispose();
	}
}
