package com.Synchronization.classes;
	
	public class UpdateSMPSM_Patterns { // Simple block and unblock to a the common list that holds the malicious patterns
	    private boolean blocked = false;

        private static UpdateSMPSM_Patterns UpdateSMPSM_Patternsinstance = null;
	    
	    private UpdateSMPSM_Patterns(){
		}
		
		public static UpdateSMPSM_Patterns getInstance(){
			if(UpdateSMPSM_Patternsinstance == null){
				UpdateSMPSM_Patternsinstance = new UpdateSMPSM_Patterns();
			}
			return UpdateSMPSM_Patternsinstance;
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
	
	
	
	

