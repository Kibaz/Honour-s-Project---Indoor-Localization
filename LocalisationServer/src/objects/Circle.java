package objects;

import org.lwjgl.util.vector.Vector2f;

public class Circle {
	
	// Fields
	private Monitor monitor; // Monitor used as reference point
	private String deviceMac; // MAC address of the device the circle refers to
	private float radius; // Radius of the circle - equal to the distance from the monitor to the detected device
	private Vector2f centre; // Location of the monitoring device i.e. Raspberry PIs
	
	// Constructor
	public Circle(Monitor monitor, String deviceMac, float radius, Vector2f centre)
	{
		this.monitor = monitor;
		this.radius = radius;
		this.centre = centre;
	}
	
	public void draw()
	{
		
	}

	// Getters and Setters
	public Monitor getMonitor() {
		return monitor;
	}
	
	public String getDeviceMac() {
		return deviceMac;
	}

	public float getRadius() {
		return radius;
	}

	public Vector2f getCentre()
	{
		return centre;
	}
	
	

}
