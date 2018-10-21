package com.Synchronization.classes;

public class JnetSynchronization { //Simple block-unblock for the use of the JnetPcap functions, because they are not thread safe.
	private boolean blocked = false;

    private static JnetSynchronization JnetInstance = null;
    
    private JnetSynchronization(){
	}
	
	public static JnetSynchronization getInstance(){
		if(JnetInstance == null){
			JnetInstance = new JnetSynchronization();
		}
		return JnetInstance;
	}
    
    public synchronized void blockJnet(String msg) throws InterruptedException {
    	while (blocked){
    		try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	blocked = true;
    }

    public synchronized void unblockJnet(String msg) throws InterruptedException{
    	blocked = false;
    	notifyAll();
    }

}
