package com.packetMonitor.jnet;
import com.Synchronization.classes.DevList;
import com.Synchronization.classes.JnetSynchronization;
import com.packetStringFunctions.string.StringFunctions;
import com.packetThread.threads.*;
import com.packetMem.shared.*;

import static com.packet.monitor.IPTrafficMonitorTest.keepRunning;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

public class JnetPcapMonitor 
{
	 
      public static void findDevices()
      {
    	//Searches for Network Interfaces and returns a list of them
    	//Then it goes inside a loop and checks whether a new network interface has been added or disconnected
    	//1)If a network interface has been added then it creates a new thread
    	//2)if a network interface has been disconnected it removes the thread from the list of the interface threads
    	//and it sends an interrupt to the disconnected thread to stop parsing packets 
    	  
        List<PcapIf> olddevs = new ArrayList<PcapIf>(); // Will be filled with NICs  
        StringBuilder errbuf = new StringBuilder(); // For any error messages 
        //Getting instances of Objects which will be used
        AddNiThread niInstance = AddNiThread.getInstance();
        SMPSMemory memInstance = SMPSMemory.getInstance(); 
		JnetSynchronization jnet = JnetSynchronization.getInstance();
		
		//Blocking for findAlldevs because it's not thread safe
		try {
			jnet.blockJnet("Before findalldevs");
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
        int r = Pcap.findAllDevs(olddevs, errbuf);  
        if (r == Pcap.NOT_OK || olddevs.isEmpty()) 
        {  
            System.err.printf("Can't read list of devices, error is %s", errbuf.toString());  
            return;  
        }  
        //Excluding any(interface) because it's a pseudo device that listens all other devices
        //and we need to listen every device separately
        for (Iterator<PcapIf> iter = olddevs.iterator(); iter.hasNext();) 
    	{
    	      PcapIf dev = iter.next();
    	      if (dev.getName().toString().equals("any")) 
    	      {
    	        iter.remove();
    	      }
    	}
        //Creating threads for the devices found
        for (PcapIf it : olddevs) 
        {  
        	niInstance.ThreadCreator(it);
        }
        Collection<PcapIf> oldcol = olddevs;//cast to a collection for better use
        try {
			jnet.unblockJnet("After findAllDevs");
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
        //unblocking the jnet
       //collect interface ip's for the devices without using jnetpcap but with the system's functions
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
		    	memInstance.SMPSMInitialize(deviceName,InterfaceIP);
		    	System.out.println("SMPSM Initialized");
		    }
		    
		  }
		} 
        catch (SocketException e) 
        {
			System.out.println(" (error retrieving network interface list)");
		}
        
        ThreadListBlocks tlb = ThreadListBlocks.getInstance();
        DevList dlist = DevList.getInstance();
        //loop until the user presses Ctrl-C
        while(keepRunning)
        {
        	//searching if an addition or an extraction of an interface has occurred 
        	try {
    			jnet.blockJnet("At while start");
    		} catch (InterruptedException e1) {
    			e1.printStackTrace();
    		}
        	List<PcapIf> tempdevs = new ArrayList<PcapIf>();
        	int t = Pcap.findAllDevs(tempdevs, errbuf);//tempdevs: a temporary device list 
            if (t == Pcap.NOT_OK || tempdevs.isEmpty()) 
            {  
                System.err.printf("Can't read list of devices, error is %s", errbuf.toString());  
                return;  
            }
        	Collection<PcapIf> tempcol = tempdevs; //cast to a collection for better use
        	//excluding any from the new device list
        	for (Iterator<PcapIf> iter = tempcol.iterator(); iter.hasNext();) 
        	{
        	      PcapIf dev = iter.next();
        	      if (dev.getName().toString().equals("any")) 
        	      {
        	        iter.remove();
        	      }
        	}
        	for (Iterator<PcapIf> iter = oldcol.iterator(); iter.hasNext();) 
        	{
        	      PcapIf dev = iter.next();
        	      if (dev.getName().toString().equals("any")) 
        	      {
        	        iter.remove();
        	      }
        	}
        	try {
				tlb.rblock();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	//check if a device has been disconnected
        	//check every thread(name), which are also the current devices with every device we got in tempcol 
        	Thread temp;
        	for(int j = niInstance.getThreadList().size() -1;j>=0;j--)
            {
                boolean found = false;
                temp = niInstance.getThreadList().get(j);
                for(PcapIf it : tempcol)
                {
                	if(it.getName().toString().equals(temp.getName().toString()))
                	{
                		found = true;
                		break;
                	}
                }
                if(!found) 
                {
                	//the device wasn't found in tempcol so it has been disconnected
                	System.out.printf("Device:%s has been Disconnected!!!\n",temp.getName().toString());
               		//check devlist and remove the device that has been disconnected
                	//block for read the devList
                	try {
                		dlist.rblock();
                	} catch (InterruptedException e) {
                		e.printStackTrace();
                	}
                	boolean founddev = false;
                	PcapIf forRemove = null;
                	for(PcapIf p : niInstance.getDevList())
                	{
                		if(p.getName().toString().equals(temp.getName().toString()))
                		{
                			founddev = true;
                			forRemove = p;
                		  	break;
                		}
                	}
                	if(founddev)
                	{
                		//System.out.println("Device has been found in devlist");
                		dlist.runblock(); //unblock read if we have found the device
                		//now block for write (remove the device)
                		try {
                			dlist.wblock();
                		} catch (InterruptedException ex) {
                		  	ex.printStackTrace();
                		}
                		  	
                		List<PcapIf> dl = niInstance.getDevList();
                		dl.remove(forRemove);
                		niInstance.setDevList(dl);
                		//System.out.println("After removing device  " + temp.getName() + " from DevList");
                		for(PcapIf p : niInstance.getDevList())
                    	{
                			System.out.println("Device: " + p.getName());
                    	}
                		try {
                		  	dlist.wunblock();
                		} catch (InterruptedException ex) {
                		    ex.printStackTrace();
                		}
                	}
                	dlist.runblock(); //unblock read for devList
                	//Remove the disconnected device from the SMPSM Memory in order to have up-to-dated statistics
                	SMPSMemory mem = SMPSMemory.getInstance();
                	mem.SMPSMRemoveDevice(temp.getName(),0);
                	mem.SMPSMRemoveDevice(temp.getName(),1);
                	//removing the thread, for the disconnected device, from the ThreadList
                	//unblock the thread list
                	tlb.runblock();
                	//block it for writing(removing thread)
            		try {
            			tlb.wblock();
            		} catch (InterruptedException ex) {
            			ex.printStackTrace();
            		}
            		List<Thread> tl = niInstance.getThreadList();
            		tl.remove(j);
            		niInstance.setThreadList(tl);
            		//unblock it from writing
            		try {
            			tlb.wunblock();
            		} catch (InterruptedException ex) {
            			ex.printStackTrace();
            		}
            		//block it again for read
            		try {
            			tlb.rblock();
            		} catch (InterruptedException e) {
            			e.printStackTrace();
            		}
            		//send an interrupt to the thread in order to stop the pcap.loop
            		temp.interrupt();
            		//System.out.println("Interrupt from jnetpcap was sended");            
            		break;
                }
            }
        	//check if a device has been added 
        	//make a thread for the new device
            for(PcapIf it : tempcol)
            {
            	boolean found = false;
            	for(Thread temp1 : niInstance.getThreadList())
            	{
            		if(it.getName().toString().equals(temp1.getName().toString()))
            		{
            			found = true;
            			break;
                	}
                }
            	if(!found) 
            	{
            		//a new device has been found(it doesn't exist in the threadList(by name))
            		String InterfaceIP;
                    //System.out.printf("Device: %s has been added!!\n",it.getName().toString());
            		//getting the interface ip
                    InterfaceIP = DeviceDetails.getIPOfdev(it.getName().toString());
                    try {
                    	jnet.unblockJnet("At while end");
                  	} catch (InterruptedException e1) {
                  		e1.printStackTrace();
                  	}
                    //make space for the new device
                    memInstance.SMPSMInitialize(it.getName(),InterfaceIP);
                    System.out.printf("Device: %s has been added!!\n",it.getName().toString());
                    //create a new thread for this device
                    niInstance.ThreadCreator(it);
                }
            }
            tlb.runblock();
            oldcol.clear();
            oldcol.addAll(tempcol); //update the main list of the devices
        	
          	try {
    			jnet.unblockJnet("At while end");
    		} catch (InterruptedException e1) {
    			e1.printStackTrace();
    		}
          	try
			{
				Thread.sleep(10000);
			}
			catch(InterruptedException e)
			{
				System.out.println("Thread interruption occured");
				e.printStackTrace();
			}	
        }
      }
}