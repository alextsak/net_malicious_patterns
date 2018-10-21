package com.packetMem.shared;


import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

/*
 * This class is received via the ksoap2 from server
 */

public class StatisticalReports implements KvmSerializable{

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
	
	@Override
	public Object getProperty(int arg0) {
		switch(arg0)
        {
        case 0:
            return UID;
        case 1:
            return Local_Time;
        case 2:
            return Device;
        case 3:
            return DeviceIP;
        case 4:
            return Pattern;
        case 5:
            return Frequency;
        }
        
        return null;
	}
	@Override
	public int getPropertyCount() {
		return 6;
	}
	@Override
	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		switch(index)
        {
        case 0:
        	info.type = PropertyInfo.STRING_CLASS;
            info.name = "UID";
            break;
        case 1:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "Local_Time";
            break;
        case 2:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "Device";
            break;
        case 3:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "DeviceIP";
            break;
        case 4:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "Pattern";
            break;
        case 5:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "Frequency";
            break;
        default:break;
        }
		
	}
	@Override
	public void setProperty(int index, Object value) {
		switch(index)
        {
        case 0:
        	UID=value.toString();
            break;
        case 1:
            Local_Time=value.toString();
            break;
        case 2:
            Device=value.toString();
            break;
        case 3:
            DeviceIP=value.toString();
            break;
        case 4:
            Pattern=value.toString();
            break;
        case 5:
            Frequency=value.toString();
            break;
        default:break;
        }
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
