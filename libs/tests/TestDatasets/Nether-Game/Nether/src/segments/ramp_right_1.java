package segments;

import utility.GameAssets;
import utility.MathsLibrary;
import Entities.Player;
import Entities.WorldSegment;
import Entities.world_entites.Rock_large;
import Entities.world_entites.Rock_medium;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class ramp_right_1 extends WorldSegment{
	private int screenWidth, screenHeight, startHeight, posInChunk, widthPos;
	private String screenResolution;
	private Boolean isActive = false;
	Player p;
	private float slopeAngle;
	
	// rock2
	Rock_medium rock;
	Rock_large rock2;
		
	private float spawnChance = 50;
	private Boolean spawnRock = false;
	
	TextureRegion rampRightTexture;
	Texture autoFiller;
	
	/**
	 * 
	 * Creates a ramp that ramps from right to left, the right side being the highest. The rams size is the same as screenHeight/4
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 * @param screenResolution
	 * @param startHeight
	 * @param startWidth
	 * @param posInChunk
	 */
	public ramp_right_1(int screenWidth, int screenHeight, String screenResolution, Player p, int startHeight, int startWidth, int posInChunk)
	{
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.screenResolution = screenResolution;
		this.startHeight = startHeight;
		this.posInChunk = posInChunk;
		this.p = p;
		
		System.out.println("ramp_right_1 created.");
	}

	@Override
	public void create() {
		
		rampRightTexture = GameAssets.getTextureRegion(GameAssets.Tr_Segment_Ramp);
		autoFiller = GameAssets.manager.get(GameAssets.Segment_Auto_Filler, Texture.class);
		this.slopeAngle = (float) MathsLibrary.getAngleDegrees( new Vector2(widthPos, startHeight), new Vector2(widthPos + screenWidth, startHeight + size()));
		
		// theres a set chance that the rock may spawn, this is set by spawnChance
		// for example, if spawnChance = 30 then theres 30% chance of a rock spawning
		if((MathsLibrary.randomNumber(0, 100) < spawnChance) && widthPos != 0)
		{
			// decides which rock is goijng to spawn
			if(MathsLibrary.randomNumber(0, 10) <= 6 )
			{
				// spawn the rock at a random point on the segmant, aslong as its not off the segments region
				float spawnPosX = widthPos + MathsLibrary.randomNumber(0, screenWidth - screenWidth/7);
				rock = new Rock_medium(screenWidth, screenHeight, screenResolution,
						spawnPosX, heightOfFloorAtPosX(spawnPosX), slopeAngle(), p);
				// create the rock resources
				rock.create();
			}
			else
			{
				float spawnPosX = widthPos + MathsLibrary.randomNumber(0, screenWidth - screenWidth/3);
				rock2 = new Rock_large(screenWidth, screenHeight, screenResolution,
						spawnPosX, heightOfFloorAtPosX(spawnPosX), slopeAngle(), p);
				// create the resources
				rock2.create();
			}	
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		// TODO Auto-generated method stub
		if(rock != null)
			rock.render(batch);
		if(rock2 != null)
			rock2.render(batch);
		
		batch.draw(rampRightTexture, (widthPos), startHeight - size() - (startHeight - screenHeight/3)*2, screenWidth, size());
		batch.draw(autoFiller, (widthPos), startHeight - (startHeight - screenHeight/3)*2-2, screenWidth, screenHeight);

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
		return this.startHeight + size();
	}

	@Override
	public int getStartPos() {
		// TODO Auto-generated method stub
		return this.startHeight;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		 //return screenHeight/7;
		return screenHeight/7;
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
			return rock.getY();
		if(rock2 != null && rock2.isAbove(x))
			return rock2.getY();
		// return the y height of the floor below the x position
		return (float) (startHeight + ((x - widthPos) * Math.sin(radAngle)));
	}
	
	@Override
	public float slopeAngle() {
		// TODO Auto-generated method stub
		return slopeAngle;
	}
}

