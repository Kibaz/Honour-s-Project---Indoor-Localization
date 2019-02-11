package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

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
	
	// Fields
	private DatagramSocket socket;
	private int port;
	
	private Thread listenThread;
	
	// Constructor
	public AppServer(int port)
	{
		this.port = port;
	}
	
	public void listen()
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
				
				
			}
			
		});
		
		listenThread.start();
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
	

}
