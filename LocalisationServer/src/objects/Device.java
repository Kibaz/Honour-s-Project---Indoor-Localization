package objects;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Vector2f;

public class Device {
	
	private String macAddress; // MAC address associated with the device
	private Vector2f location; // Location of the device relative to fixed locations of monitors
	private Map<Long,Float> signalData; // Store time stamps and RSSI values (signal strength)
	
	public Device(String macAddress, Vector2f location)
	{
		this.macAddress = macAddress;
		this.location = location;
		signalData = new HashMap<>();
	}

	public String getMacAddress() {
		return macAddress;
	}

	public Vector2f getLocation() {
		return location;
	}

	public Map<Long, Float> getSignalData() {
		return signalData;
	}
	
	
	
	

}
