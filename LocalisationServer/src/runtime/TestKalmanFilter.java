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
		float dt = 0.1f;
		// Initialise test parameters for kalman filter
		Vector state = new Vector(new float[]{0.0f,0.0f,0.1f,0.1f});
		Matrix A = new Matrix(state.getDimension(),state.getDimension());
		A.setIdentity();
		A.set(0, 2, dt);
		A.set(1, 3, dt);
		
		Matrix P = new Matrix(state.getDimension(), state.getDimension());
		P.setDiag(0.1f);
		
		Matrix Q = new Matrix(state.getDimension(),state.getDimension());
		Q.setIdentity();
		
		Matrix B = new Matrix(state.getDimension(),state.getDimension());
		B.setIdentity();
		
		Matrix H = new Matrix(2,4);
		H.setZero();
		H.set(0, 0, 1);
		H.set(1, 1, 1);
		
		Matrix R = new Matrix(2,2);
		R.setIdentity();
		
		// Configure test measurements
		Random random = new Random(); // Generate random number
		Vector Z = new Vector(new float[] {state.get(0) + (float)Math.abs(random.nextGaussian()),state.get(0) + (float)Math.abs(random.nextGaussian())});
		
		
		KalmanFilter filter = new KalmanFilter(state,Z,A,P,Q,B,H,R);
		
		Vector u = new Vector(new float[] {0,0,0,0});
		
		for(int i = 0; i < 50; i++)
		{
			filter.predict(u);
			
			filter.update();
			Z = new Vector(new float[] {filter.getState().get(0) + 0.1f *(float)Math.abs(random.nextGaussian()),
					filter.getState().get(1) + 0.1f * (float)Math.abs(random.nextGaussian())});
			filter.setMeasurement(Z);
			
			System.out.println("Iteration: " + i + " " + filter.getState());
		}

		
		
	}

}
