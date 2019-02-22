package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CSVWriter {
	
	// Cosntants
	private static final String DELIMITER = ",";
	
	/*
	 * Method to create a new CSV file with the
	 * appropriate headers
	 */
	
	public static void createCSV(String filename, String[] header)
	{
		File csv = new File(filename + ".csv");
		if(!csv.exists())
		{
			// Create CSV if the file does not already exist
			// Write the listed headers to the first row
			try(PrintWriter writer = new PrintWriter(csv))
			{
				StringBuilder builder = new StringBuilder();
				for(int i = 0; i < header.length; i++)
				{
					builder.append(header[i] + DELIMITER); // Append each header plus the CSV delimiter
					
				}
				builder.append("\n"); // Append new line to complete the row for the header
				
				
				writer.write(builder.toString());
				
				writer.close();
				
				System.out.println("CSV file " + filename + " created successfully!");
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("File of path: " + filename + " already exists!");
		}
	}
	
	/*
	 * Method to append a single row of data
	 * which can be specified as an object array
	 */
	public static void appendRow(File file, Object[] data)
	{
		if(file.exists())
		{
			try(FileWriter writer = new FileWriter(file,true))
			{
				StringBuilder builder = new StringBuilder();
				
				for(int i = 0; i < data.length; i++)
				{
					builder.append(data[i].toString() + ",");
				}
				
				builder.append("\n");
				
				writer.write(builder.toString());
				
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	

}
