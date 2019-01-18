package dataHandling;

import java.util.Date;

public class DeviceData {
	
	private String macAddress;
	private double rssi;
	private Date timeStamp;
	private Double distance;
	
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

	public Double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	
	
	

}
