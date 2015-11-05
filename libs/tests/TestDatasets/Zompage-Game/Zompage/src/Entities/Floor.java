package Entities;

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

public class Floor extends Entitie{
	
	public float size;
	// Model environment
	public ModelBuilder modelBuilder;
	public Array<ModelInstance> instances = new Array<ModelInstance>();
	// Model
	public Model floorModel;
	
	public Floor(float size)
	{
		this.size = size;
	}

	@Override
	public void create() {
		  // Model builder created, used to initially build models
        ModelBuilder modelBuilder = new ModelBuilder();
        floorModel = modelBuilder.createBox(size, 0.1f, size, 
            new Material(ColorAttribute.createDiffuse(Color.GRAY)),
            Usage.Position | Usage.Normal);
        instances.add(new ModelInstance(floorModel));
        instances.get(0).transform.setToTranslation(0f, 0f, 0f);
		
	}

	@Override
	public void render(ModelBatch batch) {
		
		batch.render(instances);
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
		instances.clear();
		floorModel.dispose();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector3 getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

}
