package Entities;

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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class TurretStandard extends Entitie
{
	// Model
	public ModelBuilder modelBuilder;
	public Array<ModelInstance> instances = new Array<ModelInstance>();
	public Model lowerBodyModel;
	public Model upperBodyModel;
	public Vector3 upperBodyPos;
	public Vector3 lowerBodyPos;
	float ubSizeX = 3f, ubSizeY = 3.5f, ubSizeZ = 8f;
	float lbSizeX = 2.5f, lbSizeY = 2.5f, lbSizeZ = 2.5f;
	float upperBodyAngle = 0;
	// Game logic
	float fireRateIncrement = 0;
	Boolean isTargetFound = false;
	float radius = 50f;
	private int targetId = 2;
	int damage = 25;
	// Utilities
	Array<Bullet> bullets = new Array<Bullet>();
	Array<Bullet> bulletsToDispose = new Array<Bullet>();
	MathsLibrary m = new MathsLibrary();
	Array<Zombie> zombies;
	
	public TurretStandard(Vector3 pos, Array<Zombie> zombies)
	{
		this.upperBodyPos = pos;
		this.lowerBodyPos = pos;
		this.zombies = zombies;
		
		create();
	}

	@Override
	public void create() 
	{
		modelBuilder = new ModelBuilder();
		
	   // create the upperBody model
        upperBodyModel = modelBuilder.createBox(ubSizeX, ubSizeY, ubSizeZ, 
	                new Material(ColorAttribute.createDiffuse(Color.BLUE)),
	                Usage.Position | Usage.Normal);
        instances.add(new ModelInstance(upperBodyModel));
        upperBodyPos.y = lbSizeY + ubSizeY/2;
        instances.get(0).transform.setToTranslation(upperBodyPos);
        
     // create the lower body model
        lowerBodyModel = modelBuilder.createBox(lbSizeX, lbSizeY, lbSizeZ, 
	                new Material(ColorAttribute.createDiffuse(Color.RED)),
	                Usage.Position | Usage.Normal);
        instances.add(new ModelInstance(lowerBodyModel));
        lowerBodyPos.y = lbSizeY/2;
        instances.get(1).transform.setToTranslation(lowerBodyPos);
	}

	@Override
	public void render(ModelBatch batch) 
	{
		batch.render(instances);
		if(!isTargetFound)
		{
			checkForTarget();
		} else
		{
			Vector3 zPos = zombies.get(targetId).getPosition();
			if(m.getDistance2D(lowerBodyPos.x, lowerBodyPos.z, zPos.x, zPos.z) > radius)
			{
				isTargetFound = false;
			} else
				targetZombie(targetId);
		}
		
		// if the last bullet in the array is ready to be disposed
		if(bullets.size > 0 && bullets.get(bullets.size-1).isDead())
		{
			// since i cant dispose of them whils the model batch is running, ill add them to a second array which is not
			// running through the render loop and the dispose of them once the model batch has stopped running
			for(int bulletsDisposed = 0; bulletsDisposed < bullets.size; bulletsDisposed++)
				bulletsToDispose.add(bullets.get(bulletsDisposed));
			bullets.clear();
		}
		
		for(int bulletsRendered = 0; bulletsRendered < bullets.size; bulletsRendered++)
			bullets.get(bulletsRendered).render(batch);
		
	}
	
	/**
	 * Sort through the entire array of zombies to check which is the closest to the turret.
	 * Aslong as the zombie is still alive and not dead
	 *
	 */
	public void checkForTarget()
	{
		// set the closest zombie to 100f, this alows the for loop to
		// update it to the first zombie position instantly
		float closestTarget = 100f;
		int closestZombieId = 0;
		
		// Initiate through the entire zombie array
		for(int i = 0; i < zombies.size; i++)
		{
			// if the zombie is dead, then ignore
			if(!zombies.get(i).isDead())
			{
				// get position of the current zombie
				Vector3 zPos = zombies.get(i).getPosition();
				// calculae distance using pythagoras
				float distance = m.getDistance2D(lowerBodyPos.x, lowerBodyPos.z, zPos.x, zPos.z);
				// if this current zombie is closer then the previous closest zombie
				// then update the previous to this and store its id in memory
				if(closestTarget > distance)
				{
					closestTarget = distance;
					closestZombieId = i;
				}
			}
		}
		// once we have checked th eentire array, we have found the closest zombie.
		// update the global zombie id to this
		this.targetId = closestZombieId;
		this.isTargetFound = true;
		// target the zombie
		targetZombie(targetId);
	}
	
	/**
	 * Targets a zombie by its given id, the method will automatically rotate to the zombie
	 *
	 * @param the zombies position in its array
	 */
	private void targetZombie(int zombieId)
	{
		// if the zombie is dea then it cannot be targeted
		if(zombies.size > 0)
		{
			if(!zombies.get(zombieId).isDead())
			{
				// get the targeted zombies position
				Vector3 zPos = zombies.get(zombieId).getPosition();
				// rotate the turret to face the zombie
				float angle = (float) m.getAngle(upperBodyPos.x, upperBodyPos.z, zPos.x, zPos.z);
				rotateTurret(angle);
				// fire a bullet in that direction
				fireWeapon(10, 4, 500f, 200f, angle, damage);
			} else
				isTargetFound = false;
		}
	}
	
	/**
	 * Set the turrets upper body rotation equal to the given value.
	 *
	 * @param angle in degrees
	 */
	public void rotateTurret(float angle)
	{
		// minus the total rotation , this creates a set method instead of an increment method
		angle = angle - upperBodyAngle;
		instances.get(0).transform.rotate(0f, 1f, 0f, angle);
		upperBodyAngle += angle;
		
	}
	
	/**
	 * Create a new bullet entitie and fire it in the direction that the player is currently facing.
	 *
	 * @param Damage done to target
	 * @param Amount of bullets fires per second
	 * @param Speed of the bullet
	 * @param type of bullet: (Default = standard)
	 */
	public void fireWeapon(int damage, int fireRate, float speed, float knockback, float angle, int type)
	{	
		// increments the "fireRateIncrement" each time the fire weapon method is called,
		// but only fires if the "fireRateIncrement" is equal to 1. This means that the 
		// bullets will only fire "fireRates" tiems per a second
		fireRateIncrement += fireRate*Gdx.graphics.getDeltaTime();
		if(fireRateIncrement >= 1)
		{
			switch(type)
			{
				case 0:
					zombies.get(targetId).applyHit(angle, 50f, damage);
					bullets.add(new TurretBulletStandard(getPosition(), zombies.get(targetId).getPosition(), angle));
				break;
				default:
					zombies.get(targetId).applyHit(angle, 50f, damage);
					bullets.add(new TurretBulletStandard(getPosition(), zombies.get(targetId).getPosition(), angle));
				break;
			}
			fireRateIncrement = 0;
		}
	}

	@Override
	public void pause() {
		for(int bulletsPaused = 0; bulletsPaused < bullets.size; bulletsPaused++)
			bullets.get(bulletsPaused).pause();
	}

	@Override
	public void resume() {
		for(int bulletsResumed = 0; bulletsResumed < bullets.size; bulletsResumed++)
			bullets.get(bulletsResumed).resume();
	}

	@Override
	public void dispose() 
	{
		instances.clear();
		upperBodyModel.dispose();
		lowerBodyModel.dispose();
		
		for(int bulletsDisposed = 0; bulletsDisposed < bullets.size; bulletsDisposed++)
			bullets.get(bulletsDisposed).dispose();
		for(int bulletsDisposed2 = 0; bulletsDisposed2 < bulletsToDispose.size; bulletsDisposed2++)
			bulletsToDispose.get(bulletsDisposed2).dispose();
	}

	@Override
	public String getName() {
		return "TurretStandard";
	}

	@Override
	public Vector3 getPosition() {
		return m.convertToVec(instances.get(0).transform.cpy());
	}

}
