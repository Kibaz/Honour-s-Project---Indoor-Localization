package utils;

import org.lwjgl.util.vector.Vector2f;

import objects.Circle;

public class Maths {
	
	// Constants acquired by experimentation and calibration
	private static final double SIGNAL_STR_AT_1_METRE = -44;
	/* Range of environmental factor from 2-4 */
	private static final double SIGNAL_DECAY = 4; // Signal Decay exponent
	private static final double PATH_LOSS = 0;
	
	private static final double WAVELENGTH = 0.125;
	
	
	/*
	 * Calculate the distance using RSSI (Received Signal Strength Indicator)
	 * Based on the known signal strength at 1 metre
	 * Formula:- Distance = 10 ^ (Measured Power(RSSI at 1 Metre) - RSSI /10 * N)
	 */
	public static double calculateDistanceFromRSSI(double receivedStr)
	{
		return Math.pow(10, (SIGNAL_STR_AT_1_METRE - receivedStr) / (10 * SIGNAL_DECAY));
	}
	
	/*
	 * General formula for determining path loss at a certain distance
	 * With consideration of fade margin of RF signals is given by
	 * P(X) = 10nlog(d/d0)+ 20 log((4 * PI * d0) / Wavelength of 2.4GHz signal)
	 * Where n = signal decay exponent
	 * d = distance between transmitter and receiver
	 * d0 = reference distance (typically 1m)
	 */
	
	/*
	 * Second distance approximation method taking into account
	 * Path loss, wavelength of 2.4GHz signal and fade margin
	 */
	public static double calculateDistanceFromRSSIWithPathLoss(double receivedStr)
	{
		return Math.pow(10, (PATH_LOSS - (20*Math.log((4*Math.PI*1)/WAVELENGTH))/10 * SIGNAL_DECAY));
	}
	
	/*
	 * Find point of intersection between 2 circles
	 * Return result as an array of 2 double values
	 * 
	 * Equation of a circle :- (x - x1)^2 + (y - y1)^2 = r^2
	 * Where x and y are the unknown coordinates of the point
	 * of intersection and x1 and y1 are the centre points of
	 * the circle
	 * R = radius
	 */
	public static Vector2f findPointOfIntersection(Circle c1, Circle c2, Circle c3)
	{
		Vector2f pointOfIntersection = new Vector2f(0,0);
		
		
		return pointOfIntersection;
	}
	
	private static Vector2f[] pointsOfIntersection(Circle c1, Circle c2)
	{
		float x; // X coordinate of intersection point
		float y; // Y coordinate of intersection point
		// Get the squared values of both radiuses
		
		
		
	}
	
	private static boolean checkPointsEqual(Vector2f point1, Vector2f point2)
	{
		if(point1.x == point2.x && point1.y == point2.y)
		{
			return true;
		}
		return false;
	}

}
