package com.webserver.server;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.database.mysql.DBCommunication;
import com.packetMem.shared.ActiveNodes;
import com.packetMem.shared.AvailableNodes;
import com.packetMem.shared.MPSMemory;
import com.packetMem.shared.MaliciousPatterns;
import com.packetMem.shared.StatisticalReports;
import com.webserver.server.WebMonitorService;

@WebService(endpointInterface="com.webserver.server.WebMonitorService")


public class WebMonitorServiceImpl implements WebMonitorService {
	 
	@Override
    @WebMethod
	public boolean registerPC(String nodeID) {
		 DBCommunication con = new DBCommunication();
		 if (ActiveNodes.getInstance().searchActiveNodes(nodeID) != -1){
			 System.out.println("Node : "+nodeID+" already online");
			 return false;
		 }
		 else{
			 ActiveNodes.getInstance().insertActiveNode(nodeID);
			 if (con.insertPC(nodeID) == -1){
				 ActiveNodes.getInstance().removeNode(nodeID);
				 System.out.println("Node : "+nodeID+" failed to be inserted in Database");
				 return false;
			 }
		 }
		System.out.println("Node : "+nodeID+" is online");
		return true;
	}

	@Override
	@WebMethod 
	public MaliciousPatterns maliciousPatternsRequest(String nodeID) {
		DBCommunication con = new DBCommunication();
		//System.out.println("Searching new patterns for "+nodeID);
		MaliciousPatterns temp = new MaliciousPatterns();
		int count = 0, pos = -1;
		if (con.NodeExistance(nodeID)==false){
			temp.setIpposition(-13);
			return temp;
		}
		
		if (MPSMemory.getInstance().getMaliciousPatterns().size() != 0){
		for (String tnode : MPSMemory.getInstance().getMaliciousPatterns()){
			if (MPSMemory.getInstance().getMaliciousPatterns().indexOf(tnode) > ActiveNodes.getInstance().getNodeDataPatternpos(nodeID)){
				temp.addPatternNode(tnode, -1);
				count++;
				pos++;
			}
		}
		ActiveNodes.getInstance().setNodeDataPatternpos(nodeID, ActiveNodes.getInstance().getNodeDataPatternpos(nodeID)+count);
		}
		count = 0;
		if (MPSMemory.getInstance().getMaliciousIPs().size() != 0){
			for (String tnode : MPSMemory.getInstance().getMaliciousIPs()){
				if (MPSMemory.getInstance().getMaliciousIPs().indexOf(tnode) > ActiveNodes.getInstance().getNodeDataIPpos(nodeID)){
					temp.addPatternNode(tnode, pos+1);
					count++;
				}
			}
			ActiveNodes.getInstance().setNodeDataIPpos(nodeID, ActiveNodes.getInstance().getNodeDataIPpos(nodeID)+count);
		}
		
		if (temp.getNewPatterns().size() == 0){
			System.out.println("No new patterns for " +nodeID);
			temp = null;
		}
		else{
			System.out.println("New Patterns for "+nodeID+" : " +temp.getNewPatterns().toString());
		}
		return temp;
	}

	@Override
	@WebMethod 
	public void maliciousPatternsStatisticalReport(String nodeID, StatisticalReports m) {
		DBCommunication con = new DBCommunication();
		System.out.println("Received Statistical Report from "+nodeID+" > Node's local time : "+m.Local_Time);
		con.insertStatisticalReport(nodeID, m.Local_Time, m);
	}

	@Override
	@WebMethod 
	public boolean unregister(String nodeID) {
		if (ActiveNodes.getInstance().searchActiveNodes(nodeID) != -1){
			ActiveNodes.getInstance().removeNode(nodeID);
			System.out.println("Unregistering Node : "+nodeID);
			return true;
		 }
		return false;
	} 
	
/********************************Project 3***********************************************************************/

	@Override
	@WebMethod(operationName = "registerUser")
    public boolean register(String username, String password, AvailableNodes nodes){
		DBCommunication con = new DBCommunication();
		System.out.println("Registering User : "+username);
		String[] templist = nodes.UID.split("#");
		/*for (String temp : templist){
			System.out.println(temp);
		}*/
		int exists = con.checkUserexistance(username, password);
		if (exists != 1){ //if user already exists then return error
			System.out.println("User already registered.");
			return false;
		}
		else if (con.insertUser(username, password, templist) == 0){
			System.out.println("New user.");
			return true;
		}
		else if(con.insertUser(username, password, templist) == 1){
			System.out.println("User already registered in database.");
		}
		else{
			System.out.println("Error registering user.");
		}
		return false;
    }

	@Override
	@WebMethod 
    public List<StatisticalReports> retrieveStatistics(String username, String password){
		DBCommunication con = new DBCommunication();
		int exists = con.checkUserexistance(username, password);
		if (exists != 0){ //If user doesn't exists then return error
			return null;
		}
		List<StatisticalReports> returnlist = new ArrayList<StatisticalReports>();
		int first=1,i=0,k=0,entry=0;
		//for(String ntemp : con.AllNodes()){
		for(k=0;k<ActiveNodes.getInstance().getNodes().size();k++){
			String ntemp = ActiveNodes.getInstance().getNodeDataUID(k);
			StatisticalReports ltemp = new StatisticalReports();
			for(String ts : con.getStatisticsList(ntemp)){
				if (ts==null){
					System.out.println("NULL");
					continue;
				}
				if (first==1){
					first=0;
					String [] tl = ts.split(" | ");
					/*for(int k=0;k<tl.length;k++){
						System.out.println(" > "+k+" >> "+tl[k]);
					}*/
					ltemp.UID=tl[2];
					ltemp.Local_Time=tl[4]+" "+tl[5];
					ltemp.Device=tl[7];
					ltemp.DeviceIP=tl[9];
					ltemp.Pattern=tl[11];
					ltemp.Frequency=tl[13];
					entry=1;
				}
				else{
					String [] tl = ts.split(" | ");
					ltemp.Device=ltemp.Device+"#"+tl[7];
					ltemp.DeviceIP=ltemp.DeviceIP+"#"+tl[9];
					ltemp.Pattern=ltemp.Pattern+"#"+tl[11];
					ltemp.Frequency=ltemp.Frequency+"#"+tl[13];
				}
			}
			first=1;
			if (entry==0){
				continue;
			}
			returnlist.add(ltemp);
			entry=0;
			//i++;
		}
		/*if (i==0){
			//System.out.println("No nodes");
			returnlist.add(new StatisticalReports());
		}
		returnlist.add(new StatisticalReports());*/
		i=0;
		for(String ntemp : con.AllNodes()){
			i++;
		}
		if (i==0){
			StatisticalReports ltemp = new StatisticalReports();
			ltemp.UID="No Nodes";
			ltemp.Local_Time="No Nodes";
			ltemp.Device="No Nodes";
			ltemp.DeviceIP="No Nodes";
			ltemp.Pattern="No Nodes";
			ltemp.Frequency="No Nodes";
			returnlist.add(ltemp);
		}
		else if (k==0){
			//System.out.println("No nodes");
			StatisticalReports ltemp = new StatisticalReports();
			ltemp.UID="No Online Nodes";
			ltemp.Local_Time="No Online Nodes";
			ltemp.Device="No Online Nodes";
			ltemp.DeviceIP="No Online Nodes";
			ltemp.Pattern="No Online Nodes";
			ltemp.Frequency="No Online Nodes";
			returnlist.add(ltemp);
		}
		returnlist.add(new StatisticalReports());
		return returnlist;
    	
    }

	@Override
	@WebMethod 
    public String retrieveMaliciousPatterns(String username, String password){
		DBCommunication con = new DBCommunication();
		int entered=0;
		int exists = con.checkUserexistance(username, password);
		if (exists != 0){ //If user doesn't exists then return error
			return "Worng User";
		}
		String result=null;
		for(String temp : MPSMemory.getInstance().getMaliciousIPs()){
			if (result==null){
				result=temp+"#";
			}
			else{
				result=result+temp+"#";
			}
			entered=1;
		}
		if(entered!=1){
			result="NOIPS#";
		}
		result=result+"ENDOFIPS#";
		for(String temp : MPSMemory.getInstance().getMaliciousPatterns()){
			result=result+temp+"#";
			entered=2;
		}
		if(entered!=2){
			result=result+"NOPATTERNS#";
		}
		return result;
    	
    }

	@Override
	@WebMethod 
    public void insertMaliciousPatterns(String username, String password, String maliciousIP, String stringPatterns){
		DBCommunication con = new DBCommunication();
		System.out.println("Malicious Patterns :");
		if (con.checkAdministrationRights(username, password) == 0){
			if (!maliciousIP.equals("#")){
				System.out.println("Given IPs : "+maliciousIP);
				String[] temp1=maliciousIP.split("#");
				for(String temp:temp1){
					MPSMemory.getInstance().insertPattern(temp, 1);
				}
			}
			if (!stringPatterns.equals("#")){
				System.out.println("Given Patterns : "+stringPatterns);
				String[] temp2=stringPatterns.split("#");
				for(String temp:temp2){
					MPSMemory.getInstance().insertPattern(temp, 0);
				}
			}
		}
		else{
			System.out.println("Pattern was not inserted.");
		}
    }
	
	@Override
	@WebMethod 
	public int login(String username, String password){
		DBCommunication con = new DBCommunication();
		System.out.println("User : "+username+" is loging in.");
		if (con.checkUserexistance(username, password) == 0){
			return con.checkAdministrationRights(username, password);
		}
		else{
			return -1;
		}
	}
	
	@Override
	@WebMethod
	public boolean logout(String username, String password){
		DBCommunication con = new DBCommunication();
		System.out.println("User : "+username+" is loging out.");
		if (con.checkUserexistance(username, password) == 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	@Override
	@WebMethod 
	public boolean delete (String username, String password, String nodeID){
		DBCommunication con = new DBCommunication();
		System.out.println("User : "+username+" deleting node : "+nodeID);
		if (con.checkAdministrationRights(username, password) == 0){
			if (con.NodeExistance(nodeID) ==  true){
				if (con.deleteNode(username, password, nodeID) == 0){
					System.out.println("Node : "+nodeID+" removed");
					ActiveNodes.getInstance().removeNode(nodeID);
					return true;
				}
				else{
					return false;
				}
			}
		}
		return false;
	}
}