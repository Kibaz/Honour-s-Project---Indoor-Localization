package graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import objects.Shape;

/*
 * Class for handling necessary loading of model/shape data
 * And translating it into data which can be handled in
 * OpenGL
 */

public class Loader {
	
	private static List<Integer> vaos = new ArrayList<>();
	private static List<Integer> vbos = new ArrayList<>();
	
	public static Shape loadToVertexArrayObject(float[] vertices, int[] indices)
	{
		int vaoID = createVertexArrayObject();
		bindIndicesBuffer(indices);
		storeDataInAttribList(0,vertices);
		unbindVertexArrayObject();
		return new Shape(vaoID,indices.length);
	}
	
	public static Shape loadToVertexArrayObject(float[] vertices)
	{
		int vaoID = createVertexArrayObject();
		//bindIndicesBuffer(indices);
		storeDataInAttribList(0,vertices);
		unbindVertexArrayObject();
		return new Shape(vaoID,vertices.length/3);
	}
	
	// Create Vertex Array Object
	// Bind Vertex Array Object
	private static int createVertexArrayObject()
	{
		int vaoID = GL30.glGenVertexArrays(); // Generate a Vertex Array Object
		vaos.add(vaoID); // Store reference to vertex array object
		GL30.glBindVertexArray(vaoID); // Bind VAO
		return vaoID;
	}
	
	private static void bindIndicesBuffer(int[] data)
	{
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		// Store the data into an integer buffer
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	// Method for storing model data in an attribute list
	private static void storeDataInAttribList(int arribNum, float[] data)
	{
		int vboID = GL15.glGenBuffers(); // Generate vertex buffer object
		vbos.add(vboID); // Store reference to vertex buffer object
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); // Bind buffer object
		/*
		 * Convert float array data into
		 * Float buffer object for OpenGL handling
		 */
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data); // Store data in float buffer object
		buffer.flip(); // Data must be flipped to prepare for reading
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(arribNum, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // Unbind Vertex Buffer Object
	}
	
	// Method to unbind Vertex Array Object
	private static void unbindVertexArrayObject()
	{
		GL30.glBindVertexArray(0);
	}
	
	// Clear all references to VAOs and VBOs
	// Ensure that these objects are removed
	public static void clear()
	{
		for(int vao: vaos)
		{
			GL30.glDeleteVertexArrays(vao);
		}
		
		for(int vbo: vbos)
		{
			GL15.glDeleteBuffers(vbo);
		}
	}

}
