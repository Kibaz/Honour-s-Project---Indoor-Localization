package dataHandling;

import java.util.Date;

public class DeviceData {
	
	private String macAddress;
	private double rssi;
	private Date timeStamp;
	
	public DeviceData(String mac, double rssi, Date timeStamp)
	{
		this.macAddress = mac;
		this.rssi = rssi;
		this.timeStamp = timeStamp;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public double getSignalStrength() {
		return rssi;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}
	
	

}
