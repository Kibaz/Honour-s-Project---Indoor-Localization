package objects;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

public class Monitor {
	
	// Fields
	private String macAddress; // Store MAC address of the monitor - Raspberry PI operating in monitor mode
	private Vector2f location; // X and Y coordinates of the monitor
	
	// Store device data detected by this monitor
	private List<Device> devices;
	
	public Monitor(String macAddress, Vector2f location)
	{
		this.macAddress = macAddress;
		this.location = location;
		devices = new ArrayList<>();
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public Vector2f getLocation() {
		return location;
	}

	public void setLocation(Vector2f location) {
		this.location = location;
	}

	public List<Device> getDevices() {
		return devices;
	}
	
	
	
	

}
