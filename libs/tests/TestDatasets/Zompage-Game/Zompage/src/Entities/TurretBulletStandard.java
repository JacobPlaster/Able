package Entities;

import Utils.MathsLibrary;

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

public class TurretBulletStandard extends Bullet{
	
	// 0.25 second life span
	float lifeSpan = 10f;

	Boolean isDead = false;
	// Model
	Model bulletModel;
	ModelInstance instance;
	ModelBuilder modelBuilder;
	Vector3 position;
	Vector3 size = new Vector3(0.2f, 0.2f, 0.2f);
	Vector3 target;
	float angle;
	//utilities
	MathsLibrary m = new MathsLibrary();
	
	public TurretBulletStandard(Vector3 pos, Vector3 target, float angle)
	{
		this.position = pos;
		this.target = target;
		this.angle = angle;
		create();
	}

	@Override
	public void create() {
		modelBuilder = new ModelBuilder();
		
		float distance = m.getDistance2D(position.x, position.z, target.x, target.z);
		float[] midPoint = m.get3rdPoint(position.x, position.z, target.x, target.z, (distance/2)+2f);
        
		bulletModel = modelBuilder.createBox(size.x, size.y, distance, 
	            new Material(ColorAttribute.createDiffuse(Color.GREEN)),
	            Usage.Position | Usage.Normal);
		instance = new ModelInstance(bulletModel);
		
		instance.transform.setToTranslation(midPoint[0], 4f, midPoint[1]);
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
