package com.Synchronization.classes;

public class UpdateSMPSM_IPs { // Simple block and unblock to a the common list that holds the malicious ips
    boolean blocked = false;
    private static UpdateSMPSM_IPs UpdateSMPSM_IPsinstance = null;
    
    private UpdateSMPSM_IPs(){
	}
	
	public static UpdateSMPSM_IPs getInstance(){
		if(UpdateSMPSM_IPsinstance == null){
			UpdateSMPSM_IPsinstance = new UpdateSMPSM_IPs();
		}
		return UpdateSMPSM_IPsinstance;
	}
	
	 public synchronized void blockSMPSM() throws InterruptedException {
	    	while (blocked){
	    		try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    	}
	    	blocked = true;
	    }

	    public synchronized void unblockSMPSM() throws InterruptedException{
	    	blocked = false;
	    	notifyAll();
	    }
}