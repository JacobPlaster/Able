package segments;

import utility.GameAssets;
import utility.MathsLibrary;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import Entities.Player;
import Entities.WorldSegment;
import Entities.world_entites.Half_Tree_1;

public class tree_jump_1 extends WorldSegment{
	
	private int screenWidth, screenHeight, startHeight, posInChunk, widthPos;
	private String screenResolution;
	private Boolean isActive = false;
	private float slopeAngle = 0;
	Player p;
	Half_Tree_1 halfTree;

	
	Texture autoFiller;
	TextureRegion flatGrass;
	
	public tree_jump_1(int screenWidth, int screenHeight, String screenResolution, Player p, int startHeight, int startWidth, int posInChunk)
	{
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.screenResolution = screenResolution;
		this.startHeight = startHeight;
		this.posInChunk = posInChunk;
		this.p = p;
		
		System.out.println("Tree_jump_1 created.");
	}

	@Override
	public void create() {
		autoFiller = GameAssets.manager.get(GameAssets.Segment_Auto_Filler, Texture.class);
		flatGrass = GameAssets.getTextureRegion(GameAssets.Tr_Segment_FlatGrass);
		
		this.slopeAngle = (float) MathsLibrary.getAngleDegrees( new Vector2(widthPos, startHeight), new Vector2(widthPos + screenWidth, startHeight + size()));
		
		float gapSpawnHeight = MathsLibrary.randomNumber(startHeight + p.getHeight(), screenHeight- p.getHeight());
		float gapSpawnWidth = MathsLibrary.randomNumber(widthPos, widthPos+screenHeight/2);
		halfTree= new Half_Tree_1(screenWidth, screenHeight, screenResolution, gapSpawnWidth, gapSpawnHeight, 0, p);
		halfTree.create();
	}

	@Override
	public void render(SpriteBatch batch) {
		halfTree.render(batch);
		for(int i = 0; i < 4; i++)
			batch.draw(flatGrass, (widthPos + (screenWidth/4)*i), startHeight - (startHeight - screenHeight/3)*2, screenWidth/4, screenHeight/16);
		batch.draw(autoFiller, (widthPos), startHeight + screenHeight/16 - (startHeight - screenHeight/3)*2 -3, screenWidth, screenHeight);

		if(isActive)
		{
		}
	}

	@Override
	public void pause() {
		halfTree.pause();
	}

	@Override
	public void resume() {
		halfTree.resume();
	}

	@Override
	public void dispose() {
		halfTree.dispose();
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
		halfTree.activate();
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
		if(halfTree.isAbove())
			return halfTree.getY();
		// return the y height of the floor below the x position
		return (float) (startHeight + ((x - widthPos) * Math.sin(radAngle)));
	}

	@Override
	public float slopeAngle() {
		// TODO Auto-generated method stub
		return slopeAngle;
	}
}