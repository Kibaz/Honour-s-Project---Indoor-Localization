package runtime;

import java.net.InetAddress;
import java.net.UnknownHostException;

import networking.Server;
import networking.TCPServer;


public class Main {

	public static void main(String[] args) {
		/*
		 * UDP server was causing problems with packets not arriving
		 * On home network
		 * 
		 * TCP also experiences issues with timing out however this could be resolved
		 * With a better wireless connection - all packets up until this point are received
		 * 
		 * Depending on location and connection strength, may need to operate
		 * Between both protocols - coded a server for each
		 */
		// UDP Server
		//Server server = new Server(8128);
		//server.start();
		//server.processData();
		
		// TCP Server
		TCPServer tcpServer = new TCPServer(8128);
		tcpServer.start();
	}

}
