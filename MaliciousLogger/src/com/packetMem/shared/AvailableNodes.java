package com.packetMem.shared;

import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

/*
 * This class is tranfered via the ksoap2 to server
 */
public class AvailableNodes implements KvmSerializable
{
    public int Count;
    public String UID;
    
    public AvailableNodes(){}
    

    public AvailableNodes(int count, String uid) {
        
        Count=count;
        UID=uid;
    }

    @Override
    public Object getProperty(int arg0) {
        
        switch(arg0)
        {
        case 0:
            return Count;
        case 1:
            return UID;
        }
        
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 2;
    }

    @Override
    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
        switch(index)
        {
        case 0:
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "Count";
            break;
        case 1:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "UID";
            break;
        default:break;
        }
    }

    public void setProperty(int index, Object value) {
        switch(index)
        {
        case 0:
            Count = Integer.parseInt(value.toString());
            break;
        case 1:
            UID = value.toString();
            break;
        default:
            break;
        }
    }
}