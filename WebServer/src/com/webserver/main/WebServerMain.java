package com.webserver.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import javax.xml.ws.Endpoint; 
import com.packetMem.shared.*;
import com.webserver.server.WebMonitorServiceImpl;
import com.adminpanel.gui.Panel;
import com.database.mysql.DBCommunication;
import com.database.mysql.Connector;

public class WebServerMain {

	public static volatile boolean keepRunning = true;
    @SuppressWarnings("resource")
	public static void main(String[] args) throws IOException { 
    	
    	System.out.println("Starting the program...");
    	try
		{
			//Catching the Ctrl-C 
			final Thread mainThread = Thread.currentThread();
			Runtime.getRuntime().addShutdownHook(new Thread() 
			{
				@Override
				public void run() 
				{
					keepRunning = false;
					try 
					{
						//asking to remove pc from Adder
						
					
						//action confirmed
						System.gc();
						mainThread.join();
						Connector.getInstance().closeConnection();
						System.out.println("Closing the Server...");
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
				}
			});
    	System.out.println("Reading from property file.");
    	Properties properties = new Properties();
    	File newfile;
        String create;
    	
    	try {
    			properties.load(new FileInputStream("Properties/config.properties"));//load to property file
    			PropertiesMemory.getInstance().setWebServerIp(properties.getProperty("WebServerIp"));
    			PropertiesMemory.getInstance().setWebServerPort(properties.getProperty("WebServerPort"));
    			PropertiesMemory.getInstance().setDbusername(properties.getProperty("dbusername"));
    			PropertiesMemory.getInstance().setDbpassword(properties.getProperty("dbpassword"));
    			PropertiesMemory.getInstance().setDburl(properties.getProperty("dburl"));
    			PropertiesMemory.getInstance().setGuiUser(properties.getProperty("guiUser"));
    			PropertiesMemory.getInstance().setGuiPass(properties.getProperty("guiPass"));
    			Connector.getInstance(	//ekkinhsh tou connector
    					properties.getProperty("dbusername"),
    					properties.getProperty("dbpassword"),
    					properties.getProperty("dburl"));
    			//System.out.println("Database : " +properties.getProperty("dburl")+" "+properties.getProperty("dbusername")+" "+properties.getProperty("dbpassword"));
    			if (Connector.getConnection() == null) {
    				System.err.println("Can't connect to database");
    			}
    			
    			
    			
                if ((create = (properties.getProperty("create"))) == null) { //Read sql file from config.properties
                   System.err.println("Create is missing from the property file.");
                }
                
                
                newfile = new File(create); //Create file with given from config.properties
                String sql = new Scanner(newfile).useDelimiter("\\Z").next();
                if(DBCommunication.executeSQL(sql)){ //Create database - clear tables
                  System.out.println("Database created successful at :");                        
                }
                else{
                    System.out.println("Database can't be created");
                   return ;
                }

    			//System.out.println(properties.getProperty("dbusername"));
    			System.out.println(properties.getProperty("dburl"));
    			//System.out.println(properties.getProperty("SleepTime"));
    		}
    	catch(IOException e){
    		//exception in property file
    	}
    	System.out.println("Creating Memory");
    	MPSMemory.getInstance().CreateMPSMemory();
    	ActiveNodes.getInstance().CreateNodesMemory();
    	System.out.println("Connecting to database");
		new DBCommunication();
		
    	System.out.println("Starting webservices at :");
		Endpoint.publish("http://" + properties.getProperty("WebServerIp") + ":" + properties.getProperty("WebServerPort") + "/WebServer/",
				new WebMonitorServiceImpl());
		System.out.println("http://" + properties.getProperty("WebServerIp") + ":" + properties.getProperty("WebServerPort") + "/WebServer/WebMonitorService?WSDL");
        //Endpoint.publish("http://"+  "0.0.0.0"+ ":" +  9999 +"/WebServer/", new WebMonitorServiceImpl()); 
		//http://0.0.0.0:9999/WebServer/WebMonitorService?WSDL
		System.out.println("Starting GUI");
		Panel.getInstance().StartPanel();
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
    }
}

