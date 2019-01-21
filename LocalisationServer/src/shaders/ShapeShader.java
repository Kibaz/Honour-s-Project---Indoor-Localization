package shaders;

public class ShapeShader extends Shader {

	private static final String VERTEX_SHADER = "src/shaders/shape_vertex_shader.txt";
	private static final String FRAGMENT_SHADER = "src/shaders/shape_fragment_shader.txt";
	
	public ShapeShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		
	}

}
