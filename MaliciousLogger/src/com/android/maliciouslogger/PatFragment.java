package com.android.maliciouslogger;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.webservice.client.ClientRequests;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;


public class PatFragment extends Fragment {

	private EditText patText;
	private Button Logout,submit, Refresh;
	private String rcvUser, rcvPass;
	
	private Boolean checkConnection;
	private ArrayList<String> IPpatterns;
	private ArrayList<String> Stringpatterns;
	private static final String PATTERN = 
	        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	
	// validates if the given string is an ip address
	public static boolean validate(final String ip){          

	      Pattern pattern = Pattern.compile(PATTERN);
	      Matcher matcher = pattern.matcher(ip);
	      return matcher.matches();             
	}
    
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState); 
		
		IPpatterns = new ArrayList<String>();	
		IPpatterns = (ArrayList<String>) ((BarActivity)getActivity()).getDatabaseAdapter().getIPPatterns();
		
		Stringpatterns = new ArrayList<String>();
		Stringpatterns = (ArrayList<String>) ((BarActivity)getActivity()).getDatabaseAdapter().getStringPatterns();
		
		rcvUser = getArguments().getString("statusUser").toString();
		rcvPass = getArguments().getString("statusUser").toString();
		//set the fragments tag
		String myTag = getTag();
        ((BarActivity)getActivity()).setPatFrag(myTag);
    }
	
	
    
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
         View fragView = inflater.inflate(R.layout.pat_layout, container,false); 
         final TableLayout tableIP = (TableLayout) fragView.findViewById(R.id.ipTable);
         final TableLayout tablePAT = (TableLayout) fragView.findViewById(R.id.patTable);
         Logout=(Button)fragView.findViewById(R.id.logoutButton);
         Refresh = (Button) fragView.findViewById(R.id.RefreshButtonPat);
         
         Logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//call the logout method from server
				AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
				alertBuilder.setTitle("Log out");
				alertBuilder.setMessage("Are you sure you want to Exit ?");
				alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						final com.network.manager.InternetConnection nm = new com.network.manager.InternetConnection(getActivity().getApplicationContext());
						checkConnection = nm.internetAlive();
						if(checkConnection == false){
							//we don't have internet,
							Toast.makeText(getActivity(), "Can't Connect. Please try again", Toast.LENGTH_SHORT).show();
							
						}
						else {
								ClientRequests cl = new ClientRequests();
								cl.logoutUser(rcvUser,rcvPass);
								if(com.webservice.client.ClientRequests.respLogout.equals("timeout") || com.webservice.client.ClientRequests.respLogout.equals("")) {
  									Toast.makeText(getActivity(),"Server is not available.\nTry again Later",Toast.LENGTH_SHORT).show();
  								}
								//erase the shared preferences
								SharedPreferences sp = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
 								SharedPreferences.Editor editor = sp.edit();
 						        editor.clear();
 						        editor.commit();
 						        // go to MainActivity
								Intent intentExit = new Intent(getActivity(),MainActivity.class);			
								getActivity().startActivity(intentExit);
								getActivity().finish();
						}
					}
				});
				alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						
					}
				});
				alertBuilder.show();
			}
				
		});
         
        // Create the first table with the stringPatterns 
         TableRow trPat = new TableRow(getActivity());
			TableLayout.LayoutParams tableRowParams=
					new TableLayout.LayoutParams
					(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
			int leftMargin = 4;
			int topMargin=4;
			int rightMargin=4;
			int bottomMargin=4;

			tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

			trPat.setLayoutParams(tableRowParams);
	
			TextView up1 = new TextView(getActivity()); 
			up1.setText("PATTERNS");
			up1.setTextColor(Color.WHITE);
			up1.setTypeface(null, Typeface.BOLD);
			up1.setTextSize(14);
			up1.setGravity(Gravity.CENTER);
			up1.setBackground(getResources().getDrawable(R.drawable.editborder));
			trPat.addView(up1);

			tablePAT.addView(trPat,new TableLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			
			
			//Insert first the string patterns
			for(int i=0;i<Stringpatterns.size();i++){
				TableRow tr1 = new TableRow(getActivity());
				TableLayout.LayoutParams tableRowParams1=
						new TableLayout.LayoutParams
						(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
				

				tableRowParams1.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

				tr1.setLayoutParams(tableRowParams1);
			
				
					TextView t1 = new TextView(getActivity());
					
					t1.setText(Stringpatterns.get(i).toString());
					t1.setTextColor(Color.WHITE);
					t1.setTextSize(12);
					t1.setGravity(Gravity.CENTER);
					t1.setBackground(getResources().getDrawable(R.drawable.editborder));
					tr1.addView(t1);
					tablePAT.addView(tr1,new TableLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				
			}	
			
			// create the other table for the IPs
			 TableRow trIP = new TableRow(getActivity());
				TableLayout.LayoutParams tableRowParams2=
						new TableLayout.LayoutParams
						(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

				tableRowParams2.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

				trIP.setLayoutParams(tableRowParams2);

				TextView up2 = new TextView(getActivity()); 
				up2.setText("IP");
				up2.setTextColor(Color.WHITE);
				up2.setTypeface(null, Typeface.BOLD);
				up2.setTextSize(14);
				up2.setGravity(Gravity.CENTER);
				up2.setBackground(getResources().getDrawable(R.drawable.editborder));
				trIP.addView(up2);

				tableIP.addView(trIP,new TableLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			
			
			// Insert the ip patterns 
			for(int i=0;i<IPpatterns.size();i++){
				TableRow tr2 = new TableRow(getActivity());
				TableLayout.LayoutParams tableRowParams3=
						new TableLayout.LayoutParams
						(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
				

				tableRowParams3.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

				tr2.setLayoutParams(tableRowParams3);
			
				
					TextView t2 = new TextView(getActivity());
					
					t2.setText(IPpatterns.get(i).toString());
					t2.setTextColor(Color.WHITE);
					t2.setTextSize(12);
					t2.setGravity(Gravity.CENTER);
					t2.setBackground(getResources().getDrawable(R.drawable.editborder));
					tr2.addView(t2);
					tableIP.addView(tr2,new TableLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				
			}	
			
        patText = (EditText) fragView.findViewById(R.id.pat);
        
        final com.network.manager.InternetConnection nm = new com.network.manager.InternetConnection(getActivity().getApplicationContext());
        submit = (Button) fragView.findViewById(R.id.submit);
        // handle the submit button
        submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 /*Validate the pattern, which the admin gave*/
				String maliciousPat = patText.getText().toString();
		        if(validate(maliciousPat)){
		        	//it is ip
		        	//check connection and send it and put it in sqlite
		        	// else put it only in sqlite
		        	checkConnection = nm.internetAlive();
		        	if(checkConnection == false){
		        		
		        		// 1 is for ip, 0 for pending
		        		// insert new row on table 
		        		if (((BarActivity)getActivity()).getDatabaseAdapter().insertPattern(maliciousPat,1,0) ==0){
		        			TableRow newIP = new TableRow(getActivity());
			        		TextView t = new TextView(getActivity());
							
							t.setText(maliciousPat);
							t.setTextColor(Color.WHITE);
							t.setTextSize(12);
							t.setGravity(Gravity.CENTER);
							t.setBackground(getResources().getDrawable(R.drawable.editborder));
							newIP.addView(t);
							tableIP.addView(newIP,new TableLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
							Toast.makeText(getActivity(), "IP: " + maliciousPat + " inserted", Toast.LENGTH_SHORT).show();
		        		}
		        		else{
		        			Toast.makeText(getActivity(),"IP already exists.",Toast.LENGTH_SHORT).show();
		        		}
		        		patText.setText("");
		        	}
		        	else {
		        		ClientRequests cr = new ClientRequests();
		        		// null because it is ip
		        		if (((BarActivity)getActivity()).getDatabaseAdapter().insertPattern(maliciousPat,1,1)==0){
		        		cr.insertPattern(rcvUser, rcvPass, maliciousPat,null);
		        		// 1 is for ip, 1 for sent
		        		// insert new row on table
		        		if(com.webservice.client.ClientRequests.respIns.equals("timeout") || com.webservice.client.ClientRequests.respIns.equals("")) {
								Toast.makeText(getActivity(),"Server is not available.\nTry again Later",Toast.LENGTH_SHORT).show();
							}
		        			TableRow newIP = new TableRow(getActivity());
			        		TextView t = new TextView(getActivity());
							
							t.setText(maliciousPat);
							t.setTextColor(Color.WHITE);
							t.setTextSize(12);
							t.setBackground(getResources().getDrawable(R.drawable.editborder));
							t.setGravity(Gravity.CENTER);
							newIP.addView(t);
							tableIP.addView(newIP,new TableLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
							Toast.makeText(getActivity(), "IP: " + maliciousPat + " inserted", Toast.LENGTH_SHORT).show();
		        		}
		        		else{
		        			
		        			Toast.makeText(getActivity(),"IP already exists.",Toast.LENGTH_SHORT).show();
		        		}
		        		patText.setText("");
		        	}
		        }
		        else
		        {
		        	//it is string pattern
		        	Toast.makeText(getActivity(), maliciousPat, Toast.LENGTH_SHORT).show();
		        	checkConnection = nm.internetAlive();
		        	if(checkConnection == false){
		        		// 0 is for stringPattern, 0 for pending
		        		// insert new row on table
		        		if (((BarActivity)getActivity()).getDatabaseAdapter().insertPattern(maliciousPat,0,0)==0){
		        			TableRow newPAT = new TableRow(getActivity());
			        		TextView t = new TextView(getActivity());
							
							t.setText(maliciousPat);
							t.setTextColor(Color.WHITE);
							t.setTextSize(12);
							t.setGravity(Gravity.CENTER);
							t.setBackground(getResources().getDrawable(R.drawable.editborder));
							newPAT.addView(t);
							tablePAT.addView(newPAT,new TableLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
							Toast.makeText(getActivity(),"Pattern:" + maliciousPat + " inserted",Toast.LENGTH_SHORT).show();
		        		}
		        		else{
		        			Toast.makeText(getActivity(),"Pattern already exists.",Toast.LENGTH_SHORT).show();
		        		}
		        		patText.setText("");
		        	}
		        	else {
		        		ClientRequests cr = new ClientRequests();
		        		// null because it is string pattern
		        		if (((BarActivity)getActivity()).getDatabaseAdapter().insertPattern(maliciousPat,0,1)==0){
		        		cr.insertPattern(rcvUser, rcvPass, null ,maliciousPat);
		        		if(com.webservice.client.ClientRequests.respIns.equals("timeout") || com.webservice.client.ClientRequests.respIns.equals("")) {
							Toast.makeText(getActivity(),"Server is not available.\nTry again Later",Toast.LENGTH_SHORT).show();
						}
		        		// 0 is for stringPattern, 1 for sent
		        		// insert new row on table
		        			TableRow newPAT = new TableRow(getActivity());
			        		TextView t = new TextView(getActivity());
							
							t.setText(maliciousPat);
							t.setTextColor(Color.WHITE);
							t.setTextSize(12);
							t.setGravity(Gravity.CENTER);
							t.setBackground(getResources().getDrawable(R.drawable.editborder));
							newPAT.addView(t);
							tablePAT.addView(newPAT,new TableLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
							Toast.makeText(getActivity(),"Pattern:" + maliciousPat + " inserted",Toast.LENGTH_SHORT).show();
		        		}
		        		else{
		        			Toast.makeText(getActivity(),"Pattern already exists.",Toast.LENGTH_SHORT).show();
		        		}
		        		patText.setText("");
		        	}
		        	
		        }
				
			}
		});
        
        // handle the refresh button 
        Refresh.setOnClickListener(new OnClickListener() {
   			
   			@Override
   			public void onClick(View v) {
   				// clear the lists
   				IPpatterns.clear();
   				Stringpatterns.clear();
   				IPpatterns = (ArrayList<String>) ((BarActivity)getActivity()).getDatabaseAdapter().getIPPatterns();
   				Stringpatterns = (ArrayList<String>) ((BarActivity)getActivity()).getDatabaseAdapter().getStringPatterns();
   				// get the fragment tag
   				String PatTag = ((BarActivity)getActivity()).getPatFrag();
   				PatFragment patFrag = (PatFragment)getActivity().getSupportFragmentManager().findFragmentByTag(PatTag);
   				final FragmentTransaction ft = getFragmentManager().beginTransaction();
   				// detach and reattach fragment 
   				ft.detach(patFrag);
   				ft.attach(patFrag);
   				ft.commit();
   				Toast.makeText(getActivity(), "Refreshed", Toast.LENGTH_SHORT).show();
   			}
   		});
       
        
        
       return  fragView;
  }
	
	
	
}