package com.Synchronization.classes;

public class MemoryCreation { //Simple block-unblock so that memory creation is completed and then the others thread can continue working
	private static MemoryCreation MemoryCreationinstance = null;
	boolean memready = false;
	
	private MemoryCreation(){
	}
	
	public static MemoryCreation getInstance(){
		if(MemoryCreationinstance == null){
			MemoryCreationinstance = new MemoryCreation();
		}
		return MemoryCreationinstance;
	}
	
	public synchronized void Waiting(){
		while (!memready) {
			//System.out.printf("%s Waiting Memory to be created\n",Thread.currentThread().getName());
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	}
	
	public synchronized void ReadyMem(){
		memready = true;
		System.out.println("Memory Ready\n");
		notifyAll();
	}
	
}
