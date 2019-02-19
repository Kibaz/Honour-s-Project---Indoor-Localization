package runtime;

import java.util.Random;

import filtering.KalmanFilter;
import filtering.Matrix;
import filtering.Vector;

/*
 * This "main" class is being used for testing
 * the implementation of a Kalman filter only.
 * 
 * This is to ensure that the Kalman Filter is producing the
 * correct/expected results and thus the implementation
 * is accurate.
 */

public class TestKalmanFilter {
	
	public static void main(String[] args)
	{
		// Attempt to fuzz raw RSSI data to test the Kalman Filter
		// Analyse results produced before implementing in main system
		double dt = 0.1;
		// Model state in localisation system as position, velocity and acceleration in 2D space
		Vector initState = new Vector(new double[] {0,0,0,0,0,0}); // x,y,v1,v2,a1,a2
		
		// Initialise matrix "A" as the transformation matrix
		Matrix A = new Matrix(initState.getDimension(),initState.getDimension());
		A.setIdentity(); // Set as identity matrix by default
		// Define values which will affect position using delta time
		// Velocity values
		A.set(0, 1, dt);
		A.set(1, 2, dt);
		A.set(3, 4, dt);
		A.set(4, 5, dt);
		
		// Acceleration values
		double halfDtSqrd = 0.5 * Math.pow(dt, 2);;
		A.set(0, 2, halfDtSqrd);
		A.set(3, 5, halfDtSqrd);
		
		
		/*
		 *  Initialise with diagonal set to medium value 
		 *  so that initial state values do no influence results
		 *  
		 *  P = State covariance matrix
		 */
		Matrix P = new Matrix(initState.getDimension(), initState.getDimension());
		P.setDiag(10); 
		
		// Process noise matrix
		Matrix Q = new Matrix(initState.getDimension(),initState.getDimension());
		for(int i = 0; i < Q.getXDim(); i++)
		{
			for(int j = 0; j < Q.getYDim(); j++)
			{
				if((i < Q.getXDim()/2 && j < Q.getYDim()/2) || (i >= Q.getXDim()/2 && j >= Q.getYDim()/2))
				{
					Q.set(i, j, 0.1f);
				}
			}
		}
		
		Matrix B = new Matrix(initState.getDimension(),initState.getDimension());
		B.setIdentity();
		
		Matrix H = new Matrix(2,initState.getDimension());
		H.setZero();
		H.set(0, 0, 1);
		H.set(1, 3, 1);
		
		Matrix R = new Matrix(2,2);
		R.setDiag(4);
		
		// Initialise filter with measurement vector set to null and initState of 0 values
		KalmanFilter filter = new KalmanFilter(initState,null,A,P,Q,B,H,R);
		
		Random random = new Random();
		
		for(int i = 0; i < 10; i++)
		{
			filter.predict(new Vector(new double[] {0,0,0,0,0,0}));
			
			Vector mNoise = new Vector(new double[] {0,random.nextGaussian()});
			
			// Determine z measurements vector based on measurement noise
			Vector z = new Vector(new double[] {0,0});
			
			filter.setMeasurement(z);
			
			filter.update();
			System.out.println(filter.getState());
		}

		
		
	}

}
