package fontHandling;

/*
 * This class will store the necessary data to construct
 * visual text on the screen in correspondence to OpenGL's 
 * rendering system.
 * 
 * Data such as vertices and texture coordinates will be used
 * to define how the text will be rendered on the screen.
 * 
 * This data is relatively similar to storing regular 2D and 3D
 * model data.
 */

public class TextModelData {
	
	// Fields
	private float[] vertices; // Store the vertex positions to form the text mesh
	private float[] textureCoords; // Store the texture coordinates for colouring/texturing the text
	
	// Constructor
	public TextModelData(float[] verts, float[] texCoords)
	{
		this.vertices = verts;
		this.textureCoords = texCoords;
	}

	// Getters
	public float[] getVertices() {
		return vertices;
	}

	public float[] getTextureCoords() {
		return textureCoords;
	}
	
	/*
	 * Method to retrieve the vertex count of the text
	 * Divide by 2 as text is processed as a 2D shape 
	 */
	public int getVertCount()
	{
		return vertices.length / 2;
	}

}
