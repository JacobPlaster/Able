package utility;


import com.badlogic.gdx.math.Vector2;


public class MathsLibrary 
{

	/**
	 * Returns a random number between the given parameteres
	 *
	 * @param Min number
	 * @param Max number
	 */
	public static int randomNumber(float min, float max)
	{
		int num;
		num =  (int) (min + (Math.random() * (max - min)));
		return num;
	}
	
	/**
	 * Returns the distance form a point to a line.
	 *
	 * @param Line start position
	 * @param Line end position
	 * @param Point
	 */
	public static double pointToLineDistance(Vector2 A, Vector2 B, Vector2 P) {
		double normalLength = Math.sqrt((B.x-A.x)*(B.x-A.x)+(B.y-A.y)*(B.y-A.y));
	    return Math.abs((P.x-A.x)*(B.y-A.y)-(P.y-A.y)*(B.x-A.x))/normalLength;
	  }
	
	/**
	 * Creates a line segment between vec a and vec b. It then returns true if vec c is above the line
	 * and false if it is below
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return bool true if above
	 */
	public static Boolean isAboveLine(Vector2 a, Vector2 b, Vector2 c){
	     return ((b.x - a.x)*(c.y - a.y) - (b.y - a.y)*(c.x - a.x)) > 0;
	}
	
	/**
	 * Returns the angle of the 2 points in degrees
	 *
	 * @param center x
	 * @param center y
	 * @param target x
	 * @param target y
	 */
	public static double getAngleDegrees(Vector2 a, Vector2 b)
	{	
		double angle = (Math.toDegrees(Math.atan2((b.x - a.x), (b.y - a.y))) - 90) % 360.0;
	
		return  angle;
	}
	
	/**
	 * Get the angle of between the two inputted points
	 * @param vec point 1
	 * @param vec point 2
	 * @return
	 */
	public static double getAngleRad(Vector2 a, Vector2 b)
	{	
		double angle = (Math.atan2((b.y - a.y), (b.x - a.x)));
		return  angle;
	}
}
