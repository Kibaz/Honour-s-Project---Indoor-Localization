package objects;

public class Shape {
	
	private int vertexArrayObjID;
	private int vertexCount;
	
	public Shape(int vaoID, int vertCount)
	{
		this.vertexArrayObjID = vaoID;
		this.vertexCount = vertCount;
	}

	public int getVertexArrayObjID() {
		return vertexArrayObjID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	

}
