package dataHandling;

import java.util.Comparator;

import utils.Maths;

public class DeviceData implements Comparator<DeviceData> {
	
	/*
	 * Class to store all relevant device data
	 * I.E Signal Strength, Distance, Time Stamps etc...
	 */
	
	// Fields
	private float distanceFromMonitor;
	private long timeStamp; // Double value to account for level of precision sent from python script
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

	@Override
	public int compare(DeviceData data1, DeviceData data2) {
		if(data1.getTimeStamp() == data2.getTimeStamp())
		{
			if(data1.getRssi() == data2.getRssi())
			{
				return 0;
			}
		}
		
		if(data1.getTimeStamp() < data2.getTimeStamp())
		{
			return -1;
		}
		
		return 1;
	}
	
	

}
