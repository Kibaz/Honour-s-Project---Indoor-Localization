package runtime;

import filtering.KalmanFilter;
import filtering.Matrix;
import filtering.Vector;
import utils.CSVHandler;

public class TestFilterFusion {
	
	public static void main(String[] args)
	{
		
		double dt = 0.016; // Time step of each update
		
		
		String[][] accelData = CSVHandler.readCSV("accel_data");
		String[][] positions = CSVHandler.readCSV("trilat_results");
		
		/*
		 * Predicting position based on acceleration only
		 */
		
		// State variables :- Pos X, Pos Y, Vel X, Vel Y, Accel X, Accel Y
		Vector state = new Vector(new double[] {0,0,0,0,0,0});
		
		Matrix P = new Matrix(state.getDimension(),state.getDimension());
		P.setIdentity();
		
		Matrix A = new Matrix(state.getDimension(),state.getDimension());
		A.setIdentity();
		A.setDiag(dt, 2);
		A.setDiag(0.5*(dt*dt), 4);
		
		Matrix B = new Matrix(state.getDimension(),2);
		B.setZero();
		B.set(0, 0, (dt*dt)/2);
		B.set(2, 0, dt);
		B.set(4, 0, 1);
		
		B.set(1, 1, (dt*dt)/2);
		B.set(3, 1, dt);
		B.set(5, 1, 1);
		
		
		Matrix Q = new Matrix(state.getDimension(),state.getDimension());
		Q.setZero();
		Q.set(0, 0, Math.pow(dt, 4)/4);
		Q.set(0, 2, Math.pow(dt, 3)/2);
		Q.set(0, 4, (dt*dt)/2);
		Q.set(1, 1, Math.pow(dt, 4)/4);
		Q.set(1, 3, Math.pow(dt, 3)/2);
		Q.set(1, 5, (dt*dt)/2);
		Q.set(2, 0, Math.pow(dt, 3)/2);
		Q.set(2, 2, (dt*dt)/2);
		Q.set(2, 4, dt);
		Q.set(3, 1, Math.pow(dt, 3)/2);
		Q.set(3, 3, (dt*dt)/2);
		Q.set(3, 5, dt);
		Q.set(4, 0, (dt*dt)/2);
		Q.set(4, 2, dt);
		Q.set(4, 4, 1);
		Q.set(5, 1, (dt*dt)/2);
		Q.set(5, 3, dt);
		Q.set(5, 5, 1);
		
		Matrix R = new Matrix(4,4);
		R.set(0, 0, 0.172484584);
		R.set(1, 1, 0.783871974);
		R.set(2, 2, 1.78827E-07);
		R.set(3, 3, 7.52499E-06);
		
		Matrix H = new Matrix(4,state.getDimension());
		H.set(0, 0, 1);
		H.set(1, 1, 1);
		H.set(2, 4, 1);
		H.set(3, 5, 1);
		
		KalmanFilter filter = new KalmanFilter(state,null,A,P,Q,B,H,R);
		
		for(int i = 1; i < accelData.length; i++)
		{
			float ax = Float.parseFloat(accelData[i][0]);
			float ay = Float.parseFloat(accelData[i][1]);
			
			float px = Float.parseFloat(positions[i][0]);
			float py = Float.parseFloat(positions[i][1]);
			
			filter.predict(new Vector(new double[] {Math.pow(0.00945856754482411041581080461873,2),Math.pow(0.0613353446228192313725036337333,2)}));
			
			filter.setMeasurement(new Vector(new double[] {px,py,ax,ay}));
			
			filter.update();
			
			System.out.println(filter.getState());
		}
		
		


	}

}
