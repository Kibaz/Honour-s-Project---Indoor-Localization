package dataHandling;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager {
	
	// Fields
	
	/*
	 * Retain map with a key for each monitor device (referenced by their MAC addresses)
	 * Which identify a device and the data acquired for that device
	 * In the monitoring process
	 */
	private ConcurrentHashMap<String,ConcurrentHashMap<String,List<DeviceData>>> monitorData; 
	
	// Constructor
	public DataManager()
	{
		monitorData = new ConcurrentHashMap<>();
	}
	
	// Getters
	public ConcurrentHashMap<String, ConcurrentHashMap<String, List<DeviceData>>> getMonitorData() {
		return monitorData;
	}
	
	/*
	 * When a new monitoring device is detected
	 * Create an entry in the "monitorData" map
	 * Typically there will be 3 Raspberry PIs operating in monitor mode
	 */
	public void registerMonitor(String address)
	{
		monitorData.put(address, new ConcurrentHashMap<>());
	}
	
	/*
	 * Register new device
	 * Store data associated with the device
	 * Assign to corresponding monitor address
	 */
	public void registerDeviceByMonitorAddress(String monAddress, String deviceAddress, DeviceData data)
	{
		List<DeviceData> list = new ArrayList<>();
		list.add(data);
		monitorData.get(monAddress).put(deviceAddress, list);
	}
	
	// Adding more device data to specific device and monitor address
	public void addDeviceData(String monAddress, String deviceAddress, DeviceData data)
	{
		monitorData.get(monAddress).get(deviceAddress).add(data);
	}
	
	// Verify whether the monitor has already been registered
	public boolean checkMonitorExists(String address)
	{
		if(monitorData.containsKey(address))
		{
			return true;
		}
		
		return false;
	}
	
	// Verify whether the device has been registered with its corresponding monitor address
	public boolean checkDeviceExists(String monAddress, String deviceAddress)
	{
		if(checkMonitorExists(monAddress))
		{
			if(monitorData.get(monAddress).contains(deviceAddress))
			{
				return true;
			}
		}
		
		return false;
	}
	
	
}
