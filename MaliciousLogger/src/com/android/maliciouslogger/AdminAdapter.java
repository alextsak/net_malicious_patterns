package com.android.maliciouslogger;




import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;


/*
 * This class creates a FragmentPagerAdapter for the administrator and pass arguments to the fragments
 */

public class AdminAdapter extends FragmentPagerAdapter {  
	

	private Bundle args;
	

	public AdminAdapter(android.support.v4.app.FragmentManager fm, Bundle args) {  
        super(fm); 
        this.args = args;
   }  
   @Override  
   public android.support.v4.app.Fragment getItem(int arg0) {  
        switch (arg0) {  
        case 0:  
        	UserFragment userFrag = new UserFragment();
        	userFrag.setArguments(args);
        	return userFrag;  
        case 1:  
        	InterfacesFragment interFrag = new InterfacesFragment();
        	interFrag.setArguments(args);
        	return interFrag;   	
        case 2:
        	PatFragment patFrag = new PatFragment();
        	patFrag.setArguments(args);
        	return patFrag;  
        case 3:
        	DelFragment delFrag = new DelFragment();
        	delFrag.setArguments(args);
        	return delFrag;  
        
        default:  
             break;  
        }  
        return null;  
   }  
   
  
   
   @Override  
   public int getCount() {  // returns the count of the fragments
        // TODO Auto-generated method stub  
        return 4;  
   }  
   
   @Override
   public int getItemPosition(Object object) {  // returns the position of the fragment
	    return POSITION_NONE;
	}

}