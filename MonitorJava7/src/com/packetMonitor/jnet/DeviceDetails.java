package com.packetMonitor.jnet;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.packetStringFunctions.string.StringFunctions;

public class DeviceDetails 
{
	public static String getIPOfdev(String deviceName)
	{	//Returns the ip of the network interface for the name given
		String InterfaceIP = null;
		try 
        {
			NetworkInterface ni = NetworkInterface.getByName(deviceName);
			while (ni == null){
				ni = NetworkInterface.getByName(deviceName);
			}
		    Enumeration<InetAddress> IpAddr = ni.getInetAddresses(); //returns an enumeration of inet addresses for the given interface
		    while (IpAddr.hasMoreElements()) 
		    {
		    	//do some processing with the subset of the inet addresses 
		    	InterfaceIP = IpAddr.nextElement().toString();
		    	InterfaceIP = StringFunctions.charElimination(InterfaceIP);
		    	if(InterfaceIP.contains(":")) //ignore unwanted char
		    	{
		    		continue;
		    	}
		    }
		    
		  } 
        catch (SocketException e) 
        {
			System.out.println(" (error retrieving network interface list)");
		}
        return InterfaceIP;
	}
	
}