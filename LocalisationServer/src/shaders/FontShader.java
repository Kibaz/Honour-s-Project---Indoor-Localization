package shaders;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class FontShader extends Shader {
	
	private static final String VERTEX_PATH = "src/shaders/font_vert.txt";
	private static final String FRAGMENT_PATH = "src/shaders/font_frag.txt";
	
	// Store location values of uniform variables for GLSL shader programs
	private int loc_colour;
	private int loc_translation;

	public FontShader() {
		super(VERTEX_PATH,FRAGMENT_PATH);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoords");
	}

	@Override
	protected void getAllUniformLocations() {
		loc_colour = super.getUniformLocation("colour");
		loc_translation = super.getUniformLocation("translation");
	}
	
	public void loadColour(Vector3f colour)
	{
		super.loadVector(loc_colour, colour);
	}
	
	public void loadTranslation(Vector2f translation)
	{
		super.load2DVector(loc_translation, translation);
	}

}
