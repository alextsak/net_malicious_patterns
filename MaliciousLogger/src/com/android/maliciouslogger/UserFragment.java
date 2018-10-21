package com.android.maliciouslogger;



import java.util.ArrayList;
import com.webservice.client.ClientRequests;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;

import android.view.View;
import android.view.View.OnClickListener;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.Toast;


import android.app.Activity;
import android.app.AlertDialog;




public class UserFragment extends ListFragment {

	ViewPager viewPager;
	private Button filter, Logout, Refresh;
	private Boolean checkConnection;	
	private String rcvUser, rcvPass, Nodes;
	private int status;	
    private ArrayAdapter<String> adapter;
    private ArrayList<String> itemsArray=null;
   

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // initialize 
        
		rcvUser = getArguments().getString("statusUser").toString();
		rcvPass = getArguments().getString("statusUser").toString();
		status = getArguments().getInt("status");
		Nodes = getArguments().getString("Nodes").toString();			
		itemsArray = new ArrayList<String>();
		itemsArray = (ArrayList<String>) ((BarActivity)getActivity()).getDatabaseAdapter().getAllNodes();
	    	
        //Log.d("UserFragment","OnCreate");
       
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View fragView = inflater.inflate(R.layout.user_layout, container, false);
    	
    	filter = (Button) fragView.findViewById(R.id.filterButton);
    	Logout = (Button) fragView.findViewById(R.id.logoutButton);
    	Refresh = (Button) fragView.findViewById(R.id.RefreshButton);
    
    	if(Nodes.equals("No nodes")){
    		Toast.makeText(getActivity(), 
	    		     "No Nodes" , Toast.LENGTH_SHORT).show();
    	}
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
 						//we don't have internet, send to sqlite only
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
 						        //go back to MainActivity
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
    	 // handle the refresh button
    	 Refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(status == 1) { // if it's the administrator, clear the itemsArray, pass the new values from sqlite and add them to adapter for the listview
					itemsArray.clear();
					itemsArray = (ArrayList<String>) ((BarActivity)getActivity()).getDatabaseAdapter().getAllNodes();
					adapter.clear();
					adapter.addAll(itemsArray);					
					adapter.notifyDataSetChanged();
				}
				else { // it's a user
					if(((BarActivity)getActivity()).getFiltered() == 0) { //if the user hasn't filtered
						itemsArray.clear();						
						itemsArray = (ArrayList<String>) ((BarActivity)getActivity()).getDatabaseAdapter().getAllNodes();
						adapter.clear();
						adapter.addAll(itemsArray);						
						adapter.notifyDataSetChanged();
					}
					else { // clear the itemsArray, pass the new values for the user from sqlite and add them to adapter for the listview
						itemsArray.clear();						
						itemsArray = (ArrayList<String>) ((BarActivity)getActivity()).getDatabaseAdapter().getAllUserNodes(rcvUser);
						adapter.clear();
						adapter.addAll(itemsArray);						
						adapter.notifyDataSetChanged();
						((BarActivity)getActivity()).setFiltered(1);
					}
				}

				Toast.makeText(getActivity(), "Refreshed", Toast.LENGTH_SHORT).show();
				
			}
		});

    	// if it's the administrator erase the filter button 
    	if(status == 1){
    		
    		filter.setVisibility(View.GONE);
    		filter.setOnClickListener(null);
    	}
    	else {
    		
    		// gather the nodes of the user 
    		filter.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
				
					itemsArray.clear();
					itemsArray = (ArrayList<String>) ((BarActivity)getActivity()).getDatabaseAdapter().getAllUserNodes(rcvUser);
					adapter.clear();
					adapter.addAll(itemsArray);					
					adapter.notifyDataSetChanged();
					((BarActivity)getActivity()).setFiltered(1);
					Toast.makeText(getActivity(), "Filtered", Toast.LENGTH_SHORT).show();
				}
			});
    	}
    	
    	return fragView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.simplerow, itemsArray);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(iListener);
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
    
    OnItemClickListener iListener = new OnItemClickListener() {
    	@Override
    	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
    		
    		viewPager = (ViewPager) getActivity().findViewById(R.id.pager);    		
    		String selectedFromList = (getListView().getItemAtPosition(arg2).toString());
    		if(((BarActivity)getActivity()).getDatabaseAdapter().isAdmin(rcvUser, rcvPass).equals("0")) { // make a check first for the user
    			ArrayList<String> templist = new ArrayList<String>();
    			templist = (ArrayList<String>) ((BarActivity)getActivity()).getDatabaseAdapter().getAllUserNodes(rcvUser);
    			int found=0;
    			for(String s : templist) {
    				if(!selectedFromList.equals(s)) { //if the user presses a node, that hes has no permission, without filtering the list 
    					Toast.makeText(getActivity(), "You don't have permissions for this node", Toast.LENGTH_SHORT).show();
    				}
    				else {
    					found = 1;
    					break;
    		    		
    				}
    			}
    			if(found == 1) { // pass the node value to the next fragment
    				String TabOfFrag = ((BarActivity)getActivity()).getfragInterfaces();	    		   
    				InterfacesFragment interfragment = (InterfacesFragment)getActivity().getSupportFragmentManager().findFragmentByTag(TabOfFrag);    					    		   
    				interfragment.updater(selectedFromList);	    		  
    				viewPager.setCurrentItem(1);
    			}
				
    		}
    		else { // pass the node value to the next fragment
    			String TabOfFrag = ((BarActivity)getActivity()).getfragInterfaces();  
	    		InterfacesFragment interfragment = (InterfacesFragment)getActivity().getSupportFragmentManager().findFragmentByTag(TabOfFrag);	    		   
	    		interfragment.updater(selectedFromList);	    		  
	    		viewPager.setCurrentItem(1);
    		}
    		
    		   
    		   
    	}
	};

	


	
}