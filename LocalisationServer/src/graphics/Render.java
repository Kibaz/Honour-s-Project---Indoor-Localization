package graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import objects.Shape;

/*
 * Class to render graphics onto the screen
 * Using OpenGL dependencies to load the data
 * Bind and unbind the data each frame
 * And draw the specified model/shape
 */

public class Render {
	
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
	public static void drawCircle(Shape shape, boolean fill)
	{
		GL30.glBindVertexArray(shape.getVertexArrayObjID());
		GL20.glEnableVertexAttribArray(0);
		if(fill)
		{
			GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, shape.getVertexCount());
		}
		else
		{
			GL11.glDrawArrays(GL11.GL_LINE_LOOP, 0, shape.getVertexCount());
		}
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

}
