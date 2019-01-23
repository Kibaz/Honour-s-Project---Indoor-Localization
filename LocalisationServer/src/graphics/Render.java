package graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import objects.Circle;
import objects.Shape;
import shaders.ShapeShader;
import utils.Maths;

/*
 * Class to render graphics onto the screen
 * Using OpenGL dependencies to load the data
 * Bind and unbind the data each frame
 * And draw the specified model/shape
 */

public class Render {
	
	private static ShapeShader shapeShader = new ShapeShader();
	
	/*
	 * Wipe the screen every frame
	 */
	public static void prepare()
	{
		GL11.glClearColor(0, 0.5f, 0.5f, 0);
	}
	
	/*
	 * Bind VAOs
	 * Draw the shape using triangles
	 * Disable and unbind VAO after drawing has complete
	 */
	public static void render(Shape shape)
	{
		GL30.glBindVertexArray(shape.getVertexArrayObjID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawElements(GL11.GL_TRIANGLES,shape.getVertexCount(),GL11.GL_UNSIGNED_INT, 0);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, shape.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
	/*
	 * Similar to basic render method
	 * However to draw a circle, the GL_TRIANGLE_FAN or GL_LINE_LOOP
	 * Provide better options for the drawing method.
	 * glDrawArrays() method used instead as indices are not required
	 */
	public static void drawCircle(Circle circle)
	{
		shapeShader.start(); // Ensure shader program is in use
		GL30.glBindVertexArray(circle.getShape().getVertexArrayObjID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		prepareCircle(circle);
		if(circle.isFilled())
		{
			GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, circle.getShape().getVertexCount());
		}
		else
		{
			GL11.glDrawArrays(GL11.GL_LINE_LOOP, 0, circle.getShape().getVertexCount());
		}
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shapeShader.stop(); // Stop using shader program after rendering is complete
	}
	
	private static void prepareCircle(Circle circle)
	{
		Matrix4f matrix = Maths.createTransformationMatrix(new Vector3f(circle.getCentre().x,circle.getCentre().y,0),0,0,0,circle.getRadius()/0.05f);
		shapeShader.loadTransformationMatrix(matrix);
		shapeShader.loadColour(circle.getColour());
		shapeShader.loadOpacity(circle.getOpacity());
	}

}
