package utils;

import java.util.List;

/*
 * Class used for basic functions which are required more than once
 */

public class Utils {
	
	/*
	 * Method to convert a list of floating point values
	 * into an array of floating point values
	 */
	public static float[] floatListToArray(List<Float> list)
	{
		float[] result = new float[list.size()];
		for(int i = 0; i < list.size(); i++)
		{
			result[i] = list.get(i);
		}
		
		return result;
	}

}
