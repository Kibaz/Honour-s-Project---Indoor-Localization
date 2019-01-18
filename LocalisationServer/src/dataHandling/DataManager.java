package dataHandling;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import objects.Device;
import objects.Monitor;

public class DataManager {	
	
	/*
	 * Determine the system setup
	 * Hold data retrieved by each monitor
	 * Hold references to the monitors and devices found
	 */
	
	// Fields
	private Monitor firstMonitor;
	private Monitor secondMonitor;
	private Monitor thirdMonitor;
	
	public DataManager()
	{
		// Initialise monitors with known MAC Addresses and Fixed Locations
		firstMonitor = new Monitor("b8:27:eb:35:2b:60", new Vector2f(0,0));
		firstMonitor = new Monitor("b8:27:eb:f7:b8:27", new Vector2f(5,5));
		firstMonitor = new Monitor("b8:27:eb:01:d1:9d", new Vector2f(10,10));
	}
	
	// Retrieve the monitor by associated MAC Address
	public Monitor getMonitorByAddress(String address)
	{
		if(firstMonitor.getMacAddress().equals(address))
		{
			return firstMonitor;
		}
		else if(secondMonitor.getMacAddress().equals(address))
		{
			return secondMonitor;
		}
		
		return thirdMonitor;
	}

	public Monitor getFirstMonitor() {
		return firstMonitor;
	}

	public Monitor getSecondMonitor() {
		return secondMonitor;
	}

	public Monitor getThirdMonitor() {
		return thirdMonitor;
	}
	
	
	
}
