package com.packetThread.threads;

import com.Synchronization.classes.DevList;
import com.Synchronization.classes.JnetSynchronization;
import com.packet.monitor.IPTrafficMonitorTest;
import com.packetMem.shared.MPSMemory;
import com.packetMonitor.jnet.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.network.Ip4;

public class AddNiThread implements Runnable 
{
	//This class implements the Runnable and creates threads for every running device
	//It captures also every packet from the current interface and decodes it
 private static AddNiThread instance = null;
 private List<PcapIf> devList = new ArrayList<PcapIf>(); //A list for every device

 public List<PcapIf> getDevList() {
	return devList;
}
public void setDevList(List<PcapIf> devList) {
	this.devList = devList;
}

private List<Thread> threadList = new ArrayList<Thread>(); //A list for the interface threads
 public List<Thread> getThreadList() {
  return threadList;
 }
 public void setThreadList(List<Thread> threadList) {
  this.threadList = threadList;
 }

 //using the singleton pattern
 private AddNiThread() //avoid instantiation
 {}
 public static AddNiThread getInstance() 
 {
  if(instance == null) 
  {
   instance = new AddNiThread();
  }
     return instance;
 }
 public void ThreadCreator(PcapIf device) //Creates thread for the given device and adds it in the thread list
 {
	 //Furthermore adds the device in the devList
	 //Because the Thread List and the devList are shared resources we use synchronization
	 
	 //System.out.println("The Thread Creator");
	 Thread NiTask;
	 DevList dlist = DevList.getInstance();
	 // System.out.println("Blocking for adding device in DevList");
	 //block devlist to add device
	 try {
	  dlist.wblock();
	 } catch (InterruptedException ex) {
		ex.printStackTrace();
	 }
	 devList.add(device);
	 //System.out.println("Unblocking for adding " + device.getName() + " in DevList");
	 //unblock the devList for writing
	 try {
	  dlist.wunblock();
	 } catch (InterruptedException ex) {
		 ex.printStackTrace();
	 }
	 NiTask = new Thread(this);
	 NiTask.setName(device.getName());
	 //System.out.printf("New Thread: %s started\n", NiTask.getName());
	 ThreadListBlocks tlb = ThreadListBlocks.getInstance();
	 //System.out.println("Blocking for adding " + NiTask.getName() + " in ThreadList");
	 //unblock thread list
	 tlb.runblock();
	 //block thread list for adding new thread
	 try {
	  tlb.wblock();
	 } catch (InterruptedException ex) {
		 ex.printStackTrace();
	 }
  
	 threadList.add(NiTask);
	 //System.out.println("Unblocking for adding " + NiTask.getName() + " in ThreadList");
	 //unblock thread list for writing
	 try {
		tlb.wunblock();
	 } catch (InterruptedException ex) {
		 ex.printStackTrace();
	 }
	 //start the thread...
	 NiTask.start();
}
 
 
 //Method run has the code that the thread will implement
 @Override
 public void run() 
 {
	 final JnetSynchronization jnet = JnetSynchronization.getInstance();
	 //System.out.printf("Ni nameThread: %s started\n", Thread.currentThread().getName());
	 //Block in order to read the device
	 try {
		 jnet.blockJnet("Before get devices");
	 } catch (InterruptedException e1) {
		 e1.printStackTrace();
	 }
  
  	String nameOfthread = Thread.currentThread().getName();
    PcapIf tempDev = null;
    for(PcapIf dev : devList )
    {
    	if(dev.getName().equals(nameOfthread))
    	{
    		tempDev = dev;
    		break;
    	}
    }
    //initializing settings for capturing
    //System.out.printf("\nChoosing '%s' :\n", (tempDev.getDescription() != null) ? tempDev.getDescription() : tempDev.getName());  
    int snaplen = 64 * 1024;           // Capture all packets, no trucation  
    int flags = Pcap.MODE_PROMISCUOUS; // capture all packets  
    int timeout = 10 * 1000;           // 10 seconds in millis  
    StringBuilder errbuf = new StringBuilder(); // For any error messages 
    final AtomicBoolean finished = new AtomicBoolean(true); //An atomic flag for helping the thread to break from the loops
    final Pcap pcap = Pcap.openLive(tempDev.getName(), snaplen, flags, timeout, errbuf);
    System.out.println("Opening " + tempDev.getName() + " for capturing");
    if (pcap == null) 
    {  
    	System.err.printf("Error while opening device for capture: " + errbuf.toString());  
    	return;  
    }
    //unblock now that we got the device
    try {
    	jnet.unblockJnet("After get diveces");
    } catch (InterruptedException e1) {
    	e1.printStackTrace();
    }
    while(finished.get() == true){
  
    	final PcapIf tempDev1 = tempDev;
    	//handler for opening the packets and getting the payload
    	PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() 
    	{  
    		Ip4 ip4 = new Ip4();
    		byte[] sourceIP4 = new byte[4];
    		public void  nextPacket(PcapPacket packet, String user) //captures every packet
    		{  
    			//block jnet in order to read the payload
    			try {
    				jnet.blockJnet("Before Handler");
    			} catch (InterruptedException e1) {
    				e1.printStackTrace();
    			}
    			//if the user pressed Ctrl-C no packets should be printed
    			int show = 0;
    			if (IPTrafficMonitorTest.keepRunning && show == 1)
    			{
    				System.out.printf("%s : Received packet at %s caplen=%-4d len=%-4d %s\n",  
    				tempDev1.getName().toString(),
    				new Date(packet.getCaptureHeader().timestampInMillis()),   
                    packet.getCaptureHeader().caplen(),  // Length actually captured  
                    packet.getCaptureHeader().wirelen(), // Original length   
                    user                                 // User supplied object  
                    );
    			}
      
    			if (finished.get() == false) {
    				pcap.breakloop(); 
    			}
    			if(packet.hasHeader(ip4))
    			{
    				//if we have ipv4 datagramm 
    				//get the sourceIP of the packet and the payload
    				String deviceIP;
    				MPSMemory mpsmInstance = MPSMemory.getInstance();
    				sourceIP4 = packet.getHeader(ip4).source();
    				String sIP4 = org.jnetpcap.packet.format.FormatUtils.ip(sourceIP4);
    				deviceIP = DeviceDetails.getIPOfdev(tempDev1.getName()); // returns the ip of the interface
    				//check that the sourceIP is not our interface
    				if(!deviceIP.equals(sIP4))
    				{
    					//get payload from packet
    					if (ip4.hasPayload()) 
    					{
    						byte[] payload = ip4.getPayload();
    						try 
    						{
    							String strPayload = new String(payload, "UTF-8");
    							//search the ip and the string in MPSM 
    							mpsmInstance.SearchMPSMemory(tempDev1.getName(), deviceIP, strPayload, 0);
    							mpsmInstance.SearchMPSMemory(tempDev1.getName(), deviceIP, sIP4, 1);
    						} 
    						catch (UnsupportedEncodingException e) {
    							e.printStackTrace();
    						}
    					}
    				}
    			}
    			//unblock the jnet
    			try {
    				jnet.unblockJnet("After Handler");
    			} catch (InterruptedException e1) {
    				e1.printStackTrace();
    			} 
    		}
    	};
    	try {
	      jnet.unblockJnet("After Handler");
	     } catch (InterruptedException e1) {
	      e1.printStackTrace();
	     } 
    	//a while loop that calls the handler
    	//the loop stops if an error occurs(device disconnected)
    	//then the thread sleeps for 10 secs and catches the interrupt
    	//and breaks from the outer loop
    	while(finished.get() && pcap.loop(Pcap.LOOP_INFINITE, jpacketHandler, "") == Pcap.OK) {  
    		;;  
    	}
    	try
    	{
    		Thread.sleep(10000);
    	}
    	catch(InterruptedException ex)
    	{
    		System.out.println("Thread " + Thread.currentThread().getName() + " interrupted");
    		Thread.currentThread().interrupt();
    		pcap.breakloop();
    		
    	}
    	System.out.println("Closing the device from capture");
    	pcap.close();
    	finished.set(false);
  }
 }
}