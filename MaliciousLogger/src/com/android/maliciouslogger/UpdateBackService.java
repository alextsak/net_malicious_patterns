package com.android.maliciouslogger;

import java.util.ArrayList;

import com.database.sqlite.DatabaseAdapter;
import com.packetMem.shared.PropertiesMemory;
import com.packetMem.shared.StatisticalReports;
import com.webservice.client.ClientRequests;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**************************************************************************/
/*
 * This class creates an update service and runs jobs for the application. 
 */
/*************************************************************************/

public class UpdateBackService extends Service {

	DatabaseAdapter da;
	private ServiceUpdater su;
	private String username;
	private String password;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	} 
	
	@Override
	public void onCreate () {
		super.onCreate();
		su = new ServiceUpdater();
		
		
		da = new DatabaseAdapter(this);
		da = da.open();
		Log.d("UpdateBackService","onCreate");
	}
	
	
	 @Override
	  public synchronized int onStartCommand(Intent intent, int flags, int startId) {
		 
		 	username = intent.getExtras().getString("username");
			password = intent.getExtras().getString("password");
			if(!su.isRunning) {
				su.start();
			}
			Log.d("UpdateBackService", "OnStart");
	   
	    return Service.START_NOT_STICKY;
	  }

	 
	@Override
	public synchronized void onDestroy() {
		super.onDestroy();
		//stop the Updater
		if(su.isRunning){
			su.interrupt();		
		}
		su=null;
		Log.d("UpdateBackService", "onDestroy");
	}
	
	
	class ServiceUpdater extends Thread{
		static final long delay=5000; // set some delay to the thread
		private boolean isRunning=false;

		public ServiceUpdater(){
			super("ServiceUpdater");
		}

		@Override
		public void run() {
			isRunning=true;
			
			
			while (isRunning) {
				try {
					Log.d("UpdateBackService", "Service Running");
					
					final com.network.manager.InternetConnection network = new com.network.manager.InternetConnection(getApplicationContext());
					Log.d("UpdateBackService", username);
					Log.d("UpdateBackService", password);
					//code for retrieving malicious paterns
						Boolean checkConnection;
						checkConnection = network.internetAlive();
						if(checkConnection){ /// if we have internet continue with update
							
							ClientRequests cr = new ClientRequests(); // create a client request object 
							if(da.isAdmin(username, password).equals("1")) { // check if the user online is the admin 
		/*************************** 1st work check for deletion **************************************************/
								
								// see on sqlite if we have something pending
								ArrayList<String> delList = new ArrayList<String>();
								delList = (ArrayList<String>) da.getAllDeletionNodes();
								// try to delete nodes from the server
									for(int i=0;i<delList.size();i++){
										String[] temp = delList.get(i).split("#");
										cr.delete(temp[1], temp[2], temp[0]);
										if(com.webservice.client.ClientRequests.nodeDelete.equals("timeout") || com.webservice.client.ClientRequests.nodeDelete.equals("")) {
											Log.i("UpdateBackService","timeout");
										}
										else if(com.webservice.client.ClientRequests.nodeDelete.equals("Success Delete")){
											// delete node from the deletion table 
											da.deleteNodefromDeletion(temp[0]);
											da.deleteNode(temp[0]);
										}
									}
								}	
								
							
/**************************************** 2nd work check the "retrieve malicious patterns" **********************************************************************************************/		
							
							cr.retrieveMaliciousPatterns(username, password);
							//split to ips and stringPatterns
							String maliciousPats = com.webservice.client.ClientRequests.resPatterns;
							Log.i("maliciousPats",maliciousPats);	
							String maliciousParts[] = maliciousPats.split("ENDOFIPS#");
							
							String listOfIPs = maliciousParts[0]; // gather all the ips
							Log.i("listOfIPs",listOfIPs);
							String listOfStrings = maliciousParts[1]; // gather all the string patterns	
							Log.i("listOfStrings",listOfStrings);
							String[] IPs = listOfIPs.split("#");
							String[] strPats = listOfStrings.split("#");
							int found=0;
							if(!listOfIPs.equals("NOIPS#")) {
								for(int i=0;i<IPs.length;i++){
									for(int j=0;j<da.getIPPatterns().size();j++){
										if(da.getIPPatterns().get(j).equals(IPs[i])){
											found=1;
											break;
										}
									}
									if(found==0){
										da.insertPattern(IPs[i], 1, 1);
									}
									found=0;
								}
								found=0;
								for(int i=0;i<da.getIPPatterns().size();i++){
									for(int j=0;j<IPs.length;j++){
										if(IPs[j].equals(da.getIPPatterns().get(i))){
											found=1;
											break;
										}
									}
									if(found==0){
										cr.insertPattern(username, password, da.getIPPatterns().get(i), null);
										if(com.webservice.client.ClientRequests.respIns.equals("timeout") || com.webservice.client.ClientRequests.respIns.equals("")) {
											Log.i("UpdateBackService","timeout");
										}
										da.deletePattern(da.getIPPatterns().get(i));
										da.insertPattern(da.getIPPatterns().get(i), 1, 1);
									}
									found=0;
								}
							}
							found=0;
							if(!listOfStrings.equals("NOPATTERNS#")) {
								for(int i=0;i<strPats.length;i++){
									for(int j=0;j<da.getStringPatterns().size();j++){
										if(da.getStringPatterns().get(j).equals(strPats[i])){
											found=1;
											break;
										}
										if(found==0){
											da.insertPattern(strPats[i], 0, 1);
										}
										found=0;
									}
								}
								found=0;
								for(int i=0;i<da.getStringPatterns().size();i++){
									for(int j=0;j<strPats.length;j++){
										if(strPats[j].equals(da.getStringPatterns().get(i))){
											found=1;
											break;
										}
										if(found==0){
											cr.insertPattern(username, password, null, da.getStringPatterns().get(i));
											if(com.webservice.client.ClientRequests.respIns.equals("timeout") || com.webservice.client.ClientRequests.respIns.equals("")) {
												Log.i("UpdateBackService","timeout");
											}
											da.deletePattern(da.getStringPatterns().get(i));
											Log.d("UpadateBackService", da.getStringPatterns().get(i).toString());
											da.insertPattern(da.getStringPatterns().get(i), 0, 1);
										}
										found=0;
									}		
								}
							}
		/****************************** 3rd work check for new statistics ***********************************************************************************************/		
					    synchronized (PropertiesMemory.getInstance().getLock1()){
						// retrieve new statices 
						cr.retrieveStatistics(username, password);
						// check for timeout
						if(com.webservice.client.ClientRequests.respStats.equals("timeout") || com.webservice.client.ClientRequests.respStats.equals("")) {
							Log.i("UpdateBackService","timeout");
						}
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
							 if(!tempUid.equals(stats.UID)){ // don't insert the same uid
								 da.insertNode(stats.UID);
								 tempUid = stats.UID;
							 }
							 if(stats.UID.equals("No Nodes")) {  // if there are no nodes on server side, delete all nodes from sqlite
								 da.deleteAllNodes();
								 break;
							 }
							 for(int j=0;j<devList.length;j++){ // insert new statistical report to sqlite
								 da.insertStatisticalReports(stats.UID, stats.Local_Time, devList[j], devIpList[j], devPatList[j], devFreqList[j]);
							 } 
						   }
						 }
			}
						Log.d("UpdateBackService","Ending Update");
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					isRunning=false;
					
				}
			}

		}
		public boolean isRunning(){
			return this.isRunning();
		}
		
	}


}