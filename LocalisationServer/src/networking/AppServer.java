package networking;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.lwjgl.util.vector.Vector2f;

import dataHandling.DataManager;
import objects.Device;
import objects.Monitor;
import utils.CSVHandler;

/*
 * This class will handle all communications
 * made between the localisation server and the client
 * applications operating on individual mobile devices
 * 
 * This will all be handled separately from the communications
 * between the raspberry PIs and the localisation server
 * 
 * Using UDP protocol for quick continuous updates
 */

public class AppServer {
	
	// Constants
	private static final int BUFFER_LENGTH = 512;
	private static final int SAMPLE_LIMIT = 500; // Limit the number of samples written for data analysis
	
	// Fields
	private DatagramSocket socket;
	private int port;
	
	private Thread listenThread;
	
	private int numAccelSamples = 0; // Counter for the number of acceleration samples taken
	
	private DataManager dataManager;
	
	// Constructor
	public AppServer(int port, DataManager dataManager)
	{
		this.port = port;
		this.dataManager = dataManager;
	}
	
	public void start()
	{
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(socket == null)
		{
			return;
		}
		
		listenThread = new Thread(new Runnable()
		{

			@Override
			public void run() {
					try {
						while(!socket.isClosed())
						{
							byte[] buffer = new byte[BUFFER_LENGTH];
							DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
							socket.receive(packet);
							
							String message = new String(packet.getData(), 0, packet.getLength());
							
							String macAddress = "";
							if(message.contains("Connecting"))
							{
								macAddress = message.split(" ")[1];
								System.out.println(macAddress);
							}
							
							// Attempt to register client
							MobileClient client = MobileClientRegister.registerClient(packet.getAddress(), macAddress.toLowerCase(), packet.getPort());
							
							System.out.println(message);
							
							if(client != null)
							{
								String msg = "ID: " + client.getID();
								send(msg.getBytes(),client.getIPAddress(),client.getPort());
							}
							
							if(message.startsWith("Acceleration"))
							{	
								// Only needs to run once for data processing - Acquire 500 samples
								if(numAccelSamples < SAMPLE_LIMIT)
								{
									acquireAccelerationDataSamples(message);
								}
								else
								{
									// Process the acceleration data received from the mobile device
									processAccelerationData(message);
								}
							}
						}
					} catch (SocketException e)
					{
						System.out.println("App Server socket closed. Services stopped.");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			
		});
		
		listenThread.start();
	}
	
	// Process acceleration data
	private float[] processAccelerationData(String message)
	{
		String messageData = message.split(";")[1]; // Separate data from header
		String[] values = messageData.split(","); // Separate each acceleration value
		
		// Indices 0,2 & 4 of split message data contain the headers
		// Indices 1,3 & 5 of split message data contain the acceleration values
		float accelX = Float.parseFloat(values[0]); // X
		float accelY = Float.parseFloat(values[1]); // Y
		float accelZ = Float.parseFloat(values[2]); // Z - although not necessary (measuring position in 2D space)
		
		float[] accelData = new float[] {accelX,accelY,accelZ};
		String macAddress = values[values.length-1]; // Last value stores mac address in String format
		// MAC Address from android stored in upper case whereas python sniffer is in lower case
		Device device = dataManager.getFirstMonitor().getDeviceIfExists(macAddress.toLowerCase());
		// Only register the device's acceleration data if it has been detected by the localisation system
		if(device != null)
		{
			device.getAccelerationData().add(accelData);
		}
		
		
		return accelData;
	}
	
	private void acquireAccelerationDataSamples(String message)
	{	
		// Write the data to a CSV for sampling and noise calibration
		// Create a CSV with specified file name if one does not exist
		CSVHandler.createCSV("accel_data", new String[] {"accel_x", "accel_y", "accel_z"});
		
		// Check if the CSV already contains 500 samples
		
		numAccelSamples = CSVHandler.rowCount("accel_data");
		if(numAccelSamples >= SAMPLE_LIMIT)
		{
			return;
		}
		
		float[] accelData = processAccelerationData(message);
		
		// Write the processed data to the newly created CSV
		CSVHandler.appendRow(new File("files/accel_data.csv"), new Object[] {accelData[0],accelData[1],accelData[2]});
	}
	
	
	/*
	 * Send data to the specified client address and port
	 * Data is handled in byte array format
	 * Client address and port will be retrieved from incoming packets
	 */
	public void send(byte[] data, InetAddress clientAddress, int clientPort)
	{
		try {
			DatagramPacket packet = new DatagramPacket(data,data.length,clientAddress,clientPort);
			socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stop()
	{
		try {
			socket.close(); // Close TCP socket
			listenThread.join(); // Wait for thread to stop running
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * This method will be used to update mobile clients
	 * with data such as location data processed by the
	 * localisation system
	 */
	public void updateClients(DataManager dataManager)
	{
		// Retrieve one of the three monitors from localisation system
		Monitor monitor = dataManager.getFirstMonitor(); 
		for(Device device: monitor.getDevices())
		{
			Vector2f location = device.getLocation();
			MobileClient client = MobileClientRegister.getClientByMACAddress(device.getMacAddress());
			if(location != null && client != null)
			{
				System.out.println(MobileClientRegister.getRegisteredClients().size());
				for(int key: MobileClientRegister.getRegisteredClients().keySet())
				{
					String msgToSend = "New location " + location.x + "," + location.y + "," + client.getMacAddress();
					MobileClient clientToReceive = MobileClientRegister.getClientByID(key);
					send(msgToSend.getBytes(),clientToReceive.getIPAddress(),clientToReceive.getPort());
				}

			}
		}
		
	}
	

}
