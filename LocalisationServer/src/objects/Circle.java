package objects;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import graphics.Loader;

public class Circle {
	
	// Fields
	private Monitor monitor; // Monitor used as reference point
	private String deviceMac; // MAC address of the device the circle refers to
	private float radius; // Radius of the circle - equal to the distance from the monitor to the detected device
	private Vector2f centre; // Location of the monitoring device i.e. Raspberry PIs
	
	private Vector3f colour;
	private float opacity = 1.0f; // MAX 1 and MIN 0
	
	private Shape shape;
	
	private boolean fill = false;
	
	// Constructor
	public Circle(Monitor monitor, String deviceMac, float radius, Vector2f centre, Shape shape, boolean fill)
	{
		this.monitor = monitor;
		this.radius = radius;
		this.centre = centre;
		this.shape = shape;
		this.fill = fill;
		colour = new Vector3f(0,0,0); // Default black
	}
	
	public void increasePosition(float dx, float dy)
	{
		this.centre.x += dx;
		this.centre.y += dy;
	}
	
	// Configuring circle vertex list
	public static Shape constructCircle(float radius, int segments)
	{
		List<Float> verts = new ArrayList<>();
		List<Integer> indexList = new ArrayList<>();
		
		float increment = (float) (2.0f * Math.PI / segments);
		for(float angle = 0.0f; angle <= 2.0f * Math.PI; angle+= increment)
		{
			verts.add((float)(radius * Math.cos(angle)));
			verts.add((float)(radius * Math.sin(angle)));
			verts.add(0f);
		}
		float[] vertices = new float[verts.size()];
		for(int i = 0; i < verts.size(); i++)
		{
			vertices[i] = verts.get(i);
		}
		
		return Loader.loadToVertexArrayObject(vertices);
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
	
	public float getOpacity() {
		return opacity;
	}



	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	
	public Vector3f getColour()
	{
		return colour;
	}
	
	public void setColour(float red, float green, float blue)
	{
		this.colour.x = red;
		this.colour.y = green;
		this.colour.z = blue;
	}
	
	public boolean isFilled()
	{
		return fill;
	}
	
	
	
	

}
