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
						Socket client = socket.accept();
						
						System.out.println(client.getInetAddress());
						BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
						String line = null;
						while((line = br.readLine())!= null) 
						{
							System.out.println(line);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
			
		}).start();;
	}

}
