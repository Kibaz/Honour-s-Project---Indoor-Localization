package graphics;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

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
		storeDataInAttribList(0,3,vertices);
		unbindVertexArrayObject();
		return new Shape(vaoID,indices.length);
	}
	
	/*
	 * Use this version of the load to vertex array object
	 * when loading font and text data
	 */
	public static int loadToVertexArrayObject(float[] positions, float[] texCoords)
	{
		int vaoID = createVertexArrayObject();
		storeDataInAttribList(0,2,positions);
		storeDataInAttribList(1,2,texCoords);
		unbindVertexArrayObject();
		return vaoID;
	}
	
	public static Shape loadToVertexArrayObject(float[] vertices)
	{
		int vaoID = createVertexArrayObject();
		//bindIndicesBuffer(indices);
		storeDataInAttribList(0,3,vertices);
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
	private static void storeDataInAttribList(int arribNum, int coordSize, float[] data)
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
		GL20.glVertexAttribPointer(arribNum, coordSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // Unbind Vertex Buffer Object
	}
	
	/*
	 * Method to load a font texture
	 */
	public static int loadFontTexture(String file, float blendFactor)
	{
		ByteBuffer image; // Store reference to image in byte buffer
		int width, height; // Store image width and height
		
		int tID = GL11.glGenTextures(); // Generate a texture from OpenGL
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tID); // Bind texture - specify texture as 2D and the ID of the generated texture
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS,blendFactor);
		
		try(MemoryStack stack = MemoryStack.stackPush())
		{
			IntBuffer w = stack.mallocInt(1); // Store width in integer buffer
			IntBuffer h = stack.mallocInt(1); // Store height in integer buffer
			IntBuffer comp = stack.mallocInt(1);
			
			// Using STBImage package for texture loading - specify whether to flip texture or not
			STBImage.stbi_set_flip_vertically_on_load(false); // Do not flip vertically when loaded
			image = STBImage.stbi_load(file, w, h, comp, 4);
			if(image == null)
			{
				throw new RuntimeException("Failed to load texture file!" + System.lineSeparator() + STBImage.stbi_failure_reason());
			}
			
			width = w.get();
			height = h.get();
		}
		
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D,0,GL11.GL_RGBA, width,height,0,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,image);
		
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0); // Un-bind current texture
		
		return tID;
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
