package objects;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import dataHandling.DataManager;
import dataHandling.DeviceData;
import filtering.KalmanFilter;
import filtering.Matrix;
import filtering.Vector;
import fontHandling.FontStyle;
import fontHandling.Fonts;
import fontHandling.TextModel;
import graphics.Loader;
import graphics.Window;
import utils.CSVHandler;
import utils.Maths;

public class Device {
	
	private final int SAMPLE_LIMIT = 500; // Limit trilateration point sampling to 500 samples
	private int sampleCount = 0; // Track number of samples written to CSV for data processing
	
	private float deltaTime = 0; // Add up time in between position estimations
	
	private String macAddress; // MAC address associated with the device
	private Vector2f location; // Location of the device relative to fixed locations of monitors
	
	// Each device will store a list of device data captured for the corresponding device
	private CopyOnWriteArrayList<DeviceData> data;
	private CopyOnWriteArrayList<Float> filteredRSSI;
	
	private CopyOnWriteArrayList<float[]> accelerationData;
	
	private Circle pointer; // Pointer for visualising devices location
	
	// Hold reference to locators
	private Circle locator1; 
	private Circle locator2;
	private Circle locator3;
	
	private TextModel addressTag;
	
	private Vector lastState;
	
	private KalmanFilter filter;
	
	// Position estimation variables
	private KalmanFilter positionFilter; // For carrying out filtering and predictions
	
	private Vector2f lastTrilaterationPoint;
	
	// Constructor
	public Device(String macAddress)
	{
		this.macAddress = macAddress;
		lastState = new Vector(new double[] {0,0});
		data = new CopyOnWriteArrayList<>();
		filteredRSSI = new CopyOnWriteArrayList<>();
		accelerationData = new CopyOnWriteArrayList<>();
		initFilter();
		initPositionFilter();
	}
	
	private void initFilter()
	{
		Vector initState = new Vector(new double[] {0,0});
		// Configure state transformation matrix
		Matrix A = new Matrix(lastState.getDimension(), lastState.getDimension());
		A.setIdentity();
		//A.set(0, 1, 0.2);
		
		Matrix P = new Matrix(lastState.getDimension(), lastState.getDimension());
		P.setDiag(100);
		
		Matrix Q = new Matrix(lastState.getDimension(), lastState.getDimension());
		Q.setDiag(0.008);
		
		Matrix B = new Matrix(lastState.getDimension(), lastState.getDimension());
		B.setIdentity();
		
		Matrix H = new Matrix(1,2);
		H.setIdentity();
		
		Matrix R = new Matrix(1,1);
		R.set(0, 0,  Math.pow(0.712,2)); // R configured to covariance over 500 samples
		
		
		filter = new KalmanFilter(initState,null,A,P,Q,B,H,R);
	}
	
	/*
	 * Initialise Kalman Filter for determining the positions
	 * of this device. Configurations for this Kalman Filter have
	 * been devised using collections of data taken using both
	 * Android smartphone and the localisation built using
	 * Raspberry PIs
	 */
	private void initPositionFilter()
	{	
		// Model initial state of position filter as X,Y positions X,Y velocity and X,Y acceleration
		Vector initialState = new Vector(new double[] {0,0,0,0,0,0});
		double dt = 1;
		
		// Transformation matrix between previous state and next state
		Matrix A = new Matrix(initialState.getDimension(),initialState.getDimension());
		A.setIdentity();
		A.setDiag(dt,2);
		A.setDiag(Math.pow(dt, 2)/2,4);
		
		// Initial state covariance matrix - this matrix adjusts itself accordingly
		Matrix P = new Matrix(initialState.getDimension(), initialState.getDimension());
		P.setDiag(1);; // Configure with high values - do not trust initial state (we do not yet know the position of the device)
		
		// Process noise covariance matrix
		Matrix Q = new Matrix(initialState.getDimension(),initialState.getDimension());
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
		
		// Configure B Matrix - Input Effect Matrix to determine the effect of acceleration on the current state
		Matrix B = new Matrix(initialState.getDimension(),2);
		B.setZero();
		B.set(0, 0, (dt*dt)/2);
		B.set(2, 0, dt);
		B.set(4, 0, 1);
		
		B.set(1, 1, (dt*dt)/2);
		B.set(3, 1, dt);
		B.set(5, 1, 1);
		
		// Configure measurement matrix to describe the measurements taken at each time step
		Matrix H = new Matrix(4,initialState.getDimension());
		H.setZero();
		H.set(0, 0, 1);
		H.set(1, 1, 1);
		H.set(2, 4, 1);
		H.set(3, 5, 1);
		
		// 4 Measurements - Px, Py, Ax, Ay - From trilateration and accelerometer
		Matrix R = new Matrix(4,4);
		R.setZero();
		R.set(0, 0, 0.172484584);
		R.set(1, 1, 0.783871974);
		R.set(2, 2, 1.78827E-07);
		R.set(3, 3, 7.52499E-06);
		
		positionFilter = new KalmanFilter(initialState,null,A,P,Q,B,H,R);
	}
	
	public void smoothRSSIData()
	{
		filteredRSSI.clear();
		// Configure and Model Kalman Filter for smoothing RSSI data

		
		Iterator<DeviceData> it = data.iterator();
		List<DeviceData> toRemove = new ArrayList<>();
		while(it.hasNext())
		{
			DeviceData curr = it.next();
			float rssi = curr.getRssi();
			
			filter.predict(new Vector(new double[] {0,0}));
			
			Vector z = new Vector(new double[] {rssi});
			filter.setMeasurement(z);
			
			filter.update();
			
			lastState = filter.getState();
			filteredRSSI.add((float) lastState.get(0));
			// Register data to be removed after processing
			toRemove.add(curr);
		}
		// When there is has been data to be filtered
		
		// Remove processed data
		data.removeAll(toRemove);
		
		/*for(DeviceData deviceData: data)
		{
			float rssi = deviceData.getRssi();
			
			filter.predict(new Vector(new double[] {0,0}));
			
			Vector z = new Vector(new double[] {rssi});
			filter.setMeasurement(z);
			
			filter.update();
			
			System.out.println(filter.getState());
		}*/
	}
	
	public void estimatePosition(DataManager dataManager, List<Device> devicePointers, Shape baseCircle, TextModel addressTag)
	{
		// Collect all data for this current device
		Device first = dataManager.getFirstMonitor().getDeviceIfExists(this.macAddress);
		Device second = dataManager.getSecondMonitor().getDeviceIfExists(this.macAddress);
		Device third = dataManager.getThirdMonitor().getDeviceIfExists(this.macAddress);
		
		// Commented code used to extract data for calibration and noise reduction
		/*if(this.macAddress.equals("b0:47:bf:92:74:97") && sampleCount < SAMPLE_LIMIT)
		{
			CSVHandler.createCSV("trilat_results", new String[] {"x","y"});
			sampleCount = CSVHandler.rowCount("trilat_results"); // Find number of samples in file
			System.out.println(sampleCount);
		}*/
		
		deltaTime += Window.getDeltaTime(); // Increment delta time over each iteration
		
		// Check whether data has been received from this device by each monitor
		if(first != null && second != null && third != null)
		{
			// Commented code used to extract data for calibration and noise reduction
			/*for(Vector2f point: points)
			{
				
				if(this.macAddress.equals("b0:47:bf:92:74:97") && sampleCount < SAMPLE_LIMIT)
				{
					CSVHandler.appendRow(new File("files/trilat_results.csv"), new Object[] {point.x,point.y});
				}
				
			}*/
			
			// Make predictions of next state using Kalman Filtering algorithm
			// Take both acceleration and trilateration results to predict next state
			double ax = 0;
			double ay = 0;
			
			double px = 0;
			double py = 0;
			
			// If acceleration data has been received from the mobile device
			// Carry out position estimation algorithms
			// Carry out trilateration algorithm with received device data
			Vector2f optimisedPoint = trilateration(dataManager,first,second,third);

			if(optimisedPoint != null)
			{
				lastTrilaterationPoint = optimisedPoint;
				/*Circle pointer = new Circle(dataManager.getFirstMonitor(),this.macAddress,0.05f,
					optimisedPoint,baseCircle,true);
				
				pointer.setColour(0, 1, 0);
				this.setPointer(pointer);
				if(!devicePointers.contains(this))
				{
					devicePointers.add(this);
				}*/
			}
			if(accelerationData.size() !=  0 || !accelerationData.isEmpty() && lastTrilaterationPoint != null)
			{
				List<float[]> processedAccelerationData = new ArrayList<>();
				for(float[] acceleration: accelerationData)
				{
					ax += acceleration[0]; // Accumulate total acceleration in X
					ay += acceleration[1]; // Accumulate total acceleration in Y
					processedAccelerationData.add(acceleration);
				}
				
				
				// Re-configure Kalman Filter matrices based on delta time
				int stateDim = positionFilter.getState().getDimension();
				
				// Re-configure A
				Matrix A = new Matrix(stateDim,stateDim);
				A.setIdentity();
				A.setDiag(deltaTime,2);
				A.setDiag(Math.pow(deltaTime, 2)/2,4);
				positionFilter.setA(A);
				
				// Re-configure Q
				Matrix Q = new Matrix(stateDim,stateDim);
				Q.setZero();
				Q.set(0, 0, Math.pow(deltaTime, 4)/4);
				Q.set(0, 2, Math.pow(deltaTime, 3)/2);
				Q.set(0, 4, (deltaTime*deltaTime)/2);
				Q.set(1, 1, Math.pow(deltaTime, 4)/4);
				Q.set(1, 3, Math.pow(deltaTime, 3)/2);
				Q.set(1, 5, (deltaTime*deltaTime)/2);
				Q.set(2, 0, Math.pow(deltaTime, 3)/2);
				Q.set(2, 2, (deltaTime*deltaTime)/2);
				Q.set(2, 4, deltaTime);
				Q.set(3, 1, Math.pow(deltaTime, 3)/2);
				Q.set(3, 3, (deltaTime*deltaTime)/2);
				Q.set(3, 5, deltaTime);
				Q.set(4, 0, (deltaTime*deltaTime)/2);
				Q.set(4, 2, deltaTime);
				Q.set(4, 4, 1);
				Q.set(5, 1, (deltaTime*deltaTime)/2);
				Q.set(5, 3, deltaTime);
				Q.set(5, 5, 1);
				positionFilter.setA(Q);
				
				Matrix B = new Matrix(stateDim,2);
				B.setZero();
				B.set(0, 0, (deltaTime*deltaTime)/2);
				B.set(2, 0, deltaTime);
				B.set(4, 0, 1);
				
				B.set(1, 1, (deltaTime*deltaTime)/2);
				B.set(3, 1, deltaTime);
				B.set(5, 1, 1);
				positionFilter.setB(B);
				
				
				// Make predictions on position based on acceleration data received
				Vector u = new Vector(new double[] {Math.pow(0.00945856754482411041581080461873,2),Math.pow(0.0613353446228192313725036337333,2)});
				positionFilter.predict(u);
				
				// Use prediction to compute the next likely state
				// Use trilateration position as reference point for the position
				Vector measurements = new Vector(new double[] {lastTrilaterationPoint.x,lastTrilaterationPoint.y,ax,ay});
				positionFilter.setMeasurement(measurements);
					
				// Update positions
				positionFilter.update();

				System.out.println(positionFilter.getState());
				Circle pointer = new Circle(dataManager.getFirstMonitor(),this.macAddress,0.05f,
						new Vector2f((float) positionFilter.getState().get(0),(float) positionFilter.getState().get(1)),baseCircle,true);
				pointer.setColour(0, 1, 0);
				this.setPointer(pointer);
				if(!devicePointers.contains(this))
				{
					devicePointers.add(this);
				}
				
				// Remove processed acceleration data
				accelerationData.removeAll(processedAccelerationData);
				
				// Reset delta time after position estimation has been processed
				deltaTime %= deltaTime;
				
			}
			

			
		}
		
	}
	
	private Vector2f trilateration(DataManager dataManager,Device first, Device second, Device third)
	{
		List<Vector2f> pointsFound = new ArrayList<>(); // Store list of points found by algorithm
		List<Vector2f[]> pointsOfX = new ArrayList<>(); // Store arrays of points to fill found points array
		
		// Loop through the data received from each monitor for this device
		// Acquire all points of intersection created by the RSSI data
		
		double[] distances = new double[first.getData().size() + second.getData().size() + third.getData().size()];
		int distCount = 0;
		for(int i = 0; i < first.getData().size(); i++)
		{
			distances[distCount] = first.getData().get(i).getDistanceFromMonitor();
			distCount++;
		}
		
		for(int i = 0; i < second.getData().size(); i++)
		{
			distances[distCount] = second.getData().get(i).getDistanceFromMonitor();
			distCount++;
		}
		
		for(int i = 0; i < third.getData().size(); i++)
		{
			distances[distCount] = third.getData().get(i).getDistanceFromMonitor();
			distCount++;
		}
		
		// Locators from First and Second monitor
		for(DeviceData data1: first.getData())
		{
			Circle c1 = new Circle(dataManager.getFirstMonitor(),first.getMacAddress(),data1.getDistanceFromMonitor(),
					dataManager.getFirstMonitor().getLocation(),null,false);
			for(DeviceData data2: second.getData())
			{
				Circle c2 = new Circle(dataManager.getSecondMonitor(),second.getMacAddress(),data2.getDistanceFromMonitor(),
						dataManager.getSecondMonitor().getLocation(),null,false);
				pointsOfX.add(Maths.pointsOfIntersection(c1, c2));
			}
		}
		
		// Locators from First and Third monitors
		for(DeviceData data1: first.getData())
		{
			Circle c1 = new Circle(dataManager.getFirstMonitor(),first.getMacAddress(),data1.getDistanceFromMonitor(),
					dataManager.getFirstMonitor().getLocation(),null,false);
			for(DeviceData data2: third.getData())
			{
				Circle c2 = new Circle(dataManager.getThirdMonitor(),third.getMacAddress(),data2.getDistanceFromMonitor(),
						dataManager.getThirdMonitor().getLocation(),null,false);
				pointsOfX.add(Maths.pointsOfIntersection(c1, c2));
			}
		}
		
		// Locators from Second and Third monitors
		for(DeviceData data1: second.getData())
		{
			Circle c1 = new Circle(dataManager.getSecondMonitor(),second.getMacAddress(),data1.getDistanceFromMonitor(),
					dataManager.getSecondMonitor().getLocation(),null,false);
			for(DeviceData data2: third.getData())
			{
				Circle c2 = new Circle(dataManager.getThirdMonitor(),third.getMacAddress(),data2.getDistanceFromMonitor(),
						dataManager.getThirdMonitor().getLocation(),null,false);
				pointsOfX.add(Maths.pointsOfIntersection(c1, c2));
			}
		}
			
		first.getData().clear();
		second.getData().clear();
		third.getData().clear();
		
		// Populate list of points found with list of points arrays
		for(int i = 0; i < pointsOfX.size(); i++)
		{
			for(int j = 0; j < pointsOfX.get(i).length; j++)
			{
				pointsFound.add(pointsOfX.get(i)[j]);
			}
		}
		
		Vector2f[] locations = new Vector2f[] {dataManager.getFirstMonitor().getLocation(),
												dataManager.getSecondMonitor().getLocation(),
												dataManager.getThirdMonitor().getLocation()};
		
		if(!pointsFound.isEmpty())
		{
			double lowestMse = Maths.meanSquaredError(pointsFound.get(0), locations, distances);
			int bestPointIndex = 0;
			for(int i = 1; i < pointsFound.size(); i++)
			{
				double mse = Maths.meanSquaredError(pointsFound.get(i), locations, distances);
				if(Math.abs(mse) - 0 < lowestMse)
				{
					lowestMse = mse;
					bestPointIndex = i;
				}
			}
			
			return pointsFound.get(bestPointIndex);
		}

		
		
		return null;
	}

	public Vector2f getLocation() {
		return location;
	}
	
	public void setLocation(Vector2f location)
	{
		this.location = location;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public CopyOnWriteArrayList<DeviceData> getData() {
		return data;
	}

	public Circle getPointer() {
		return pointer;
	}

	public void setPointer(Circle pointer) {
		this.pointer = pointer;
	}

	public Circle getLocator1() {
		return locator1;
	}

	public void setLocator1(Circle locator1) {
		this.locator1 = locator1;
	}

	public Circle getLocator2() {
		return locator2;
	}

	public void setLocator2(Circle locator2) {
		this.locator2 = locator2;
	}

	public Circle getLocator3() {
		return locator3;
	}

	public void setLocator3(Circle locator3) {
		this.locator3 = locator3;
	}
	
	public TextModel getAddressTag()
	{
		return addressTag;
	}
	
	public void setAddressTag(TextModel model)
	{
		this.addressTag = model;
	}
	
	public Vector getLastState()
	{
		return lastState;
	}
	
	public CopyOnWriteArrayList<Float> getFilteredRSSI()
	{
		return filteredRSSI;
	}
	
	public CopyOnWriteArrayList<float[]> getAccelerationData()
	{
		return accelerationData;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Device other = (Device) obj;
		if (macAddress == null) {
			if (other.macAddress != null)
				return false;
		} else if (!macAddress.equals(other.macAddress))
			return false;
		return true;
	}
	
	
	
	
	
	
	
	

}
