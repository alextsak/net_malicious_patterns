package com.packetIO.stream;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReadWriteTextFileJDK8 {
  
  final static Charset ENCODING = StandardCharsets.UTF_8;
  
  //For smaller files

  /**
   Note: the javadoc of Files.readAllLines says it's intended for small
   files. But its implementation uses buffering, so it's likely good 
   even for fairly large files.
  */  
  public static List<String> readSmallTextFile(String aFileName) throws IOException {
    Path path = Paths.get(aFileName);
    return Files.readAllLines(path, ENCODING);
    //read every line from given file
  }
  
  public static void writeSmallTextFile(List<String> aLines, String aFileName) throws IOException {
    Path path = Paths.get(aFileName);
    Files.write(path, aLines, ENCODING);
    //write every line to given file
  }

  //For larger files
  
  public static ArrayList<String> readLargerTextFile(String aFileName) throws IOException {
    Path path = Paths.get(aFileName);
    ArrayList<String> temp = new ArrayList<String>();
    try (BufferedReader reader = Files.newBufferedReader(path, ENCODING)){
      String line = null;
      while ((line = reader.readLine()) != null) {
        //process each line : add it to a list which is returned at the end
    	temp.add(line);
        //log(line);
      }
    }
    return temp;
  }
  
  public static void writeLargerTextFile(String aFileName, List<String> aLines) throws IOException {
    Path path = Paths.get(aFileName);
    try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)){
      for(String line : aLines){
        writer.write(line);
        writer.newLine();
        //write every line to given file
      }
    }
  }

  public static void log(Object aMsg){
    System.out.println(String.valueOf(aMsg));
  }
  
} 