package runtime;

import java.util.Random;

import org.apache.commons.math3.filter.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/*
 * This class is being used to test the Apache Commons
 * Kalman Filter package in order to compare it with
 * the basic implementation created in this project.
 * 
 * This will verify whether the results are correct
 * in the basic implementation and provide a better
 * understanding of the Kalman Filter.
 */

public class TestFilterPackage {
	
	public static void main(String args[])
	{
		double dt = 0.1;
		double halfDtSqrd = 0.5 * dt * dt;
		
		RealMatrix A = new Array2DRowRealMatrix(new double[][] {{1,dt,halfDtSqrd,0,0,0},
																{0,1,dt,0,0,0},
																{0,0,1,0,0,0},
																{0,0,0,1,dt,halfDtSqrd},
																{0,0,0,0,1,dt},
																{0,0,0,0,0,1}});
		
		RealMatrix B = new Array2DRowRealMatrix(new double[][] {{1,0,0,0,0,0},
																{0,1,0,0,0,0},
																{0,0,1,0,0,0},
																{0,0,0,1,0,0},
																{0,0,0,0,1,0},
																{0,0,0,0,0,1}});
		
		RealMatrix H = new Array2DRowRealMatrix(new double[][] {{1,0,0,0,0,0},
																{0,0,0,1,0,0}});
		
		RealMatrix Q = new Array2DRowRealMatrix(new double[][] {{0.1,0.1,0.1,0,0,0},
																{0.1,0.1,0.1,0,0,0},
																{0.1,0.1,0.1,0,0,0},
																{0,0,0,0.1,0.1,0.1},
																{0,0,0,0.1,0.1,0.1},
																{0,0,0,0.1,0.1,0.1}});
		
		RealMatrix P = new Array2DRowRealMatrix(new double[][] {{10,0,0,0,0,0},
																{0,10,0,0,0,0},
																{0,0,10,0,0,0},
																{0,0,0,10,0,0},
																{0,0,0,0,10,0},
																{0,0,0,0,0,10}});
		
		RealMatrix R = new Array2DRowRealMatrix(new double[][] {{4,0},
																{0,4}});
		
		RealVector X = new ArrayRealVector(new double[] {0,0,0,0,0,0});
		
		ProcessModel pm = new DefaultProcessModel(A,B,Q,X,P);
		MeasurementModel mm = new DefaultMeasurementModel(H,R);
		
		KalmanFilter filter = new KalmanFilter(pm,mm);
		
		
		for(int i = 0; i < 10; i++)
		{
			filter.predict();
			Random random = new Random();
			double x = Math.abs(0.1f * random.nextGaussian());
			double y = Math.abs(0.1f * random.nextGaussian());
			RealVector z = new ArrayRealVector(new double[] {0.1,0.1});
			
			filter.correct(z);
			
			System.out.println(filter.getStateEstimation()[0]);
			System.out.println(filter.getStateEstimation()[1]);
			System.out.println(filter.getStateEstimation()[2]);
			System.out.println(filter.getStateEstimation()[3]);
			System.out.println(filter.getStateEstimation()[4]);
			System.out.println(filter.getStateEstimation()[5]);
		}

	}

}
