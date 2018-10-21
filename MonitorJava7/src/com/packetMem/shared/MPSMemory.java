package com.packetMem.shared;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.Synchronization.classes.UpdateMPSM;
import com.packetIO.stream.ReadWriteTextFileJDK8;

public class MPSMemory {

	private ArrayList<String> MaliciousPatterns;
	private ArrayList<String> MaliciousIPs;
	private static MPSMemory MPSMinstance = null;
	
	public ArrayList<String> getMaliciousPatterns() {
		return MaliciousPatterns;
	}

	public void setMaliciousPatterns(ArrayList<String> maliciousPatterns) {
		MaliciousPatterns = maliciousPatterns;
	}

	public ArrayList<String> getMaliciousIPs() {
		return MaliciousIPs;
	}

	public void setMaliciousIPs(ArrayList<String> maliciousIPs) {
		MaliciousIPs = maliciousIPs;
	}
	
	private MPSMemory(){
	}
	
	public static MPSMemory getInstance(){
		if(MPSMinstance == null){
			MPSMinstance = new MPSMemory();
		}
		return MPSMinstance;
	}
	
	public void CreateMPSMemory(String fileInputIPs,String fileInputPat) throws IOException{
		//Reading lines from given files and adding them to each list
		MaliciousIPs = ReadWriteTextFileJDK8.readLargerTextFile(fileInputIPs);
		MaliciousPatterns = ReadWriteTextFileJDK8.readLargerTextFile(fileInputPat);
		ReadWriteTextFileJDK8.log(MaliciousIPs);
		ReadWriteTextFileJDK8.log(MaliciousPatterns);
		System.out.printf("Creating MPSMemory Done.\n");
	}
	
	public void SearchMPSMemory(String device, String d_IP, String pat, int option /* 0:pattern 1:IP*/){
		//Search for given pattern in MPSM lists. If found call SMPSMUpdate to update the SMPSMemory lists.
		UpdateMPSM up_mem = UpdateMPSM.getInstance();
		if(option == 1)
		{
			try 
			{
				up_mem.rblockMPSM();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			// readers block in order to read MaliciousIPs list
			for(String temp : MaliciousIPs){
				Pattern pattern = Pattern.compile(temp);
			    Matcher  matcher = pattern.matcher(pat);
		        int count = 0;
		        while (matcher.find())
		            count++;
		        if (count>0){
		        	SMPSMemory.getInstance().SMPSMUpdateIPs(device, d_IP, temp, count);
		        }
			}
			up_mem.runblockMPSM();
			// readers unblock after the read of MaliciousIPs list
		}
		else
		{
			try 
			{
				up_mem.rblockMPSM();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			// readers block in order to read MaliciousPatterns list
			for(String temp : MaliciousPatterns){
				Pattern pattern = Pattern.compile(temp);
			    Matcher  matcher = pattern.matcher(pat);
		        int count = 0;
		        while (matcher.find())
		            count++;
		        if (count>0){
		        	SMPSMemory.getInstance().SMPSMUpdatePatterns(device, d_IP, temp, count);
		        }
			}
			up_mem.runblockMPSM();
			// readers unblock after the read of MaliciousPatterns list
		}
	}

}