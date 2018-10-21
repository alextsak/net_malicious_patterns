package com.android.maliciouslogger;

import java.util.ArrayList;


import com.webservice.client.ClientRequests;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class InterfacesFragment extends ListFragment {

	
	private TextView received;
	private String rcvStr="";
	private ViewPager viewPager;
	private String rcvUser,rcvPass;
	private Button Logout, Refresh;
	private Boolean checkConnection;
	private ArrayAdapter<String> adapter;	
	private ArrayList<String> interfaces;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // initialize 
        interfaces = new ArrayList<String>();
        
		rcvUser = getArguments().getString("statusUser").toString();
		rcvPass = getArguments().getString("statusUser").toString();		
			
		Log.d("InterFrag","OnCreate");
    }
	
	
	
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
              Bundle savedInstanceState) {  
           View fragView = inflater.inflate(R.layout.interfaces_layout, container,false); 
           Logout=(Button)fragView.findViewById(R.id.logoutButton);
           Refresh = (Button) fragView.findViewById(R.id.RefreshButtonInter);
           
           // handle the refresh 
           Refresh.setOnClickListener(new OnClickListener() {
   			
   			@Override
   			public void onClick(View v) {
   					rcvStr = received.getText().toString();
   					if(!rcvStr.equals("")) { // if the user choose a node
   						if(interfaces.size() != 0) { // if there are interfaces
   						interfaces.clear();			// clear the list
   						// make a query again 
   						interfaces = (ArrayList<String>) ((BarActivity)getActivity()).getDatabaseAdapter().getAllNodeDevices(rcvStr);
   						adapter.clear();
   						//add new interfaces to the listView and refresh it
   						adapter.addAll(interfaces);						
   						adapter.notifyDataSetChanged();  
   						Toast.makeText(getActivity(), "Refreshed", Toast.LENGTH_SHORT).show();
   					}
   					else { // the user has not picked a node so don't show anything 
   						rcvStr = "";
   						interfaces.clear();
   						adapter.clear();
   						adapter.notifyDataSetChanged();
   						Toast.makeText(getActivity(), "Nothing to Display", Toast.LENGTH_SHORT).show();
   					}
   				}
   			}
   		});
           // handle the logout button 
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
  						//we don't have internet
  							Toast.makeText(getActivity(), "Can't Connect. Please try again", Toast.LENGTH_SHORT).show();	
  							
  						}
  						else {
  								ClientRequests cl = new ClientRequests();
  								cl.logoutUser(rcvUser,rcvPass);
  								// handle timeout 
  								if(com.webservice.client.ClientRequests.respLogout.equals("timeout") || com.webservice.client.ClientRequests.respLogout.equals("")) {
  									Toast.makeText(getActivity(),"Server is not available.\nTry again Later",Toast.LENGTH_SHORT).show();
  								}
  								//erase the login preferences 
  								SharedPreferences sp = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
 								SharedPreferences.Editor editor = sp.edit();
 						        editor.clear();
 						        editor.commit();
 						        // go back to MainActivity 
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
           
           received = ((TextView)fragView.findViewById(R.id.uidText1));
           // set the tag of the fragment 
           String myTag = getTag();
           ((BarActivity)getActivity()).setfragInterfaces(myTag);
          
         return  fragView;
    }  
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
 
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();    
    }
    // set a listener for the items in the list 
    OnItemClickListener iListener = new OnItemClickListener() {
    	@Override
    	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
    		// create the data to pass to next activity 
    		String selectedFromList1 = (getListView().getItemAtPosition(arg2).toString());
    		viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
    		TextView tx = (TextView) viewPager.findViewById(R.id.uidText1); 
    		Intent statsIntent = new Intent(getActivity(), StatsActivity.class);
    			statsIntent.putExtra("user", rcvUser);
    			statsIntent.putExtra("pass", rcvPass);
    			statsIntent.putExtra("uid", tx.getText().toString());
    			statsIntent.putExtra("interface", selectedFromList1);
    			getActivity().startActivity(statsIntent);
	   
    	}
	};

	
	
	
	
	// this method communicates with the UserFragment and the BarActivity which holds the fragments
	 public void updater(String t){
		
		 // set new data to view because the user has picked a node 
		 rcvStr = received.getText().toString();
		 received.setText(t);
		 interfaces = (ArrayList<String>) ((BarActivity)getActivity()).getDatabaseAdapter().getAllNodeDevices(t);
		 adapter = new ArrayAdapter<String>(getActivity(), R.layout.simplerow, interfaces);
	     setListAdapter(adapter);
	     getListView().setOnItemClickListener(iListener);
		 adapter.notifyDataSetChanged();
	     
			
		 }
}