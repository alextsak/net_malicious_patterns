package com.android.maliciouslogger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.database.sqlite.DatabaseAdapter;
import com.packetMem.shared.StatisticalReports;
import com.webservice.client.ClientRequests;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText tempu, tempp;
	private Button login, signup;
	private Boolean exit = false; // for exiting the application 
	private SharedPreferences loginOptions;
	private SharedPreferences.Editor editor;
	private Boolean saveUser;
	private static MainActivity firstActivity; // instance of this activity
	private Boolean checkConnection;
	private ImageView img; 
	private String serverip;
	DatabaseAdapter databaseAdapter;
	
	private static final String PATTERN = 
	        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	public static boolean validate(final String ip){          

	      Pattern pattern = Pattern.compile(PATTERN);
	      Matcher matcher = pattern.matcher(ip);
	      return matcher.matches();             
	}
    
	
	public Boolean getSaveUser() {
		return saveUser;
	}


	public void setSaveUser(Boolean saveUser) {
		this.saveUser = saveUser;
	}


	public SharedPreferences getLoginOptions() {
		return loginOptions;
	}


	public void setLoginOptions(SharedPreferences loginOptions) {
		this.loginOptions = loginOptions;
	}


	public SharedPreferences.Editor getEditor() {
		return editor;
	}


	public void setEditor(SharedPreferences.Editor editor) {
		this.editor = editor;
	}
		
	public static MainActivity getInstance(){
		   return   firstActivity;
	}

	
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		firstActivity = this;
		
		//handle the click on image to set the sever ip
		img = (ImageView) findViewById(R.id.imageView1);
		img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
				 alertDialog.setTitle("Server's IP");
				 alertDialog.setMessage("Enter Server's IP ?");

				 final EditText input = new EditText(MainActivity.this);  
				  LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				                        LinearLayout.LayoutParams.MATCH_PARENT,
				                        LinearLayout.LayoutParams.MATCH_PARENT);
				  input.setLayoutParams(lp);
				  alertDialog.setView(input);
				 
				 alertDialog.setPositiveButton("0K", new DialogInterface.OnClickListener() {
				         public void onClick(DialogInterface dialog, int which) {
				        	 serverip = input.getText().toString();
				             if (serverip.equals("")) {				                 	
				                     Toast.makeText(getApplicationContext(),"Please Fill the Field", Toast.LENGTH_SHORT).show();
				                    
				                 } 
				             else if (!validate(serverip)) {
				            	 Toast.makeText(getApplicationContext(),"InCorrect IP Format", Toast.LENGTH_SHORT).show();
				             }
				             else {
				                    
				                     com.webservice.client.ClientRequests.ip = serverip;
				                 }
				             }
				     });

				 alertDialog.setNegativeButton("Cancel",
				     new DialogInterface.OnClickListener() {
				         public void onClick(DialogInterface dialog, int which) {
				             dialog.cancel();
				         }
				     });

				 alertDialog.show();
				 }

			});
			
		
		
		//check for internet connection
		final com.network.manager.InternetConnection nm = new com.network.manager.InternetConnection(getApplicationContext());
		databaseAdapter=new DatabaseAdapter(this);
		databaseAdapter=databaseAdapter.open();
		tempu = (EditText)findViewById(R.id.userLogin);
		tempp = (EditText)findViewById(R.id.passLogin);
		
		//remember the user
		loginOptions = getSharedPreferences("loginPrefs", MainActivity.MODE_PRIVATE);
	    editor = loginOptions.edit();
	    
	    saveUser = loginOptions.getBoolean("saveUser", false);
	    if(saveUser==true){
	    	Log.d("MainActivity", "sharedPrefs");
	    	tempu.setText(loginOptions.getString("userSaver", ""));
	    	tempp.setText(loginOptions.getString("passSaver", ""));
	    	Intent intentlogin = new Intent(MainActivity.this,BarActivity.class);
	    	if(databaseAdapter.isAdmin(tempu.getText().toString(), tempp.getText().toString()).equals("1")){
	    		intentlogin.putExtra("status", 1);
	    		intentlogin.putExtra("statusUser", loginOptions.getString("userSaver", ""));
	    		intentlogin.putExtra("statusPass", loginOptions.getString("passSaver", ""));
	    		intentlogin.putExtra("Nodes", loginOptions.getString("Nodes", ""));
	    		MainActivity.this.startActivity(intentlogin);
	    	}
	    	else{
	    		intentlogin.putExtra("status", 0);
	    		intentlogin.putExtra("statusUser", loginOptions.getString("userSaver", ""));
	    		intentlogin.putExtra("statusPass", loginOptions.getString("passSaver", ""));
	    		intentlogin.putExtra("Nodes", loginOptions.getString("Nodes", ""));
	    		MainActivity.this.startActivity(intentlogin);
	    	}
	    	
	    }
	    
	    
		//set listener for login button	
		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new View.OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				
				final String userName=tempu.getText().toString();
				final String passWord=tempp.getText().toString();
				
				if(userName.equals("") || passWord.equals("")){
					
					Toast.makeText(getApplicationContext(), "Please fill all the fields",
							Toast.LENGTH_SHORT).show();
				}
				else {
					//check if we have internet
					checkConnection = nm.internetAlive();
					if(checkConnection==false) { // we don't have internet
						Toast.makeText(getApplicationContext(),"Internet Connection is Down \n Try again Later",Toast.LENGTH_SHORT).show();
					}
					else {
						Toast.makeText(getApplicationContext(),"Loading Data.\n Please wait",Toast.LENGTH_SHORT).show();
						 ClientRequests cr = new ClientRequests();
						
						 cr.loginUser(userName,passWord);
						 if(com.webservice.client.ClientRequests.respLogin.equals("timeout") || com.webservice.client.ClientRequests.respLogin.equals("")) {
							 Toast.makeText(getApplicationContext(),"Server is not available.\nTry again Later",Toast.LENGTH_SHORT).show();
						 }
						 else if(com.webservice.client.ClientRequests.respLogin.equals("Success User") ) {
							 cr.retrieveStatistics(userName, passWord);
							 if(com.webservice.client.ClientRequests.resplist == null) {
								 Toast.makeText(getApplicationContext(),"Something went wrong.\nPlease try again later",Toast.LENGTH_SHORT).show();
							 }
							 else {
								 if(com.webservice.client.ClientRequests.respStats.equals("No nodes")) { 
									 
									 Toast.makeText(getApplicationContext(),"You have logged in",Toast.LENGTH_SHORT).show();
									 //save the preferences 
									 editor.putBoolean("saveUser", true);
									 editor.putString("userSaver", userName);
									 editor.putString("passSaver", passWord);
									 editor.putInt("status", 0);
									 editor.putString("Nodes", "No nodes");
									 editor.commit();
									 
									 // go to next activity
									 Intent intentUser = new Intent(MainActivity.this,BarActivity.class);
									 intentUser.putExtra("status", 0);
									 intentUser.putExtra("statusUser", userName);
									 intentUser.putExtra("statusPass", passWord);
									 intentUser.putExtra("Nodes", "No nodes");
									 MainActivity.this.startActivity(intentUser);
									 finish();
	 
								 }
								 else { // we have nodes so retrieve the statistics 
									 for(int i=0; i<com.webservice.client.ClientRequests.resplist.size();i++){
										 StatisticalReports stats = com.webservice.client.ClientRequests.resplist.get(i);
										 String[] devList = stats.Device.split("#");
										 String[] devIpList = stats.DeviceIP.split("#");
										 String[] devPatList = stats.Pattern.split("#");
										 String[] devFreqList = stats.Frequency.split("#");
										 if(stats.UID.equals("No Online Nodes")) {  // if there are no online nodes on server side do nothing
											 break;
										 }
										 for(int j=0;j<devList.length;j++){
											 databaseAdapter.insertStatisticalReports(stats.UID, stats.Local_Time, devList[j], devIpList[j], devPatList[j], devFreqList[j]);
										 } 
									 }
									 Toast.makeText(getApplicationContext(),"You have logged in",Toast.LENGTH_SHORT).show();
									 //save the preferences
									 editor.putBoolean("saveUser", true);
									 editor.putString("userSaver", userName);
									 editor.putString("passSaver", passWord);
									 editor.putInt("status", 0);
									 editor.putString("Nodes", "Nodes");
									 editor.commit();
									 
									 //go to next activity
									 Intent intentUser = new Intent(MainActivity.this,BarActivity.class);
									 intentUser.putExtra("status", 0);
									 intentUser.putExtra("statusUser", userName);
									 intentUser.putExtra("statusPass", passWord);
									 intentUser.putExtra("Nodes", "Nodes");
									 MainActivity.this.startActivity(intentUser);
									 finish();
								 }
							 } 
						 }
						 else if(com.webservice.client.ClientRequests.respLogin.equals("Success Admin")){ 
							 cr.retrieveStatistics(userName, passWord);
							 
							 if(databaseAdapter.isAdmin(userName, passWord)==null) { 
								 databaseAdapter.insertUser(userName, passWord, 1);
							 }
							 if(com.webservice.client.ClientRequests.resplist == null){
								 Toast.makeText(getApplicationContext(),"Something went wrong.\nPlease try again later",Toast.LENGTH_SHORT).show();
							 }
							 else{
								 if(com.webservice.client.ClientRequests.respStats.equals("No nodes")) {
									 Toast.makeText(getApplicationContext(),"Hello Admin",Toast.LENGTH_SHORT).show();
									 editor.putBoolean("saveUser", true);
									 editor.putString("userSaver", userName);
									 editor.putString("passSaver", passWord);
									 editor.putInt("status", 1);
									 editor.putString("Nodes", "No nodes");
									 editor.commit();
									 
									 Intent intentUser = new Intent(MainActivity.this,BarActivity.class);
									 intentUser.putExtra("status", 1); // 1 for admin
									 intentUser.putExtra("statusUser", userName);
									 intentUser.putExtra("statusPass", passWord);
									 intentUser.putExtra("Nodes", "No nodes");
									 MainActivity.this.startActivity(intentUser);
									 finish();
								 }
								 else {
									 for(int i=0; i<com.webservice.client.ClientRequests.resplist.size();i++){
										 StatisticalReports stats = com.webservice.client.ClientRequests.resplist.get(i);
										 String[] devList = stats.Device.split("#");
										 String[] devIpList = stats.DeviceIP.split("#");
										 String[] devPatList = stats.Pattern.split("#");
										 String[] devFreqList = stats.Frequency.split("#");
										 if(stats.UID.equals("No Online Nodes")) {  // if there are no online nodes on server side do nothing
											 break;
										 }
										 for(int j=0;j<devList.length;j++){
											 databaseAdapter.insertStatisticalReports(stats.UID, stats.Local_Time, devList[j], devIpList[j], devPatList[j], devFreqList[j]);
										 } 
									 }
									 Toast.makeText(getApplicationContext(),"Hello Admin",Toast.LENGTH_SHORT).show();
									 editor.putBoolean("saveUser", true);
									 editor.putString("userSaver", userName);
									 editor.putString("passSaver", passWord);
									 editor.putInt("status", 1);
									 editor.putString("Nodes", "Nodes");
									 editor.commit();
									 
									 Intent intentUser = new Intent(MainActivity.this,BarActivity.class);
									 intentUser.putExtra("status", 1); // 1 for admin
									 intentUser.putExtra("statusUser", userName);
									 intentUser.putExtra("statusPass", passWord);
									 intentUser.putExtra("Nodes", "Nodes");
									 MainActivity.this.startActivity(intentUser);
									 finish();
								 }
							 }
							 
						 }
						 else{
							 Toast.makeText(getApplicationContext(),"Login failed.",Toast.LENGTH_SHORT).show(); 
						 }
					 }
					 
				}
			}
		});
		 

		//set listener for register button
		signup = (Button) findViewById(R.id.Reg);
	    signup.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View view) {
	            Intent myIntent = new Intent(MainActivity.this, RegisterActivity.class);
	            MainActivity.this.startActivity(myIntent);
	        }

	    });
		
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
		
	// handle the back button and close the application
	@Override
	public void onBackPressed() {
	        if (exit) {
	        	//sound
            	MediaPlayer powerdownwav = MediaPlayer.create(this, R.raw.powerdown);
            	powerdownwav.start();
	            finish(); // finish activity
	        } 
	        else {
	            	Toast.makeText(this, "Press Back again to Exit.",Toast.LENGTH_SHORT).show();
	            	exit = true;
	            	new Handler().postDelayed(new Runnable() {
	            		@Override
	            		public void run() {
	            			exit = false;
	            		}
	            	}, 3 * 1000);

	        	}

	    	}	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	    // Close The Connection with the Database
		databaseAdapter.close();
	}
}
