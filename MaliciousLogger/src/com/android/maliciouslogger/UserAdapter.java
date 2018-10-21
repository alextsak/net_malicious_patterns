package com.android.maliciouslogger;




import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;


/*
 * This class creates a FragmentPagerAdapter for the user and pass arguments to the fragments
 */

public class UserAdapter extends FragmentPagerAdapter {  
	
	private String Data;
	private Bundle args;
	
    public String getData() {
		return Data;
	}
	public void setData(String data) {
		Data = data;
	}
	
	
	
	public UserAdapter(android.support.v4.app.FragmentManager fm, Bundle args) {  
        super(fm); 
        this.args = args;
   }  
   @Override  
   public android.support.v4.app.Fragment getItem(int arg0) {  
        switch (arg0) {  
        case 0:  
        	UserFragment userFrag = new UserFragment();
        	Log.i("args",args.toString());
        	userFrag.setArguments(args);
        	return userFrag; 
        case 1:  
        	InterfacesFragment interFrag = new InterfacesFragment();
        	Log.i("args",args.toString());
        	interFrag.setArguments(args);
        	return interFrag;    	
        
        default:  
             break;  
        }  
        return null;  
   }  
   
   
   
   @Override  
   public int getCount() {  
        // TODO Auto-generated method stub  
        return 2;  
   }  
   
   @Override
   public int getItemPosition(Object object) {
	    return POSITION_NONE;
	}

}
