package filtering;

public class State {
	
	// Fields
	private float[] stateMatrix; // Store matrix of data being filtered
	
	// Constructor - configure state with pre-defined data
	public State(float[] positions)
	{
		stateMatrix = new float[positions.length];
		for(int i = 0; i < positions.length; i++)
		{
			stateMatrix[i] = positions[i];
		}
	}
	
	// Constructor - initialise state with origin values I.E. 0 for all
	public State()
	{
		stateMatrix = new float[3];
		stateMatrix[0] = 0;
		stateMatrix[1] = 0;
		stateMatrix[3] = 0;
	}
	
	
	// Getters and Setters
	public float[] getMatrix()
	{
		return stateMatrix;
	}
	
	public void setMatrix(float[] positions)
	{
		stateMatrix = new float[positions.length];
		for(int i = 0; i < positions.length; i++)
		{
			stateMatrix[i] = positions[i];
		}
	}

}
