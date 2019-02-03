package fontHandling;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class TextModel {
	
	// Fields
	private String content; // The block of text
	private float fontSize; // Size of the font
	
	private int vaoID; // Hold reference to the VAO data is stored in
	private int vertCount; // Number of vertices to construct the text mesh
	private Vector3f colour = new Vector3f(0,0,0); // Specify colour for the text - black by default
	
	private Vector2f position; // Position of the text on the screen - relative to OpenGL's positioning system
	
	private float maxLineSize; // The maximum width of a line in the text before requiring a new line
	private int numLines; // Number of lines covered by the block of text;
	
	private FontStyle fontStyle; // Store the type of font used
	
	// Specify whether the text is centred on the screen
	// False by default
	private boolean centred = false;
	
	// Constructor
	public TextModel(String content, float fontSize, FontStyle fontStyle, Vector2f position, float maxLineSize, boolean centred)
	{
		this.content = content;
		this.fontSize = fontSize;
		this.fontStyle = fontStyle;
		this.position = position;
		this.maxLineSize = maxLineSize;
		this.centred = centred;
		// Load TextModel
		TextHandler.loadText(this);
	}
	
	// Method to remove the text from the font renderer
	public void remove()
	{
		// remove text
		TextHandler.removeText(this);
	}
	
	// Getters and Setters
	
	public FontStyle getFont()
	{
		return fontStyle;
	}
	
	public void setColour(float red, float green, float blue)
	{
		colour.set(red, green, blue);
	}
	

	public Vector3f getColour() {
		return colour;
	}

	
	public int getNumberOfLines()
	{
		return numLines;
	}
	
    public void setNumberOfLines(int numLines) {
        this.numLines = numLines;
    }

	public String getContent() {
		return content;
	}

	public float getFontSize() {
		return fontSize;
	}

	public void setMesh(int vao, int vertCount)
	{
		this.vaoID = vao;
		this.vertCount = vertCount;
	}
	
	public int getMesh() {
		return vaoID;
	}

	public int getVertCount() {
		return vertCount;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public void setPosition(Vector2f position)
	{
		this.position = position;
	}

	public float getMaxLineSize() {
		return maxLineSize;
	}

	public boolean isCentred() {
		return centred;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
