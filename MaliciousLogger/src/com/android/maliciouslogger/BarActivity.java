package com.android.maliciouslogger;

import com.database.sqlite.DatabaseAdapter;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class BarActivity extends FragmentActivity implements ActionBar.TabListener{

	private ActionBar actionbar;   
    private ViewPager SwipePager;
    private UserAdapter usrAdapter;
    private int status;
    private AdminAdapter adAdapter;
    private DatabaseAdapter databaseAdapter;
    private String statusUser,statusPass,Nodes;
    private String fragInterfaces, PatFrag, DelFrag;
    private int filtered = 0; // for knowing if the user has pressed filter 
    
	public int getFiltered() {
		return filtered;
	}

	public void setFiltered(int filtered) {
		this.filtered = filtered;
	}

	
/***************** Setters-getters for the tags of every fragment ****************************************************/
	
	public String getPatFrag() {
		return PatFrag;
	}

	public void setPatFrag(String patFrag) {
		PatFrag = patFrag;
	}

	public String getDelFrag() {
		return DelFrag;
	}

	public void setDelFrag(String delFrag) {
		DelFrag = delFrag;
	}
	
	public void setfragInterfaces(String t){
	    	fragInterfaces = t;
	}
	    
	public String getfragInterfaces(){
	     return fragInterfaces;
	}
	
/*********** Setter-getter for the database adapter *****************************************/
	public DatabaseAdapter getDatabaseAdapter() {
		return databaseAdapter;
	}

	public void setDatabaseAdapter(DatabaseAdapter databaseAdapter) {
		this.databaseAdapter = databaseAdapter;
	}
    
   

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar_layout);
        
         // initialize the datababase adapter 
        databaseAdapter=new DatabaseAdapter(this);
		databaseAdapter=databaseAdapter.open();		
        filtered = 0;
        Log.d("BarActivity","onCreate");
        // get data from previous activity 
        Intent intent = getIntent();
        status = intent.getIntExtra("status",-1); // if status = 1 then it's the admin else simple user 
        statusUser = intent.getStringExtra("statusUser");
        statusPass = intent.getStringExtra("statusPass");
        Nodes = intent.getStringExtra("Nodes");
        
        // Start the service for updating 
        Intent serviceIntent = new Intent(BarActivity.this,UpdateBackService.class);
        serviceIntent.putExtra("username", statusUser);
        serviceIntent.putExtra("password", statusPass);
        BarActivity.this.startService(serviceIntent);
        
        if(status == 1){ //the admin's tabs
        	
        	MediaPlayer mp = null;
            mp = MediaPlayer.create(this, R.raw.weareonlineandready);
            mp.start();
        	
        	// create a bundle and pass the data to the admin adapter pager
        	Bundle args = new Bundle();
        	if(Nodes.equals("No nodes")) {
        		args.putString("Nodes", "No nodes");
        	}
        	args.putString("Nodes", "Nodes");
        	args.putInt("status", 1);
        	args.putString("statusUser", statusUser);
        	args.putString("statusPass", statusPass);
        	
        	// create the actionbar with the tabs and the fragmentpageradapter for the swipe view 
        	SwipePager = (ViewPager) findViewById(R.id.pager);  
        	adAdapter = new AdminAdapter(getSupportFragmentManager(),args);  
            actionbar = getActionBar();
           
            SwipePager.setAdapter(adAdapter);  
            actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); 
            Tab nodes;
        	nodes = actionbar.newTab().setText("Admin Nodes").setTabListener(this);
            
        	actionbar.addTab(nodes); 
        
        	actionbar.addTab(actionbar.newTab().setText("Interfaces").setTabListener(this));  
        	actionbar.addTab(actionbar.newTab().setText("Patterns").setTabListener(this));  
        	actionbar.addTab(actionbar.newTab().setText("Delete").setTabListener(this)); 
        	
        	
        	SwipePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {  
        		@Override  
        		public void onPageSelected(int arg0) {  
      	
        			actionbar.setSelectedNavigationItem(arg0);  
        		}  
        		@Override  
        		public void onPageScrolled(int arg0, float arg1, int arg2) {  
                  // TODO Auto-generated method stub  
        		}  
        		@Override  
        		public void onPageScrollStateChanged(int arg0) {  
                  // TODO Auto-generated method stub  
        		}  
        	});
        	
        }
        else if (status == 0)
        { // the user's tabs
        	
        	MediaPlayer mp = null;
            mp = MediaPlayer.create(this, R.raw.importingpreferences);
            mp.start();
        	
        	// create a bundle and pass the data to the admin adapter pager
        	Bundle args = new Bundle();
        	if(Nodes.equals("No nodes")) {
        		args.putString("Nodes", "No nodes");
        	}
        	args.putString("Nodes", "Nodes");
        	args.putString("statusUser", statusUser);
        	args.putString("statusPass", statusPass);
        	args.putInt("status", 0);
        	
        	// create the actionbar with the tabs and the fragmentpageradapter for the swipe view 
        	SwipePager = (ViewPager) findViewById(R.id.pager);  
            usrAdapter = new UserAdapter(getSupportFragmentManager(), args);  
            actionbar = getActionBar();
            
            SwipePager.setAdapter(usrAdapter);  
            actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); 
            Tab nodes;
        	nodes = actionbar.newTab().setText("User Nodes").setTabListener(this);
        	
        	actionbar.addTab(nodes); 
        
        	actionbar.addTab(actionbar.newTab().setText("Interfaces").setTabListener(this));  
       
        	SwipePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {  
        		@Override  
        		public void onPageSelected(int arg0) {  
        			
        			actionbar.setSelectedNavigationItem(arg0);  
        		}  
        		@Override  
        		public void onPageScrolled(int arg0, float arg1, int arg2) {  
                  // TODO Auto-generated method stub  
        		
        			
        		}  
        		@Override  
        		public void onPageScrollStateChanged(int arg0) {  
                  // TODO Auto-generated method stub  
        		}  
        	}); 
        	
        }
        else {
        	//an error occured
        	Log.d("BarActivity","Error Occured");
        }
       
      
    }
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId()){
	 		case android.R.id.home:
	 			// app icon in action bar clicked; goto parent activity.
	 			this.finish();
	 			return true;
	 		case R.id.action_settings:
	 			return true;
	 		default:
	            return super.onOptionsItemSelected(item);
	 		}
	 		
	}
	
	
	// handle the back button on viewpager 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK && SwipePager.getCurrentItem() == 1) {
	    	SwipePager.setCurrentItem(0, true);
	        return true;
	    } 
	    else if(keyCode == KeyEvent.KEYCODE_BACK && SwipePager.getCurrentItem() == 3) {
	    	SwipePager.setCurrentItem(2, true);
	        return true;
	    }
	    else if(keyCode == KeyEvent.KEYCODE_BACK && SwipePager.getCurrentItem() == 2) {
	    	SwipePager.setCurrentItem(1, true);
	        return true;
	    }
	    else if(keyCode == KeyEvent.KEYCODE_BACK && SwipePager.getCurrentItem() == 0) {
	    	SharedPreferences loginOptions = getSharedPreferences("loginPrefs", MainActivity.MODE_PRIVATE);
	    	SharedPreferences.Editor editor = loginOptions.edit();
	    	editor.putBoolean("saveUser", true);
			 editor.putString("userSaver", statusUser);
			 editor.putString("passSaver", statusPass);
			 editor.putInt("status", status);
			 editor.putString("Nodes", Nodes);
			 editor.commit();
	    	finish();
	        return true;
	    }
	    else {
	       return super.onKeyDown(keyCode, event);
	    }
	}

	
	 
     public void onTabSelected(Tab tab, FragmentTransaction ft) { 

    		 SwipePager.setCurrentItem(tab.getPosition());    	 
     }  
     @Override  
     public void onTabUnselected(Tab tab, FragmentTransaction ft) {  
          // TODO Auto-generated method stub  
     }



	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}  
	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
	    // Close The Database connection 
		databaseAdapter.close();
	}


	
	
}
