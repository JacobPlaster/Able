package segments;

import utility.GameAssets;
import utility.MathsLibrary;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import Entities.Player;
import Entities.WorldSegment;
import Entities.world_entites.Rock_medium;

public class left_wall extends WorldSegment{
	
	private int screenWidth, screenHeight, startHeight, posInChunk, widthPos;
	private String screenResolution;
	private Boolean isActive = false;
	private float slopeAngle = 0;
	Player p;
	
	Texture autoFiller;
	TextureRegion lining, wallCorner, flatGrass;
	
	public left_wall(int screenWidth, int screenHeight, String screenResolution, Player p, int startHeight, int startWidth, int posInChunk)
	{
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.screenResolution = screenResolution;
		this.startHeight = startHeight;
		this.posInChunk = posInChunk;
		this.p = p;
		
		System.out.println("left_wall created.");
	}

	@Override
	public void create() {
		
		autoFiller = GameAssets.manager.get(GameAssets.Segment_Auto_Filler, Texture.class);
		flatGrass = GameAssets.getTextureRegion(GameAssets.Tr_Segment_FlatGrass);
		lining = GameAssets.getTextureRegion(GameAssets.Segment_Wall_1);
		wallCorner = GameAssets.getTextureRegion(GameAssets.Segment_Hole_Corner_1);
		
		this.slopeAngle = (float) MathsLibrary.getAngleDegrees( new Vector2(widthPos, startHeight), new Vector2(widthPos + screenWidth, startHeight));
	}

	@Override
	public void render(SpriteBatch batch) {
		for(int i = 0; i < 3; i++)
			batch.draw(flatGrass, (widthPos + (screenWidth/4)*i), startHeight - (startHeight - screenHeight/3)*2, screenWidth/4, screenHeight/16);
		// the lining on the wall and corner
		batch.draw(lining, (widthPos + (screenWidth/4)*3), startHeight - (startHeight  - screenHeight/3)*2, screenWidth/25 , screenHeight/100 + screenHeight/4);
		// the lower floor
		batch.draw(flatGrass, (widthPos + (screenWidth/4)*3), startHeight - size() - (startHeight - screenHeight/3)*2, screenWidth/4, screenHeight/16);
		batch.draw(autoFiller, (widthPos + (screenWidth/4)*3), startHeight - size() + screenHeight/16 - (startHeight - screenHeight/3)*2 -2, (screenWidth/4), screenHeight/2);
		// before the wall
		batch.draw(autoFiller, (widthPos), startHeight + screenHeight/16 - (startHeight - screenHeight/3)*2 -2, (screenWidth/4)*3, screenHeight);
	

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
		return -screenHeight/4;
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
	}

	@Override
	public void deActivate() {
		// TODO Auto-generated method stub
		this.isActive = false;
	}
	
	@Override
	public Boolean isActive() {
		// TODO Auto-generated method stub
		return this.isActive;
	}

	@Override
	public float heightOfFloorAtPosX(float x) {
		double radAngle = MathsLibrary.getAngleRad( new Vector2(widthPos, startHeight), new Vector2(widthPos + screenWidth, startHeight));
		if (x > widthPos + (screenWidth/4)*3)
			return startHeight + size();
		// return the y height of the floor below the x position
		return (float) startHeight;
	}

	@Override
	public float slopeAngle() {
		// TODO Auto-generated method stub
		return slopeAngle;
	}
}