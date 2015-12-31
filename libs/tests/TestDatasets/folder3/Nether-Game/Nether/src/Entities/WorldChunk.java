package Entities;

import java.util.ArrayList;
import java.util.List;

import segments.flat;
import utility.SegmentLoader;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WorldChunk 
{
	
	private List<WorldSegment> chunkSegments;
	private int difficulty;
	private int screenWidth, screenHeight, chunkStartHeight, chunkStartWidth;
	private String screenResolution;
	SegmentLoader loader;
	Player player;
	
	WorldSegment currentSegment;
	
	
	/**
	 * Creates a chunk of world which accepts 10 chunk segments. The chunk controls which background images are used.
	 * The difficulty decides how hard the contained segments will be, the difficulty should get harder the more the user
	 * progresses through the game.
	 * The size of the world chunk is 20x the size of the phones width.
	 * 
	 * 	Difficulty:
	 * 	0 = easy;
	 * 	1 = medium;
	 * 	2 = hard;
	 * 	3 = expert
	 * 	4 = heroic
	 * 
	 * @param device width
	 * @param device height
	 * @param difficulty
	 */
	public WorldChunk(int screenWidth, int screenHeight, String screenResolution, int chunkStartHeight, int chunkStartWidth, Player p, int difficulty, SegmentLoader loader)
	{
		this.difficulty = difficulty;
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.screenResolution = screenResolution;
		this.chunkStartHeight = chunkStartHeight;
		this.player = p;
		this.loader = loader;
		this.chunkStartWidth = chunkStartWidth;

		this.chunkSegments = new ArrayList<WorldSegment>();
		
		System.out.println("New chunk created.");
		generateSegments();
	}
	
	/**
	 * When the chunk is created, generate 10 segments via the chunk loader class.
	 */
	private void generateSegments()
	{
		if(chunkSegments.size() <= 0)
		{
			for(int i = 0; i < 10; i++)
			{
				// If we are not creating the first segment in the chunk, then link it to the last segment height
				if(chunkSegments.size() > 0)
				{
					chunkSegments.add(loader.getSegment(difficulty, chunkSegments.get(i-1).getEndHeight()));
					chunkSegments.get(i).setStartHeight(chunkSegments.get(i-1).getEndHeight());
				}
				else
				{
					// if we are generating the first chunk then use the chunk start pos
					if(chunkStartWidth > 0)
					{
						// if its the first segment but is not in the first chunk
						chunkSegments.add(loader.getSegment(difficulty, chunkStartHeight));
						chunkSegments.get(i).setStartHeight(chunkStartHeight);
					}
					else
					{
						// if it is the first segment of the first chunk
						chunkSegments.add(new flat(screenWidth, screenHeight, screenResolution, player, 0, 0, 0));
						chunkSegments.get(i).setStartHeight(chunkStartHeight);
					}
				}
				// set the chunk x position equal to where it is located in the chunk
				chunkSegments.get(i).setPosInChunk(i);
				// set its width equal to the width of the screen
				chunkSegments.get(i).setWidth(chunkStartWidth + (i * (screenWidth)));
				// create the chunks
				chunkSegments.get(i).create();
				// de activate the segment
				chunkSegments.get(i).deActivate();
			}
		}
	}
	
	public void render(SpriteBatch batch)
	{	
		// could make this smarter (only render when on screen)
		for(int i = 0; i < chunkSegments.size(); i++)
			chunkSegments.get(i).render(batch);
	}
	
	/**
	 * Deactivates the segment with the given list index number
	 * @param segmentNum
	 */
	public void deActivateSegment(int segmentNum)
	{
		chunkSegments.get(segmentNum).deActivate();
	}
	/**
	 * Activates the segment with the given list index number
	 * @param segmentNum
	 */
	public void activateSegment(int segmentNum)
	{
		chunkSegments.get(segmentNum).activate();
		this.currentSegment = chunkSegments.get(segmentNum);
	}
	/** 
	 * Returns the segment with the given list index number
	 * @param segmentNum
	 * @return
	 */
	public WorldSegment getActiveSegment()
	{
		return currentSegment;
	}
	/**
	 * Checks to see if the segment with the given index number is active
	 * @param segmentNum
	 * @return
	 */
	public Boolean getStateOfSegment(int segmentNum)
	{
		return chunkSegments.get(segmentNum).isActive();
	}
	
	public void pause()
	{
		for(int i = 0; i < chunkSegments.size(); i++)
			chunkSegments.get(i).pause();
	}
	
	public void resume()
	{
		for(int i = 0; i < chunkSegments.size(); i++)
			chunkSegments.get(i).resume();
	}
	
	public void dispose()
	{
		for(int i = 0; i < chunkSegments.size(); i++)
			chunkSegments.get(i).dispose();
	}
	
	/** 
	 * Gets the pixel position of where the chunk ends
	 * @return end position
	 */
	public int getEndPos()
	{
		return chunkStartHeight + (screenWidth * 10);
	}
	
	/** 
	 * Gets the pixel height of where the chunk ends
	 * @return end position
	 */
	public int getEndHeight()
	{
		return chunkSegments.get(chunkSegments.size()-1).getEndHeight();
	}
	
	public void activate()
	{
		for(int i = 0; i < chunkSegments.size(); i++)
			chunkSegments.get(i).activate();
	}
	
	public void deActivate()
	{
		for(int i = 0; i < chunkSegments.size(); i++)
			chunkSegments.get(i).deActivate();
	}
	
	public int currentSegmentNumber(float xPos)
	{
		float posInChunk = xPos-(chunkStartWidth - screenWidth/4);
		
		 if(posInChunk >= 0 && posInChunk <= screenWidth)
		  		return 1;
		 if(posInChunk > screenWidth && posInChunk <= screenWidth*2)
		  		return 2;
		 if(posInChunk > screenWidth*2 && posInChunk <= screenWidth*3)
		  		return 3;
		 if(posInChunk > screenWidth*3 && posInChunk <= screenWidth*4)
		  		return 4;
		 if(posInChunk > screenWidth*4 && posInChunk <= screenWidth*5)
		  		return 5;
		 if(posInChunk > screenWidth*5 && posInChunk <= screenWidth*6)
		  		return 6;
		 if(posInChunk > screenWidth*6 && posInChunk <= screenWidth*7)
		  		return 7;
		 if(posInChunk > screenWidth*7 && posInChunk <= screenWidth*8)
		  		return 8;
		 if(posInChunk > screenWidth*8 && posInChunk <= screenWidth*9)
		  		return 9;
		 if(posInChunk > screenWidth*9 && posInChunk <= screenWidth*10)
		  		return 10;
		 // this is not part of the chunk but it helps to detect when the player has completely left the chunk
		 if(posInChunk > screenWidth*10 && posInChunk <= screenWidth*12)
		  		return 11;
		 
		
		return 0;
	}
}

