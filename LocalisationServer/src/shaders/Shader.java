package shaders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class Shader {
	
	private int program; // Reference to the shader program for OpenGL
	private int vertexShaderID; // Determines how the model should be shaded in relation to vertex positions
	private int fragmentShaderID; // Determines how the colours will be manipulated
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public Shader(String vertexShader, String fragmentShader)
	{
		vertexShaderID = loadShader(vertexShader,GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentShader,GL20.GL_FRAGMENT_SHADER);
		program = GL20.glCreateProgram();
		GL20.glAttachShader(program, vertexShaderID);
		GL20.glAttachShader(program, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(program);
		GL20.glValidateProgram(program);
		getAllUniformLocations();
	}
	
	public void start()
	{
		GL20.glUseProgram(program);
	}
	
	public void stop()
	{
		GL20.glUseProgram(0);
	}
	
	public void cleanUp()
	{
		stop();
		GL20.glDetachShader(program, vertexShaderID);
		GL20.glDetachShader(program, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(program);
	}
	
	private int loadShader(String filename, int type)
	{
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while((line = reader.readLine()) != null)
			{
				shaderSource.append(line).append("//\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		int shader = GL20.glCreateShader(type);
		GL20.glShaderSource(shader, shaderSource);
		GL20.glCompileShader(shader);
		if(GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
		{
			System.out.println(GL20.glGetShaderInfoLog(shader,500));
			System.err.println("Could not compiler shader!");
			System.exit(-1);
		}
		
		return shader;

	}
	
	protected abstract void bindAttributes();
	
	protected abstract void getAllUniformLocations();
	
	protected void bindAttribute(int attr, String varName)
	{
		GL20.glBindAttribLocation(program, attr, varName);
	}
	
	protected int getUniformLocation(String uniformName)
	{
		return GL20.glGetUniformLocation(program, uniformName);
	}
	
	protected void loadMatrix(int location, Matrix4f matrix)
	{
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4fv(location, false, matrixBuffer);
	}
	
	protected void loadVector(int location, Vector3f vector)
	{
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	protected void loadFloat(int location, float value)
	{
		GL20.glUniform1f(location, value);
	}

}
