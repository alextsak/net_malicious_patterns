package com.network.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnection {

	private Context cont;
	public InternetConnection(Context context){
		this.cont=context;
	}
	// determine with a system service if we have internet, returns true on success
	public boolean internetAlive(){
		ConnectivityManager connection = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connection!=null){
			NetworkInfo[] info = connection.getAllNetworkInfo();
			if(info!=null)
				for(int i=0; i<info.length; i++) {
					if(info[i].getState()==NetworkInfo.State.CONNECTED){
						return true;
					}
				}
		}
		return false;
	}
	
	
	
}
