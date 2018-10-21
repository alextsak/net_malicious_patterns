package com.packetMem.shared;


public class PropertiesMemory {

	private static PropertiesMemory memory;
	private  Object lock1;
	
	private PropertiesMemory(){
		lock1 = new Object();
		
	}
	public static PropertiesMemory getInstance() {
		if (memory == null) {
			synchronized (PropertiesMemory.class) {
				memory = new PropertiesMemory();
			}
		}
		return memory;
	}
	
	public Object getLock1() {
		return lock1;
	}
	public void setLock1(Object lock1) {
		this.lock1 = lock1;
	}
	
}