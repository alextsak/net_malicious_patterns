package com.packet.monitor;

import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import com.webserver.server.WebMonitorService;
import com.webserver.server.WebMonitorServiceImplServiceLocator;


public class IPTrafficMonitorTest   
{
	
	public static volatile boolean keepRunning = true; //Checks if the user has pressed Ctrl+c
	private static String PCUID = null;
	private static WebMonitorServiceImplServiceLocator service = null;
	private static WebMonitorService server = null;
	private static int server_status = 1;
	private static String serverip = null;
	private static String serverport = null;

	public static String getServerip() {
		return serverip;
	}

	public static void setServerip(String serverip) {
		IPTrafficMonitorTest.serverip = serverip;
	}

	public static String getServerport() {
		return serverport;
	}

	public static void setServerport(String serverport) {
		IPTrafficMonitorTest.serverport = serverport;
	}

	public static int getServer_status() {
		return server_status;
	}

	public static void setServer_status(int server_status) {
		IPTrafficMonitorTest.server_status = server_status;
	}

	public String getPCUID() {
		return PCUID;
	}

	public static void setPCUID(String pCUID) {
		PCUID = pCUID;
	}

	public static WebMonitorServiceImplServiceLocator getService() {
		return service;
	}

	public static void setService(WebMonitorServiceImplServiceLocator service) {
		IPTrafficMonitorTest.service = service;
	}

	public static WebMonitorService getServer() {
		return server;
	}

	public static void setServer(WebMonitorService server) {
		IPTrafficMonitorTest.server = server;
	}

	public static void main(String[] args) 
	{
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
						System.out.println("");
						//action confirmed
						if (server_status == 1){
							System.out.println("Removing PC from Adder...");
							boolean unregistered = getServer().unregister(PCUID);
							if( unregistered == true){
								System.out.println("Successfully removed");
							}
							else{
								System.out.println("Already removed");
							}
							System.gc();
							mainThread.join();
						}
						else{
							System.gc();
						}
						System.out.println("Closing the Program...");
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					} catch (RemoteException e) {
						System.out.println("Communication with server failed");
					}
				}
			});
		
			System.out.println("Starting Monitoring");
			//Parsing and Checking the Arguments from the command line 
			try
			{
				int argc = 0;
				Properties properties = new Properties();
				properties.load(new FileInputStream("config.properties"));//load to property file
				String serverip, serverport;
				int sleeptime = 5000;
				serverip = properties.getProperty("WebServerIp");
				setServerip(serverip);
				serverport = properties.getProperty("WebServerPort");
				setServerport(serverport);
				sleeptime = Integer.parseInt(properties.getProperty("SleepTime"));
				
				for(String s : args)
			    {
			     System.out.println(s);
			     argc++;
			    }
				if (argc < 6){
					System.out.println("An argument is missing");
					return;
				}
				int ippos = -1 , patpos = -1, idpos = -1;
				for(argc = 0; argc < 6; argc++){
					if (args[argc].equals("-ip")){
						ippos = argc+1;
						File uidFile = new File(args[ippos]);
						if(!uidFile.exists()) //if the file does not exist close
						  {  
							System.out.println("File of ips does not exist");
							return;
						  }
					}
					if (args[argc].equals("-pat")){
						patpos = argc+1;
						File uidFile = new File(args[patpos]);
						if(!uidFile.exists()) //if the file does not exist close
						  {  
							System.out.println("File of patterns does not exist");
							return;
						  }
					}
					if (args[argc].equals("-id")){
						idpos = argc+1;
					}
				}
				if (ippos == -1 || patpos == -1 || idpos == -1){
					System.out.println("Wrong Arguments");
					System.out.println("Correct example of given arguments : -ip 'ip_patterns file' -pat 'payload_patterns file' -id 'pc id file'");
				}
			    
				//Creating the Main Threads
				
				server_status = 1;
				System.out.println("Trying to connect to "+serverip+" "+serverport);
				System.out.println("");
				try {
					setService(new WebMonitorServiceImplServiceLocator("http://"+serverip+":"+serverport+"/WebServer/WebMonitorService?WSDL", new QName(
						    "http://server.webserver.com/", "WebMonitorServiceImplService")));
					setServer(getService().getWebMonitorServiceImplPort());
				    server_status = 1;
				} catch (ServiceException e) {
					System.out.println("Server is down");
					server_status = 0;
					e.printStackTrace();
				}
			    
			    if (server_status==0){
			    	System.exit(-1);
			    }
			    else{
				
			    	IPTrafficMonitor shareMemsTask = new IPTrafficMonitor("shareMemsTask");
			    	shareMemsTask.setFileInputIps(args[ippos]);
			    	shareMemsTask.setFileInputPat(args[patpos]);
			    	Thread smt = new Thread(shareMemsTask);
			    	//System.out.println("Starting " + smt.getName() + "...");
			    	smt.setName("shareMemsTask");
			    	smt.start();
			    	//System.out.println("After the shareMemStart...");
			   
			    	IPTrafficMonitor discoverNiTask = new IPTrafficMonitor("discoverNiTask");
			    	Thread dnt = new Thread(discoverNiTask);
			    	//System.out.println("Starting " + dnt.getName() + "...");
			    	dnt.setName("discoverNiTask");
			    	dnt.start();
			      
			    	IPTrafficMonitor maliciousInfoTask = new IPTrafficMonitor("maliciousInfoTask");
				    Thread mit = new Thread(maliciousInfoTask);
				    maliciousInfoTask.setService(getService());
				    maliciousInfoTask.setServer(getServer());
				    maliciousInfoTask.setSleepTime(sleeptime);
				    //System.out.println("Starting " + mit.getName() + "...");
				    mit.setName("maliciousInfoTask");
				    mit.start();
				    
				    IPTrafficMonitor PCregister = new IPTrafficMonitor("PCregister");
				    PCregister.setUidFile(args[idpos]);
				    PCregister.setService(getService());
				    PCregister.setServer(getServer());
				    PCregister.setSleepTime(sleeptime);
				    Thread reg = new Thread(PCregister);
				    //System.out.println("Starting " + reg.getName() + "...");
				    reg.setName("PCregister");
				    reg.start();
				    
				    //System.out.println("Creating tasks completed, main ends.\n");
				    smt.join();
				    dnt.join();
				    mit.join();
				    reg.join();
			    }
			}
			catch(InterruptedException ex)
			{
				System.out.println(ex.toString());
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}