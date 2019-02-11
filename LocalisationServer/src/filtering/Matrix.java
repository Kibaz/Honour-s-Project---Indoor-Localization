package filtering;

import java.util.ArrayList;
import java.util.List;

public class Matrix {
	
	private int x_dim;
	private int y_dim;
	
	private float[][] matrix;
	
	// Constructor
	public Matrix(int x_dim, int y_dim)
	{
		// Initialise matrix with specified values
		this.x_dim = x_dim;
		this.y_dim = y_dim;
		matrix = new float[x_dim][y_dim];
	}
	
	public float[][] get()
	{
		return matrix;
	}
	
	public float get(int i, int j)
	{
		return matrix[i][j];
	}
	
	/*
	 * Sets all values in matrix to 0
	 */
	public void setZero()
	{
		for(int i = 0; i < x_dim; i++)
		{
			for(int j = 0; j < x_dim; j++)
			{
				matrix[i][j] = 0;
			}
		}
	}
	
	/*
	 * Set specified index equal to value passed
	 */
	public void set(int i, int j, float val)
	{
		matrix[i][j] = val;
	}
	
	public Matrix multiply(Matrix other)
	{
		Matrix result = null;
		// Check dimensions of matrices are compatible
		int dim1 = this.x_dim * this.y_dim;
		int dim2 = other.x_dim * other.y_dim;
		if(dim1 == dim2)
		{
			// Loop through first matrix from columns
			/* 
			 * i = row index 1st matrix
			 * j = column index 1st matrix
			 * 
			 * k = column index 2nd matrix
			 * l = column index 2nd matrix
			 */
			
			// Resultant matrix will have same no. rows as first matrix
			// Resultant matrix will have same no. columns as second matrix
			result = new Matrix(this.x_dim,other.y_dim);
			result.setZero(); // Initialise with 0s
			
			// Loop through x dimension of resultant matrix
			for(int k = 0; k < result.x_dim; k++)
			{
				// Loop through first matrix
				// Flip i and j index to extract column values from 2nd matrix
				for(int i = 0; i < this.x_dim; i++)
				{
					for(int j = 0; j < this.y_dim; j++)
					{
						/*
						 * Multiply each element from a row in the first matrix
						 * with each element from a column in the second matrix
						 * 
						 * Each row in first matrix must multiply with each column
						 * in second matrix - the final value of resultant matrix
						 * is the combination of the row X column dot product
						 */
						float val = this.matrix[k][j] * other.matrix[j][i];		
						result.matrix[k][i] += val; // Append to current index of resultant matrix
					}
				}
			}

			

		}
		else
		{
			// Report error upon incompatible dimensions for multiplication
			throw new RuntimeException("Error: Cannot multiply matrices with incompatible dimensions!");
		}
		
		return result;
		
	}
	
	// Configure the current matrix as an identity matrix
	// I.E. Set all diagonal values to 1 and all remaining values to 0
	public void setIdentity()
	{
		// Set the values in diagonal (top-left to bottom right to 1)
		// Set all other values to 0
		for(int i = 0; i < x_dim; i++)
		{
			for(int j = 0; j < y_dim; j++)
			{
				if(i == j)
				{
					matrix[i][j] = 1;
				}
				else
				{
					matrix[i][j] = 0;
				}
				
			}
		}
	}

	/*
	 * Print the matrix to console in
	 * matrix format
	 */
	public void print()
	{
		String output = ""; // Store output as a string
		for(int i = 0; i < x_dim; i++)
		{
			for(int j = 0; j < y_dim; j++)
			{
				if(j == y_dim-1) // When reached end of row
				{
					output += matrix[i][j] + "\n"; // Append new line
				}
				else
				{
					output += matrix[i][j] + ","; // Append comma until end of row
				}
			}
		}
		// Output the output string to the console
		// This should have been formatted to appear as a matrix
		System.out.println(output);
	}

}
