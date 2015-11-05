package segments;

import utility.GameAssets;
import utility.MathsLibrary;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import Entities.Player;
import Entities.WorldSegment;
import Entities.world_entites.Rock_large;
import Entities.world_entites.Rock_medium;

public class flat extends WorldSegment{
	
	private int screenWidth, screenHeight, startHeight, posInChunk, widthPos;
	private String screenResolution;
	private Boolean isActive = false;
	private float slopeAngle = 0;
	Player p;
	
	// rocks
	Rock_medium rock;
	Rock_large rock2;
	
	private float spawnChance = 30;
	
	Texture autoFiller;
	TextureRegion flatGrass;
	
	public flat(int screenWidth, int screenHeight, String screenResolution, Player p, int startHeight, int startWidth, int posInChunk)
	{
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.screenResolution = screenResolution;
		this.startHeight = startHeight;
		this.posInChunk = posInChunk;
		this.p = p;
		
		System.out.println("Flat created.");
	}

	@Override
	public void create() {
		
		autoFiller = GameAssets.manager.get(GameAssets.Segment_Auto_Filler, Texture.class);
		flatGrass = GameAssets.getTextureRegion(GameAssets.Tr_Segment_FlatGrass);
		this.slopeAngle = (float) MathsLibrary.getAngleDegrees( new Vector2(widthPos, startHeight), new Vector2(widthPos + screenWidth, startHeight + size()));

		// theres a set chance that the rock may spawn, this is set by spawnChance
		// for example, if spawnChance = 30 then theres 30% chance of a rock spawning
		if((MathsLibrary.randomNumber(0, 100) < spawnChance) && widthPos != 0)
		{
			// decides which rock is goijng to spawn
			if(MathsLibrary.randomNumber(0, 1) == 1 )
			{
				// spawn the rock at a random point on the segmant, aslong as its not off the segments region
				rock = new Rock_medium(screenWidth, screenHeight, screenResolution,
						widthPos + MathsLibrary.randomNumber(0, screenWidth - screenWidth/7), heightOfFloorAtPosX(400), 0, p);
				// create the rock resources
				rock.create();
			}
			else
			{
				rock2 = new Rock_large(screenWidth, screenHeight, screenResolution,
						widthPos + MathsLibrary.randomNumber(0, screenWidth - screenWidth/3), heightOfFloorAtPosX(400), 0, p);
				// create the resources
				rock2.create();
			}	
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		
		if(rock != null)
			rock.render(batch);
		if(rock2 != null)
			rock2.render(batch);
		for(int i = 0; i < 4; i++)
			batch.draw(flatGrass, (widthPos + (screenWidth/4)*i), startHeight - (startHeight - screenHeight/3)*2, screenWidth/4, screenHeight/16);
		batch.draw(autoFiller, (widthPos), startHeight + screenHeight/16 - (startHeight - screenHeight/3)*2 -3, screenWidth, screenHeight);
	

		if(isActive)
		{
		}
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
		if(rock != null)
			rock.dispose();
		if(rock2 != null)
			rock2.dispose();
	}

	@Override
	public int getEndHeight() {
		// TODO Auto-generated method stub
		return this.startHeight;
	}

	@Override
	public int getStartPos() {
		// TODO Auto-generated method stub
		return this.startHeight;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setStartHeight(int position) {
		this.startHeight = position;
	}

	@Override
	public void setPosInChunk(int position) {
		this.posInChunk = position;
	}
	
	@Override
	public void setWidth(int position)
	{
		this.widthPos = position;
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		this.isActive = true;
		if(rock != null)
			rock.activate();
		if(rock2 != null)
			rock2.activate();
	}

	@Override
	public void deActivate() {
		// TODO Auto-generated method stub
		this.isActive = false;
		if(rock != null)
			rock.deActivate();
		if(rock2 != null)
			rock2.deActivate();
	}
	
	@Override
	public Boolean isActive() {
		// TODO Auto-generated method stub
		return this.isActive;
	}

	@Override
	public float heightOfFloorAtPosX(float x) {
		double radAngle = MathsLibrary.getAngleRad( new Vector2(widthPos, startHeight), new Vector2(widthPos + screenWidth, startHeight + size()));
		// the height of the floor is changed to the height of the rock
		if(rock != null && rock.isAbove(x))
			return (float) (rock.getY() + ((x - widthPos) * Math.sin(radAngle)));
		if(rock2 != null && rock2.isAbove(x))
			return (float) (rock2.getY() + ((x - widthPos) * Math.sin(radAngle)));
		// return the y height of the floor below the x position
		return (float) (startHeight + ((x - widthPos) * Math.sin(radAngle)));
	}

	@Override
	public float slopeAngle() {
		// TODO Auto-generated method stub
		return slopeAngle;
	}
}
