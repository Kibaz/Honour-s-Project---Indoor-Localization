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

import dataHandling.DeviceData;
import filtering.KalmanFilter;
import filtering.Matrix;
import filtering.Vector;
import fontHandling.FontStyle;
import fontHandling.Fonts;
import fontHandling.TextModel;
import graphics.Loader;
import graphics.Window;
import utils.CSVWriter;

public class Device {
	
	private String macAddress; // MAC address associated with the device
	private Vector2f location; // Location of the device relative to fixed locations of monitors
	
	// Each device will store a list of device data captured for the corresponding device
	private CopyOnWriteArrayList<DeviceData> data;
	private CopyOnWriteArrayList<Float> filteredRSSI;
	
	private Circle pointer; // Pointer for visualising devices location
	
	// Hold reference to locators
	private Circle locator1; 
	private Circle locator2;
	private Circle locator3;
	
	private TextModel addressTag;
	
	private Vector lastState;
	
	private KalmanFilter filter;
	
	// Constructor
	public Device(String macAddress)
	{
		this.macAddress = macAddress;
		lastState = new Vector(new double[] {0,0});
		data = new CopyOnWriteArrayList<>();
		filteredRSSI = new CopyOnWriteArrayList<>();
		initFilter();
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
		R.set(0, 0,  Math.pow(3.480229877,2)); // R configured to covariance over 500 samples
		
		
		filter = new KalmanFilter(initState,null,A,P,Q,B,H,R);
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
			System.out.println(lastState);
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
