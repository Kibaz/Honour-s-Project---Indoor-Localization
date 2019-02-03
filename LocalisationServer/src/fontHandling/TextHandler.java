package fontHandling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graphics.Loader;

public class TextHandler {
	
	private static Map<FontStyle, List<TextModel>> texts = new HashMap<>();
	
	private static TextRenderer renderer;
	
	public static void init()
	{
		renderer = new TextRenderer();
	}
	
	public static void render()
	{
		renderer.render(texts);
	}
	
	public static void loadText(TextModel text)
	{
		FontStyle font = text.getFont();
		TextModelData data = font.loadText(text);
		int vao = Loader.loadToVertexArrayObject(data.getVertices(), data.getTextureCoords());
		text.setMesh(vao, data.getVertCount());
		List<TextModel> textBatch = texts.get(font);
		if(textBatch == null)
		{
			textBatch = new ArrayList<>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public static void removeText(TextModel text)
	{
		List<TextModel> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if(textBatch.isEmpty())
		{
			texts.remove(text.getFont());
		}
	}
	
	public static void clear()
	{
		renderer.cleanUp();
	}

}
