package dataHandling;

import utils.Maths;

public class DeviceData {
	
	/*
	 * Class to store all relevant device data
	 * I.E Signal Strength, Distance, Time Stamps etc...
	 */
	
	// Fields
	private float distanceFromMonitor;
	private long timeStamp;
	private float rssi;
	
	// Constructor
	public DeviceData(long timeStamp, float rssi)
	{
		this.timeStamp = timeStamp;
		this.rssi = rssi;
		// Get distance by calculating it using the RSSI value captured by the monitor system
		this.distanceFromMonitor = Maths.calculateDistanceFromRSSI(this.rssi);
	}

	public float getDistanceFromMonitor() {
		return distanceFromMonitor;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public float getRssi() {
		return rssi;
	}
	
	

}
