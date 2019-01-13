package runtime;

import java.net.InetAddress;
import java.net.UnknownHostException;

import networking.Server;
import networking.TCPServer;


public class Main {

	public static void main(String[] args) {
		Server server = new Server(8128);
		server.start();
		//server.processData();
	}

}
