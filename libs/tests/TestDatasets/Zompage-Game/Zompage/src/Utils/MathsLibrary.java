package Utils;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class MathsLibrary {
	
	/**
	 * Get difference between two points in a 2D environment
	 *
	 * @param first point x
	 * @param first point y
	 * @param second point x
	 * @param second point y
	 */
	public float getDistance2D(float x1, float y1, float x2, float y2)
	{
		float dx = x1 - x2;
		float dy = y1 - y2;
		float answer = (float) Math.sqrt((dx * dx) + (dy * dy));
		
		return answer;
	}
	
	/**
	 * Get difference between two points in a 3D environment
	 *
	 * @param first point x
	 * @param first point y
	 * @param first point z
	 * @param second point x
	 * @param second point y
	 * @param second point z
	 */
	public float getDistance3D(float x1, float y1, float x2, float y2, float z1, float z2)
	{
		float dx = x1 - x2;
		float dy = y1 - y2;
		float dz = z1 - z2;
		float answer = (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
		
		return answer;
	}
	
	/**
	 * Gets a 3rd point along the given line when the line reaches a certain radius away from the 1st point
	 *
	 * @param first point x
	 * @param first point y
	 * @param second point x (to get line)
	 * @param second point y (to get line)
	 * @param radius to get 3rd point
	 */
	public float[] get3rdPoint(float x1, float y1, float x2, float y2, float radius)
	{	
		float[] p = new float[2];
	      
	      float dx = x1 - x2;
	      float dy = y1 - y2;
	      float angle = (float) Math.atan(Math.abs(dy/dx));
	      
	      // Make the cos and sin methods work in all quadrants of the radius
	      if(dx >= 0 && dy >= 0)
	      {	// Top right
		      p[0] = (float)(x1 - (radius*Math.cos(angle)));
		      p[1] = (float)(y1 - (radius*Math.sin(angle)));
	      } else if (dx >= 0 && dy < 0)
	      {// Bottom right
	    	  p[0] = (float)(x1 - (radius*Math.cos(angle)));
		      p[1] = (float)(y1 + (radius*Math.sin(angle)));
	      } else if (dx < 0 && dy < 0)
	      {// Bottom left
	    	  p[0] = (float)(x1 + (radius*Math.cos(angle)));
		      p[1] = (float)(y1 + (radius*Math.sin(angle)));
	      } else if (dx < 0 && dy >= 0)
	      {
	    	  p[0] = (float)(x1 + (radius*Math.cos(angle)));
		      p[1] = (float)(y1 - (radius*Math.sin(angle)));
	      }
		
		return p;
	}
		
	
	/**
	 * Returns the angle of the 2 points in degrees
	 *
	 * @param center x
	 * @param center y
	 * @param target x
	 * @param target y
	 */
	public double getAngle(float x1, float y1, float x2, float y2)
	{	
		double angle = (Math.toDegrees(Math.atan2((x2 - x1), (y2 - y1))) + 180) % 360.0;
	
		return  angle;
	}
	
	public double getAngleRad(float x1, float y1, float x2, float y2)
	{	
		double angle = (Math.atan2((y2 - y1), (x2 - x1)));
	
		return  angle;
	}
	
	/**
	 * Converts a Matrix4 object into a vevtor3 object. 
	 *
	 * @param Matrix to convert
	 * @return 
	 */
	public Vector3 convertToVec(Matrix4 m)
	{
		Vector3 v = new Vector3(0, 0, 0);
		v = m.getTranslation(v);
		return v;
	}
	
	/**
	 * Returns a random number between the given parameteres
	 *
	 * @param Min number
	 * @param Max number
	 */
	public float randomNumber(float min, float max)
	{
		float num;
		num = (float) (min + (Math.random() * (max - min)));
		return num;
	}
	
	/**
	 * Returns the point at the end of a line at a certain distance.
	 *
	 * @param Angle in radians
	 * @param Distance
	 */
	public Vector3 getLineViaAngle(Matrix4 m, float angle, float distance)
	{
		float incX = 0;
		float incZ = 0;
		
		incX -= (float) (distance * Math.sin(0));
		incZ -= (float) (distance * Math.cos(0));
		
		m.translate(incX, 0, incZ);
				
		return convertToVec(m);
	}

	/**
	 * Returns the distance form a point to a line.
	 *
	 * @param Line start position
	 * @param Line end position
	 * @param Point
	 */
	public double pointToLineDistance(Vector3 A, Vector3 B, Vector3 P) {
	    double normalLength = Math.sqrt((B.x-A.x)*(B.x-A.x)+(B.z-A.z)*(B.z-A.z));
	    return Math.abs((P.x-A.x)*(B.z-A.z)-(P.z-A.z)*(B.x-A.x))/normalLength;
	  }
}
