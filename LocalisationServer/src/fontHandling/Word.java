package fontHandling;

import java.util.ArrayList;
import java.util.List;

/*
 * This class represents a word (i.e set of characters with no spacing)
 * within a block/line of text
 * A word will contain a set of characters and be stored within a "line"
 * of text
 */

public class Word {
	
	// Fields
	private List<Character> characters = new ArrayList<>(); // Store the set of characters which form this word
	private double width = 0; // Store the width of the word - depicted by the characters and their accumulated width
	private double fontSize; // Store font size - this will affect the positioning in OpenGL's system
	
	// Constructor
	public Word(double fontSize)
	{
		this.fontSize = fontSize;
	}
	
	// Method to add a character to the current word as text is processed
	public void addCharacter(Character character)
	{
		characters.add(character);
		width += character.getxAdvance() * fontSize;
	}
	
	protected List<Character> getCharacters()
	{
		return characters;
	}
	
	public double getWidth()
	{
		return width;
	}

}
