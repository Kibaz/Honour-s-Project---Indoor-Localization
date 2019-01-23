package objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Vector2f;

public class Device {
	
	private String macAddress; // MAC address associated with the device
	private Vector2f location; // Location of the device relative to fixed locations of monitors
	private float signalStrength; // Current signal strength
	private float timeStamp; // Current time stamp
	
	private List<Float> signalData; // Retain a list of signal strengths detected
	
	public Device(String macAddress, float signalStrength, float timeStamp)
	{
		this.macAddress = macAddress;
		this.signalStrength = signalStrength;
		this.timeStamp = timeStamp;
		signalData  = new ArrayList<>();
	}
	
	

	public float getSignalStrength() {
		return signalStrength;
	}



	public void setSignalStrength(float signalStrength) {
		this.signalStrength = signalStrength;
	}



	public float getTimeStamp() {
		return timeStamp;
	}



	public void setTimeStamp(float timeStamp) {
		this.timeStamp = timeStamp;
	}



	public String getMacAddress() {
		return macAddress;
	}
	
	public List<Float> getSignalData()
	{
		return signalData;
	}

	public Vector2f getLocation() {
		return location;
	}
	
	public void setLocation(Vector2f location)
	{
		this.location = location;
	}
	
	
	
	

}
