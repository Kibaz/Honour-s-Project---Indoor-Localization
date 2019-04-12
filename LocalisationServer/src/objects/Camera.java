package objects;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.util.vector.Vector3f;

import graphics.Window;
import inputs.MouseButton;

public class Camera {
	
	private static final float MOUSE_SENSITIVITY_FACTOR = 0.05f;
	
	private Vector3f position;
	
	private float pitch = 0;
	private float yaw = 0;
	
	private float zoom = 5;
	
	public Camera()
	{
		position = new Vector3f(0,0,0);
		position.z = zoom;
	}
	
	public void calculateZoom()
	{
		GLFW.glfwSetScrollCallback(Window.getWindowID(), new GLFWScrollCallback() {

			@Override
			public void invoke(long window, double xOffset, double yOffset) {
				float zoomLevel = 0;
				zoomLevel = (float) (yOffset * 1f);
				position.z -= zoomLevel;
			}
			
		});
	}
	
	public void move()
	{
		calculateZoom();
		calculatePosition();
	}
	
	private void calculatePosition()
	{
		// IF LEFT MOUSE BUTTON IS PRESSED
		if(MouseButton.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_1))
		{
			this.position.x += Window.getMouseDeltaX() * MOUSE_SENSITIVITY_FACTOR;
			this.position.y += Window.getMouseDeltaY() * MOUSE_SENSITIVITY_FACTOR;
		}
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public Vector3f getPosition() {
		return position;
	}
	
	

}
