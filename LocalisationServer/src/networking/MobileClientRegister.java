package networking;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * This class will act as a register
 * to store client mobile devices which
 * communicate with the server to receive
 * updates. The register will provide
 * functionality to register newly
 * connected clients and remove any
 * clients which become redundant or
 * disconnect from the server.
 */

public class MobileClientRegister {
	
	/*
	 * Use a map to store reference to a client
	 * - Client may be referenced by unique ID or by MAC address
	 * - MAC addresses of the communicating device can only be
	 * acquired when using a physical device - the android emulator
	 * does not support layer 2 properties.
	 */
	private static ConcurrentHashMap<Integer,MobileClient> registeredClients = new ConcurrentHashMap<>();
	
	public static Map<Integer,MobileClient> getRegisteredClients()
	{
		return registeredClients;
	}
	
	
	// Method to register a new mobile client using the specified information
	// Do not register the client if it has already been registered
	public static MobileClient registerClient(InetAddress ipAddress, String macAddress, int port)
	{
		// Create new mobile client instance
		MobileClient client = new MobileClient(ipAddress,macAddress,port);
		
		for(int id: registeredClients.keySet())
		{
			MobileClient curr = registeredClients.get(id);
			// If the client has already been registered
			if(curr.getIPAddress().equals(client.getIPAddress()) &&
					curr.getPort() == client.getPort())
			{
				// Do not continue
				return null;
			}
		}
		
		// Insert new client into the register map
		registeredClients.put(client.getID(), client);
		return client;
	}
	
	// Method to retrieve a mobile client by its associated unique identifier
	public static MobileClient getClientByID(int id)
	{
		return registeredClients.get(id);
	}
	
	// Method to retrieve a mobile client by its associatd MAC address
	public static MobileClient getClientByMACAddress(String macAddress)
	{
		for(int id: registeredClients.keySet())
		{
			MobileClient curr = registeredClients.get(id);
			// When a client is found with the specified MAC address
			if(curr.getMacAddress().equals(macAddress))
			{
				// Return the found client
				return curr;
			}
		}
		// Default to null if no client is found with the specified MAC address
		return null;
	}
	
	// Method to remove a client instance by its unique identifier
	// Do not attempt to remove a client if the unique identifier does not exist
	public static boolean removeClientByID(int id)
	{
		if(registeredClients.containsKey(id))
		{
			registeredClients.remove(id);
			return true;
		}
		
		return false;
		
	}
	
	// Method to remove a client instance by its associated MAC Address
	// Do not attempt to remove client if a client is not found
	public static boolean removeClientByMACAddress(String macAddress)
	{
		int clientToRemove = 0;
		for(int id: registeredClients.keySet())
		{
			MobileClient client = registeredClients.get(id);
			if(client.getMacAddress().equals(macAddress))
			{
				
			}
		}
		
		if(clientToRemove != 0)
		{
			registeredClients.remove(clientToRemove);
			return true;
		}
		
		return false;
	}

}
