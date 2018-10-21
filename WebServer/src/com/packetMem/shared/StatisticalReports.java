package com.packetMem.shared;

public class StatisticalReports{

	public String UID = null;
	public String Local_Time = null;
	public String Device = null;
	public String DeviceIP = null;
	public String Pattern = null;
	public String Frequency = null;
	
	public StatisticalReports(){}
	
	public StatisticalReports(String uid, String local_time, String device, String deviceip, String pattern, String frequency){
		UID=uid;
		Local_Time=local_time;
		Device=device;
		DeviceIP=deviceip;
		Pattern=pattern;
		Frequency=frequency;
	}
	
	public String[] getValues(int index) {
		String[] templist = null;
		switch(index)
        {
        case 0:
        	templist = UID.split("#");
        	return templist;
        case 1:
        	templist = Device.split("#");
        	return templist;
        case 2:
        	templist = DeviceIP.split("#");
        	return templist;
        case 3:
        	templist = Pattern.split("#");
        	return templist;
        case 4:
        	templist = Frequency.split("#");
        	return templist;
        default:return null;
        }
	}
	
}
