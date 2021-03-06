package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import objects.Camera;
import utils.Maths;

public class ShapeShader extends Shader {

	private static final String VERTEX_SHADER = "src/shaders/shape_vertex_shader.txt";
	private static final String FRAGMENT_SHADER = "src/shaders/shape_fragment_shader.txt";
	
	// Uniform locations to be registered by GLSL shader program
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_colour;
	private int location_opacity;
	
	public ShapeShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_colour = super.getUniformLocation("colour");
		location_opacity = super.getUniformLocation("opacity");
	}
	
	public void loadColour(Vector3f colour)
	{
		super.loadVector(location_colour, colour);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix)
	{
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix)
	{
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera)
	{
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadOpacity(float opacity)
	{
		super.loadFloat(location_opacity, opacity);
	}

}
