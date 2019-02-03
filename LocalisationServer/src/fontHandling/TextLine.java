package fontHandling;

import java.util.ArrayList;
import java.util.List;

/*
 * This class is a representation of a line in a block of text
 * For example, if a specified block of text covers multiple lines,
 * then the data stored in this class will store all the words
 * and characters relevant to each line generated for the block
 * of text.
 */

public class TextLine {
	
	// Fields
	
	private double maxLength; // The full length the line of text covers
	private double spaceSize; // Spacing between words
	
	private List<Word> words = new ArrayList<>(); // Store all the words corresponding to this line of text
	private double currentLineLength = 0; // Store current length of the line of text as text is processed
	
	// Constructor
	public TextLine(double spaceWidth, double fontSize, double maxLength)
	{
		this.spaceSize = spaceWidth * fontSize;
		this.maxLength = maxLength;
	}
	
	// Try to add a new word to the current line
	public boolean attemptToAddWord(Word word)
	{
		double additionalLength = word.getWidth();
		additionalLength += !words.isEmpty() ? spaceSize : 0;
		if(currentLineLength + additionalLength <= maxLength)
		{
			words.add(word);
			currentLineLength += additionalLength;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	// Getters
	public double getMaxLength()
	{
		return maxLength;
	}
	
	public double getLineLength()
	{
		return currentLineLength;
	}
	
	public List<Word> getWords()
	{
		return words;
	}

}
