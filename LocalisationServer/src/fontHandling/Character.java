package fontHandling;

/*
 * This class is used to generate a character with positional data
 * and offsets which correspond to the text and font file used when
 * processing the text. It's positional data is all in relation to
 * the words and lines where it resides. 
 */

public class Character {
	
	// Fields
	private int ID; // Identification value for referencing specific characters
	private double xTexCoord; // Actual X coordinate of the character - relative to OpenGL's positioning system
	private double yTexCoord; // Actual Y coordinate of the character - relative to OpenGL's positioning system
	private double xMaxTexCoord; // X coordinate where the character finishes
	private double yMaxTexCoord; // Y coordinate where the character finishes - max height
	private double xOffset; // Offset from other characters i.e spacing
	private double yOffset; // Offset from the line below i.e spacing on the y axis
	private double sizeX; // Width of character
	private double sizeY; // Height of character
	private double xAdvance;
	
	
	// Constructor
	public Character(int id, double xTexCoord, double yTexCoord, double xTexSize, double yTexSize,
			double xOffset, double yOffset, double sizeX, double sizeY, double xAdvance) {
		this.ID = id;
		this.xTexCoord = xTexCoord;
		this.yTexCoord = yTexCoord;
		this.xMaxTexCoord = xTexSize + xTexCoord;
		this.yMaxTexCoord = yTexSize + yTexCoord;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.xAdvance = xAdvance;
	}
	
	// Getters
	public int getID() {
		return ID;
	}

	public double getxTexCoord() {
		return xTexCoord;
	}

	public double getyTexCoord() {
		return yTexCoord;
	}

	public double getxMaxTexCoord() {
		return xMaxTexCoord;
	}

	public double getyMaxTexCoord() {
		return yMaxTexCoord;
	}

	public double getxOffset() {
		return xOffset;
	}

	public double getyOffset() {
		return yOffset;
	}

	public double getSizeX() {
		return sizeX;
	}

	public double getSizeY() {
		return sizeY;
	}

	public double getxAdvance() {
		return xAdvance;
	}

}
