package com.packetMem.shared;

import java.util.ArrayList;
import com.Synchronization.classes.*;

public class SMPSMemory {
	
	private ArrayList<SMPSM> StatMaliciousPatterns;
	private ArrayList<SMPSM> StatMaliciousIPs;
	private static SMPSMemory SMPSMinstance = null;
	
	public ArrayList<SMPSM> getStatMaliciousPatterns() {
		return StatMaliciousPatterns;
	}

	public void setStatMaliciousPatterns(ArrayList<SMPSM> statMaliciousPatterns) {
		StatMaliciousPatterns = statMaliciousPatterns;
	}

	public ArrayList<SMPSM> getStatMaliciousIPs() {
		return StatMaliciousIPs;
	}

	public void setStatMaliciousIPs(ArrayList<SMPSM> statMaliciousIPs) {
		StatMaliciousIPs = statMaliciousIPs;
	}
	
	private SMPSMemory(){
	}
	
	public int CreateSMPSMemory(){ //Creates Object and lists
		SMPSMemory SMPSMem = SMPSMemory.getInstance();
		SMPSMem.StatMaliciousIPs = new ArrayList<SMPSM>();
		SMPSMem.StatMaliciousPatterns = new ArrayList<SMPSM>();
		System.out.printf("Creating SMPSMemory Done.\n");
		return 0;
	}
	
	public static SMPSMemory getInstance(){
		if(SMPSMinstance == null){
			SMPSMinstance = new SMPSMemory();
		}
		return SMPSMinstance;
	}
	
	public void SMPSMInitialize(String device, String d_IP/*List of devices and their ips*/){ //Creates nodes in lists for given device
		MPSMemory MPSM = MPSMemory.getInstance();
		UpdateMPSM up_mem = UpdateMPSM.getInstance();
		UpdateSMPSM_Patterns up_pat = UpdateSMPSM_Patterns.getInstance();
		UpdateSMPSM_IPs up_ips = UpdateSMPSM_IPs.getInstance();
		try {
			up_mem.rblockMPSM();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// readers block to get MaliciousPatterns list
		System.out.printf("Given : Device: %s   IP: %s \n", device, d_IP);
		ArrayList<String> tempCol = MPSM.getMaliciousPatterns();
		up_mem.runblockMPSM();
		// readers unblock after getting MaliciousPatterns list
		try {
			up_pat.blockSMPSM();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//block for the use of SMPSMemory
		for(String temp : tempCol){
			SMPSM tempSMPSM = new SMPSM();
			tempSMPSM.setInterfaceName(device);
			tempSMPSM.setInterfaceIP(d_IP);
			tempSMPSM.setPattern(temp);
			tempSMPSM.setFrequency(0);
			StatMaliciousPatterns.add(tempSMPSM);
		}
		try {
			up_pat.unblockSMPSM();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//unblock after the use of SMPSMemory
		try {
			up_mem.rblockMPSM();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// readers block to get MaliciousIPs list
		ArrayList<String> tempCol1 = MPSM.getMaliciousIPs();
		up_mem.runblockMPSM();
		// readers unblock after getting MaliciousIPs list
		try {
			up_ips.blockSMPSM();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//block for the use of SMPSMemory
		for(String temp : tempCol1){
			SMPSM tempSMPSM = new SMPSM();
			tempSMPSM.setInterfaceName(device);
			tempSMPSM.setInterfaceIP(d_IP);
			tempSMPSM.setPattern(temp);
			tempSMPSM.setFrequency(0);
			StatMaliciousIPs.add(tempSMPSM);
		}
		try {
			up_ips.unblockSMPSM();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//unblock after the use of SMPSMemory
	}
	
	public ArrayList<String> SMPSMprint(int option /* 0: Patterns 1:IP */){ //Prints out all the nodes of the list
		ArrayList<String> templist = new ArrayList<String>();
		if (option == 0){
			UpdateSMPSM_Patterns up_pat = UpdateSMPSM_Patterns.getInstance();
			try {
				up_pat.blockSMPSM();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(SMPSM temp : StatMaliciousPatterns){
				System.out.printf("%s", temp.toString());
				templist.add(temp.toString());
			}
			try {
				up_pat.unblockSMPSM();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else{
			UpdateSMPSM_IPs up_ips = UpdateSMPSM_IPs.getInstance();
			try {
				up_ips.blockSMPSM();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(SMPSM temp : StatMaliciousIPs){
				System.out.printf("%s", temp.toString());
				templist.add(temp.toString());
			}
			try {
				up_ips.unblockSMPSM();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    }
		return templist;
	}
	
	public void SMPSMRemoveDevice(String device, int option /* 0: Patterns 1:IP */){ //Removes given Device's nodes from the lists
		if (option == 0){
			UpdateSMPSM_Patterns up_pat = UpdateSMPSM_Patterns.getInstance();
			try {
				up_pat.blockSMPSM();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(int i = StatMaliciousPatterns.size() - 1; i >= 0; i--){
				if(StatMaliciousPatterns.get(i).getInterfaceName().equals(device)){
					StatMaliciousPatterns.remove(i);
				}
			}
			try {
				up_pat.unblockSMPSM();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else{
			UpdateSMPSM_IPs up_ips = UpdateSMPSM_IPs.getInstance();
			try {
				up_ips.blockSMPSM();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(int i = StatMaliciousIPs.size() - 1; i >= 0; i--){
				if(StatMaliciousIPs.get(i).getInterfaceName().equals(device)){
					StatMaliciousIPs.remove(i);
				}
			}
			try {
				up_ips.unblockSMPSM();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void SMPSMUpdatePatterns(String device, String d_IP, String Pattern, int count){//Updates the Malicious Patterns list
		int found =0;
		UpdateSMPSM_Patterns up_pat = UpdateSMPSM_Patterns.getInstance();
		
		try {
			up_pat.blockSMPSM();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(SMPSM temp : StatMaliciousPatterns){ //increase pattern's frequency
			if(device.equals(temp.getInterfaceName()) && d_IP.equals(temp.getInterfaceIP()) && Pattern.equals(temp.getPattern())){
					temp.setFrequency(temp.getFrequency()+count);
					found = 1;
					//System.out.printf("%s", temp.toString());
					break;
				}
			}
			SMPSM tempSMPSM = new SMPSM();
			tempSMPSM.setInterfaceName(device);
			tempSMPSM.setInterfaceIP(d_IP);
			tempSMPSM.setPattern(Pattern);
			tempSMPSM.setFrequency(count);
			
			if(found == 0)
			{
				for(SMPSM temp : StatMaliciousPatterns){ //add new pattern
					if(device.equals(temp.getInterfaceName()) && d_IP.equals(temp.getInterfaceIP())){
						int pos = StatMaliciousPatterns.indexOf(temp);
						StatMaliciousPatterns.add(pos, tempSMPSM);
						found = 1;
						//System.out.printf("%s", temp.toString());
						break;
					}
				}
				if(found == 0){ //add new device ip
					for(SMPSM temp : StatMaliciousPatterns){
						if(device.equals(temp.getInterfaceName())){ 
							int pos = StatMaliciousPatterns.indexOf(temp);
							StatMaliciousPatterns.add(pos, tempSMPSM);
							found = 1;
							//System.out.printf("%s", temp.toString());
							break;
						}
					}
				}
				
				if(found == 0){ //add new device
					//Must add for every other pattern frequency 0
					StatMaliciousPatterns.add(tempSMPSM);
					try {
						up_pat.unblockSMPSM();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					SMPSMInitialize(tempSMPSM.getInterfaceName(), tempSMPSM.InterfaceIP);
					found = 1;
					//System.out.printf("%s", temp.toString());
				}
			}
			try {
				up_pat.unblockSMPSM();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	
	public void SMPSMUpdateIPs(String device, String d_IP, String pat, int count){//Updates Malicious IPs list
		int found =0;
		
		UpdateSMPSM_IPs up_ips = UpdateSMPSM_IPs.getInstance();
		
		try {
			up_ips.blockSMPSM();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
			for(SMPSM temp : StatMaliciousIPs){ //increase pattern's frequency
				if(device.equals(temp.getInterfaceName()) && d_IP.equals(temp.getInterfaceIP()) && pat.equals(temp.getPattern())){
					temp.setFrequency(temp.getFrequency()+count);
					found = 1;
					//System.out.printf("%s", temp.toString());
					break;
				}
			}
			SMPSM tempSMPSM = new SMPSM();
			tempSMPSM.setInterfaceName(device);
			tempSMPSM.setInterfaceIP(d_IP);
			tempSMPSM.setPattern(pat);
			tempSMPSM.setFrequency(count);
			
			if(found == 0){
				for(SMPSM temp : StatMaliciousIPs){ //add new pattern
					if(device.equals(temp.getInterfaceName()) && d_IP.equals(temp.getInterfaceIP())){
						int pos = StatMaliciousIPs.indexOf(temp);
						StatMaliciousIPs.add(pos, tempSMPSM);
						found = 1;
						//System.out.printf("%s", temp.toString());
						break;
					}
				}
				if(found == 0){ //add new device ip
					for(SMPSM temp : StatMaliciousIPs){
						if(device.equals(temp.getInterfaceName())){ 
							int pos = StatMaliciousIPs.indexOf(temp);
							StatMaliciousIPs.add(pos, tempSMPSM);
							found = 1;
							//System.out.printf("%s", temp.toString());
							break;
						}
					}
				}
				
				if(found == 0){ //add new device
					//Must add for every other pattern frequency 0
					StatMaliciousPatterns.add(tempSMPSM);
					try {
						up_ips.unblockSMPSM();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					SMPSMInitialize(tempSMPSM.getInterfaceName(), tempSMPSM.InterfaceIP);
					found = 1;
					//System.out.printf("%s", temp.toString());
				}
			}
			try {
				up_ips.unblockSMPSM();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	
	class SMPSM {
		private String InterfaceName;
		private String InterfaceIP;
		private String Pattern;
		private Integer Frequency;
		
		public String getInterfaceName() {
			return InterfaceName;
		}
		public void setInterfaceName(String interfaceName) {
			InterfaceName = interfaceName;
		}
		public String getInterfaceIP() {
			return InterfaceIP;
		}
		public void setInterfaceIP(String interfaceIP) {
			InterfaceIP = interfaceIP;
		}
		public String getPattern() {
			return Pattern;
		}
		public void setPattern(String pattern) {
			Pattern = pattern;
		}
		public Integer getFrequency() {
			return Frequency;
		}
		public void setFrequency(Integer frequency) {
			Frequency = frequency;
		}
		
		@Override
		public String toString() {
			String str = "";
			str += "Device : " + this.InterfaceName + " | ";
			str += "Device IP : " + this.InterfaceIP + " | ";
			str += "Pattern : " + this.Pattern + " | ";
			str += "Frequency : " + this.Frequency + "\n";
			return str;
		}
		
	}
}