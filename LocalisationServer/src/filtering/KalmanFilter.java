package filtering;

public class KalmanFilter {
	
	// Notes:
	
	/*
	 * Transition Model Formula: Xt = AtXt-1 + Btut + et
	 * Where Xt is a combination of the previous state (Xt-1) provided
	 * some transformation matrix of A, an input value used to change
	 * the state (u) and the noise (e).
	 * 
	 * Observation Model Formula: Zt = CtXt + St
	 * Where C is the transformation matrix and S is the 
	 * measurement of noise (noise caused by faulty measurements).
	 * 
	 * Prediction Formulae:
	 * u_t = ut-1
	 * _t = _t-1 + Rt
	 * 
	 * Kalman Gain:
	 */
	
	// Fields
	// Specify the attributes which form the Kalman filtering algorithm
	
	// Constructor
	public KalmanFilter()
	{
		
	}
	
	public void update()
	{
		
	}
	
	public void predict()
	{
		
		
	}

}
