package Zombies;

import Entities.Player;
import PowerPacks.Bomb;
import PowerPacks.FireRate;
import PowerPacks.FullHealth;
import PowerPacks.IncreasedSpeed;
import PowerPacks.PowerPack;
import Utils.MathsLibrary;
import Utils.WaveManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class ZombieFast extends Zombie{
	
	// Zombie
	
	int health = 70;
	float speed = 32f;
	private int zombieGold = 15;
	public Vector3 pos;
	public Vector3 bodySize = new Vector3(3f, 6.5f, 3f);
	
	// zombie modal
	public Model zombieModel;
	public ModelInstance instance;
	public float zombieAngle = 0;
	Boolean isDead = false;
	// Targeted player
	Player p;
	Vector3 playerPos;
	// Maths library 
	public MathsLibrary m = new MathsLibrary();
	// power packs (zombie array is also for powerPacks)
	Array<PowerPack> powerPacks;
	Array<Zombie> zombies;
	// utilities
	WaveManager wm;
	
	/**
	 * Non-playable character which attacks players
	 * 
	 * @param Spawn location
	 * @param The player to attack
	 */
	public ZombieFast(Vector3 pos, Player p, Array<PowerPack> powerPacks, Array<Zombie> zombies, WaveManager waveManager)
	{
		this.pos = pos;
		this.pos.y = bodySize.y/2;
		this.p = p;
		this.powerPacks = powerPacks;
		this.zombies = zombies;
		this.wm = waveManager;
	}

	@Override
	public void create() 
	{
		 ModelBuilder modelBuilder = new ModelBuilder();
	        
	     // create the modal of the zombie
        zombieModel = modelBuilder.createBox(bodySize.x, bodySize.y, bodySize.z, 
	                new Material(ColorAttribute.createDiffuse(Color.RED)),
	                Usage.Position | Usage.Normal);
        instance = (new ModelInstance(zombieModel));
        instance.transform.setToTranslation(pos);     
	}

	@Override
	public void render(ModelBatch batch) 
	{
		if(!isDead)
		{
			batch.render(instance);
			moveZombie(speed);
			checkZombieCollision();
			checkCollisionPlayer();
		}
		if(health <= 0 && !isDead)
			kill();
	}
	
	/**
	 * Move zombie in the direction that it is facing to a given distance.
	 *
	 * @param Distance to move
	 */
	public void moveZombie(float distance)
	{
		// get vector 3 position from the targeted player
		Vector3 playerPos = p.getPosition();
		pos = m.convertToVec(instance.transform.cpy());
		// get the angle from the zombie to th eplayer
		float targetAngle = (float) m.getAngle(pos.x, pos.z, playerPos.x, playerPos.z);
		// rotate the zombie to the calculated angle
		rotateZombie(targetAngle);
		// divide distance to slow down the movement
		float incrementX = 0;
		float incrementZ = 0;
		
		// move player according to distance in the direction that 
		// the player is currently facing (towards the player)
		incrementX -= (distance * Math.sin(0)) * Gdx.graphics.getDeltaTime();
		incrementZ -= (distance * Math.cos(0)) * Gdx.graphics.getDeltaTime();
		instance.transform.translate(incrementX, 0, incrementZ);
	}
	
	/**
	 * Set the rotation of the zombie to a given angle in degrees.
	 *
	 * @param Angle to set
	 */
	public void rotateZombie(float angle)
	{
		angle = angle - this.zombieAngle;
		// since instance.transform just increments the rotation and i want to set the rotion
		// subtract the total angle form the incremnt angle to get a set angle
		//System.out.println("Angle to rotate: " + angle);
		instance.transform.rotate(0f, 1f, 0f, angle);
		// add the angle back on (this stops the increment for having the negative effect)
		this.zombieAngle += angle;
	}
	
	public void kill()
	{
		instance.transform.rotate(0f,  0f, 1f, 90);
		this.isDead = true;
		dropPowerPack();
		wm.addKill();
		p.addMoney(zombieGold);
	}
	
	/**
	 * Apply force in the opposite direction that the zombie is facing and subtract away health.
	 * This is alot more accurate then the delta version, however it alot less efficient.
	 *
	 * @param Speed of knockback
	 * @param Distance
	 * @param damage to health
	 */
	public void applyHit(float angle, float distance, int damage)
	{
		float originalAngle = getAngle();
		
		this.health -= damage;

		float incrementX = 0;
		float incrementZ = 0;
		rotateZombie(angle);
		incrementX -= (distance * Math.sin(0)) * Gdx.graphics.getDeltaTime();
		incrementZ -= (distance * Math.cos(0)) * Gdx.graphics.getDeltaTime();
		instance.transform.translate(incrementX, 0, incrementZ);
		rotateZombie(originalAngle);
	}
	
	private void checkCollisionPlayer()
	{
		Vector3 playerPos = p.getPosition();
		pos = m.convertToVec(instance.transform.cpy());
		
		float distance = m.getDistance2D(pos.x, pos.z, playerPos.x, playerPos.z);
		
		if(distance <= bodySize.x)
		{
			p.applyHit();
		}
	}
	
	// check if the zombie is within attacking distance form the player
	private void checkZombieCollision()
	{
		Vector3 thisPos = getPosition();
		for(int i = 0; i < zombies.size; i++)
		{
			if(!zombies.get(i).isDead())
			{
				Vector3 zPos = zombies.get(i).getPosition();
				float distance  = m.getDistance2D(thisPos.x, thisPos.z, zPos.x, zPos.z);
				if(distance < bodySize.x)
				{
					float angle = (float) m.getAngle(thisPos.x, thisPos.z, zPos.x, zPos.z);
					zombies.get(i).applyHit(angle, bodySize.x, 0);
				}
			}
		}
	}
	
	public void dropPowerPack()
	{
		// once in every 40 zombies killedd
		int dropRate = (int) m.randomNumber(0, 10);
		if(dropRate == 5)
		{
			// the rarety of each item
			int itemSpecificDropRate = (int) m.randomNumber(0, 100);
			
			if(itemSpecificDropRate >= 0 && itemSpecificDropRate < 30)
			{
				powerPacks.add(new FullHealth(getPosition(), p));
				powerPacks.get(powerPacks.size-1).create();
			}
			if(itemSpecificDropRate >= 30 && itemSpecificDropRate < 55)
			{
				powerPacks.add(new IncreasedSpeed(getPosition(), p));
				powerPacks.get(powerPacks.size-1).create();
			}
			if(itemSpecificDropRate >= 55 && itemSpecificDropRate <= 85)
			{
				powerPacks.add(new FireRate(getPosition(), p));
				powerPacks.get(powerPacks.size-1).create();
			}
			if(itemSpecificDropRate >= 85)
			{
				powerPacks.add(new Bomb(getPosition(), p, zombies));
				powerPacks.get(powerPacks.size-1).create();
			}
			
		}
	}

	@Override
	public void pause()
	{
		
	}

	@Override
	public void resume() 
	{
		
	}
	
	public Boolean isDead()
	{
		return isDead;
	}
	
	public float getAngle()
	{
		return zombieAngle;
	}

	@Override
	public void dispose() 
	{
		zombieModel.dispose();
	}

	@Override
	public String getName() 
	{
		return "ZombieStandard";
	}

	@Override
	public Vector3 getPosition() 
	{
		pos = m.convertToVec(instance.transform.cpy());
		return pos;
	}

}