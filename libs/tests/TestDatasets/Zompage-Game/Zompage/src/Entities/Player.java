package Entities;

import UserInterface.AndroidJoystick;
import UserInterface.HealthHud;
import Utils.MathsLibrary;
import Zombies.Zombie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Player extends Entitie{
	
	// Model environment
	public ModelBuilder modelBuilder;
	public Array<ModelInstance> instances = new Array<ModelInstance>();
	// Model main body
	public Model upperBodyModel;
	public Vector3 upperBodySize = new Vector3(3.6f, 3.6f, 3f);
	Vector3 upperBodyPos;
	public float angleUpperBody = 0;
	public Model lowerBodyModel;
	public Vector3 lowerBodySize = new Vector3(3f, 4.5f, 3f);
	Vector3 lowerBodyPos;
	public float angleLowerBody = 0;
	// Maths
	MathsLibrary m = new MathsLibrary();
	// related entities
	Array<Bullet> bullets = new Array<Bullet>();
	Array<Bullet> bulletsToDispose = new Array<Bullet>();
	Array<Zombie> zombies;
	HealthHud healthHud;
	float fireRateIncrement = 1;
	float fireRateMultiplier = 1;
	float movementSpeedMultiplier = 1;
	
	/**
	 * Playable entitie whch can be controlled using the contained methods
	 * 
	 * @param Spawn location
	 * @param Left analog stick (controls movement)
	 * @param Right analog stick (controls aim)
	 * @param Array cotainign all active zombies
	 */
	public Player(Vector3 pos, AndroidJoystick left, AndroidJoystick right, Array<Zombie> zombies, HealthHud healthHud)
	{
		this.upperBodyPos = new Vector3(pos.x, pos.y + (lowerBodySize.y) + (upperBodySize.y/2), pos.z);
		this.lowerBodyPos = new Vector3(pos.x, pos.y + (lowerBodySize.y/2), pos.z);
		this.zombies = zombies;
		this.healthHud = healthHud;
	}

	@Override
	public void create() 
	{
		 ModelBuilder modelBuilder = new ModelBuilder();
		        
		// create the upper body, change the width, height and depth on the below line
        upperBodyModel = modelBuilder.createBox(upperBodySize.x, upperBodySize.y, upperBodySize.z, 
	                new Material(ColorAttribute.createDiffuse(Color.BLUE)),
	                Usage.Position | Usage.Normal);
        instances.add(new ModelInstance(upperBodyModel));
        instances.get(0).transform.setToTranslation(upperBodyPos);
	        
     // create the lower body, change the width, height and depth on the below line
        lowerBodyModel = modelBuilder.createBox(lowerBodySize.x, lowerBodySize.y, lowerBodySize.z, 
	                new Material(ColorAttribute.createDiffuse(Color.RED)),
	                Usage.Position | Usage.Normal);
        
        instances.add(new ModelInstance(lowerBodyModel));
        instances.get(1).transform.setToTranslation(lowerBodyPos);
	}

	@Override
	public void render(ModelBatch batch) 
	{		
		batch.render(instances);
		for(int bulletsRendered = 0; bulletsRendered < bullets.size; bulletsRendered++)
			bullets.get(bulletsRendered).render(batch);
		// the bullet is ready for the next shot
		if(!Gdx.input.isTouched())
		{
			fireRateIncrement = 1;
			
			// if the last bullet in the array is ready to be disposed
			if(bullets.size > 0 && bullets.get(bullets.size-1).isDead())
			{
				// since i cant dispose of them whils the model batch is running, ill add them to a second array which is not
				// running through the render loop and the dispose of them once the model batch has stopped running
				for(int bulletsDisposed = 0; bulletsDisposed < bullets.size; bulletsDisposed++)
					bulletsToDispose.add(bullets.get(bulletsDisposed));
				bullets.clear();
			}
		}
	}
	
	@Override
	public void pause() 
	{
		for(int bulletsPaused = 0; bulletsPaused < bullets.size; bulletsPaused++)
			bullets.get(bulletsPaused).pause();
	}
	
	@Override
	public void resume() 
	{
		for(int bulletsResumed = 0; bulletsResumed < bullets.size; bulletsResumed++)
			bullets.get(bulletsResumed).resume();
	}
	
	
	/**
	 * Create a new bullet entitie and fire it in the direction that the player is currently facing.
	 *
	 * @param Damage done to target
	 * @param Amount of bullets fires per second
	 * @param Speed of the bullet
	 * @param type of bullet: (Default = standard)
	 */
	public void fireWeapon(int damage, int fireRate, float speed, float knockback, int type)
	{	
		// increments the "fireRateIncrement" each time the fire weapon method is called,
		// but only fires if the "fireRateIncrement" is equal to 1. This means that the 
		// bullets will only fire "fireRates" tiems per a second
		fireRate = (int) (fireRate * fireRateMultiplier);
		fireRateIncrement += fireRate*Gdx.graphics.getDeltaTime();
		if(fireRateIncrement >= 1)
		{
			switch(type)
			{
				case 0:
					bullets.add(new BulletStandard(instances.get(0).transform.cpy(), this.getAngleUpperBody(), damage, speed, knockback, zombies));
				break;
				default:
					bullets.add(new BulletStandard(instances.get(0).transform.cpy(), this.getAngleUpperBody(), damage, speed, knockback, zombies));;
				break;
			}
			fireRateIncrement = 0;
		}
	}
	
	/**
	 * Moves the player in the direction of the angle
	 *
	 * @param Add the angle for the player direction
	 * @param Distance that the player will travel (speed)
	 */
	public void movePlayer(float distance)
	{
		// use delta time allow for equal speeds across different framerates
		distance = distance * movementSpeedMultiplier;
		distance = (float) (distance/1.3);
		float incrementX = 0;
		float incrementZ = 0;
		
		// move player according to distance in the direction that 
		// the player is currently facing (move forward)
		incrementX -= (distance * Math.sin(0)) * Gdx.graphics.getDeltaTime();
		incrementZ -= (distance * Math.cos(0)) * Gdx.graphics.getDeltaTime();

		instances.get(1).transform.translate(incrementX, 0, incrementZ);
		lowerBodyPos.add(incrementX, 0, incrementZ);
		// A bit hacky but create a matrix equal to the lowerbody motrix and then
		// rotate it equal to the upper body rotation before setting the upperbody matrix equal
		// to the new and updated matrix (this is the only method that allows me to do this without
		// creating image flicker)
		Matrix4 ubm = new Matrix4();
		ubm = instances.get(1).transform.cpy();
		ubm.translate(0, (lowerBodySize.y/2) + (upperBodySize.y/2), 0);
		// set the upperbody matrix equal to the new matrix, and rotate it using a pricate method
		instances.get(0).transform.set(setMatrixRationToOriginal(angleUpperBody, ubm));
	}
	
	
	// FOR USE OF THE POWERPACKS
	
	public void doubleFireRate()
	{
		fireRateMultiplier = 2;
	}
	
	public void doubleMovementSpeed()
	{
		movementSpeedMultiplier = 2;
	}
	
	public void resetFireRate()
	{
		fireRateMultiplier = 1;
	}
	public void resetMovementSpeed()
	{
		movementSpeedMultiplier = 1;
	}
	//////////////////////////////////
	
	/**
	 * Apply hit to the player, this will create a damage animation and effect and will also update the 
	 * health hud.
	 */
	public void applyHit()
	{
		healthHud.damage();
	}
	
	/**
	 * Returns the angle of the lower body in degrees.
	 */
	public float getAngleLowerBody()
	{
		return angleLowerBody;
	}
	
	/**
	 * Returns the angle of the Upper body in degrees.
	 */
	public float getAngleUpperBody()
	{
		return angleUpperBody;
	}
	
	/**
	 * Sets the rotation of the lower body equal to the given angle in degrees.
	 * In order to convert to degrees from radiant use yourRadianvalue * 180/Math.PI
	 *
	 * @param Angle in degrees
	 */
	public void rotateLowerBody(float angle)
	{
		// since instance.transform just increments the rotation and i want to set the rotion
		// subtract the total angle form the incremnt angle to get a set angle
		angle = angle - angleLowerBody;
		instances.get(1).transform.rotate(0f, 1f, 0f, angle);
		// add the angle back on (this stops the increment for having the negative effect)
		angleLowerBody += angle;
	}
	
	/**
	 * Sets the rotation of the upper body equal to the given angle in degrees.
	 * In order to convert to degrees from radiant use yourRadia nvalue * 180/Math.PI
	 *
	 * @param Angle in degrees
	 */
	public void rotateUpperBody(float angle)
	{
		// see rotate lowerBody comments
		angle = angle - angleUpperBody;
		instances.get(0).transform.rotate(0f, 1f, 0f, angle);
		angleUpperBody += angle;
	}
	
	/**
	 * Sets the rotation of the matrix equal to the given angle in degrees.
	 * In order to convert to degrees from radiant use yourRadianvalue * 180/Math.PI
	 *
	 * @param Angle in degrees
	 * @param Matrix to convert
	 * @return Converted matrix
	 */
	private Matrix4 setMatrixRationToOriginal(float angle, Matrix4 m)
	{
		// again, we want to set and not increment the rotation (see rotatelowerBody comments)
		angle = angle - angleLowerBody;
		// rotate the matrix 
		m.rotate(0f, 1f, 0f, angle);
		return m;
	}
	
	/**
	 * Adds money to the players overall wallet via the healthHud class
	 *
	 * @param amount
	 */
	public void addMoney(int amount)
	{
		healthHud.addMoney(amount);
	}
	
	@Override
	public void dispose() 
	{
		// dispose when the class is disposed
		instances.clear();
		upperBodyModel.dispose();
		lowerBodyModel.dispose();
		
		for(int bulletsDisposed = 0; bulletsDisposed < bullets.size; bulletsDisposed++)
			bullets.get(bulletsDisposed).dispose();
		for(int bulletsDisposed2 = 0; bulletsDisposed2 < bulletsToDispose.size; bulletsDisposed2++)
			bulletsToDispose.get(bulletsDisposed2).dispose();
	}
	
	public void giveHealth()
	{
		healthHud.heal();
	}

	@Override
	public String getName() 
	{
		return "Player";
	}

	@Override
	public Vector3 getPosition() 
	{
		lowerBodyPos = m.convertToVec(instances.get(1).transform.cpy());
		return lowerBodyPos;
	}

}
