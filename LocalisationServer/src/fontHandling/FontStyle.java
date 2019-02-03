package fontHandling;

import java.io.File;

/*
 * This class will store the data which represents the style
 * of font used to form the block of text, e.g. Arial, Times New Roman etc...
 */

public class FontStyle {
	
	// Fields
	private int textureAtlas; // Hold reference to texture ID
	private FontLoader loader; // Store loader for loading the text data
	
	// Constructor
	public FontStyle(int textureID, File fontFile)
	{
		this.textureAtlas = textureID;
		// Initialise font loader using specified font file
		this.loader = new FontLoader(fontFile);
	}
	
	public TextModelData loadText(TextModel text)
	{
		return loader.createMesh(text);
	}
	
	// Getters
	public int getTextureAtlas() {
		return textureAtlas;
	}
	
	

}
