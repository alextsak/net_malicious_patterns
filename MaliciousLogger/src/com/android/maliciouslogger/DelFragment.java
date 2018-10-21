package com.android.maliciouslogger;

import java.io.IOException;
import java.util.ArrayList;

import com.packetMem.shared.PropertiesMemory;
import com.webservice.client.ClientRequests;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow.LayoutParams;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class DelFragment extends Fragment {

	private Button Logout, delete, Refresh;	

	private String rcvUser, rcvPass;
	private EditText nodeToDel;
	
	private ArrayList<String> nodes;
	private Boolean checkConnection;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // initialize 
        
		nodes = new ArrayList<String>();
		rcvUser = getArguments().getString("statusUser").toString();
		rcvPass = getArguments().getString("statusUser").toString();
		nodes = (ArrayList<String>) ((BarActivity)getActivity()).getDatabaseAdapter().getAllNodes();
		// set the tag of the fragment
		String myTag = getTag();
        ((BarActivity)getActivity()).setDelFrag(myTag);
		
    }
	
	public boolean deleter(String delNode){ // returns true if the deletion of the node was successful
		synchronized (PropertiesMemory.getInstance().getLock1()){
		final com.network.manager.InternetConnection nm = new com.network.manager.InternetConnection(getActivity().getApplicationContext());
		checkConnection = nm.internetAlive();
		if(checkConnection == false){
		//we don't have internet, send to sqlite only
			((BarActivity)getActivity()).getDatabaseAdapter().deleteNode(delNode);
			((BarActivity)getActivity()).getDatabaseAdapter().insertDeletionNode(delNode, rcvUser, rcvPass);
		}
		else {
			// we have internet
			
			ClientRequests cr = new ClientRequests();
			cr.delete(rcvUser, rcvPass, delNode);
			
			if(com.webservice.client.ClientRequests.nodeDelete.equals("Problem")){
				Toast.makeText(getActivity(), "A problem Occured \n on deletion", Toast.LENGTH_SHORT).show();
				return false;
			}
			else if(com.webservice.client.ClientRequests.nodeDelete.equals("timeout") || com.webservice.client.ClientRequests.nodeDelete.equals("")){
				Toast.makeText(getActivity(), "Server is down\n Please try again.", Toast.LENGTH_SHORT).show();
				return false;
			}
			else if(com.webservice.client.ClientRequests.nodeDelete.equals("Success Delete")) {
			    ((BarActivity)getActivity()).getDatabaseAdapter().deleteNode(delNode);
			   }
		}
	  }
		return true;
	}
	
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
         View fragView = inflater.inflate(R.layout.del_layout, container,false); 
         nodeToDel = (EditText) fragView.findViewById(R.id.node);
         delete = (Button) fragView.findViewById(R.id.delete);
         Logout=(Button)fragView.findViewById(R.id.logoutButton);
         Refresh = (Button)fragView.findViewById(R.id.RefreshButtonDel);
         
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
 						        //go to MainActivity
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
         
         
         
         // create a table which holds the nodes
         final TableLayout table = (TableLayout) fragView.findViewById(R.id.nodeTable);
         
         
         
         TableRow tr = new TableRow(getActivity());
			TableLayout.LayoutParams tableRowParams=
					new TableLayout.LayoutParams
					(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
			int leftMargin = 4;
			int topMargin=4;
			int rightMargin=4;
			int bottomMargin=4;

			tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

			tr.setLayoutParams(tableRowParams);
			tr.setId(0);
			TextView up1 = new TextView(getActivity()); 
			up1.setText("NODES");
			up1.setTextColor(Color.WHITE);
			up1.setTypeface(null, Typeface.BOLD);
			up1.setTextSize(14);
			up1.setGravity(Gravity.CENTER);
			up1.setBackground(getResources().getDrawable(R.drawable.editborder));
			tr.addView(up1);

			table.addView(tr,new TableLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
			for(int i=0;i<nodes.size();i++){
				TableRow tr1 = new TableRow(getActivity());
				TableLayout.LayoutParams tableRowParams1=
						new TableLayout.LayoutParams
						(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
				
				tableRowParams1.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
				tr1.setLayoutParams(tableRowParams1);
				tr1.setId(i+1);
				TextView t1 = new TextView(getActivity());
				t1.setText(nodes.get(i).toString());
				t1.setTextColor(Color.WHITE);
				t1.setTextSize(12);
				t1.setGravity(Gravity.CENTER);
				t1.setBackground(getResources().getDrawable(R.drawable.editborder));
				tr1.addView(t1);
				table.addView(tr1,new TableLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				
			}	
         
 
         // handle the delete button
         delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
				alertBuilder.setTitle("Deletion");
				alertBuilder.setMessage("Are you sure you want to delete " + nodeToDel.getText().toString() + "?");
				alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						String nodeID = nodeToDel.getText().toString();
						Log.d("DelFragment",nodeID);
						if(nodes.contains(nodeID))
						{
							if(deleter(nodeID)){
								for(int i=0; i<nodes.size();i++) {
									if(nodes.get(i).equals(nodeID)) {
										 table.removeViewAt(i+1);
									}
									
								}
								Toast.makeText(getActivity(), "Node: " + nodeID +  " deleted", Toast.LENGTH_SHORT).show();
								nodeToDel.setText("");
							}
							else {
								Toast.makeText(getActivity(), "A problem Occured", Toast.LENGTH_SHORT).show();
								
							}
		
						}
						else {
							Toast.makeText(getActivity(), "The Node you typed\n does not exists", Toast.LENGTH_SHORT).show();
						}
					}
				});
				alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {

					}
				});
				alertBuilder.show();
			}
		});
         // handle the refresh button
         Refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { // refresh the fragment
				nodes.clear();
				nodes = (ArrayList<String>) ((BarActivity)getActivity()).getDatabaseAdapter().getAllNodes();
				String DelTag = ((BarActivity)getActivity()).getDelFrag();
				DelFragment delFrag = (DelFragment)getActivity().getSupportFragmentManager().findFragmentByTag(DelTag);
				final FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.detach(delFrag);
				ft.attach(delFrag);
				ft.commit();
				/*MediaPlayer mp = new MediaPlayer();
				mp.reset();
				try {
					mp.setDataSource("android.resource://com.android.maliciouslogger/res/raw/atyourservicesir.wav");
					mp.prepare();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mp.start();*/
				Toast.makeText(getActivity(), "Refreshed", Toast.LENGTH_SHORT).show();
			}
		}); 
         
         
         
       return  fragView;
  } 
	
	
	
	
	
}