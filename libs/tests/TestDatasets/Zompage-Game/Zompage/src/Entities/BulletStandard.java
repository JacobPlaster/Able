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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class BulletStandard extends Bullet{
	
	static float MAX_RANGE = 200f;
	// 0.25 second life span
	float lifeSpan = 10f;
	
	Vector3 position;
	Matrix4 positionM;
	Vector3 size = new Vector3(0.4f, 0.4f, MAX_RANGE);
	float angle, speed, knockback;
	int damage;
	Boolean isDead = false;
	// Model
	Model bulletModel;
	ModelInstance instance;
	ModelBuilder modelBuilder;
	//utilities
	MathsLibrary m = new MathsLibrary();
	Array<Zombie> zombies;
	
	public BulletStandard(Matrix4 posM, float angle, int damage, float speed, float knockback, Array<Zombie> zombies)
	{
		this.position = m.convertToVec(posM);
		this.positionM = posM;
		this.angle = angle;
		this.damage = damage;
		this.speed = speed;
		this.zombies = zombies;
		this.knockback = knockback;
		
		create();
	}

	@Override
	public void create() {
		modelBuilder = new ModelBuilder();
        
        checkIfHit();
	}
	
	/**
	 * Checks if the bullet is less then 4ft away from the target.
	 */
	private void checkIfHit()
	{
		
		Vector3 point2 = m.getLineViaAngle(positionM, angle, MAX_RANGE/2);
		
		float closestToPlayer = MAX_RANGE;
		int zombId = -1;
		
		for(int i = 0; i < zombies.size; i++)
		{	
			Vector3 zombPosition = zombies.get(i).getPosition();
			float distToLine = (float) m.pointToLineDistance(new Vector3(position.x, 0, position.z), new Vector3(point2.x, 0, point2.z),  zombPosition);
			float distToPlayer = m.getDistance2D(position.x, position.z, zombPosition.x, zombPosition.z);
			
			float zombAngle = zombies.get(i).getAngle();
			System.out.println("Angle: " + zombAngle);
			
			if(distToLine <= 2.5f && !zombies.get(i).isDead())
			{
				if(distToPlayer < closestToPlayer)
				{
					// stop the bullets form effecting zombies in the opposite direction
					if(zombAngle > angle+130 || zombAngle < angle-130)
					{
						zombId = i;
						closestToPlayer = distToPlayer;
					}
				}
			}
		}
		if(zombId >= 0)
			zombies.get(zombId).applyHit(angle, knockback, damage);
		
		bulletModel = modelBuilder.createBox(size.x, size.y, closestToPlayer, 
	            new Material(ColorAttribute.createDiffuse(Color.GREEN)),
	            Usage.Position | Usage.Normal);
		instance = new ModelInstance(bulletModel);
		if(closestToPlayer != MAX_RANGE)
		{
			float[] drawPos = m.get3rdPoint(position.x, position.z, point2.x, point2.z, (closestToPlayer/2)+2f);
			instance.transform.setToTranslation(drawPos[0], 4f, drawPos[1]);
		} else
			instance.transform.setToTranslation(point2.x, 4f, point2.z);
		
		instance.transform.rotate(0, 1f, 0, angle);
	}

	@Override
	public void render(ModelBatch batch) {
		if(!isDead)
		{
			//moveBullet(speed);
			batch.render(instance);
			// life span clock
			lifeSpan -= (150f * Gdx.graphics.getDeltaTime());
			if(lifeSpan <= 0)
			{
				isDead = true;
			}
		}
	}
	
	/**
	 * Has the bullet reached is maximum distance or hit its target.
	 * 
	 * @param Booelan true = dead
	 */
	public Boolean isDead()
	{
		return this.isDead;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		instance = null;
		bulletModel.dispose();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "BulletStandard";
	}

	@Override
	public Vector3 getPosition() {
		position = m.convertToVec(instance.transform.cpy());
		return position;
	}

}
