package utils;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import objects.Circle;

public class Maths {
	
	// Constants acquired by experimentation and calibration
	private static final double SIGNAL_STR_AT_1_METRE = -41;
	/* Range of environmental factor from 2-4 */
	private static final double SIGNAL_DECAY = 4; // Signal Decay exponent
	private static final double PATH_LOSS = 0;
	
	private static final double WAVELENGTH = 0.125;
	
	
	/*
	 * Calculate the distance using RSSI (Received Signal Strength Indicator)
	 * Based on the known signal strength at 1 metre
	 * Formula:- Distance = 10 ^ (Measured Power(RSSI at 1 Metre) - RSSI /10 * N)
	 */
	public static float calculateDistanceFromRSSI(double receivedStr)
	{
		return (float) Math.pow(10, (SIGNAL_STR_AT_1_METRE - receivedStr) / (10 * SIGNAL_DECAY));
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
		Vector2f pointOfIntersection = null; // Store common point of intersection
		
		// Find points of intersection between Circle 1 and 2
		Vector2f[] pointsOfIntersection = pointsOfIntersection(c1,c2);
		
		/* 
		 * Verify whether Circle 3 intersects at the intersection points
		 * Discovered with circle 1 and circle 2
		 */
		
		for(int i = 0; i < pointsOfIntersection.length; i++)
		{
			float dx = pointsOfIntersection[i].x - c3.getCentre().x;
			float dy = pointsOfIntersection[i].y - c3.getCentre().y;
			float distance = (float) Math.sqrt((dx * dx) + (dy * dy));
			if(Math.abs(distance) == c3.getRadius())
			{
				return pointsOfIntersection[i];
			}
		}
		
		return pointOfIntersection;
	}
	
	public static Vector2f[] pointsOfIntersection(Circle c1, Circle c2)
	{	
		// Subtract centres to get distance between centres
		Vector2f distanceBetweenCentres = Vector2f.sub(c2.getCentre(), c1.getCentre(), null);
		float distance = (float) Math.sqrt((distanceBetweenCentres.x * distanceBetweenCentres.x) + 
				(distanceBetweenCentres.y * distanceBetweenCentres.y));
		
		// Determine number of solutions
		if(distance > c1.getRadius() + c2.getRadius())
		{
			// No solutions as the circles are too far apart to intersect
			return new Vector2f[0];
		}
		else if(distance < Math.abs(c1.getRadius() - c2.getRadius()))
		{
			// No solutions because circles exist inside one another
			return new Vector2f[0];
		}
		else if((distance == 0) && (c1.getRadius() == c2.getRadius()))
		{
			// No solution because the circles coincide (Are equal)
			return new Vector2f[0];
		}
		else
		{
			float a = ((c1.getRadius() * c1.getRadius()) - (c2.getRadius() * c2.getRadius()) + (distance * distance)) / (2.0f * distance);
			float h = (float) Math.sqrt(c1.getRadius() * c1.getRadius() - a*a);
			
			// Find the point at the right angles to each triangle drawn at intersection of the circles
			Vector2f pointAtRightAngle = new Vector2f(c1.getCentre().x + a * (distanceBetweenCentres.x)/distance,
													c1.getCentre().y + a * (distanceBetweenCentres.y)/distance);
			
			Vector2f intersection1 = new Vector2f(pointAtRightAngle.x + h * (c2.getCentre().y - c1.getCentre().y)/distance,
												pointAtRightAngle.y - h * (c2.getCentre().x - c1.getCentre().x)/distance);
			Vector2f intersection2 = new Vector2f(pointAtRightAngle.x - h * (c2.getCentre().y - c1.getCentre().y)/distance,
					pointAtRightAngle.y + h * (c2.getCentre().x - c1.getCentre().x)/distance);
			
			return new Vector2f[] {intersection1, intersection2};
		}
		
	}
	
	/*
	 * Verify whether 2 points are equal
	 * Check X value and Y value
	 * Return result as a boolean value
	 */
	private static boolean checkPointsEqual(Vector2f point1, Vector2f point2)
	{
		if(point1.x == point2.x && point1.y == point2.y)
		{
			return true;
		}
		return false;
	}
	
	/*
	 * Create a transformation matrix to be ported
	 * To the GLSL shaders which will handle movement
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translate, float rx, float ry, float rz, float scale)
	{
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translate, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
		return matrix;
	}

}
