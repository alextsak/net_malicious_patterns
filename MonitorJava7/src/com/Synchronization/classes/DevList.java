package com.Synchronization.classes;


public class DevList { //Reader Writers block for a common list between threads
	boolean wblocked = false;
    boolean rblocked = false;
    private int nreaders = 0;
    private int nwriters = 0;
    private int writeRequests = 0;
    private static DevList DevListInstance = null;
    
    private DevList(){
	}
	
	public static DevList getInstance(){
		if(DevListInstance == null){
			DevListInstance = new DevList();
		}
		return DevListInstance;
	}

	
	 public synchronized void wblock() throws InterruptedException {
	    	writeRequests++;
	    	while (nreaders > 0 || nwriters > 0){
	    		try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    	}
	    	writeRequests--;
	    	nwriters++;
	    }

	    public synchronized void wunblock() throws InterruptedException{
	    	nwriters--;
	    	notifyAll();
	    }
	    
	    public synchronized void runblock() {
	    	nreaders--;
	    	notifyAll();
	    }
	    
	    public synchronized void rblock() throws InterruptedException{
	    	while (nwriters > 0 || writeRequests > 0){
	    		try {
					wait();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
	    	}
	    	nreaders = nreaders + 1;
	    }

}