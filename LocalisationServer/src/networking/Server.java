package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

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
						System.out.println(packet.getLength());
						// Received packet data in string format
						String msg = new String(packet.getData(), 0, packet.getLength());
						System.out.println(msg); // Output received message to console
						
						// Carry processing of packet information
						processPacket();
					}
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}).start();

	}
	
	private void processPacket()
	{
		
	}

}
