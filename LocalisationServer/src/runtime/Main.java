package runtime;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import dataHandling.DataManager;
import graphics.Loader;
import graphics.Render;
import graphics.Window;
import networking.Server;
import networking.TCPServer;
import objects.Circle;
import objects.Device;
import objects.Monitor;
import objects.Shape;
import shaders.ShapeShader;
import utils.Maths;


public class Main {
	
	private static float opacityTimer = 0; 

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
		
		// Initialise Data Manager for handling incoming data
		DataManager dataManager = new DataManager();
				
		// UDP Server
		//Server server = new Server(8128);
		//server.start();
		//server.processData();
		
		// TCP Server
		TCPServer tcpServer = new TCPServer(8128);
		tcpServer.start(dataManager);
		
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
		
		Shape baseCircle = Circle.constructCircle(0.05f, 100);
		
		List<Circle> monitors = new ArrayList<>();
		/*
		 * Initialise a circle to be drawn for each monitor
		 * Use monitor location as centre of the circle
		 */
		Circle monitor1 = new Circle(dataManager.getFirstMonitor(),"",0.05f,dataManager.getFirstMonitor().getLocation(),baseCircle,true);
		monitor1.setColour(1, 0, 0);
		Circle monitor2 = new Circle(dataManager.getSecondMonitor(),"",0.05f,dataManager.getSecondMonitor().getLocation(),baseCircle,true); 
		monitor2.setColour(1, 0, 0);
		Circle monitor3 = new Circle(dataManager.getThirdMonitor(),"",0.05f,dataManager.getThirdMonitor().getLocation(),baseCircle,true);
		monitor3.setColour(1, 0, 0);
		
		monitors.add(monitor1);
		monitors.add(monitor2);
		monitors.add(monitor3);
		
		// Graphics loop - refresh whilst window is running
		while(!Window.isClosed())
		{	
			Window.clear();
			Render.prepare(); // Clear window
			// Use renderer to draw a circle
			// True = Fill circle
			// False = Hollow circle
			alterOpacityTimer();
			
			handleData(dataManager,baseCircle);
			for(Circle circle: monitors)
			{
				Render.drawCircle(circle);
			}
			
			Window.update();
		}
		
		// After window has been closed
		Window.destroy(); // Terminate window and GLFW
		Loader.clear(); // Clear all loaded data
		tcpServer.stop(); // Stop the TCP server
		// server.stop(); // Stop the UDP server
		
	}
	
	private static void alterOpacityTimer()
	{
		opacityTimer += Window.getDeltaTime();
		
		if(opacityTimer > 1.0f)
		{
			opacityTimer %= 1.0f;
		}
	}
	
	private static void handleData(DataManager dataManager, Shape baseCircle)
	{
		for(Device device: dataManager.getFirstMonitor().getDevices())
		{
			// Check if the other monitors have detected the same device
			Device checkDevice1 = dataManager.getSecondMonitor().getDeviceIfExists(device.getMacAddress());
			Device checkDevice2 = dataManager.getSecondMonitor().getDeviceIfExists(device.getMacAddress());
			if(checkDevice1 != null && checkDevice2 != null)
			{
				for(int i = 0; i < device.getSignalData().size(); i++)
				{
					float first = device.getSignalData().get(i);
					for(int j = 0; j < checkDevice1.getSignalData().size(); j++)
					{
						float second = checkDevice1.getSignalData().get(j);
						for(int k = 0; k < checkDevice2.getSignalData().size(); k++)
						{
							if(device.getMacAddress().equals("b0:47:bf:92:74:97"))
							{
								float third = checkDevice2.getSignalData().get(k);
								float distMonitor1 = (float) Math.floor(Maths.calculateDistanceFromRSSI(first));
								float distMonitor2 = (float) Math.floor(Maths.calculateDistanceFromRSSI(second));
								float distMonitor3 = (float) Math.floor(Maths.calculateDistanceFromRSSI(third));
								
								Circle locator1 = new Circle(dataManager.getFirstMonitor(),device.getMacAddress(),distMonitor1,
										dataManager.getFirstMonitor().getLocation(),baseCircle,false);
								Circle locator2 = new Circle(dataManager.getSecondMonitor(),device.getMacAddress(),distMonitor2,
										dataManager.getSecondMonitor().getLocation(),baseCircle,false);
								Circle locator3 = new Circle(dataManager.getThirdMonitor(),device.getMacAddress(),distMonitor3,
										dataManager.getThirdMonitor().getLocation(),baseCircle,false);
								Render.drawCircle(locator1);
								Render.drawCircle(locator2);
								Render.drawCircle(locator3);
								
								Vector2f pointOfIntersection = Maths.findPointOfIntersection(locator1, locator2, locator3);
								// If a point of intersection is found across all 3 locators
								if(pointOfIntersection != null)
								{
									// Draw a device
									Circle locatedDevice = new Circle(dataManager.getFirstMonitor(),device.getMacAddress(),0.05f,
											pointOfIntersection,baseCircle,true);
									locatedDevice.setColour(0, 1, 0); // Set to green
									locatedDevice.setOpacity(opacityTimer);
									Render.drawCircle(locatedDevice);
								}
							}

						}
					}
				}

				
			}
		}
	}

}
