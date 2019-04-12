package graphics;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import inputs.MouseButton;
import inputs.MouseCursor;

public class Window {
	
	/*
	 * Class to construct and initialise a GLFW window
	 * Window to support a graphical display of device locations
	 * This view will be replicated on the client devices
	 * I.E Mobile applications
	 */
	
	// Constants
	/*
	 * Configure window 800 x 600 pixels
	 * Can be altered at any point
	 */
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	
	private static final String TITLE = "Localisation System Visualisation";
	
	
	// Fields
	private static long windowID; // Store a reference value for the GLFW window
	
	private static double lastFrameInterval;
	private static float delta;
	
	// Mouse input handling
	private static GLFWCursorPosCallback cursorCallback; // Mouse cursor position tracking
	private static GLFWMouseButtonCallback mouseButtonCallback; // Mouse button listener
	
	private static float lastCursorPosX;
	private static float lastCursorPosY;
	
	private static float mouseDeltaX;
	private static float mouseDeltaY;
	
	
	/*
	 * Initialising GLFW
	 * Creating Window to support graphical display
	 */
	public static void init()
	{
		// Set error callback
		GLFWErrorCallback.createPrint(System.err).set(); // Catch errors during operation
		
		// Initialise GLFW
		// Check whether GLFW is initialised properly
		if(!GLFW.glfwInit())
		{
			throw new IllegalStateException("Unable to intialise GLFW for graphical display!");
		}
		
		// Configure GLFW window properties
		GLFW.glfwDefaultWindowHints(); // Set default properties
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE); // Window cannot be resized
		
		// Create GLFW window :- returns long value - store as ID
		windowID = GLFW.glfwCreateWindow(WIDTH, HEIGHT, TITLE, MemoryUtil.NULL, MemoryUtil.NULL);
		
		// Check whether window has been created
		if(windowID == MemoryUtil.NULL)
		{
			throw new RuntimeException("Failed to initialise/create the GLFW window for graphical display!");
		}
		
		
		// Configure input callbacks
		cursorCallback = new MouseCursor();
		GLFW.glfwSetCursorPosCallback(windowID, cursorCallback);
		
		mouseButtonCallback = new MouseButton();
		GLFW.glfwSetMouseButtonCallback(windowID, mouseButtonCallback);
		
		// Create memory stack for initialising vid mode
		/*
		 * Vid Mode in GLFW allows for control over window positioning
		 * This can also aid with window resizing
		 * This is not a necessity, however can be useful.
		 */
		try (MemoryStack stack = MemoryStack.stackPush())
		{
			IntBuffer bufferWidth = stack.mallocInt(1);
			IntBuffer bufferHeight = stack.mallocInt(1);
			
			// Retrieve window dimension settings
			GLFW.glfwGetWindowSize(windowID, bufferWidth, bufferHeight);
			
			// Initialise vid mode pulling in settings of underlying machine's display
			GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			
			GLFW.glfwSetWindowPos(windowID,
					((vidMode.width() - bufferWidth.get(0))/2), 
					((vidMode.height() - bufferHeight.get(0))/2));
			
			GLFW.glfwMakeContextCurrent(windowID); // Set as main window
			GL.createCapabilities(); // Enable support for OpenGL
			
			GLFW.glfwSwapInterval(1); // Clear window every frame
			
			GLFW.glfwShowWindow(windowID); // Spawn window
			
			lastFrameInterval = GLFW.glfwGetTime();
			lastCursorPosX = MouseCursor.getXPos();
			lastCursorPosY = MouseCursor.getYPos();
		}
	}
	
	/*
	 * To be called every frame
	 * Ensures the window is cleared
	 */
	public static void clear()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	
	/*
	 * Updates required every frame
	 */
	public static void update()
	{
		GLFW.glfwSwapBuffers(windowID);
		GLFW.glfwPollEvents();
		
		double currentFrameTime = GLFW.glfwGetTime();
		delta = (float) (currentFrameTime - lastFrameInterval);
		lastFrameInterval = currentFrameTime;
		
		float currentMousePosX = MouseCursor.getXPos();
		float currentMousePosY = MouseCursor.getYPos();
		mouseDeltaX = currentMousePosX - lastCursorPosX;
		mouseDeltaY = currentMousePosY - lastCursorPosY;
		lastCursorPosX = currentMousePosX;
		lastCursorPosY = currentMousePosY;
	}
	
	public static float getDeltaTime()
	{
		return delta;
	}
	
	public static long getWindowID()
	{
		return windowID;
	}
	
	public static int getWidth()
	{
		return WIDTH;
	}
	
	public static int getHeight()
	{
		return HEIGHT;
	}
	
	public static float getMouseDeltaX()
	{
		return mouseDeltaX;
	}
	
	public static float getMouseDeltaY()
	{
		return mouseDeltaY;
	}
	
	/*
	 * Destroy the window when the process is ended
	 * Ensure the GLFW is terminated
	 */
	public static void destroy()
	{
		GLFW.glfwTerminate();
	}
	
	/*
	 * Verify whether the GLFW window is closed
	 */
	public static boolean isClosed()
	{
		return GLFW.glfwWindowShouldClose(windowID);
	}

}
