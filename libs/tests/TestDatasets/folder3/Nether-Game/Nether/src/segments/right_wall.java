package segments;

import utility.GameAssets;
import utility.MathsLibrary;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import Entities.Player;
import Entities.WorldSegment;
import Entities.world_entites.Rock_medium;

public class right_wall extends WorldSegment{
	
	private int screenWidth, screenHeight, startHeight, posInChunk, widthPos;
	private String screenResolution;
	private Boolean isActive = false;
	private float slopeAngle = 0;
	Player p;
	
	Texture autoFiller;
	TextureRegion lining, flatGrass;
	Sprite liningSprite;
	
	public right_wall(int screenWidth, int screenHeight, String screenResolution, Player p, int startHeight, int startWidth, int posInChunk)
	{
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.screenResolution = screenResolution;
		this.startHeight = startHeight;
		this.posInChunk = posInChunk;
		this.p = p;
		
		System.out.println("right_wall created.");
	}

	@Override
	public void create() {
		
		autoFiller = GameAssets.manager.get(GameAssets.Segment_Auto_Filler, Texture.class);
		flatGrass = GameAssets.getTextureRegion(GameAssets.Tr_Segment_FlatGrass);
		lining = GameAssets.getTextureRegion(GameAssets.Segment_Wall_1);
		this.slopeAngle = (float) MathsLibrary.getAngleDegrees( new Vector2(widthPos, startHeight), new Vector2(widthPos + screenWidth, startHeight));
		
		liningSprite = new Sprite(lining);
		liningSprite.setFlip(true, false);
		liningSprite.setSize(screenWidth/24, screenHeight/100 + screenHeight/4);
		liningSprite.setPosition(widthPos - liningSprite.getWidth() +(screenWidth/4)*3, startHeight + screenHeight/100 - (startHeight  + liningSprite.getHeight()/2 - screenHeight/3)*2);
	}

	@Override
	public void render(SpriteBatch batch) {
		for(int i = 0; i < 3; i++)
			batch.draw(flatGrass, (widthPos + (screenWidth/4)*i), startHeight - (startHeight - screenHeight/3)*2, screenWidth/4, screenHeight/16);
		// the wall
		batch.draw(flatGrass, (widthPos + (screenWidth/4)*3), (startHeight - size()) - (startHeight - screenHeight/3)*2, screenWidth/4, screenHeight/16);
		batch.draw(autoFiller,(widthPos + (screenWidth/4)*3), (startHeight - size()) + screenHeight/16 - (startHeight - screenHeight/3)*2-2, screenWidth/4, screenHeight);
		liningSprite.draw(batch);
		// before the wall
		batch.draw(autoFiller, (widthPos), startHeight + screenHeight/16 - (startHeight - screenHeight/3)*2-2, screenWidth, screenHeight);

		if(isActive)
		{
			float pX = p.getPosFrontX();
			float pY = p.getPosBottomY()  - p.getHeight()/3;
				if(pX > widthPos + ((screenWidth/4)*3) - liningSprite.getWidth())
				{
					if(pY < (startHeight + size()))
						p.killPlayer();
				}
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
		return screenHeight/4;
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
		if (x > widthPos - liningSprite.getWidth()*2 + (screenWidth/4)*3)
			return startHeight + size();
		// return the y height of the floor below the x position
		return (float) (startHeight + ((x - widthPos) * Math.sin(radAngle)));
	}

	@Override
	public float slopeAngle() {
		// TODO Auto-generated method stub
		return slopeAngle;
	}
}
