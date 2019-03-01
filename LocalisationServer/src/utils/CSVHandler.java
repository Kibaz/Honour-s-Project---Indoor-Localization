package utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CSVHandler {
	
	// Cosntants
	private static final String DELIMITER = ",";
	
	/*
	 * Method to create a new CSV file with the
	 * appropriate headers
	 */
	
	public static void createCSV(String filename, String[] header)
	{
		File csv = new File("files/" + filename + ".csv");
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
			// Do nothing - CSV already exists
		}
	}
	
	/*
	 * Method to read a CSV file
	 */
	public static String[][] readCSV(String filename)
	{
		BufferedReader br = null;
		List<String> values = new ArrayList<>();
		
		int numCols = 0;
		int numRows = 0;
		
		try {
			br = new BufferedReader(new FileReader("files/" + filename + ".csv"));
			String line = "";
			while((line = br.readLine()) != null) // While reader has not reached EOF
			{
				numRows++;
				String[] rowData = line.split(DELIMITER);
				for(int i = 0; i < rowData.length; i ++)
				{
					values.add(rowData[i]);
				}
				numCols = rowData.length;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[][] csvData = new String[numRows][numCols];
		int count = 0;
		for(int i = 0; i < numRows; i++)
		{
			for(int j = 0; j < numCols; j++)
			{
				csvData[i][j] = values.get(count);
				count++;
			}
		}
		
		return csvData;
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
	
	public static int rowCount(String filename)
	{
		InputStream inputStream;
		int count = 0;
		int readChars = 0;
		try {
			inputStream = new BufferedInputStream(new FileInputStream("files/"+ filename + ".csv"));
			byte[] chars = new byte[1024]; // Store read characters from file input stream
			while((readChars = inputStream.read(chars)) != -1)
			{
				for(int i = 0; i < readChars; i++)
				{
					if(chars[i] == '\n') // If the character is a new line
					{
						count++; // Increment counter
					}
				}
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count-1; // -1 As header is not included
	}
	
	

}
