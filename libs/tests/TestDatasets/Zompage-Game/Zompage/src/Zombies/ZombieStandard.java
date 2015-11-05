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

public class ZombieStandard extends Zombie{
	
	
	// zobie
	private int health = 100;
	float speed = 22f;
	private int zombieGold = 5;
	public Vector3 pos;
	private Vector3 torsoSize = new Vector3(4f, 6f, 3f);
	private Vector3 headSize = new Vector3(3f, 3f, 3f);
	private Vector3 armSize = new Vector3(1f, 1.1f, 4.5f);
	
	// zombie modal
	private Model zombieTorso;
	private Model zombieHead;
	private Model zombieArm;
	private Array<ModelInstance> instances = new Array<ModelInstance>();
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
	public ZombieStandard(Vector3 pos, Player p, Array<PowerPack> powerPacks, Array<Zombie> zombies, WaveManager waveManager)
	{
		this.pos = pos;
		this.pos.y = torsoSize.y/2;
		this.p = p;
		this.powerPacks = powerPacks;
		this.zombies = zombies;
		this.wm = waveManager;
	}

	@Override
	public void create() 
	{
		 ModelBuilder modelBuilder = new ModelBuilder();
	        
	     // creates the main torso of the body and places it into the instances array at 0
        zombieTorso = modelBuilder.createBox(torsoSize.x, torsoSize.y, torsoSize.z, 
	                new Material(ColorAttribute.createDiffuse(Color.BLUE)),
	                Usage.Position | Usage.Normal);
        instances.add(new ModelInstance(zombieTorso));
        instances.get(0).transform.setToTranslation(pos);   
        
        // creates the head
        zombieHead = modelBuilder.createBox(headSize.x, headSize.y, headSize.z, 
                new Material(ColorAttribute.createDiffuse(Color.RED)),
                Usage.Position | Usage.Normal);
	    instances.add(new ModelInstance(zombieHead));
	    instances.get(1).transform.setToTranslation(pos.x, pos.y + (pos.y/2 + headSize.y), pos.z); 
	    
	    // creates the Arm
	    zombieArm = modelBuilder.createBox(armSize.x, armSize.y, armSize.z, 
                new Material(ColorAttribute.createDiffuse(Color.RED)),
                Usage.Position | Usage.Normal);
	    // right arm
	    instances.add(new ModelInstance(zombieArm));
	    instances.get(2).transform.setToTranslation(pos.x + (torsoSize.x - armSize.x), pos.y, pos.z); 
	    // left arm
	    instances.add(new ModelInstance(zombieArm));
	    instances.get(3).transform.setToTranslation(pos.x - (torsoSize.x - armSize.x), pos.y, pos.z); 
	}

	@Override
	public void render(ModelBatch batch) 
	{
		if(!isDead)
		{
			batch.render(instances);
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
		pos = getPosition();
		// get the angle from the zombie to th eplayer
		float targetAngle = (float) m.getAngle(pos.x, pos.z, playerPos.x, playerPos.z);
		// rotate the zombie to the calculated angle
		rotateZombie(targetAngle);
		
		float incrementX = 0;
		float incrementZ = 0;
		
		// move player according to distance in the direction that 
		// the player is currently facing (towards the player)
		incrementX -= (distance * Math.sin(0)) * Gdx.graphics.getDeltaTime();
		incrementZ -= (distance * Math.cos(0)) * Gdx.graphics.getDeltaTime();
		
		// Torso
		instances.get(0).transform.translate(incrementX, 0, incrementZ);
		// Head
		instances.get(1).transform.translate(incrementX, 0, incrementZ);
		//arms
		instances.get(2).transform.translate(incrementX, 0, incrementZ);
		instances.get(3).transform.translate(incrementX, 0, incrementZ);
	}
	
	/**
	 * Set the rotation of the zombie to a given angle in degrees.
	 *
	 * @param Angle to set
	 */
	public void rotateZombie(float angle)
	{
		angle -= this.zombieAngle;
		// since instance.transform just increments the rotation and i want to set the rotion
		// subtract the total angle form the incremnt angle to get a set angle
		// head
		instances.get(0).transform.rotate(0f, 1f, 0f, angle);
		// torso
		instances.get(1).transform.rotate(0f, 1f, 0f, angle);
		//arms
		instances.get(2).transform.rotate(0f, 1f, 0f, angle);
		instances.get(3).transform.rotate(0f, 1f, 0f, angle);
		// add the angle back on (this stops the increment for having the negative effect)
		this.zombieAngle += angle;
	}
	
	public void kill()
	{
		this.isDead = true;
		dropPowerPack();
		wm.addKill();
		p.addMoney(zombieGold);
	}
	
	/**
	 * Apply force in the opposite direction that the zombie is facing and subtract away health.
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
		// torso
		instances.get(0).transform.translate(incrementX, 0, incrementZ);
		// head
		instances.get(1).transform.translate(incrementX, 0, incrementZ);
		//arms
		instances.get(2).transform.translate(incrementX, 0, incrementZ);
		instances.get(3).transform.translate(incrementX, 0, incrementZ);
		
		rotateZombie(originalAngle);
	}
	
	
	private void checkCollisionPlayer()
	{
		Vector3 playerPos = p.getPosition();
		pos = m.convertToVec(instances.get(0).transform.cpy());
		
		float distance = m.getDistance2D(pos.x, pos.z, playerPos.x, playerPos.z);
		
		if(distance <= torsoSize.x)
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
				if(distance < torsoSize.x*1.5)
				{
					float angle = (float) m.getAngle(thisPos.x, thisPos.z, zPos.x, zPos.z);
					zombies.get(i).applyHit(angle, torsoSize.x*2, 0);
				}
			}
		}
	}
	
	public void dropPowerPack()
	{
		// once in every 40 zombies killedd
		int dropRate = (int) m.randomNumber(0, 20);
		if(dropRate == 10)
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
		zombieTorso.dispose();
		zombieHead.dispose();
	}

	@Override
	public String getName() 
	{
		return "ZombieStandard";
	}

	@Override
	public Vector3 getPosition() 
	{
		pos = m.convertToVec(instances.get(0).transform.cpy());
		return pos;
	}

}
