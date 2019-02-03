package fontHandling;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import shaders.FontShader;

public class TextRenderer {
	
	private FontShader shader;
	
	public TextRenderer()
	{
		this.shader = new FontShader();
	}
	
	public void render(Map<FontStyle, List<TextModel>> texts)
	{
		prepare();
		for(FontStyle font: texts.keySet())
		{
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());
			List<TextModel> list = texts.get(font);
			for(TextModel text : list)
			{
				renderText(text);
			}
		}
		finish();
	}

	private void prepare()
	{	
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		shader.start();
	}
	
	private void renderText(TextModel text)
	{
		GL30.glBindVertexArray(text.getMesh());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		shader.loadColour(text.getColour());
		shader.loadTranslation(text.getPosition());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertCount());
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	private void finish()
	{
		shader.stop();
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void cleanUp()
	{
		shader.cleanUp();
	}

}
