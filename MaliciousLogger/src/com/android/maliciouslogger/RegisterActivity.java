package com.android.maliciouslogger;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.database.sqlite.DatabaseAdapter;
import com.packetMem.shared.AvailableNodes;
import com.packetMem.shared.StatisticalReports;
import com.webservice.client.ClientRequests;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.Toast;



public class RegisterActivity extends Activity{
	
	private EditText uid, regUsername, regPassword;
	private Button add, reg, how;
	private Boolean checkConnection;
	private CheckBox cb;
	private String serverip;
	private ArrayList<String> uids = new ArrayList<String>(); // holds the uids that the user adds 
	DatabaseAdapter databaseAdapter;
	
	private static final String PATTERN = 
	        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	public static boolean validate(final String ip){          

	      Pattern pattern = Pattern.compile(PATTERN);
	      Matcher matcher = pattern.matcher(ip);
	      return matcher.matches();             
	}
    
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        // initialize 
        databaseAdapter=new DatabaseAdapter(this);
		databaseAdapter=databaseAdapter.open();
        how = (Button) findViewById(R.id.how);
        cb = (CheckBox) findViewById(R.id.checkButton1);
        // handle the "How to Register button"
        how.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder alertBuilder = new AlertDialog.Builder(RegisterActivity.this);
    			alertBuilder.setTitle("Deletion");
    			alertBuilder.setMessage("Add firstly as many nodes as you want \n to observe and then press Register \n");
    			alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    				
    				@Override
    				public void onClick(DialogInterface arg0, int arg1) {
    					
    				}
    			});
    			alertBuilder.show();
    		}
				
		});
    		
        final com.network.manager.InternetConnection nm = new com.network.manager.InternetConnection(getApplicationContext());
        final AvailableNodes avNodes = new AvailableNodes();
        
        uid = (EditText)findViewById(R.id.editUid);
        regUsername = (EditText) findViewById(R.id.newuserText);
        regPassword = (EditText) findViewById(R.id.newpassText);
        
        //make arraylist to put the uid from the user given
        
        reg = (Button) findViewById(R.id.Reg);
        reg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            	
            	final String userStr = regUsername.getText().toString();
            	final String PassStr = regPassword.getText().toString();
            	
            	if(uids == null )//check also the arraylist
            	{
            		Toast.makeText(getApplicationContext(), "Please Insert first a node Id",
        					Toast.LENGTH_SHORT).show();
            		
            	}
            	else if(userStr.equals("") || PassStr.equals("")){
 					Toast.makeText(getApplicationContext(), "Please fill all the fields",
 					Toast.LENGTH_SHORT).show();   
 			   	}
            	else{
            		 
            		 //check the connection as in login
            		 checkConnection = nm.internetAlive();
					 ClientRequests cr = new ClientRequests();
					 if(checkConnection==false){
						 Toast.makeText(getApplicationContext(),"Internet Connection is Down",Toast.LENGTH_SHORT).show();	 
					 }
					 else{
						 cr.registerUser(userStr, PassStr, avNodes);
						 if (com.webservice.client.ClientRequests.respReg.equals("") || com.webservice.client.ClientRequests.respReg.equals("timeout")){
							 Toast.makeText(getApplicationContext(),"Server is not available.\nTry again Later",Toast.LENGTH_SHORT).show();
						 }
						 else if(com.webservice.client.ClientRequests.respReg.equals("Success Reg") ){
							 cr.retrieveStatistics(userStr, PassStr);
							
							 databaseAdapter.insertUser(userStr, PassStr, 0);
							 for(String node : uids){
								 databaseAdapter.insertPC_has_User(node, userStr);
							 }
							 if(com.webservice.client.ClientRequests.resplist == null){
								 Toast.makeText(getApplicationContext(),"Something went wrong.\nPlease try again later",Toast.LENGTH_SHORT).show();
							 }
							 else{
								 
								 for(int i=0; i<com.webservice.client.ClientRequests.resplist.size();i++){
									 StatisticalReports stats = com.webservice.client.ClientRequests.resplist.get(i);
									 String[] devList = stats.Device.split("#");
									 String[] devIpList = stats.DeviceIP.split("#");
									 String[] devPatList = stats.Pattern.split("#");
									 String[] devFreqList = stats.Frequency.split("#");
									 String tempUid = "";
									 if(stats.UID.equals("No Online Nodes")) {  // if there are no online nodes on server side do nothing
										 break;
									 }
									 if(!tempUid.equals(stats.UID)){ // don't insert the same node 
										 databaseAdapter.insertNode(stats.UID);
										 tempUid = stats.UID;
									 }
									 for(int j=0;j<devList.length;j++){
										 databaseAdapter.insertStatisticalReports(stats.UID, stats.Local_Time, devList[j], devIpList[j], devPatList[j], devFreqList[j]);
									 } 
									 
								 }
							 }
							 Toast.makeText(getApplicationContext(),"Successful Registration.\nPlease wait.",Toast.LENGTH_SHORT).show();
							 
							 regUsername.setText("");
							 regPassword.setText("");
							 uid.setText("");
							 //go to next activity 
							 Intent intentUser = new Intent(RegisterActivity.this,BarActivity.class);
							 intentUser.putExtra("status", 0); // 1 for admin
							 intentUser.putExtra("statusUser", userStr);
							 intentUser.putExtra("statusPass", PassStr);
							 intentUser.putExtra("Nodes", "Nodes");
							 RegisterActivity.this.startActivity(intentUser);
							 MainActivity.getInstance().finish();
							 finish();
						 }
						 else {
							 Toast.makeText(getApplicationContext(),"Unsuccessful Registration.\nPlease try again.",Toast.LENGTH_SHORT).show();
						 }
					 }
                 	
            		 
            		 
            		
            	 }	
            	
            }

        });
        
        // handle the add button for inserting nodes
        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	final String uidStr2 = uid.getText().toString();
            	//add uid to available nodes
            	(avNodes.Count)++;
            	//check if the node has already been inserted
            	if(uids.contains(uidStr2)){
            		Toast.makeText(getApplicationContext(),"Node: " + uidStr2 + "already \n   exists",Toast.LENGTH_SHORT).show();
            	}
            	else{
            		uids.add(uidStr2);           	
            		if(avNodes.UID == null){
            			avNodes.UID = uidStr2 + "#";
            		}
            		else {
            			avNodes.UID = avNodes.UID + uidStr2 + "#";
            		}
            		uid.setText("");
            		Toast.makeText(getApplicationContext(),"Node: " + uidStr2 + "\n added",Toast.LENGTH_SHORT).show();
            	}
            }

        });
        
        // handle the checkbox for inserting the server's ip
        cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
				 alertDialog.setTitle("Server's IP");
				 alertDialog.setMessage("Enter Server's IP ?");

				 final EditText input = new EditText(RegisterActivity.this);  
				  LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				                        LinearLayout.LayoutParams.MATCH_PARENT,
				                        LinearLayout.LayoutParams.MATCH_PARENT);
				  input.setLayoutParams(lp);
				  alertDialog.setView(input);
				 
				 alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
				
		
			
        
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	    // Close The Database
		databaseAdapter.close();
	}
	
}
