package PowerPacks;

import Entities.Player;
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

public class FullHealth extends PowerPack{
	
	// Power pack model
	Model ppModel;
	Vector3 pos;
	float width = 3f, height = 3f, depth = 1.5f;
	ModelInstance instance;
	// other
	Player player;
	MathsLibrary m = new MathsLibrary();
	// game logic
	Boolean used = false;
	float existenceTimer = 30;
	

	/**
	 * Creates a power pack which fully replenishes the players health when it is activated
	 */
	public FullHealth(Vector3 position, Player player)
	{
		this.pos = position;
		this.player = player;
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
		 ModelBuilder modelBuilder = new ModelBuilder();
	        
	     // create the modal of the zombie
        ppModel = modelBuilder.createBox(width, height, depth, 
	                new Material(ColorAttribute.createDiffuse(Color.YELLOW)),
	                Usage.Position | Usage.Normal);
        instance = (new ModelInstance(ppModel));
        instance.transform.setToTranslation(pos);     
	}

	@Override
	public void render(ModelBatch batch) 
	{
		if(!used)
		{
			// TODO Auto-generated method stub
			batch.render(instance);
			checkActivation();
			
			float degrees = 50f * Gdx.graphics.getDeltaTime();
			instance.transform.rotate(0f, 1f, 0f, degrees);
			
			existenceTimer -=  Gdx.graphics.getDeltaTime();
			
			if(existenceTimer <= 0)
				used = true;
		}
	}
	
	public void checkActivation()
	{
		Vector3 playerPos = player.getPosition();
		float distance = m.getDistance2D(pos.x, pos.z, playerPos.x, playerPos.z);
		
		if(distance <= width/2 + player.lowerBodySize.x)
		{
			activate();
			used = true;
		}
	}
	
	public void activate()
	{
		player.giveHealth();
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
		// TODO Auto-generated method stub
		ppModel.dispose();
	}
	
	public Boolean getState()
	{
		return used;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "PP FullHealth";
	}

	@Override
	public Vector3 getPosition() {
		// TODO Auto-generated method stub
		pos = m.convertToVec(instance.transform.cpy());
		return pos;
	}

}
