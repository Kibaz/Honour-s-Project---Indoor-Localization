package objects;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import dataHandling.DeviceData;
import fontHandling.FontStyle;
import fontHandling.Fonts;
import fontHandling.TextModel;
import graphics.Loader;
import graphics.Window;

public class Device {
	
	private String macAddress; // MAC address associated with the device
	private Vector2f location; // Location of the device relative to fixed locations of monitors
	
	// Each device will store a list of device data captured for the corresponding device
	private CopyOnWriteArrayList<DeviceData> data;
	
	private Circle pointer; // Pointer for visualising devices location
	
	// Hold reference to locators
	private Circle locator1; 
	private Circle locator2;
	private Circle locator3;
	
	private TextModel addressTag;
	
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
