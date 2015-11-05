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

public class single_hole_toupper_1 extends WorldSegment{
	
	private int screenWidth, screenHeight, startHeight, posInChunk, widthPos, endHeight;
	private String screenResolution;
	private Boolean isActive = false;
	private float slopeAngle = 0;
	Player p;
	
	// right side sprites
	// we are usign sprite, so then we can flip them
	Sprite rightCorner, rightLining;
	
	private float holeStartWidth, holeEndWidth;
	
	
	Texture autoFiller;
	TextureRegion leftCorner, leftLining;
	
	public single_hole_toupper_1(int screenWidth, int screenHeight, String screenResolution, Player p, int startHeight, int startWidth, int posInChunk)
	{
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.screenResolution = screenResolution;
		this.startHeight = startHeight;
		this.posInChunk = posInChunk;
		this.p = p;
		
		System.out.println("Single_Hole_toupper_1.");
	}

	@Override
	public void create() {
		holeStartWidth = MathsLibrary.randomNumber(widthPos+screenWidth/4, widthPos+screenWidth/3);
		holeEndWidth = MathsLibrary.randomNumber((widthPos + screenWidth)-screenWidth/3, (widthPos + screenWidth)-screenWidth/3);
		
		autoFiller = GameAssets.manager.get(GameAssets.Segment_Auto_Filler, Texture.class);
		leftCorner = GameAssets.getTextureRegion(GameAssets.Segment_Hole_Corner_1);
		leftLining = GameAssets.getTextureRegion(GameAssets.Segment_Hole_Lining_1);
		
		endHeight = startHeight + size();
		
		// flipped sprites for the opposite side of the slope
		rightCorner = new Sprite(leftCorner);
		rightCorner.setFlip(true, false);
		rightCorner.setSize(screenWidth/12, screenHeight/8);
		rightCorner.setPosition(holeEndWidth,
				 endHeight - (endHeight - screenHeight/3)*2);
		
	}

	@Override
	public void render(SpriteBatch batch) {
		
		// LEFT SIDE
		batch.draw(autoFiller, widthPos, (startHeight + rightCorner.getHeight()) - (startHeight - screenHeight/3)*2, (holeStartWidth - widthPos) - leftLining.getRegionWidth(), screenHeight);
		batch.draw(leftCorner, holeStartWidth-rightCorner.getWidth(), startHeight - (startHeight - screenHeight/3)*2, screenWidth/12, screenHeight/8);
		// lining
		batch.draw(autoFiller, widthPos, startHeight - (startHeight - screenHeight/3)*2,  (holeStartWidth - widthPos)-rightCorner.getWidth(), screenHeight);
		batch.draw(autoFiller, widthPos, (startHeight + rightCorner.getHeight()) - (startHeight - screenHeight/3)*2,  (holeStartWidth - widthPos), screenHeight);
		
		// RIGHT SIDE
		// +5 because screenWidth/3 will return some cray number like xx.xxxxxxxxxxx so we round it up
		batch.draw(autoFiller, holeEndWidth + rightCorner.getWidth(), endHeight - (endHeight - screenHeight/3)*2, (widthPos + screenWidth) - (holeEndWidth + rightCorner.getWidth()), screenHeight);
		batch.draw(autoFiller, holeEndWidth, (endHeight + rightCorner.getHeight()) - (endHeight - screenHeight/3)*2, (widthPos + screenWidth) - (holeEndWidth + rightCorner.getWidth()), screenHeight);
		rightCorner.draw(batch);
		
		if(isActive)
		{
			// make the returned number positive
			if(p.getPosBottomY() < Math.abs(heightOfFloorAtPosX(p.getPosFrontX())) &&
					p.getPosX()+ p.getWidth()*1.1 > (holeEndWidth))
			{
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
		double radAngle = MathsLibrary.getAngleRad( new Vector2(widthPos, startHeight), new Vector2(widthPos + screenWidth, startHeight + size()));
		// return the y height of the floor below the x position
		// if there is a whole, then return the negative value of wher the line hsould be
		if((x - p.getWidth()/2) > holeStartWidth-screenWidth/6 && x < holeEndWidth-p.getWidth())
			return  -100;
		if( x > holeEndWidth-p.getWidth())
			return endHeight;
		return (float) startHeight;
	}

	@Override
	public float slopeAngle() {
		// TODO Auto-generated method stub
		return slopeAngle;
	}
}