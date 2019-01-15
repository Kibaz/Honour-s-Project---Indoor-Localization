package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	
	private ServerSocket socket;
	private int port;
	
	public TCPServer(int port)
	{
		this.port = port;
		try {
			socket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start()
	{
		new Thread(new Runnable() {

			@Override
			public void run() {
				while(true)
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
							System.out.println(message);
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
			
		}).start();;
	}

}
