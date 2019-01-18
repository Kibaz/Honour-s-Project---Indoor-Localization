package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

public class TCPServer {
	
	private ServerSocket socket;
	private int port;
	
	private boolean listening;
	private Thread serverThread;
	
	public TCPServer(int port)
	{
		this.port = port;
		try {
			socket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		listening = false;
	}
	
	public void start()
	{
		listening = true;
		serverThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while(listening)
				{
					try {
						// Wait for client connections
						Socket clientSocket = socket.accept();
						
						// Get input stream from client socket
						// Configure reader to read the messages sent during the connection
						BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
						String message = ""; // Store current message
						while((message = reader.readLine()) != null) // Read each message in input stream
						{
							processPacket(message);
						}
					} catch (SocketException e)
					{
						System.out.println("Server socket closed. Server stopped.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
			
		});
		
		serverThread.start();
	}
	
	/*
	 * Stop all operation of the server
	 * Close server socket
	 * Stop listening for packets/connections
	 * Wait for server thread to finish
	 */
	public void stop()
	{
		try {
			socket.close(); // Close TCP socket
			listening = false; // Stop listening for connections
			serverThread.join(); // Wait for thread to stop running
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void processPacket(String msg)
	{
		/*
		 * Verify message header
		 */
		if(msg.startsWith("REQUEST INFORMATION"))
		{
			/*
			 * Packet structure denoted in scapy Python scripts
			 * Comma used as delimiter
			 */
			String[] data = msg.split(",");
			// Ignore index 0 - this is the message header
			String device_MAC = data[1]; // Receiver's MAC address
			String monitor_MAC = data[2]; // Transmitter/APs MAC address
			double signal_str = Double.parseDouble(data[3]); // The signal strength received
			/*
			 * Extracting the time stamp from the message
			 * Original method sent from Python code 
			 * Time-stamp as a float/double value
			 * Convert to long value
			 * Must multiply by 1000 as Java handles time
			 * in milliseconds
			 */
			Float tempTimeStamp = new Float(data[4]);
			long timeStamp = tempTimeStamp.longValue();
			
		}
	}

}
