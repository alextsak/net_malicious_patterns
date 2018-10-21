package com.packetMem.shared;

import java.util.ArrayList;

public class MaliciousPatterns {

	private ArrayList<String> NewPatterns = new ArrayList<String>();
	private int ipposition = 0;

	public ArrayList<String> getNewPatterns() {
		return NewPatterns;
	}

	public void setNewPatterns(ArrayList<String> newPatterns) {
		NewPatterns = newPatterns;
	}
	
	public void addPatternNode(String pat, int pos){ //Adds given pattern to NewPatterns and updates the position of the first ip pattern
		NewPatterns.add(pat);
		setIpposition(pos);
	}

	public int getIpposition() {
		return ipposition;
	}

	public void setIpposition(int ipposition) {
		this.ipposition = ipposition;
	}
}
