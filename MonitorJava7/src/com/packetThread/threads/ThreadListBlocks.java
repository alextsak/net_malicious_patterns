package com.packetThread.threads;


public class ThreadListBlocks { //Readers Writers block for the common list 'ThreadList' between threads
	boolean wblocked = false;
    boolean rblocked = false;
    private int nreaders = 0;
    private int nwriters = 0;
    private int writeRequests = 0;
    private static ThreadListBlocks ThreadListBlocksinstance = null;
    
    private ThreadListBlocks(){
	}
	
	public static ThreadListBlocks getInstance(){
		if(ThreadListBlocksinstance == null){
			ThreadListBlocksinstance = new ThreadListBlocks();
		}
		return ThreadListBlocksinstance;
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