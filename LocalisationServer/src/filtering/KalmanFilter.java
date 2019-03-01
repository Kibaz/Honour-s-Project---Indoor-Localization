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
	 * K = pH^T(HpH^T + R)^-1 Where ^T is the transpose of the matrix
	 * xK = X + K(z - HX)
	 * PxK = (I - KH)p
	 * Kalman Gain:
	 */
	
	// Fields
	// Specify the attributes which form the Kalman filtering algorithm
	private Vector lastState;
	private Vector X; // State variables vector
	
	private Vector Z; // Measurements returned by sensors at each location
	
	private Matrix P; // State covariance matrix
	private Matrix Q; // Process covariance matrix / noise
	private Matrix H; // Model of the sensor technology - can be adjusted to fit model
	private Matrix R; // Confidence matrix
	
	private Matrix I; // Simply stores an identity matrix
	
	private Matrix A; // Transformation matrix between states
	private Matrix B; // The input effect matrix
	
	private Matrix K; // Kalman gain, used in the update section of the Kalman filter
	
	// Constructor
	public KalmanFilter(Vector X, Vector Z,Matrix A, Matrix P, Matrix Q, Matrix B, Matrix H, Matrix R)
	{
		this.X = X;
		this.lastState = X;
		this.A = A;
		this.P = P;
		this.Q = Q;
		this.B = B;
		this.H = H;
		this.R = R;
		this.Z = Z;
		
		// Initialise I as a standard Identity matrix
		I = new Matrix(X.getDimension(),X.getDimension());
		I.setIdentity();
	}
	
	public void update()
	{
		// K = P*H^{T} * (H*P*H^{T} + R) ^ -1
		// Break equation up H*P*H^{T} + R = S
		Matrix S = H.multiply(P.multiply(H.transpose())).add(R);
		K = P.multiply(H.transpose()).multiply(S.inverse());
		Vector y = Z.sub(lastState.transform(H));
		X = X.add(y.transform(K));
		P = (I.sub(K.multiply(H))).multiply(P);
		lastState = X;
	}
	
	// Predict using control input unit
	// The control input unit (u) determines how states are transitioned
	public void predict(Vector u)
	{
		// X = A * prevState + B * u
		X = lastState.transform(A).add(u.transform(B));
		// P = A * P * A^T + Q
		/*
		 * Handle A * P and A^T separately
		 * before multiplication.
		 * 
		 * Also, handle multiplication in order of
		 * A^T multiplied by AP to result in correct matrix
		 */
		Matrix AT = A.transpose();
		Matrix AP = A.multiply(P);
		P = AT.multiply(AP).add(Q);
	}
	
	/* Predict without control input unit
	public void predict()
	{

	}*/
	
	public Vector getState()
	{
		return X;
	}
	
	public void setMeasurement(Vector Z)
	{
		this.Z = Z;
	}

	public void setQ(Matrix q) {
		Q = q;
	}

	public void setA(Matrix a) {
		A = a;
	}

	public void setB(Matrix b) {
		B = b;
	}
	
	

}
