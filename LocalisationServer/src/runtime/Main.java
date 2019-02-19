package runtime;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import animation.Animator;
import dataHandling.DataManager;
import dataHandling.DeviceData;
import filtering.KalmanFilter;
import filtering.Matrix;
import filtering.Vector;
import fontHandling.FontStyle;
import fontHandling.Fonts;
import fontHandling.TextHandler;
import fontHandling.TextModel;
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
		
		// Initialise text handler for rendering text
		TextHandler.init();
		TextModel addressTag = new TextModel("bc:45:cd:77:6c:98",0.75f,Fonts.arial,new Vector2f(0,0),0.2f,true);
		TextHandler.removeText(addressTag); // Remove text from renderer
		
		/*
		 * Testing font/text rendering
		 * Use test text as a test text model
		 * Configure a font style using a font file
		 */
		//FontStyle font = new FontStyle(Loader.loadFontTexture("files/arial.png", 0),new File("files/arial.fnt"));
		//TextModel testText = new TextModel("test",1,font,new Vector2f(0,0),1,false);
		//testText.setColour(1, 1, 1);
		
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
		
		/*
		 * Testing using the conversion method from regular device and monitor positional data
		 * to the screen space positional data used by the font rendering system
		 * 
		 * Vector3f tagPos = Maths.covertCoordinates(new Vector3f(dataManager.getFirstMonitor().getLocation().x,
				dataManager.getFirstMonitor().getLocation().y,0));
		addressTag.setPosition(new Vector2f(tagPos.x,tagPos.y));*/
		
		monitors.add(monitor1);
		monitors.add(monitor2);
		monitors.add(monitor3);
		
		/*
		 * Testing Matrix class and internal functionality
		 */
		//Matrix matrix = new Matrix(2,2);
		//matrix.setIdentity();
		//System.out.println(matrix);
		
		/*
		 * Testing matrix multiplication
		Matrix matrix = new Matrix(2,3);
		matrix.set(0, 0, 1);
		matrix.set(0, 1, 2);
		matrix.set(0, 2, 3);
		matrix.set(1, 0, 4);
		matrix.set(1, 1, 5);
		matrix.set(1, 2, 6);
		
		Matrix matrix1 = new Matrix(3,2);
		matrix1.set(0, 0, 7);
		matrix1.set(0, 1, 8);
		matrix1.set(1, 0, 9);
		matrix1.set(1, 1, 10);
		matrix1.set(2, 0, 11);
		matrix1.set(2, 1, 12);
		
		Matrix result = matrix.multiply(matrix1);
		System.out.println(result);
		*/
		
		/*
		 * Testing matrix transposition
		Matrix matrix = new Matrix(3,3);
		matrix.set(0, 0, 1);
		matrix.set(0, 1, 2);
		matrix.set(0, 2, 3);
		matrix.set(1, 0, 4);
		matrix.set(1, 1, 5);
		matrix.set(1, 2, 6);
		matrix.set(2, 0, 7);
		matrix.set(2, 1, 8);
		matrix.set(2, 2, 9);
		System.out.println(matrix);
		Matrix other = matrix.transpose();
		System.out.println(other);
		*/
		
		/*
		 * Testing matrix - vector transformation
		 */
		/*
		Vector vector = new Vector(3);
		vector.set(0, 2); vector.set(1, 1); vector.set(2, 0);
		
		Matrix matrix = new Matrix(2,3);
		matrix.set(0, 0, 1);
		matrix.set(0, 1, -1);
		matrix.set(0, 2, 2);
		matrix.set(1, 0, 0);
		matrix.set(1, 1, -3);
		matrix.set(1, 2, 1);
		
		Vector test = vector.transform(matrix);
		System.out.println(test);*/
		
		/*
		 * Testing matrix inverse calculation
		Matrix matrix = new Matrix(3,3);
		matrix.setIdentity();
		System.out.println(matrix);
		Matrix inverse = matrix.inverse();
		*/
		
		// Initialise state variables and matrices required to instantiate Kalman Filter
		Vector initState = new Vector(new float[] {0,0,0}); // Store 3 distance values as state variables - correspond to each monitor
		
		Matrix A = new Matrix(initState.getDimension(),initState.getDimension()); // Transformation matrix between states
		A.setIdentity(); // Configure as an identity matrix as states will not be transitioned with RSSI values
		
		Matrix P = new Matrix(initState.getDimension(),initState.getDimension());
		P.setIdentity(); // State covariance matrix - can be altered during filtering process - init as identity
		
		Matrix Q = new Matrix(initState.getDimension(), initState.getDimension());
		Q.setIdentity(); // Noise associated with the measurements - can be calculated after measurements have been taken
		
		Matrix B = new Matrix(initState.getDimension(), initState.getDimension());
		B.setIdentity(); // Control input effect matrix - redundant in RSSI filtering
		
		/*
		 * The H Matrix is the measuring matrix
		 * This tells the filter what is being measured
		 * and how it relates to the state vector
		 * 
		 * Number of rows are equal to the number of variable types
		 * i.e. distance, velocity etc...
		 * 
		 * Number of columns correspond to the number 
		 * of values stored in the state vector
		 */
		Matrix H = new Matrix(1,initState.getDimension());
		H.set(0, 0, 1);
		// Measurement noise covariance matrix - store measurement uncertainty
		// This can be re-configured based on the accuracy of the localisation system
		Matrix R = new Matrix(1,1);
		R.setIdentity();
		
		
		// Initialise Kalman Filter to filter raw RSSI data
		// Z (measurement) set to null until data is acquired from monitoring system
		KalmanFilter filter = new KalmanFilter(initState,null,A,P,Q,B,H,R);
		
		
		// Graphics loop - refresh whilst window is running
		while(!Window.isClosed())
		{	
			Window.clear();
			Render.prepare(); // Clear window
			// Use renderer to draw a circle
			// True = Fill circle
			// False = Hollow circle
			alterOpacityTimer();
			
			filterData(dataManager,filter);
			//handleData(dataManager,baseCircle,devicePointers,addressTag);
			
			// Carry out rendering
			for(Device device: devicePointers)
			{
				Animator.update(device);
				Animator.animateLocator(device.getLocator1());
				Animator.animateLocator(device.getLocator2());
				Animator.animateLocator(device.getLocator3());
				Render.drawCircle(device.getPointer());
				Render.drawCircle(device.getLocator1());
				Render.drawCircle(device.getLocator2());
				Render.drawCircle(device.getLocator3());
			}
			
			for(Circle circle: monitors)
			{
				Render.drawCircle(circle);
			}
			TextHandler.render();
			Window.update();
		}
		
		// After window has been closed
		Window.destroy(); // Terminate window and GLFW
		Loader.clear(); // Clear all loaded data
		TextHandler.clear();
		tcpServer.stop(); // Stop the TCP server
		// server.stop(); // Stop the UDP server
		
	}
	
	private static void alterOpacityTimer()
	{
		opacityTimer += Window.getDeltaTime();
		opacity = (float) Math.abs(Math.sin(opacityTimer));
	}
	
	private static void filterData(DataManager dataManager,KalmanFilter filter)
	{
		for(Device device: dataManager.getFirstMonitor().getDevices())
		{
			// Check if the other monitors have detected the same device
			Device checkDevice1 = dataManager.getSecondMonitor().getDeviceIfExists(device.getMacAddress());
			Device checkDevice2 = dataManager.getThirdMonitor().getDeviceIfExists(device.getMacAddress());
			
			if(checkDevice1 != null && checkDevice2 != null)
			{
				if(device.getData().size() > 20 && checkDevice1.getData().size() > 20 && checkDevice2.getData().size() > 20)
				{
					for(int i = 0; i < 20; i++)
					{
						// No requirement of control input vector - set to 0,0,0
						filter.predict(new Vector(new float[] {0,0,0}));
						
						// Acquire and set measurements
						float dist1 = device.getData().get(i).getDistanceFromMonitor();
						float dist2 = checkDevice1.getData().get(i).getDistanceFromMonitor();
						float dist3 = checkDevice2.getData().get(i).getDistanceFromMonitor();
						Vector Z = new Vector(new float[] {dist1});
						filter.setMeasurement(Z);
						
						// Update state values in accordance to measurements and uncertainty
						filter.update();
						
						// Output state values upon filter completion
						//System.out.println(filter.getState());
					}
				}
			}
		}
	}
	
	private static void handleData(DataManager dataManager, Shape baseCircle, List<Device> devicePointers, TextModel addressTag)
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
				// Get iterator for each device data list in each device instance
				Iterator<DeviceData> iterator1 = device.getData().iterator();
				Iterator<DeviceData> iterator2 = checkDevice1.getData().iterator();
				Iterator<DeviceData> iterator3 = checkDevice2.getData().iterator();
				
				List<Circle> monitor1Locators = new ArrayList<>();
				List<Circle> monitor2Locators = new ArrayList<>();
				List<Circle> monitor3Locators = new ArrayList<>();
				
				List<DeviceData> dataToBeRemoved1 = new ArrayList<>();
				List<DeviceData> dataToBeRemoved2 = new ArrayList<>();
				List<DeviceData> dataToBeRemoved3 = new ArrayList<>();
				
				while(iterator1.hasNext())
				{
					DeviceData data = iterator1.next();
					
					// Check redundancy of the data being handled
					if(System.currentTimeMillis() - data.getTimeStamp() > 1000)
					{
						dataToBeRemoved1.add(data);
					}
					else
					{
						Circle locator = new Circle(dataManager.getFirstMonitor(),device.getMacAddress(),
								data.getDistanceFromMonitor(),dataManager.getFirstMonitor().getLocation(),baseCircle,false);
						locator.setOpacity(1f);
						monitor1Locators.add(locator);
					}
				}
				
				while(iterator2.hasNext())
				{
					DeviceData data = iterator2.next();
					
					// Check redundancy of the data being handled
					if(System.currentTimeMillis() - data.getTimeStamp() > 1000)
					{
						dataToBeRemoved2.add(data);
					}
					else
					{
						Circle locator = new Circle(dataManager.getSecondMonitor(),device.getMacAddress(),
								data.getDistanceFromMonitor(),dataManager.getSecondMonitor().getLocation(),baseCircle,false);
						locator.setOpacity(1f);
						monitor2Locators.add(locator);
					}
				}
				
				while(iterator3.hasNext())
				{
					DeviceData data = iterator3.next();
					
					// Check redundancy of the data being handled
					if(System.currentTimeMillis() - data.getTimeStamp() > 1000)
					{
						dataToBeRemoved3.add(data);
					}
					else
					{
						Circle locator = new Circle(dataManager.getThirdMonitor(),device.getMacAddress(),
								data.getDistanceFromMonitor(),dataManager.getThirdMonitor().getLocation(),baseCircle,false);
						locator.setOpacity(1f);
						monitor3Locators.add(locator);
					}
				}
				
				// Remove redundant data
				device.getData().removeAll(dataToBeRemoved1);
				checkDevice1.getData().removeAll(dataToBeRemoved2);
				checkDevice2.getData().removeAll(dataToBeRemoved3);
				
				
				
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
								Circle deviceCircle = new Circle(dataManager.getFirstMonitor(),device.getMacAddress(),
										0.05f,pointOfIntersection,baseCircle,true);
								deviceCircle.setColour(0, 1, 0); // Set discovered device colour to green
								device.setPointer(deviceCircle); // Update devices pointer
								device.setLocation(pointOfIntersection);
								// Render each locator on screen
								device.setLocator1(c1);
								device.setLocator2(c2);
								device.setLocator3(c3);
								// Set each locator at default radius of 0.05f - required for locator animation
								device.getLocator1().setRadius(0.05f);
								device.getLocator2().setRadius(0.05f);
								device.getLocator3().setRadius(0.05f);
								
								if(device.getAddressTag() != null)
								{	
									// Determine position of the address tag
									Vector3f tagPos = Maths.covertCoordinates(new Vector3f(device.getLocation().x,
											device.getLocation().y, 0)); // Z values will be zero as positional data is 2 Dimensional
									
									// Modify the address tag's position
									float offsetX = device.getAddressTag().getMaxLineSize() / 2f;
									float offsetY = device.getPointer().getMaxRadius();
									device.getAddressTag().setPosition(new Vector2f(tagPos.x - offsetX,tagPos.y - offsetY));
									
									
									
								}
								
								// If the device has not already been stored
								if(!devicePointers.contains(device))
								{
									devicePointers.add(device);
									addressTag.setContent(device.getMacAddress());
									addressTag.setColour(1, 1, 1);
									device.setAddressTag(addressTag);
									TextHandler.loadText(addressTag);
								}

								
							}
						}
					}
				}
			

			}

		}
	}

}
