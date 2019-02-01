package runtime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import dataHandling.DataManager;
import dataHandling.DeviceData;
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
	private static float opacity = 0;

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
		
		List<Device> devicePointers = new ArrayList<>();
		List<Circle> locators = new ArrayList<>();
		
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
			
			handleData(dataManager,baseCircle,devicePointers,locators);
			for(Circle circle: monitors)
			{
				Render.drawCircle(circle);
			}
			
			for(Device device: devicePointers)
			{
				Render.drawCircle(device.getPointer());
			}
			
			for(Circle cirlce: locators)
			{
				Render.drawCircle(cirlce);
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
		opacity = (float) Math.abs(Math.sin(opacityTimer));
	}
	
	private static void handleData(DataManager dataManager, Shape baseCircle, List<Device> devicePointers, List<Circle> locators)
	{
		// Loop through every device registered with the first monitor
		for(Device device: dataManager.getFirstMonitor().getDevices())
		{
			// Check if the other monitors have detected the same device
			Device checkDevice1 = dataManager.getSecondMonitor().getDeviceIfExists(device.getMacAddress());
			Device checkDevice2 = dataManager.getThirdMonitor().getDeviceIfExists(device.getMacAddress());
			
			// If the same device has been detected by the other monitors
			if(checkDevice1 != null && checkDevice2 != null)
			{
				if(device.getMacAddress().equals("b0:47:bf:92:74:97"))
				{
					// Get iterator for each device data list in each device instance
					Iterator<DeviceData> iterator1 = device.getData().iterator();
					Iterator<DeviceData> iterator2 = checkDevice1.getData().iterator();
					Iterator<DeviceData> iterator3 = checkDevice2.getData().iterator();
					
					List<Circle> monitor1Locators = new ArrayList<>();
					List<Circle> monitor2Locators = new ArrayList<>();
					List<Circle> monitor3Locators = new ArrayList<>();
					
					while(iterator1.hasNext())
					{
						DeviceData data = iterator1.next();
						Circle locator = new Circle(dataManager.getFirstMonitor(),device.getMacAddress(),
								data.getDistanceFromMonitor(),dataManager.getFirstMonitor().getLocation(),baseCircle,false);
						locator.setOpacity(0.4f);
						monitor1Locators.add(locator);
					}
					
					while(iterator2.hasNext())
					{
						DeviceData data = iterator2.next();
						Circle locator = new Circle(dataManager.getSecondMonitor(),device.getMacAddress(),
								data.getDistanceFromMonitor(),dataManager.getSecondMonitor().getLocation(),baseCircle,false);
						locator.setOpacity(0.4f);
						monitor2Locators.add(locator);
					}
					
					while(iterator3.hasNext())
					{
						DeviceData data = iterator3.next();
						Circle locator = new Circle(dataManager.getThirdMonitor(),device.getMacAddress(),
								data.getDistanceFromMonitor(),dataManager.getThirdMonitor().getLocation(),baseCircle,false);
						locator.setOpacity(0.4f);
						monitor3Locators.add(locator);
					}
					
					locators.addAll(monitor1Locators);
					locators.addAll(monitor2Locators);
					locators.addAll(monitor3Locators);
					
					/*
					 * Check for intersections between
					 * the locators generated for each
					 * monitor. Render the locators
					 */
					for(Circle c1: monitor1Locators)
					{	
						for(Circle c2: monitor2Locators)
						{
							for(Circle c3: monitor3Locators)
							{
								Vector2f pointOfIntersection = Maths.findPointOfIntersection(c1, c2, c3);
								if(pointOfIntersection != null)
								{
									System.out.println(pointOfIntersection);
									Circle deviceCircle = new Circle(dataManager.getFirstMonitor(),device.getMacAddress(),
											0.05f,pointOfIntersection,baseCircle,true);
									deviceCircle.setColour(0, 1, 0); // Set discovered device colour to green
									device.setPointer(deviceCircle); // Update devices pointer
									// If the device has not already been stored
									if(!devicePointers.contains(device))
									{
										devicePointers.add(device);
									}
									
								}
							}
						}
					}
				}

			}

		}
	}

}
