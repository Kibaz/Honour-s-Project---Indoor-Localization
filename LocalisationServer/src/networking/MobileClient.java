package networking;

import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * This class will be used to represent
 * a connected client instance, containing
 * the necessary information required to pass
 * information back to the communicating client
 * and any other information to uniquely
 * identify the client i.e. ID and MAC Address
 */

public class MobileClient {
	
	// Fields
	
	/*
	 * Specify atomic integer to auto increment 
	 * client ID when a new mobile 
	 * client is registered
	 */
	private static AtomicInteger count = new AtomicInteger(0);
	
	private int id; // Store unique identifier for the client
	private InetAddress ipAddress; // Reference to client's IP address to send messages from this server
	private int port; // Reference to client's port to send messages from this server
	private String macAddress; // Reference to the MAC address of the client's mobile device
	
	// Constructor
	public MobileClient(InetAddress ipAddress,String macAddress, int port)
	{
		id = count.incrementAndGet(); // Auto-increment ID value
		this.ipAddress = ipAddress;
		this.macAddress = macAddress;
		this.port = port;
	}

	// Getters and Setters
	public int getID() {
		return id;
	}

	public InetAddress getIPAddress() {
		return ipAddress;
	}

	public int getPort() {
		return port;
	}

	public String getMacAddress() {
		return macAddress;
	}
	

}
