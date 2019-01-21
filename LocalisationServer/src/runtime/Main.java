package runtime;

import org.lwjgl.util.vector.Vector2f;

import graphics.Loader;
import graphics.Render;
import graphics.Window;
import networking.Server;
import networking.TCPServer;
import objects.Circle;
import objects.Monitor;
import objects.Shape;
import shaders.ShapeShader;
import utils.Maths;


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
		
		// Initialise window
		Window.init();
		
		/*
		 * Determining whether a common point of intersection
		 * Can be found between 3 circles
		 * This code will be made redundant as it can only be
		 * used for testing purposes - similar process will be
		 * used in determining the location of a device
		 */
		/*
		Circle c1 = new Circle(new Monitor("2F:B2:67:90:47:56",new Vector2f(0,0)),"2F:B4:65:90:47:58",5,new Vector2f(0,0));
		Circle c2 = new Circle(new Monitor("2F:B2:67:90:47:56",new Vector2f(0,5)),"2F:B4:65:90:47:58",5,new Vector2f(0,5));
		Circle c3 = new Circle(new Monitor("2F:B2:67:90:47:56",new Vector2f(0,2.5f)),"2F:B4:65:90:47:58",5,new Vector2f(0,2.5f));
		
		Maths.findPointOfIntersection(c1, c2, c3);*/
		  
		Circle c1 = new Circle(new Monitor("2F:B2:67:90:47:56",new Vector2f(0,0)),"2F:B4:65:90:47:58",0.5f,new Vector2f(0,0));
		c1.constructCircle(100);
		
		ShapeShader shader = new ShapeShader();
		
		// Graphics loop - refresh whilst window is running
		while(!Window.isClosed())
		{	
			Window.clear();
			Render.prepare(); // Clear window
			shader.start();
			// Use renderer to draw a circle
			// True = Fill circle
			// False = Hollow circle
			Render.drawCircle(c1.getShape(), false);
			shader.stop();
			Window.update();
		}
		
		// After window has been closed
		Window.destroy(); // Terminate window and GLFW
		Loader.clear(); // Clear all loaded data
		tcpServer.stop(); // Stop the TCP server
		// server.stop(); // Stop the UDP server
		
	}

}
