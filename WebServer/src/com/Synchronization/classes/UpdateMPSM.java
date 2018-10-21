package com.Synchronization.classes;

public class UpdateMPSM { //Reader Writers block for the common lists in MPSM that contains the malicious ips and patterns
    boolean wblocked = false;
    boolean rblocked = false;
    private int nreaders = 0;
    private int nwriters = 0;
    private int writeRequests = 0;
    private static UpdateMPSM UpdateMPSMinstance = null;
    
    private UpdateMPSM(){
	}
	
	public static UpdateMPSM getInstance(){
		if(UpdateMPSMinstance == null){
			UpdateMPSMinstance = new UpdateMPSM();
		}
		return UpdateMPSMinstance;
	}

	
	 public synchronized void wblockMPSM() throws InterruptedException {
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

	    public synchronized void wunblockMPSM() throws InterruptedException{
	    	nwriters--;
	    	notifyAll();
	    }
	    
	    public synchronized void runblockMPSM() {
	    	nreaders--;
	    	notifyAll();
	    }
	    
	    public synchronized void rblockMPSM() throws InterruptedException{
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