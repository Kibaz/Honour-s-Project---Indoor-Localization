package fontHandling;

import java.io.File;

import graphics.Loader;

public class Fonts {
	
	// Store fonts to be used
	public static final FontStyle arial = new FontStyle(Loader.loadFontTexture("files/arial.png", 0), new File("files/arial.fnt"));

}
