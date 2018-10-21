package com.packetStringFunctions.string;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class CheckUID {
	public static String checkForUID(String InputFile) 
	{
	//checks for a uid inside the file given  and returns a string with this unique id
	  String code = null;
	  try
	  {
	   File uidFile = new File(InputFile);
	   
	   if(uidFile.exists()) //if the file exists read the code
	   {  
		   FileReader fileReader = new FileReader(uidFile);
		   BufferedReader bufferedReader = new BufferedReader(fileReader);
		   if (uidFile.isDirectory()){ //if the string is a directory create the input_id.txt inside this directory
			   uidFile = new File(InputFile+"input_id.txt");
			   uidFile.createNewFile();
		   }
		   if (bufferedReader.readLine() == null){ //if the file is empty
			   UUID id = UUID.randomUUID(); // creating id for registering the PC to the Adder
			    FileWriter fileWriter = null;
			    try {
			    	fileWriter = new FileWriter(uidFile);
			    	fileWriter.write(id.toString());
			    } catch (IOException e) {
			     e.printStackTrace();
			    }
		   }
	    StringBuffer buf = new StringBuffer();
	    String line = null;
	    Path path = Paths.get(InputFile);
	    try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
	    	//append the stringBuffer with the contents of the file
	      while ((line = reader.readLine()) != null) {
	        //process each line in some way
	    	  buf.append(line);
	 	      buf.append("\n");
	      }  
	    }
	    code = buf.toString();
	    fileReader.close();
	   }
	   else // the file doesn't exists so create one...
	   {
	    UUID id = UUID.randomUUID(); 
	    FileWriter fileWriter = null;
	    try {
	    	uidFile.createNewFile();
	    	fileWriter = new FileWriter(uidFile);
	    	fileWriter.write(id.toString());
	    	fileWriter.close();
	    } catch (IOException e) {
	     e.printStackTrace();
	    }
	    code = id.toString();
	   }
	   
	  }
	  catch (IOException e) {
	   e.printStackTrace();
	  } 
	  return code;
	 }

}