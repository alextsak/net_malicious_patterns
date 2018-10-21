package com.webservice.client;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.util.Log;

import com.packetMem.shared.AvailableNodes;
import com.packetMem.shared.StatisticalReports;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


/**********************************************************/
/* This class is called for making the communication with the Server.  */
/*********************************************************/
public class ClientRequests {

	private final String URL = "http://192.168.1.5:9999/WebServer/WebMonitorService?WSDL";  // Static URL
	private final String URLDYN = "http://"+ip+":9999/WebServer/WebMonitorService?WSDL";   // Dynamic URL given by the user
	private final String NAMESPACE = "http://server.webserver.com/";                     
	public static String ip=""; //for dynamic ip
	
	private int timeout = 5000;  // set a timeout for the server response
	public static String usrRegister; // register
    public static String usrLogin; // login
    public static String usrLogout;
    
    public static String respReg=""; //register response
    public static String respLogin=""; //login response
    public static String respStats=""; // statistics response
    public static String respIns="";   // insert pattern's response
    public static String nodeDelete=""; // delete node response
    public static String resPatterns=""; // retrieve patterns response
    public static String respLogout="";   // logout response
    
	public static Thread registerThread; // register thread
	public static Thread loginThread;    // login thread
	public static Thread retrieveStatsThread; // retrieve statistics thread
	public static Thread retrievePatThread;   // retrieve malicious patterns thread
	public static Thread insertPatThread;     // insert patterns thread
	public static Thread logoutThread;		  // logout thread
	public static Thread deleteThread;       // delete thread
	public static List<StatisticalReports> resplist; // response list for the statistical reports
	
	
	
	public static String getSoapAction(String NAMESPACE,String METHOD_NAME) {  // creates the soap action
	    return "\"" + NAMESPACE + METHOD_NAME + "\""; 
	}
	
/************************************************ thread for the login request *******************************/
	public void loginUser(final String username, final String password){
		loginThread = new Thread()
		{
			@Override
			public void run() {
				
				String tempuser="";
				String temppass="";
								
				try {
					// Create the request 
					SoapObject request = new SoapObject(NAMESPACE, "login");
					tempuser = username;
					PropertyInfo propInfo = new PropertyInfo();
					propInfo.name = "arg0"; 
					propInfo.setValue(tempuser);
					propInfo.setType(String.class);
					request.addProperty(propInfo);
					
					temppass = password;
					PropertyInfo propInfo1 = new PropertyInfo();
					propInfo1.name = "arg1"; 
					propInfo1.setValue(temppass);
					propInfo1.setType(String.class);
					request.addProperty(propInfo1);
											
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					
					envelope.setOutputSoapObject(request);
					HttpTransportSE ht;
					// see if we have dynamic or static server ip
					if(ip.equals("")) {
						ht = new HttpTransportSE(URL, timeout);
					}
					else {
						ht = new HttpTransportSE(URLDYN, timeout);
					}
					// call the webservice
					ht.call(getSoapAction(NAMESPACE, "login") , envelope);
					final  SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
					usrLogin = response.toString();
					
					// handle the response
					
					int flag = Integer.parseInt(usrLogin);
					if(flag==0){
						respLogin="Success Admin";
					}
					else if(flag == 1){
						respLogin="Success User";
					}
					else{
						respLogin="Please Register";
					}
										
					}catch (SocketTimeoutException e) {   // catch the timeout, if occurred
												
						respLogin = "timeout";
					}
					catch( Exception e )
	               {
						e.printStackTrace();
	               }
				
			}
			
        };
			  
        loginThread.start();
		try {
				loginThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
	 }
	
	
/*********************************************** thread for the register request *******************************/
	public void registerUser(final String username, final String password, final AvailableNodes uidNodes){
		registerThread = new Thread()
		{
			@Override
			public void run() {
				
				String tempuser="";
				String temppass="";
				
								
				try {
					// Create the request
					SoapObject request = new SoapObject(NAMESPACE, "registerUser");
					
					tempuser = username;
					PropertyInfo propInfo = new PropertyInfo();
					propInfo.name = "arg0"; 
					propInfo.setValue(tempuser);
					propInfo.setType(String.class);
					request.addProperty(propInfo);
					
					temppass = password;
					PropertyInfo propInfo1 = new PropertyInfo();
					propInfo1.name = "arg1"; 
					propInfo1.setValue(temppass);
					propInfo1.setType(String.class);
					request.addProperty(propInfo1);
					
					PropertyInfo propInfo2 = new PropertyInfo();

					propInfo2.setName("arg2"); 
					propInfo2.setValue(uidNodes);
					propInfo2.setType(uidNodes.getClass());
					request.addProperty(propInfo2);
					
					
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					envelope.setOutputSoapObject(request);
					envelope.addMapping(NAMESPACE, "arg2", uidNodes.getClass());
					HttpTransportSE ht;
					if(ip.equals("")) {
						ht = new HttpTransportSE(URL, timeout);
					}
					else {
						ht = new HttpTransportSE(URLDYN, timeout);
					}
					
					ht.call(getSoapAction(NAMESPACE,"registerUser"), envelope);
					SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
					usrRegister = response.toString();
					// handle the response
					
					boolean flag = Boolean.parseBoolean(usrRegister);
					
					if(flag){
						respReg="Success Reg";
					}
					else{
						respReg="Unsuccessful Reg";
					}
					}catch (SocketTimeoutException e) {
								
						respReg = "timeout";
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				
			}
			
        };
			  
        registerThread.start();
		try {
				registerThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
	 }
	
	
	
	
	
/*********************************************** thread for the retrieveMaliciousPatterns request *******************************/	
	public void retrieveMaliciousPatterns(final String username, final String password){
		

		retrievePatThread = new Thread()
		{
			@Override
			public void run() {
				
				String tempuser="";
				String temppass="";
								
				try {
					// Create the request 
					
					SoapObject request = new SoapObject(NAMESPACE, "retrieveMaliciousPatterns");
					tempuser = username;
					PropertyInfo propInfo = new PropertyInfo();
					propInfo.name = "arg0"; 
					propInfo.setValue(tempuser);
					propInfo.setType(String.class);
					request.addProperty(propInfo);
					
					temppass = password;
					PropertyInfo propInfo1 = new PropertyInfo();
					propInfo1.name = "arg1"; 
					propInfo1.setValue(temppass);
					propInfo1.setType(String.class);
					request.addProperty(propInfo1);
											
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					envelope.setOutputSoapObject(request);
					HttpTransportSE ht;
					if(ip.equals("")) {
						ht = new HttpTransportSE(URL, timeout);
					}
					else {
						ht = new HttpTransportSE(URLDYN, timeout);
					}
					ht.call(getSoapAction(NAMESPACE, "retrieveMaliciousPatterns") , envelope);
					final  SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
					resPatterns = response.toString();
					
					
					}catch (SocketTimeoutException e) {
						
						resPatterns = "timeout";
					}
					catch( Exception e )
	               {
						e.printStackTrace();
	               }
				
			}
			
        };
			  
        retrievePatThread.start();
		try {
			retrievePatThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
	
	 }
	
/*********************************************** thread for the retrieveStatistics request *******************************/		
	public void retrieveStatistics(final String username, final String password){
		retrieveStatsThread = new Thread()
		{
			@Override
			public void run() {
				
				String tempuser="";
				String temppass="";
								
				try {
					//create the request 
					SoapObject request = new SoapObject(NAMESPACE, "retrieveStatistics");
					tempuser = username;
					PropertyInfo propInfo = new PropertyInfo();
					propInfo.name = "arg0"; 
					propInfo.setValue(tempuser);
					propInfo.setType(String.class);
					request.addProperty(propInfo);
					
					temppass = password;
					PropertyInfo propInfo1 = new PropertyInfo();
					propInfo1.name = "arg1"; 
					propInfo1.setValue(temppass);
					propInfo1.setType(String.class);
					request.addProperty(propInfo1);
											
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					envelope.setOutputSoapObject(request);
					HttpTransportSE ht;
					if(ip.equals("")) {
						ht = new HttpTransportSE(URL, timeout);
					}
					else {
						ht = new HttpTransportSE(URLDYN, timeout);
					}
					envelope.addMapping(NAMESPACE, "retrieveStatistics",new StatisticalReports().getClass());
					ht.debug=true;
					ht.call(getSoapAction(NAMESPACE, "retrieveStatistics") , envelope);
					// Logging the raw request and response (for debugging purposes)
					//Log.d(TAG, "HTTP REQUEST:\n" + ht.requestDump);
					//Log.d(TAG, "HTTP RESPONSE:\n" + ht.responseDump);
					
					if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
					    SoapObject soapObject = (SoapObject) envelope.bodyIn;
					    Log.d(">>", "instanceof SoapObject");
					} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
					    SoapFault soapFault = (SoapFault) envelope.bodyIn;
					    Log.d(">>", "instanceof SoapFault");
					    throw new Exception(soapFault.getMessage());
					} else if (envelope.bodyIn instanceof Vector<?>) { // SoapObject = SUCCESS Vector
					    //SoapFault soapVector = (SoapFault) envelope.bodyIn;
					    Log.d(">>", "instanceof Vector");
					}
					
					
					resplist = new ArrayList<StatisticalReports>();
					//handle the response 
					@SuppressWarnings("unchecked")
					Vector<SoapObject> response = (Vector<SoapObject>)envelope.getResponse();
					@SuppressWarnings("unused")
					StatisticalReports[] srs = new StatisticalReports[response.size()];
					Log.i("responce size", String.valueOf(response.size()));
					for (int i = 0; i < response.size()-1; i++) {
						SoapObject pii = (SoapObject)response.get(i);
						if(pii.getPropertyCount() != 0) {
							StatisticalReports sr = new StatisticalReports();
							sr.UID = pii.getProperty(0).toString();
							Log.i("i", String.valueOf(i));
							Log.i("sr", sr.UID);
							sr.Local_Time = pii.getProperty(1).toString();
							Log.i("sr", sr.Local_Time);
							sr.Device = pii.getProperty(2).toString();
							Log.i("sr", sr.Device);
							sr.DeviceIP = pii.getProperty(3).toString();
							Log.i("sr", sr.DeviceIP);
							sr.Pattern = pii.getProperty(4).toString();
							Log.i("sr", sr.Pattern);
							sr.Frequency = pii.getProperty(5).toString();
							Log.i("sr", sr.Frequency);
							respStats = "Nodes";
							resplist.add(sr);
						}
						else {
							respStats = "No nodes";
						}
			        }
					Log.i("After soap","After soap");
										
					}catch (SocketTimeoutException e) {
						
						respStats = "timeout";
					}
					catch( Exception e )
	               {
						e.printStackTrace();
	               }
				
			}
			
        };
			  
        retrieveStatsThread.start();
		try {
			retrieveStatsThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
	 }
	
	
/*********************************************** thread for the delete request *******************************/	
	public void delete(final String username, final String password, final String nodeID){
		deleteThread = new Thread()
		{
			@Override
			public void run() {
				
				String tempuser="";
				String temppass="";
				String tempNode="";
								
				try {
					//create the request 
					SoapObject request = new SoapObject(NAMESPACE, "delete");
					tempuser = username;
					PropertyInfo propInfo = new PropertyInfo();
					propInfo.name = "arg0"; 
					propInfo.setValue(tempuser);
					propInfo.setType(String.class);
					request.addProperty(propInfo);
					
					temppass = password;
					PropertyInfo propInfo1 = new PropertyInfo();
					propInfo1.name = "arg1"; 
					propInfo1.setValue(temppass);
					propInfo1.setType(String.class);
					request.addProperty(propInfo1);
					
					tempNode = nodeID;
					PropertyInfo propInfo2 = new PropertyInfo();
					propInfo2.name = "arg2"; 
					propInfo2.setValue(tempNode);
					propInfo2.setType(String.class);
					request.addProperty(propInfo2);
											
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					
					envelope.setOutputSoapObject(request);
					
					HttpTransportSE ht;
					if(ip.equals("")) {
						ht = new HttpTransportSE(URL, timeout);
					}
					else {
						ht = new HttpTransportSE(URLDYN, timeout);
					}
					ht.call(getSoapAction(NAMESPACE, "delete") , envelope);
					final  SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
					nodeDelete = response.toString();
					//handle the response 
					
					boolean flag = Boolean.parseBoolean(nodeDelete);
					if(flag){
						nodeDelete = "Success Delete";
					}
					else {
						nodeDelete = "Problem";
					}
										
					}catch (SocketTimeoutException e) {
						
						nodeDelete = "timeout";
					}
					catch( Exception e )
	               {
						e.printStackTrace();
	               }
				
			}
			
        };
			  
        deleteThread.start();
		try {
			deleteThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
	 }
	
/*********************************************** thread for the insertPattern request *******************************/	
	public void insertPattern(final String username, final String password, final String maliciousIP, final String stringPatterns){
		insertPatThread = new Thread()
		{
			@Override
			public void run() {
				
				String tempuser="";
				String temppass="";
				String tempip="";
				String temppat="";
				
								
				try {
					//Log.i("CL_Ins_user",username);
					//Log.i("CL_Ins_pass",password);
					// creates the request
					SoapObject request = new SoapObject(NAMESPACE, "insertMaliciousPatterns");
					
					tempuser = username;
					PropertyInfo propInfo = new PropertyInfo();
					propInfo.name = "arg0"; 
					propInfo.setValue(tempuser);
					propInfo.setType(String.class);
					request.addProperty(propInfo);
					
					temppass = password;
					PropertyInfo propInfo1 = new PropertyInfo();
					propInfo1.name = "arg1"; 
					propInfo1.setValue(temppass);
					propInfo1.setType(String.class);
					request.addProperty(propInfo1);
					
					tempip = maliciousIP;
					if(tempip == null){
						tempip="#";
					}
					PropertyInfo propInfo2 = new PropertyInfo();
					propInfo2.name = "arg2"; 
					propInfo2.setValue(tempip);
					propInfo2.setType(String.class);
					request.addProperty(propInfo2);
					
					temppat = stringPatterns;
					if(temppat == null){
						temppat="#";
					}
					PropertyInfo propInfo3 = new PropertyInfo();
					propInfo3.name = "arg3"; 
					propInfo3.setValue(temppat);
					propInfo3.setType(String.class);
					request.addProperty(propInfo3);
						
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					
					envelope.setOutputSoapObject(request);
					
					HttpTransportSE ht;
					if(ip.equals("")) {
						ht = new HttpTransportSE(URL, timeout);
					}
					else {
						ht = new HttpTransportSE(URLDYN, timeout);
					}
					ht.call(getSoapAction(NAMESPACE, "insertMaliciousPatterns") , envelope);
					//set a dummy response because the insertMaliciousPatterns is void
					respIns = "#returned#";
					}catch (SocketTimeoutException e) {
					
						respIns = "timeout";
					}
										
					catch (Exception e) {
						e.printStackTrace();
					}
				
			}
			
        };
			  
        insertPatThread.start();
		try {
			insertPatThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
	 }
	
/*********************************************** thread for the logout request *******************************/	
	public void logoutUser(final String username, final String password){
		logoutThread = new Thread()
		{
			@Override
			public void run() {
				
				String tempuser="";
				String temppass="";
								
				try {
					//create the request
					SoapObject request = new SoapObject(NAMESPACE, "logout");
					tempuser = username;
					PropertyInfo propInfo = new PropertyInfo();
					propInfo.name = "arg0"; 
					propInfo.setValue(tempuser);
					propInfo.setType(String.class);
					request.addProperty(propInfo);
					
					temppass = password;
					PropertyInfo propInfo1 = new PropertyInfo();
					propInfo1.name = "arg1"; 
					propInfo1.setValue(temppass);
					propInfo1.setType(String.class);
					request.addProperty(propInfo1);
											
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					
					envelope.setOutputSoapObject(request);
					
					HttpTransportSE ht;
					if(ip.equals("")) {
						ht = new HttpTransportSE(URL, timeout);
					}
					else {
						ht = new HttpTransportSE(URLDYN, timeout);
					}
					ht.call(getSoapAction(NAMESPACE, "logout") , envelope);
					final  SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
					usrLogout = response.toString();
					
					//handle the response 
					Boolean flag = Boolean.parseBoolean(usrLogout);
					if(flag){
						respLogout="Success logout";
					}
					else {
						respLogout="Problem";
					}
										
					}catch (SocketTimeoutException e) {
						
						respLogout = "timeout";
					}
					catch( Exception e )
	               {
						e.printStackTrace();
	               }
				
			}
			
        };
			  
        logoutThread.start();
		try {
				logoutThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
	 }
	
	
	
	
	
	
}