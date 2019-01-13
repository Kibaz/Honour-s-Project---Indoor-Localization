package dataHandling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
	
	private static Map<String,List<DeviceData>> deviceDataHandler = new HashMap<>();
	
	public static void createNewEntry(String macAddress)
	{
		List<DeviceData> dataList = new ArrayList<>();
		deviceDataHandler.put(macAddress,dataList);
	}
	
	public static void addDataByAddress(String macAddress, DeviceData data)
	{
		List<DeviceData> dataList = deviceDataHandler.get(macAddress);
		dataList.add(data);
	}
	
	public static List<DeviceData> getDataByAddress(String macAddress)
	{
		return deviceDataHandler.get(macAddress);
	}
	
	public static boolean checkDeviceExists(String macAddress)
	{
		if(deviceDataHandler.containsKey(macAddress))
		{
			return true;
		}
		
		return false;
	}

}
