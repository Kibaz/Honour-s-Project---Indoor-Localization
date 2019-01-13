package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dataHandling.DataManager;
import dataHandling.DeviceData;

public class Server {
	
	// Fields
	private int port; // Port which the server will listen for UDP packets on
	private boolean listening; // Determine if server should listen for requests
	
	private byte[] buffer; // Buffer used for collecting packet data
	
	// Constructor
	public Server(int port)
	{
		this.port = port;
		listening = false;
		buffer = new byte[512];
	}
	
	/*
	 * Start listening to clients
	 * Clients are the raspberry pis operating in monitor mode
	 * This server will listen for packets sent from the pis
	 * Packets will contain information on WiFi enabled devices in the area
	 * This information will consist of data extracted from "probe requests"
	 * Information can be associated with the corresponding MAC addresses
	 */
	public void start()
	{
		listening = true; // Begin listening for packets
		new Thread(new Runnable(){

			@Override
			public void run() {
				try(DatagramSocket socket = new DatagramSocket(port))
				{
					// While the server is listening for packets on UDP socket
					while(listening)
					{
						DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);
						socket.receive(packet); // Receive packets - block until a packet is received
						// Received packet data in string format
						String msg = new String(packet.getData(), 0, packet.getLength());
						
						// Carry processing of packet information
						processPacket(msg);
					}
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}).start();

	}
	
	private void processPacket(String msg)
	{
		/*
		 * Verify message header
		 */
		if(msg.startsWith("REQUEST INFORMATION"))
		{
			/*
			 * Packet structure denoted in scapy python scripts
			 * Comma used as delimiter
			 */
			String[] data = msg.split(",");
			// Ignore index 0 - this is the message header
			String device_MAC = data[1]; // Device's MAC address
			double signal_str = Double.parseDouble(data[2]); // The signal strength received
			/*
			 * Extracting the time stamp from the message
			 * Original method sent from python code 
			 * Timestamp as a float/double value
			 * Convert to long value
			 * Must multiply by 1000 as Java handles time
			 * in milliseconds
			 * Convert long value to Date object
			 */
			Float tempTimeStamp = new Float(data[3]);
			long timeStamp = tempTimeStamp.longValue();
			Date dateTime = new Date(timeStamp*1000); // Java handles time in milliseconds
			DeviceData deviceData = new DeviceData(device_MAC, signal_str, dateTime);
			/*
			 * Create new entry in the data manager
			 * if the MAC address does not already
			 * exist in the device data handler
			 */
			if(!DataManager.checkDeviceExists(device_MAC))
			{
				DataManager.createNewEntry(device_MAC);
			}
			DataManager.addDataByAddress(device_MAC, deviceData);
		}
	}

}
