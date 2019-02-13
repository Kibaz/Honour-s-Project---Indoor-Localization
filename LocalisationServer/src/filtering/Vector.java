package filtering;

public class Vector {
	
	/*
	 * A vector is a 1 dimensional matrix
	 */
	
	// Fields
	private float[] vector;
	private int dimension;
	
	
	// Constructor - initialise vector of any length
	public Vector(int dimension)
	{
		this.dimension = dimension;
		this.vector = new float[dimension];
	}
	
	// Second constructor to initialise vector with set values
	public Vector(float[] vector)
	{
		this.vector = vector;
		this.dimension = vector.length;
	}
	
	// Retrieve vector
	public float[] get()
	{
		return vector;
	}
	
	// Retrieve specific value from vector
	public float get(int i)
	{
		return vector[i];
	}
	
	// Set all values in vector to 0
	public void setZero()
	{
		for(int i = 0; i < vector.length; i++)
		{
			vector[i] = 0;
		}
	}
	
	public void set(int index, float val)
	{
		vector[index] = val;
	}
	
	/*
	 * Method to transform this vector by a matrix
	 * compatible for the transformation
	 * 
	 * A transformation is effectively the dot product
	 * between a matrix and a vector
	 * 
	 * The result is vector of dimension equal to the matrix'
	 * number of rows
	 */
	public Vector transform(Matrix matrix)
	{
		Vector result = new Vector(matrix.getXDim());
		
		// Check whether vector and matrix are compatible for transformation
		// Number of columns in matrix must equal the dimension of the vector
		if(this.dimension != matrix.getYDim())
		{
			throw new RuntimeException("Error: The vector and transformation matrix are not compatible!");
		}
		
		// Loop through values in the transformation matrix
		for(int i = 0; i < matrix.getXDim(); i++)
		{
			for(int j = 0; j < matrix.getYDim(); j++)
			{
				// Calculate the dot product between vector and matrix
				float val = this.vector[j] * matrix.get()[i][j];
				result.vector[i] += val; // Add to current value
			}
		}

		
		return result;
	}
	
	/*
	 * Method to add two vectors together
	 * This method will return a new vector
	 */
	public Vector add(Vector other)
	{
		// Check whether vectors are compatible for addition
		// I.E. must have the same dimension
		if(this.dimension != other.dimension)
		{
			throw new RuntimeException("Error: Cannot add incompatible vectors!");
		}
		
		// Resultant vector will have same dimension as participant vectors
		// Initialise resultant vector
		Vector result = new Vector(this.dimension); 
		
		for(int i = 0; i < this.dimension; i++)
		{
			result.vector[i] = this.vector[i] + other.vector[i];
		}
		
		return result;
	}
	
	public Vector sub(Vector other)
	{
		// Check whether vectors are compatible for subtraction
		// I.E. must have the same dimension
		if(this.dimension != other.dimension)
		{
			throw new RuntimeException("Error: Cannot subtract incompatible vectors!");
		}
		
		// Resultant vector will have same dimension as participant vectors
		// Initialise resultant vector
		Vector result = new Vector(this.dimension); 
		
		for(int i = 0; i < this.dimension; i++)
		{
			result.vector[i] = this.vector[i] - other.vector[i];
		}
		
		return result;
	}
	
	// Get the length of the vector
	public int getDimension()
	{
		return dimension;
	}
	
	/*
	 * Override toString() method to allow
	 * vector to be printed to console
	 * in strict vector format
	 */
	@Override
	public String toString()
	{
		String output = "Vector [";
		
		for(int i = 0; i < vector.length; i++)
		{
			if(i != vector.length-1)
			{
				output += vector[i] + ",";
			}
			else
			{
				output += vector[i];
			}
			
		}
		output += "] dim = " + dimension;
		
		return output;
	}

}
