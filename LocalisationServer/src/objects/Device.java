package objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.util.vector.Vector2f;

import dataHandling.DeviceData;

public class Device {
	
	private String macAddress; // MAC address associated with the device
	private Vector2f location; // Location of the device relative to fixed locations of monitors
	
	// Each device will store a list of device data captured for the corresponding device
	private CopyOnWriteArrayList<DeviceData> data;
	
	private Circle pointer; // Pointer for visualising devices location
	
	// Constructor
	public Device(String macAddress)
	{
		this.macAddress = macAddress;
		data = new CopyOnWriteArrayList<>();
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
