package filtering;

import java.util.ArrayList;
import java.util.List;

public class Matrix {
	
	private int x_dim;
	private int y_dim;
	
	private double[][] matrix;
	
	// Constructor
	public Matrix(int x_dim, int y_dim)
	{
		// Initialise matrix with specified values
		this.x_dim = x_dim;
		this.y_dim = y_dim;
		matrix = new double[x_dim][y_dim];
	}
	
	public double[][] get()
	{
		return matrix;
	}
	
	public double get(int i, int j)
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
	public void set(int i, int j, double val)
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
						double val = this.matrix[k][j] * other.matrix[j][i];		
						result.matrix[k][i] += val; // Append to current index of resultant matrix
					}
				}
			}

			

		}
		else if(this.y_dim == other.x_dim)
		{
			/*
			 * We can still multiply matrices as long as 
			 * the number of columns in the first matrix matches the
			 * number of rows in the second matrix
			 */
			
			// The resultant matrix = numRows 1st matrix X numCols 2nd matrix
			result = new Matrix(this.x_dim,other.y_dim);
			
			// Loop through resultant matrix to set the values of the matrix
			for(int i = 0; i < result.x_dim; i++)
			{
				for(int j = 0; j < result.y_dim; j++)
				{
					// Loop through first multiplier's row elements
					// Note: first.y_dim == second.x_dim
					for(int k = 0; k < this.y_dim; k++)
					{
						double val = this.matrix[i][k] * other.matrix[k][j];
					    result.matrix[i][j] += val;
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
	
	public void setDiag(double value)
	{
		// Set the values in diagonal (top-left to bottom right equal to specified value)
		// Set all other values to 0
		for(int i = 0; i < x_dim; i++)
		{
			for(int j = 0; j < y_dim; j++)
			{
				if(i == j)
				{
					matrix[i][j] = value;
				}
				else
				{
					matrix[i][j] = 0;
				}
				
			}
		}
	}
	
	/*
	 * The transpose of a matrix is a matrix
	 * whose row values are the column values
	 * of the original matrix and the column values
	 * the row values of the original matrix
	 * 
	 * Swaps row and column values
	 */
	public Matrix transpose()
	{
		// Initialise new matrix with the dimensions of original matrix swapped
		Matrix result = new Matrix(this.y_dim,this.x_dim);
		
		for(int i = 0; i < this.x_dim; i++)
		{
			for(int j = 0; j < this.y_dim; j++)
			{
				result.matrix[j][i] = this.matrix[i][j];
			}
		}

		
		return result;
	}
	
	/*
	 * Method to calculate the inverse of this matrix
	 * The inverse is a matrix that when multiplied by
	 * the original matrix will produce an identity matrix
	 */
	public Matrix inverse()
	{
		Matrix result = new Matrix(this.x_dim,this.y_dim);
		
		if(this.x_dim != this.y_dim)
		{
			throw new RuntimeException("Error: Cannot find the inverse of a matrix not in the form (n x n)");
		}
		
		// Find determinant
		double det = determinant(this.matrix,this.matrix.length);
		if(det == 0)
		{
			System.out.println("No inverse can be found!");
			return null;
		}
		
		// Find adjoint
		double[][] adj = new double[this.matrix.length][this.matrix.length];
		adjoint(this.matrix,adj);
		
		// Find inverse using formula inverse(Matrix) = adj(Matrix) / det(Matrix)
		for(int i = 0; i < this.x_dim; i++)
		{
			for(int j = 0; j < this.y_dim; j++)
			{
				result.matrix[i][j] = adj[i][j]/det;
			}
		}
		
		return result;
	}
	
	private double determinant(double[][] matrix, int n)
	{
		double det = 0;
		
		if(n == 1)
		{
			return matrix[0][0];
		}
		
		double[][] temp = new double[matrix.length][matrix.length];
		
		int sign = 1; // Start with positive sign
		
		for(int i = 0; i < matrix.length; i++)
		{
			// Get the cofactor
			cofactor(matrix,temp,0,i,n);
			det += sign * matrix[0][i] * determinant(temp, n-1);
			
			// terms to be added with alternative sign, thus switch sign
			sign = -sign;
		}
		
		
		return det;
	}
	
	private void cofactor(double[][] matrix,double[][] temp, int p, int q, int n)
	{
		int i = 0;
		int j = 0;
		
		for(int row = 0; row < n; row++)
		{
			for(int col = 0; col < n; col++)
			{
				if(row != p && col != q)
				{
					temp[i][j++] = matrix[row][col];
					
					if(j == n - 1)
					{
						j = 0;
						i++;
					}
				}
			}
		}
	}
	
	private void adjoint(double[][] matrix, double[][] adj)
	{
		if(matrix.length == 1)
		{
			adj[0][0] = 1;
			return;
		}
		
		int sign = 1;
		double temp[][] = new double[matrix.length][matrix.length];
		
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix.length; j++)
			{
				cofactor(matrix,temp,i,j,matrix.length);
				
				sign = ((i+j)%2==0) ? 1: -1;
				
				adj[j][i] = (sign) * (determinant(temp, matrix.length-1));
			}
		}
		
	}
	
	public Matrix add(Matrix other)
	{
		Matrix result = new Matrix(this.x_dim,this.y_dim);
		
		// Check whether matrices have the same dimensions and are thus compatible for addition
		if(this.x_dim != other.x_dim || this.y_dim != other.y_dim)
		{
			throw new RuntimeException("Error: Matrices are not compatible for addition operation!");
		}
		
		for(int i = 0; i < this.x_dim; i++)
		{
			for(int j = 0; j < this.y_dim; j++)
			{
				result.matrix[i][j] = this.matrix[i][j] + other.matrix[i][j];
			}
		}
		
		return result;
	}
	
	public Matrix sub(Matrix other)
	{
		Matrix result = new Matrix(this.x_dim,this.y_dim);
		
		// Check whether matrices have the same dimensions and are thus compatible for subtraction
		if(this.x_dim != other.x_dim || this.y_dim != other.y_dim)
		{
			throw new RuntimeException("Error: Matrices are not compatible for subtraction operation!");
		}
		
		for(int i = 0; i < this.x_dim; i++)
		{
			for(int j = 0; j < this.y_dim; j++)
			{
				result.matrix[i][j] = this.matrix[i][j] - other.matrix[i][j];
			}
		}
		
		return result;
	}

	/*
	 * Print the matrix to console in
	 * matrix format
	 */
	public String toString()
	{
		String output = "["; // Store output as a string
		for(int i = 0; i < x_dim; i++)
		{
			for(int j = 0; j < y_dim; j++)
			{
				if(j == y_dim-1) // When reached end of row
				{
					if(i == x_dim -1)
					{
						output += matrix[i][j];
					}
					else
					{
						output += matrix[i][j] + "\n"; // Append new line
					}
					
				}
				else
				{
					output += matrix[i][j] + ","; // Append comma until end of row
				}
			}
		}
		
		output += "]";
		return output;
	}
	
	public int getXDim()
	{
		return x_dim;
	}
	
	public int getYDim()
	{
		return y_dim;
	}

}
