package objects;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import graphics.Loader;

public class Circle {
	
	// Fields
	private Monitor monitor; // Monitor used as reference point
	private String deviceMac; // MAC address of the device the circle refers to
	private float radius; // Radius of the circle - equal to the distance from the monitor to the detected device
	private Vector2f centre; // Location of the monitoring device i.e. Raspberry PIs
	
	private Shape shape;
	
	// Constructor
	public Circle(Monitor monitor, String deviceMac, float radius, Vector2f centre)
	{
		this.monitor = monitor;
		this.radius = radius;
		this.centre = centre;
	}
	
	// Configuring circle vertex list
	public void constructCircle(int segments)
	{
		List<Float> verts = new ArrayList<>();
		List<Integer> indexList = new ArrayList<>();
		
		float increment = (float) (2.0f * Math.PI / segments);
		for(float angle = 0.0f; angle <= 2.0f * Math.PI; angle+= increment)
		{
			verts.add((float)(radius * Math.cos(angle) + centre.x));
			verts.add((float)(radius * Math.sin(angle) + centre.y));
			verts.add(0f);
		}
		float[] vertices = new float[verts.size()];
		for(int i = 0; i < verts.size(); i++)
		{
			vertices[i] = verts.get(i);
		}
		
		shape = Loader.loadToVertexArrayObject(vertices);
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
	
	public Shape getShape()
	{
		return shape;
	}
	
	
	
	

}
