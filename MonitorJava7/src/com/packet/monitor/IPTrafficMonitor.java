package com.packet.monitor;

import com.packetMem.shared.MPSMemory;
import com.packetMem.shared.SMPSMemory;
import com.packetMonitor.jnet.JnetPcapMonitor;
import com.packetStringFunctions.string.CheckUID;
import com.packetStringFunctions.string.StringFunctions;
import com.webserver.server.StatisticalReports;
import com.webserver.server.WebMonitorServiceImplServiceLocator;
import com.webserver.server.MaliciousPatterns;
import com.Synchronization.classes.*;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

import com.webserver.server.WebMonitorService;


public class IPTrafficMonitor implements Runnable
{
	private String threadName = null;
	private String fileInputIps = null;
	private String fileInputPat = null;
	private String uidFile = null;
	private String uidCode = null;
	private int SleepTime = 5000;
	private WebMonitorServiceImplServiceLocator service = null;
    private WebMonitorService server = null;
	 
	public int getSleepTime() {
		return SleepTime;
	}
	public void setSleepTime(int sleepTime) {
		SleepTime = sleepTime;
	}
    
	public WebMonitorServiceImplServiceLocator getService() {
		return service;
	}
	public void setService(WebMonitorServiceImplServiceLocator service) {
		this.service = service;
	}
	public WebMonitorService getServer() {
		return server;
	}
	public void setServer(WebMonitorService server) {
		this.server = server;
	}
	public String getUidCode() {
		return uidCode;
	}
	public void setUidCode(String uidCode) {
		this.uidCode = uidCode;
	}
	 
	 public String getUidFile() {
	  return uidFile;
	 }
	 public void setUidFile(String uidFile) {
	  this.uidFile = uidFile;
	 }
	 public String getFileInputIps() {
	  return fileInputIps;
	 }
	 public void setFileInputIps(String fileInputIps) {
	  this.fileInputIps = fileInputIps;
	 }
	 public String getFileInputPat() {
	  return fileInputPat;
	 }
	 public void setFileInputPat(String fileInputPat) {
	  this.fileInputPat = fileInputPat;
	 }
	 
	 public IPTrafficMonitor(String threadName){
	  this.threadName = threadName;
	 }
	 
	 
	 public String getThreadName() {
	  return threadName;
	 }


	 public void setThreadName(String threadName) {
	  this.threadName = threadName;
	 }


	@Override
	public void run()
	{
		MemoryCreation mem = MemoryCreation.getInstance();
		Registration reg = Registration.getInstance();
		
		if("discoverNiTask" == getThreadName()) //Here enters the thread which searches which device is active (is mounted and has an ip) or not 
		{
			mem.Waiting();
			System.out.println("The thread for discovering network interfaces started...");
			//System.out.println("I will call the JnetPcap...");
			JnetPcapMonitor.findDevices();
			
		}
		else if("shareMemsTask" == getThreadName()) //Here enters the thread which is responsible for the memory creation (MPSMemory and SMPSMemory)
		{
			System.out.println("The thread for creating the memory started...");
			//System.out.println("I will Create the shared Memory...");
			MPSMemory MPSMem = MPSMemory.getInstance();
			SMPSMemory SMPSMem = SMPSMemory.getInstance();
			try {
				MPSMem.CreateMPSMemory(getFileInputIps(),getFileInputPat());
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (SMPSMem.CreateSMPSMemory() != 0)
			{
				System.out.println("Problem Creating SMPSM Memory");
			}
			mem.ReadyMem();
		}
		else if("maliciousInfoTask" == getThreadName()){ //Here enters the thread which sends to and receives from the adder messages 
			setUidCode(reg.WaitingReg());
			System.out.println("The thread for communicating with the adder started...");
		    Timer timer = new Timer();
			timer.schedule(new Sendmsg(), SleepTime, SleepTime);
		}
		else if("PCregister" == getThreadName()){ //Here enters the thread responsible for the registration of the PC to the adder
			mem.Waiting();
			System.out.println("The thread PC Register for sending registration to the adder started...");
			try {
				System.out.println("Setting : " + getServer() + " " + getService());
				setUidCode(CheckUID.checkForUID(getUidFile()).split("\n")[0]);
				System.out.println("PC sends code: " + getUidCode());
				
				int count = 0, success = 0;
				while (success == 0 && count < 3){
					if (getServer().registerPC(getUidCode()) == false){
						count++;
						System.out.println("Error in Registration trying again with new uid. Attemp: "+count);
						try {
							File newUID = new File(getUidFile());
							newUID.delete();
						}catch(Exception e){
							System.out.println("Error Deleting file");
							e.printStackTrace();
						}
						setUidCode(CheckUID.checkForUID(getUidFile()));
						System.out.println("PC sends code: " + getUidCode());
					}
					else {
						success = 1;
					}
				}
				if (count == 3){
					System.out.println("Failed to register PC. Client is closing.");
					IPTrafficMonitorTest.keepRunning = false;
					System.exit(-3); //Registration failed
				}
			} catch (RemoteException e) {
				System.out.println("Connection with server lost at registration");
				IPTrafficMonitorTest.keepRunning = false;
				IPTrafficMonitorTest.setServer_status(0);;
				e.printStackTrace();
				System.exit(-2); //Connection to server lost
			}
			IPTrafficMonitorTest.setPCUID(getUidCode());
			reg.ReadyReg(getUidCode());
			
		}
	}
	
	class Sendmsg extends TimerTask { //Timer to print messages for the communication between the maliciousInfaTask and the adder
		
		//private WebMonitorServiceImplServiceLocator service;
	    //private WebMonitorService server;

		@Override 
	    public void run() {
			if (IPTrafficMonitorTest.keepRunning){
				System.out.println("Any News from " + IPTrafficMonitorTest.getServerip() + " for " + getUidCode() + " ?");
			    MaliciousPatterns temp;
			    String[] tempstring;
				try {
					
					temp = getServer().maliciousPatternsRequest(getUidCode());
					
					if (temp != null){
						if (temp.getIpposition()==-13){
							System.out.println("Node removed from server.");
							IPTrafficMonitorTest.keepRunning = false;
							IPTrafficMonitorTest.setServer_status(0);
							System.exit(-3); //Node removed from server
						}
						if(temp.getNewPatterns().length != 0){
							tempstring = temp.getNewPatterns();
							int lengthofarraylist;
							lengthofarraylist = temp.getNewPatterns().length;
							for(int i=0;i<lengthofarraylist;i++){
								if (i>=temp.getIpposition() && temp.getIpposition() != -1){
									try {
										UpdateMPSM.getInstance().wblockMPSM();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									MPSMemory.getInstance().getMaliciousIPs().add(tempstring[i]);
									try {
										UpdateMPSM.getInstance().wunblockMPSM();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									try 
							        {
									  String deviceName;
									  Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
									  while (en.hasMoreElements()) 
									  {
										//processing the interfaces for a clearer format
									    NetworkInterface InterfaceName = en.nextElement();
									    deviceName = InterfaceName.toString();
									    deviceName = StringFunctions.stringSplitter(deviceName);
									    Enumeration<InetAddress> IpAddr = InterfaceName.getInetAddresses();
									    String InterfaceIP;
									    while (IpAddr.hasMoreElements()) 
									    {
									    	InterfaceIP = IpAddr.nextElement().toString();
									    	InterfaceIP = StringFunctions.charElimination(InterfaceIP);
									    	if(InterfaceIP.contains(":"))
									    	{
									    		continue;
									    	}
									    	//initialize every device in SMPSM memory 
									    	SMPSMemory.getInstance().SMPSMUpdateIPs(deviceName, InterfaceIP, tempstring[i], 0);
									    	System.out.println("Pattern : "+tempstring[i]+" for : "+deviceName+" "+InterfaceIP+" received");
									    }
									    
									  }
									} 
							        catch (SocketException e) 
							        {
										System.out.println(" (error retrieving network interface list)");
									}
										
								}
								else{
									try {
										UpdateMPSM.getInstance().wblockMPSM();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									MPSMemory.getInstance().getMaliciousPatterns().add(tempstring[i]);
									try {
										UpdateMPSM.getInstance().wunblockMPSM();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									try 
							        {
									  String deviceName;
									  Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
									  while (en.hasMoreElements()) 
									  {
										//processing the interfaces for a clearer format
									    NetworkInterface InterfaceName = en.nextElement();
									    deviceName = InterfaceName.toString();
									    deviceName = StringFunctions.stringSplitter(deviceName);
									    Enumeration<InetAddress> IpAddr = InterfaceName.getInetAddresses();
									    String InterfaceIP;
									    while (IpAddr.hasMoreElements()) 
									    {
									    	InterfaceIP = IpAddr.nextElement().toString();
									    	InterfaceIP = StringFunctions.charElimination(InterfaceIP);
									    	if(InterfaceIP.contains(":"))
									    	{
									    		continue;
									    	}
									    	//initialize every device in SMPSM memory 
									    	SMPSMemory.getInstance().SMPSMUpdatePatterns(deviceName, InterfaceIP, tempstring[i], 0);
									    	System.out.println("Pattern : "+tempstring[i]+" for : "+deviceName+" "+InterfaceIP+" received");
									    }
									    
									  }
									} 
							        catch (SocketException e) 
							        {
										System.out.println(" (error retrieving network interface list)");
									}
								}
							}
						}
						else{
							System.out.printf("No News - Good News\n");
						}
					}
					else {
						System.out.printf("No News - Good News\n");
					}
				    StatisticalReports report = new StatisticalReports();
				    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					Date date = new Date();
					String local_time = dateFormat.format(date);
					report.setUID(getUidCode());
					report.setLocal_Time(local_time);
					ArrayList<String> s1 = SMPSMemory.getInstance().SMPSMprint(0);
					ArrayList<String> s2 = SMPSMemory.getInstance().SMPSMprint(1);
					int first=1;
					for(String treport : s1){
						String[] templist = treport.split(" ");
						if (first==1){
							first=0;
							report.setDevice(templist[2]);
							report.setDeviceIP(templist[7]);
							report.setPattern(templist[11]);
							report.setFrequency(templist[15]);
						}
						else{
							report.setDevice(report.getDevice()+"#"+templist[2]);
							report.setDeviceIP(report.getDeviceIP()+"#"+templist[7]);
							report.setPattern(report.getPattern()+"#"+templist[11]);
							report.setFrequency(report.getFrequency()+"#"+templist[15]);
						}
					}
					first=1;
					for(String treport : s2){
						String[] templist = treport.split(" ");
						if (first==1){
							first=0;
							report.setDevice(report.getDevice()+"#"+templist[2]);
							report.setDeviceIP(report.getDeviceIP()+"#"+templist[7]);
							report.setPattern(report.getPattern()+"#"+templist[11]);
							report.setFrequency(report.getFrequency()+"#"+templist[15]);
						}
						else{
							report.setDevice(report.getDevice()+"#"+templist[2]);
							report.setDeviceIP(report.getDeviceIP()+"#"+templist[7]);
							report.setPattern(report.getPattern()+"#"+templist[11]);
							report.setFrequency(report.getFrequency()+"#"+templist[15]);	
						}
					}
				    server.maliciousPatternsStatisticalReport(getUidCode(), report);
				} catch (RemoteException e1) {
					System.out.println("Connection with server lost at patterns update");
					IPTrafficMonitorTest.keepRunning = false;
					IPTrafficMonitorTest.setServer_status(0);
					System.exit(-2); //Connection to server lost
					e1.printStackTrace();
				}
			}
	    }
	 }

}
