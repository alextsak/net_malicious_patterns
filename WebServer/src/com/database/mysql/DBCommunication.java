package com.database.mysql;

import java.sql.*;
import java.util.ArrayList;
import com.packetMem.shared.PropertiesMemory;
import com.packetMem.shared.StatisticalReports;

public class DBCommunication {
	
	static String sql;
    PreparedStatement statement =null;
    ResultSet resultSet = null;
    
    public int insertPC(String nodeID){ //If not already exists, insert PC/Laptop's UID into the database
    	String sqlquery = null;
    	PreparedStatement pstatement = null;
    	Statement statement = null;
    	sqlquery = "select UID from pc_uid where UID='"+nodeID+"'";
    	ResultSet answer = null;
    	try {
    	synchronized (PropertiesMemory.getInstance().getLock1()){
    		statement = Connector.getConnection().createStatement();
			answer = statement.executeQuery(sqlquery);
    		int count = 0;
        	while(answer.next()){
				count++;
			}
        	//System.out.println("In connection DB : "+count);
        	if (count > 0){
        		return 1;
        	}
        	else{
        		sqlquery = "insert into pc_uid(UID) values (?)";
        		pstatement = Connector.getConnection().prepareStatement(sqlquery);
				pstatement.setString(1, nodeID);
				pstatement.addBatch();
				if (pstatement.executeBatch().length > 0){
					return 0;
				}
				else{
					return -1;
				}
        	}
    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        finally{
        	try{
        		if(pstatement != null)
        			pstatement.close();
        		if(statement != null)
        			statement.close();
        	}catch(SQLException e){
        	return -1;
        	}
        }
        return 0;
    }
    
    public int insertStatisticalReport(String nodeID, String local_time, StatisticalReports m){ //Insert Statistical report from received node into the database
    	String sqlquery = null;
    	PreparedStatement pstatement = null;
    	try {
    	synchronized (PropertiesMemory.getInstance().getLock1()){
    		for(int i=0; i<m.getValues(1).length; i++){
        		sqlquery = "insert into StatisticalReports(UID,LOCAL_TIME,NODE_DATA) values (?,?,?)";
        		pstatement = Connector.getConnection().prepareStatement(sqlquery);
				pstatement.setString(1, nodeID);
				pstatement.setString(2, local_time);
				String temp = m.getValues(1)[i]+" | "+m.getValues(2)[i]+" | "+m.getValues(3)[i]+" | "+m.getValues(4)[i].split("\n")[0];
				pstatement.setString(3, temp);
				pstatement.addBatch();
				if (pstatement.executeBatch().length <= 0){
					//System.out.println("Length <= 0");
					return -1;
				}
    		}
    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        finally{
        	try{
        		if(pstatement != null)
        			pstatement.close();
        	}catch(SQLException e){
        	return -1;
        	}
        }
        return 0;
    }

	@SuppressWarnings("finally")
	public ArrayList<String> getStatisticsList(String nodeID){ //Get latest statistical report for the given PC/Laptop's UID
		ArrayList<String> statsList = new ArrayList<String>();
		ResultSet set1=null;
		PreparedStatement pstate =null;
		try {
			synchronized (PropertiesMemory.getInstance().getLock1()) {
				sql = null;
				sql = "select * from StatisticalReports where LOCAL_TIME = (select MAX(LOCAL_TIME) from StatisticalReports s where s.UID = ?) and UID = ?;";
				pstate = Connector.getConnection().prepareStatement(sql);
				pstate.setString(1, nodeID);
				pstate.setString(2, nodeID);
				pstate.addBatch();
				set1 = pstate.executeQuery();
				while (set1.next()) {
					String temp="";
					temp+=(" | "+set1.getString("UID")+" | "+set1.getString("LOCAL_TIME")+" | " + set1.getString("NODE_DATA"));
					
					statsList.add(temp);
				}
			}
			if(pstate!=null)
				pstate.close();
			if(set1!=null)
				set1.close();
		} catch (SQLException e) {
			e.printStackTrace();
			if(pstate!=null && set1!=null)
				try {
					pstate.close();
					set1.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		}
		finally{
			//System.out.println("Size : "+statsList.size()+" Statistics list : "+statsList);
			return statsList;
		}
	}
	
	@SuppressWarnings("finally")
	public static boolean executeSQL(String sql){ //Execute given sql query

        boolean result = true;
        try {
            Statement statement = null;
            statement = Connector.getConnection().createStatement();
            String[] inst = sql.toString().split(";");
            for(int i = 0; i<inst.length; i++){  
                if(!inst[i].trim().equals("")){  
                    statement.executeUpdate(inst[i]);  
                }  
            }
        } catch (SQLException ex) {
            System.err.println("Error at ExecuteSQL");
            result = false;
        }
        finally{
            return result;
        }
    }
	
/*************************Project3 Functions***************************************************/
	
	@SuppressWarnings("finally")
	public ArrayList<String> getOnlineUsers(){ //Get Online Users
		ArrayList<String> usersList = new ArrayList<String>();
		ResultSet set1=null;
		PreparedStatement pstate =null;
		try {
			synchronized (PropertiesMemory.getInstance().getLock1()) {
				sql = null;
				sql = "select * from users where user_status = ?;";
				pstate = Connector.getConnection().prepareStatement(sql);
				pstate.setString(1, "ONLINE");
				pstate.addBatch();
				set1 = pstate.executeQuery();
				while (set1.next()) {
					usersList.add(set1.getString("username"));
				}
			}
			if(pstate!=null)
				pstate.close();
			if(set1!=null)
				set1.close();
		} catch (SQLException e) {
			e.printStackTrace();
			if(pstate!=null && set1!=null)
				try {
					pstate.close();
					set1.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		}
		finally{
			//System.out.println("Size : "+statsList.size()+" Statistics list : "+statsList);
			return usersList;
		}
	}
	
	public int insertUser(String username, String password, String[] nodes){ //If not already exists, insert PC/Laptop's UID into the database
    	String sqlquery = null;
    	PreparedStatement pstatement = null;
    	Statement statement = null;
    	sqlquery = "select username from users where username='"+username+"'";
    	ResultSet answer = null;
    	try {
    	synchronized (PropertiesMemory.getInstance().getLock1()){
    		statement = Connector.getConnection().createStatement();
			answer = statement.executeQuery(sqlquery);
    		int count = 0;
        	while(answer.next()){
				count++;
			}
        	//System.out.println("In connection DB : "+count);
        	if (count > 0){
        		return 1;
        	}
        	else{
        		sqlquery = "insert into users(username,user_password,isAdmin) values (?,?,?)";
        		pstatement = Connector.getConnection().prepareStatement(sqlquery);
				pstatement.setString(1, username);
				pstatement.setString(2, password);
				pstatement.setString(3, "NO");
				pstatement.addBatch();
				if (pstatement.executeBatch().length <= 0){
					return -1;
				}
        	}
        	//System.out.println(nodes);
        	if (nodes != null){
        		for(String node : nodes){
        			//System.out.println(node);
        			sqlquery = "select UID from pc_uid where UID='"+node+"'";
        			answer = statement.executeQuery(sqlquery);
        			int counts=0;
                	while(answer.next()){
        				counts++;
        			}
                	if (counts==0){
                		sqlquery = "insert into pc_uid(UID) values (?)";
            			pstatement = Connector.getConnection().prepareStatement(sqlquery);
            			pstatement.setString(1, node);
            			pstatement.addBatch();
            			if (pstatement.executeBatch().length <= 0){
            				return -1;
            			}
            			if(pstatement != null)
                			pstatement.close();
                	}
        			sqlquery = "insert into users_has_pcs(UID,username) values (?,?)";
        			pstatement = Connector.getConnection().prepareStatement(sqlquery);
        			pstatement.setString(1, node);
        			pstatement.setString(2, username);
        			pstatement.addBatch();
        			if (pstatement.executeBatch().length <= 0){
        				return -1;
        			}
        		}
        	}
    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        finally{
        	try{
        		if(pstatement != null)
        			pstatement.close();
        		if(statement != null)
        			statement.close();
        	}catch(SQLException e){
        	return -1;
        	}
        }
        return 0;
    }
	
	public ArrayList<String> UserNodes(String username, String password){ //Return a list of nodes of the given user
    	String sqlquery = null;
    	PreparedStatement pstatement = null;
    	Statement statement = null;
    	sqlquery = "select username from users where username='"+username+"'";
    	ResultSet answer = null;
    	ArrayList<String> templist = new ArrayList<String>();
    	try {
    	synchronized (PropertiesMemory.getInstance().getLock1()){
    		statement = Connector.getConnection().createStatement();
			answer = statement.executeQuery(sqlquery);
    		int count = 0;
        	while(answer.next()){
				count++;
			}
        	//System.out.println("In connection DB : "+count);
        	if (count > 0){
        		return null;
        	}
        	else{
        		sqlquery = "select UID from users_has_pcs where username = ?";
        		pstatement = Connector.getConnection().prepareStatement(sqlquery);
				pstatement.setString(1, username);
				pstatement.addBatch();
				ResultSet set1 = pstatement.executeQuery();
				while (set1.next()) {
					templist.add(set1.getString("UID"));
				}
        	}
    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        finally{
        	try{
        		if(pstatement != null)
        			pstatement.close();
        		if(statement != null)
        			statement.close();
        	}catch(SQLException e){
        	return null;
        	}
        }
        return templist;
    }
	
	public ArrayList<String> AllNodes(){ //Return a list of nodes
    	String sqlquery = null;
    	PreparedStatement pstatement = null;
    	ResultSet answer = null;
    	ArrayList<String> templist = new ArrayList<String>();
    	try {
    	synchronized (PropertiesMemory.getInstance().getLock1()){
        	sqlquery = "select UID from pc_uid";
        	pstatement = Connector.getConnection().prepareStatement(sqlquery);
			pstatement.addBatch();
			answer = pstatement.executeQuery();
			while (answer.next()) {
				templist.add(answer.getString("UID"));
			}
    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        finally{
        	try{
        		if(pstatement != null)
        			pstatement.close();
        	}catch(SQLException e){
        	return null;
        	}
        }
        return templist;
    }
	
	public int checkAdministrationRights(String username, String password){ //If user is an administrator then return 0 else 1 (if returns -1 then something went wrong)
    	String sqlquery = null, temp=null;
    	PreparedStatement pstatement = null;
    	Statement statement = null;
    	sqlquery = "select isAdmin from users where username='"+username+"' and user_password='"+password+"'";
    	ResultSet answer = null;
    	try {
    	synchronized (PropertiesMemory.getInstance().getLock1()){
    		statement = Connector.getConnection().createStatement();
			answer = statement.executeQuery(sqlquery);
    		int count = 0;
        	while(answer.next()){
				count++;
				temp=answer.getString("isAdmin");
			}
        	//System.out.println("In connection DB : "+count);
        	if (temp == null){
        		return -1;
        	}
        	else if (temp.equals("YES")){
        		return 0;
        	}
        	else{
        		return 1;
        	}
    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        finally{
        	try{
        		if(pstatement != null)
        			pstatement.close();
        		if(statement != null)
        			statement.close();
        	}catch(SQLException e){
        	return -1;
        	}
        }
        return 1;
    }
	
	public int checkUserexistance(String username, String password){ //If user exists then return 0 else 1 (if returns -1 then something went wrong)
    	String sqlquery = null, temp=null;
    	PreparedStatement pstatement = null;
    	Statement statement = null;
    	sqlquery = "select username, user_password from users where username='"+username+"' and user_password='"+password+"'";
    	ResultSet answer = null;
    	try {
    	synchronized (PropertiesMemory.getInstance().getLock1()){
    		statement = Connector.getConnection().createStatement();
			answer = statement.executeQuery(sqlquery);
    		int count = 0;
        	while(answer.next()){
				count++;
				temp=answer.getString("username");
			}
        	//System.out.println("In connection DB : "+count);
        	if (temp == null){
        		//System.out.println("Not sush a user");
        		return 1;
        	}
        	else if (temp.equals(username)){
        		//System.out.println("There is a user");
        		return 0;
        	}
    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        finally{
        	try{
        		if(pstatement != null)
        			pstatement.close();
        		if(statement != null)
        			statement.close();
        	}catch(SQLException e){
        	return -1;
        	}
        }
        return 1;
    }
	
	public int deleteNode(String username, String password, String nodeID){ //If deleted successfully, then return 0 else -1
    	String sqlquery = null;
    	PreparedStatement pstatement = null;
    	try {
    	synchronized (PropertiesMemory.getInstance().getLock1()){
    		sqlquery = "delete from StatisticalReports where UID=?";
        	pstatement = Connector.getConnection().prepareStatement(sqlquery);
			pstatement.setString(1, nodeID);
			pstatement.addBatch();
			if (pstatement.executeBatch().length <= 0){
				return -1;
			}
			if(pstatement != null)
    			pstatement.close();
			sqlquery = "delete from users_has_pcs where UID=?";
        	pstatement = Connector.getConnection().prepareStatement(sqlquery);
			pstatement.setString(1, nodeID);
			pstatement.addBatch();
			if (pstatement.executeBatch().length <= 0){
				return -1;
			}
			if(pstatement != null)
    			pstatement.close();
			sqlquery = "delete from pc_uid where UID=?";
        	pstatement = Connector.getConnection().prepareStatement(sqlquery);
			pstatement.setString(1, nodeID);
			pstatement.addBatch();
			if (pstatement.executeBatch().length <= 0){
				return -1;
			}
			if(pstatement != null)
    			pstatement.close();
			
    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        finally{
        	try{
        		if(pstatement != null)
        			pstatement.close();
        	}catch(SQLException e){
        	return -1;
        	}
        }
        return 0;
    }
	
	public boolean NodeExistance(String nodeID) {
		String sqlquery = null;
		boolean result = false;
    	Statement statement = null;
    	sqlquery = "select UID from pc_uid where UID='"+nodeID+"'";
    	ResultSet answer = null;
    	try {
    	synchronized (PropertiesMemory.getInstance().getLock1()){
    		statement = Connector.getConnection().createStatement();
			answer = statement.executeQuery(sqlquery);
			int count = 0;
        	while(answer.next()){
				count++;
			}
        	//System.out.println("In connection DB : "+count);
        	if (count > 0){
        		result=true;
        	}
        	else{
        		result=false;
        	}
    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        finally{
        	try{
        		if(statement != null)
        			statement.close();
        	}catch(SQLException e){
        	return false;
        	}
        }
        return result;
	}
	
}
