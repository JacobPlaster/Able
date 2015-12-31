package utility;

import java.util.ArrayList;
import java.util.List;

import segments.flat;
import segments.left_wall;
import segments.ramp_left_1;
import segments.ramp_left_2;
import segments.ramp_left_3;
import segments.ramp_left_4;
import segments.ramp_right_1;
import segments.ramp_right_2;
import segments.ramp_right_3;
import segments.ramp_right_4;
import segments.right_wall;
import segments.single_hole_tolow_1;
import segments.single_hole_toupper_1;
import segments.tree_jump_1;
import segments.single_hole_1;
import Entities.Player;
import Entities.WorldSegment;

public class SegmentLoader {
	
	List<WorldSegment> easy;
	List<WorldSegment> medium;
	List<WorldSegment> hard;
	
	private int screenWidth, screenHeight;
	private String screenResolution;
	Player p;
	private int currentDifficultyLevel = 0;
	
	/**
	 * Creates a segment loader which is used to return random segments, however it never
	 * returns a segment that would push the ground off the screen.
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 * @param screenResolution
	 */
	public SegmentLoader(int screenWidth, int screenHeight, String screenResolution, Player p)
	{	
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.screenResolution = screenResolution;
		this.p = p;
		
		this.easy = new ArrayList<WorldSegment>();
		this.medium = new ArrayList<WorldSegment>();;
		this.hard = new ArrayList<WorldSegment>();;
		
		// adds all of the existing segments to their difficulty list sets
		// this segment will not actually be used, its just created so that we can use the search segments function
		// in order to see if(currentHeight + segments.get(randomNumber).size() < screenHeight-(screenHeight/3))
		// btw, this takes up hardly any resources since the create method is not being called
		this.easy.add(new flat(screenWidth, screenHeight, screenResolution, p, 0, 0, 0));
		// right side ramps
		this.easy.add(new ramp_right_1(screenWidth, screenHeight, screenResolution, p, 0, 0, 0));
		this.easy.add(new ramp_right_2(screenWidth, screenHeight, screenResolution, p, 0, 0, 0));
		this.easy.add(new ramp_right_3(screenWidth, screenHeight, screenResolution, p, 0, 0, 0));
		this.easy.add(new ramp_right_4(screenWidth, screenHeight, screenResolution, p, 0, 0, 0));
		// left side ramps
		this.easy.add(new ramp_left_1(screenWidth, screenHeight, screenResolution, p, 0, 0, 0));
		this.easy.add(new ramp_left_2(screenWidth, screenHeight, screenResolution, p, 0, 0, 0));
		this.easy.add(new ramp_left_3(screenWidth, screenHeight, screenResolution, p, 0, 0, 0));
		this.easy.add(new ramp_left_4(screenWidth, screenHeight, screenResolution, p, 0, 0, 0));
		// wholes (single)
		this.medium.add(new single_hole_1(screenWidth, screenHeight, screenResolution, p, 0, 0, 0));
		// Gaps
		this.medium.add(new tree_jump_1(screenWidth, screenHeight, screenResolution, p, 0, 0, 0));
		// for testing purposes
		this.hard.add(new right_wall(screenWidth, screenHeight, screenResolution, p, 0, 0, 0));
		this.hard.add(new left_wall(screenWidth, screenHeight, screenResolution, p, 0, 0, 0));
		this.hard.add(new single_hole_tolow_1(screenWidth, screenHeight, screenResolution, p, 0, 0, 0));
		this.hard.add(new single_hole_toupper_1(screenWidth, screenHeight, screenResolution, p, 0, 0, 0));
		this.hard.add(new tree_jump_1(screenWidth, screenHeight, screenResolution, p, 0, 0, 0));
	}

	/**
	 * Finds the perfect segment for the situation and then returns it to the world chunk class
	 * 
	 * @param difficulty
	 * @param currentHeight
	 * @return WorldSegment
	 */
	public WorldSegment getSegment(int difficulty, int currentHeight)
	{
		// randomly chooses either 1, 2 or 3 depending on the value of the currentDifficulty
		int segmentSelector = MathsLibrary.randomNumber(0, difficulty);// CHANGE THE '2' TO 'difficulty'
		System.out.println("Diffuculty: " + segmentSelector);
		
		if(segmentSelector <= 0)
			return getFreshSegment(searchSegments(easy, currentHeight), segmentSelector);
		if(segmentSelector == 1)
			return getFreshSegment(searchSegments(medium, currentHeight), segmentSelector);
		if(segmentSelector >= 2)
			return getFreshSegment(searchSegments(hard, currentHeight), segmentSelector);
		
		return new flat(screenWidth, screenHeight, screenResolution, p, 0, 0, 0);
	}
	
	/** 
	 * Search through the list of segments in order to find a random but suitable segment
	 * 
	 * @param segments
	 * @param currentHeight
	 * @return random segment id
	 */
	private int searchSegments(List<WorldSegment> segments, int currentHeight)
	{
		Boolean segmentFound = false;
		do
		{
			int randomNumber = MathsLibrary.randomNumber(0, segments.size());
			// makes sure the next chosen block doesnt go too high or too low
			if((currentHeight + segments.get(randomNumber).size() < screenHeight-(screenHeight/4)) 
					&& (currentHeight + segments.get(randomNumber).size() > screenHeight/4))
			{
				segmentFound = true;
				return randomNumber;
			}
		}
		while(!segmentFound);
		
		// if all else fails, return flat grass
		return 0;
	}

	/**
	 * Uses a list element id and difficulty to create a fresh new object reference.
	 * 
	 * @param list elementID
	 * @param difficulty
	 * @return New object
	 */
	private WorldSegment getFreshSegment(int elementID, int difficulty)
	{	
		//if(difficulty > -1)
			//return new tree_jump_1(screenWidth, screenHeight, screenResolution, p, 0, 0, 0);
		
	/*	int testInt = MathsLibrary.randomNumber(0, 3);
		if( testInt == 1)
			return new right_wall(screenWidth, screenHeight, screenResolution, p, 0, 0, 0);
		if( testInt != 1)
			return new left_wall(screenWidth, screenHeight, screenResolution, p, 0, 0, 0); */
		
		
		if(difficulty <= 0)
		{
			switch(elementID)
			{
			case 0:
				return new flat(screenWidth, screenHeight, screenResolution, p, 0, 0, 0);
			case 1:
				return new ramp_right_1(screenWidth, screenHeight, screenResolution, p, 0, 0, 0);
			case 2:
				return new ramp_right_2(screenWidth, screenHeight, screenResolution, p, 0, 0, 0);
			case 3:
				return new ramp_right_3(screenWidth, screenHeight, screenResolution, p, 0, 0, 0);
			case 4:
				return new ramp_right_4(screenWidth, screenHeight, screenResolution, p, 0, 0, 0);
			case 5:
				return new ramp_left_1(screenWidth, screenHeight, screenResolution, p, 0, 0, 0);
			case 6:
				return new ramp_left_2(screenWidth, screenHeight, screenResolution, p, 0, 0, 0);
			case 7:
				return new ramp_left_3(screenWidth, screenHeight, screenResolution, p, 0, 0, 0);
			case 8:
				return new ramp_left_4(screenWidth, screenHeight, screenResolution, p, 0, 0, 0);
			}
		} else if (difficulty == 1)
		{
			switch(elementID)
			{
			case 0:
				return new tree_jump_1(screenWidth, screenHeight, screenResolution, p, 0, 0, 0);
			case 1:
				return new single_hole_1(screenWidth, screenHeight, screenResolution, p, 0, 0, 0);
			default:
				return new tree_jump_1(screenWidth, screenHeight, screenResolution, p, 0, 0, 0);
			}
		} else if (difficulty >= 2)
		{
			switch(elementID)
			{
			// just returns flat grass since we havnt got any level 3 segments
			case 0:
				return new right_wall(screenWidth, screenHeight, screenResolution, p, 0, 0, 0);
			case 1: 
				return new left_wall(screenWidth, screenHeight, screenResolution, p, 0, 0, 0);
			case 2:
				return new single_hole_tolow_1(screenWidth, screenHeight, screenResolution, p, 0, 0, 0);
			case 3: 
				return new single_hole_toupper_1(screenWidth, screenHeight, screenResolution, p, 0, 0, 0);
			case 4:
				return new tree_jump_1(screenWidth, screenHeight, screenResolution, p, 0, 0, 0);
			}
		}
		
		return null;
	}

}
