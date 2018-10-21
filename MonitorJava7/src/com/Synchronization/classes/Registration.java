package com.Synchronization.classes;

public class Registration { //Simple block-unblock so that registration is completed and then the packet sending to the adder can commence
	private static Registration Registrationinstance = null;
	String UIDRegistered = null;
	boolean pcRegistered = false;
	
	private Registration(){
	}
	
	public static Registration getInstance(){
		if(Registrationinstance == null){
			Registrationinstance = new Registration();
		}
		return Registrationinstance;
	}

	public synchronized String WaitingReg(){
		while (!pcRegistered) {
			//System.out.printf("%s Waiting PC Register to complete its registration\n",Thread.currentThread().getName());
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
		return UIDRegistered;
	}
	
	public synchronized void ReadyReg(String UID){
		pcRegistered = true;
		System.out.println("PC Registration Ready\n");
		UIDRegistered = UID;
		notifyAll();
	}
}
