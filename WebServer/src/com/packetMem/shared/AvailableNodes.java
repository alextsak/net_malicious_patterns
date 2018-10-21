package com.packetMem.shared;

public class AvailableNodes {
    public int Count;
    public String UID;
    
    public AvailableNodes(){}
    

    public AvailableNodes(int count, String uid) {
        
        Count=count;
        UID=uid;
    }


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

    public int getPropertyCount() {
        return 2;
    }

}