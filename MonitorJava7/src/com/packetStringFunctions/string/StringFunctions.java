package com.packetStringFunctions.string;

public class StringFunctions{
	
	public static String charElimination(String str) //eliminates space and replaces it with "/"
	{
		String res;
		res = str.replace("/", "");
		return res;
	}
	
	public static String stringSplitter(String str) //removes unwanted chars from the string i.e. ":"
	{
		String fstr=null;
		if (str.contains(" ")) 
		{
			String[] parts = str.split("\\s+");
			String part1 = parts[0]; 
			if(part1.contains(":"))
			{
				String[] res = part1.split(":");
				fstr = res[1];
			}
		} 
		else 
		{
		    throw new IllegalArgumentException("String " + str + " does not contain ....");
		}
		return fstr;
	}	
}