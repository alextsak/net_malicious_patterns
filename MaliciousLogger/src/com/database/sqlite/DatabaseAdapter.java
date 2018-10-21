package com.database.sqlite;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;

public class DatabaseAdapter {
	
	public static final int DATABASE_VERSION = 2;
	static final String DATABASE_NAME = "maliciouslogger.db";
	public static final String TABLE_USER = "USER";
	public static final String TABLE_SR ="StatisticalReports";
	public static final String TABLE_NODES="Nodes";
	public static final String TABLE_PATTERNS="Patterns";
	public static final String TABLE_PC_HAS_USER="pc_has_user";
	public static final String TABLE_PC_DELETION="pc_deletion";
	
	static final String DATABASE_CREATE_USER= "create table "+TABLE_USER+"("+"ID"+" integer primary key autoincrement,USERNAME text,PASSWORD text, isAdmin integer);"; //0:user | 1: administrator
	static final String DATABASE_CREATE_SR= "create table "+TABLE_SR+"(REPORT_ID integer primary key autoincrement,"+"UID text,LOCAL_TIME text,Device text,Device_IP text,Pattern text, Frequency text, FOREIGN KEY(UID) REFERENCES "+TABLE_NODES+"(UID));";
	static final String DATABASE_CREATE_PC_HAS_USER= "create table "+TABLE_PC_HAS_USER+"(UID text,"+"OWNER text, FOREIGN KEY(UID) REFERENCES "+TABLE_NODES+"(UID), FOREIGN KEY(OWNER) REFERENCES "+TABLE_USER+"(USERNAME));";
	static final String DATABASE_CREATE_PATTERNS= "create table "+TABLE_PATTERNS+"(ID integer primary key autoincrement,Pattern text,Type int, Status int);";//Type ( 1: IP | 0: Pattern ), Status ( 0: Pending | 1: Send )
	static final String DATABASE_CREATE_NODES= "create table "+TABLE_NODES+"(ID integer primary key autoincrement, UID text);";
	static final String DATABASE_CREATE_PC_DELETION= "create table "+TABLE_PC_DELETION+"(ID integer primary key autoincrement, UID text, USERNAME text, PASSWORD text);";
	
	public  SQLiteDatabase db;
	private DatabaseManager dbManager;
	private final Context context;
	public static String str1;
	
	
	public DatabaseAdapter(Context contexts){
		context = contexts;
		dbManager = new DatabaseManager(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public SQLiteDatabase getDatabaseInstance(){
		return db;
	}
	
	public DatabaseAdapter open() throws SQLException{
		db =dbManager.getWritableDatabase();
		return this;
	}
	
	public DatabaseAdapter openread() throws SQLException{
		db =dbManager.getReadableDatabase();
		return this;
	}
	
	public void close(){
		db.close();
	}
	
	public void insertUser(String userName, String passWord, int isadmin){ //Insert User
		open();
		ContentValues newValues = new ContentValues();
		newValues.put("USERNAME", userName);
		newValues.put("PASSWORD", passWord);
		newValues.put("isAdmin", isadmin);
		db.insert(TABLE_USER, null, newValues);
		close();
	}
	
	public String isAdmin(String userName, String passWord){ //Return if the given user is Administrator
		openread();
		String temp = null;
		String selectQuery= "SELECT isAdmin FROM "+ TABLE_USER+ " WHERE USERNAME='"+userName+"' and PASSWORD='"+passWord+"'";
		Cursor cursor =db.rawQuery(selectQuery, null);
		if(cursor.moveToFirst()){
			do{
				String total1 = String.valueOf(cursor.getString(0));
				temp = total1;
			}while(cursor.moveToNext());
		}
		cursor.close();
		close();
		return temp;
	}
	
	public void insertNode(String nodeID){
		open();
		String selectQuery= "SELECT UID FROM "+ TABLE_NODES+" WHERE UID='"+nodeID+"'";
		Cursor cursor =db.rawQuery(selectQuery, null);
		int found = 0;
		if(cursor.moveToFirst()){
			do{
				String total1 = String.valueOf(cursor.getString(0));
				if (total1.equals(nodeID)){
					found=1;
				}
			}while(cursor.moveToNext());
		}
		cursor.close();
		if (found == 0){ //If nodeID doesn't exist in Nodes then add it
			ContentValues newValues = new ContentValues();
			newValues.put("UID", nodeID);
			db.insert(TABLE_NODES, null, newValues);
			Log.i("Node :", nodeID);
		}
		close();
	}
	
	public void insertPC_has_User(String nodeID,String owner){
		open();
		ContentValues newValues = new ContentValues();
		newValues.put("UID", nodeID);
		newValues.put("OWNER", owner);
		db.insert(TABLE_PC_HAS_USER, null, newValues);
		Log.i("PC->User :", nodeID);
		close();
	}
	
	public void insertStatisticalReports(String nodeID, String Local_Time, String Device, String IP, String Pattern, String Frequency){ //Insert Report
		open();
		String selectQuery= "SELECT * FROM "+ TABLE_SR+" WHERE UID='"+nodeID+"' and LOCAL_TIME = (SELECT MAX(LOCAL_TIME) FROM "+TABLE_SR+" s WHERE s.UID='"+nodeID+"')";
		Cursor cursor =db.rawQuery(selectQuery, null);
		int exists = 0;
		if(cursor.moveToFirst()){
			do{
				String total1 = String.valueOf(cursor.getString(1));
				String total2 = String.valueOf(cursor.getString(2));
				String total3 = String.valueOf(cursor.getString(3));
				String total4 = String.valueOf(cursor.getString(4));
				String total5 = String.valueOf(cursor.getString(5));
				if (total1.equals(nodeID) && total2.equals(Local_Time) && total3.equals(Device) && total4.equals(IP) && total5.equals(Pattern)){
					exists=1;
				}
			}while(cursor.moveToNext());
		}
		cursor.close();
		if (exists==1){
			close();
			return;
		}
		selectQuery= "SELECT UID FROM "+ TABLE_NODES+" WHERE UID='"+nodeID+"'";
		cursor =db.rawQuery(selectQuery, null);
		int found = 0;
		if(cursor.moveToFirst()){
			do{
				String total1 = String.valueOf(cursor.getString(0));
				if (total1.equals(nodeID)){
					found=1;
				}
			}while(cursor.moveToNext());
		}
		cursor.close();
		if (found == 0){ //If nodeID doesn't exist in Nodes then add it
			ContentValues newValues = new ContentValues();
			newValues.put("UID", nodeID);
			db.insert(TABLE_NODES, null, newValues);
			Log.i("Stats 1 :", nodeID);
		}
		ContentValues newValues = new ContentValues();
		newValues.put("UID", nodeID);
		newValues.put("LOCAL_TIME", Local_Time);
		newValues.put("Device", Device);
		newValues.put("Device_IP", IP);
		newValues.put("Pattern", Pattern);
		newValues.put("Frequency", Frequency);
		db.insert("StatisticalReports", null, newValues);
		Log.i("Stats 2 :", nodeID);
		close();
	}
	
	public int insertPattern(String Pattern, int type, int status){ //Insert given Pattern
		if (Pattern.equals("NOPATTERNS#")){
			return -1;
		}
		open();
		String selectQuery= "SELECT Pattern FROM "+ TABLE_PATTERNS+" WHERE Pattern='"+Pattern+"'";
		Cursor cursor =db.rawQuery(selectQuery, null);
		int found = 0;
		if(cursor.moveToFirst()){
			do{
				String total1 = String.valueOf(cursor.getString(0));
				if (total1.equals(Pattern)){
					found=1;
				}
			}while(cursor.moveToNext());
		}
		cursor.close();
		if (found == 0){ //If nodeID doesn't exist in Nodes then add it
			ContentValues newValues = new ContentValues();
			newValues.put("Pattern", Pattern);
			newValues.put("Type", type);
			newValues.put("Status", status);
			db.insert("Patterns", null, newValues);
			close();
			return 0;
		}
		else{
			close();
			return 1;
		}
	}
	
	public void insertDeletionNode(String id, String user, String pass){ //Insert node to be deleted in next update
		open();
		ContentValues newValues = new ContentValues();
		newValues.put("UID", id);
		newValues.put("USERNAME", user);
		newValues.put("PASSWORD", pass);
		db.insert(TABLE_PC_DELETION, null, newValues);
		close();
	}
	
	public List<String> getAllDeletionNodes(){ //Get the UID from all the nodes
		openread();
		List<String> nodesList = new ArrayList<String>();
		String selectQuery= "SELECT * FROM "+ TABLE_PC_DELETION;
		Cursor cursor =db.rawQuery(selectQuery, null);
		if(cursor.moveToFirst()){
			do{
				String total1 = String.valueOf(cursor.getString(1))+"#"+String.valueOf(cursor.getString(2))+"#"+String.valueOf(cursor.getString(3))+"#";
				nodesList.add(total1);
			}while(cursor.moveToNext());
		}
		cursor.close();
		close();
		return nodesList;
	}
	
	public List<String> getAllNodes(){ //Get the UID from all the nodes
		openread();
		List<String> nodesList = new ArrayList<String>();
		String selectQuery= "SELECT distinct UID FROM "+ TABLE_NODES;
		Cursor cursor =db.rawQuery(selectQuery, null);
		if(cursor.moveToFirst()){
			do{
				String total1 = String.valueOf(cursor.getString(0));
				nodesList.add(total1);
			}while(cursor.moveToNext());
		}
		cursor.close();
		close();
		for(String temp:nodesList){
			Log.i("GetALL : Node",temp);
		}
		
		return nodesList;
	}
	
	public List<String> getAllUserNodes(String username){ //Get the UID from the nodes owned by the logged in user
		openread();
		List<String> nodesList = new ArrayList<String>();
		String selectQuery= "SELECT distinct UID FROM "+ TABLE_PC_HAS_USER+" WHERE OWNER='"+username+"'";
		Cursor cursor =db.rawQuery(selectQuery, null);
		if(cursor.moveToFirst()){
			do{
				String total1 = String.valueOf(cursor.getString(0));
				nodesList.add(total1);
			}while(cursor.moveToNext());
		}
		cursor.close();
		close();
		return nodesList;
	}
	
	
	public List<String> getAllNodeDevices(String nodeID){ //Get all the devices of the given node
		openread();
		Log.i("Database","Inside getAllNodeDevices with: " + nodeID);
		List<String> deviceList = new ArrayList<String>();
		String selectQuery= "SELECT distinct Device FROM "+ TABLE_SR+" WHERE LOCAL_TIME = (SELECT MAX(LOCAL_TIME) FROM "+TABLE_SR+" s WHERE s.UID='"+nodeID+"') and UID='"+nodeID+"'";
		Cursor cursor =db.rawQuery(selectQuery, null);
		if(cursor.moveToFirst()){
			do{
				String total1 = String.valueOf(cursor.getString(0));
				deviceList.add(total1);
			}while(cursor.moveToNext());
		}
		cursor.close();
		close();
		for(String s : deviceList) {
			Log.i("Database",s);
		}
		
		return deviceList;
		
	}
	
	public List<String> getAllNodeDeviceReports(String nodeID, String device){ //Get all reports fro given device of the given node
		openread();
		List<String> deviceList = new ArrayList<String>();
		String selectQuery= "SELECT * FROM "+ TABLE_SR+" WHERE LOCAL_TIME = (SELECT MAX(LOCAL_TIME) FROM "+TABLE_SR+" s WHERE s.UID='"+nodeID+"') and UID='"+nodeID+"' and Device='"+device+"'";
		Cursor cursor =db.rawQuery(selectQuery, null);
		if(cursor.moveToFirst()){
			do{
				String total1 = String.valueOf(cursor.getString(2))+"#"+String.valueOf(cursor.getString(4))+"#"+String.valueOf(cursor.getString(5))+"#"+String.valueOf(cursor.getString(6))+"#";
				Log.i("DatabaseAdapter",total1);
				deviceList.add(total1);
			}while(cursor.moveToNext());
		}
		cursor.close();
		close();
		return deviceList;
		
	}
	
	public List<String> getAllPatterns(){ //Get Patterns
		openread();
		List<String> nodesList = new ArrayList<String>();
		String selectQuery= "SELECT Pattern FROM "+ TABLE_PATTERNS;
		Cursor cursor =db.rawQuery(selectQuery, null);
		if(cursor.moveToFirst()){
			do{
				String total1 = String.valueOf(cursor.getString(0));
				nodesList.add(total1);
			}while(cursor.moveToNext());
		}
		cursor.close();
		close();
		return nodesList;
	}
	
	public List<String> getIPPatterns(){ //Get IP Patterns
		openread();
		List<String> nodesList = new ArrayList<String>();
		String selectQuery= "SELECT Pattern FROM "+ TABLE_PATTERNS +" WHERE Type='1'";
		Cursor cursor =db.rawQuery(selectQuery, null);
		if(cursor.moveToFirst()){
			do{
				String total1 = String.valueOf(cursor.getString(0));
				nodesList.add(total1);
			}while(cursor.moveToNext());
		}
		cursor.close();
		close();
		return nodesList;
	}
	
	public List<String> getStringPatterns(){ //Get String Patterns
		openread();
		List<String> nodesList = new ArrayList<String>();
		String selectQuery= "SELECT Pattern FROM "+ TABLE_PATTERNS +" WHERE Type='0'";
		Cursor cursor =db.rawQuery(selectQuery, null);
		if(cursor.moveToFirst()){
			do{
				String total1 = String.valueOf(cursor.getString(0));
				nodesList.add(total1);
			}while(cursor.moveToNext());
		}
		cursor.close();
		close();
		return nodesList;
	}
	
	public void deleteUser(String userName){
		open();
		db.delete("user", "USERNAME=?", new String[]{userName});		 //DELETE given user
		close();
	}
	
	public void deleteAllUsers(){
		open();
		db.execSQL("delete from "+ TABLE_USER);               //DELETE ALL USERS
		close();
	}
	
	public void deleteAllNodes(){
		open();
		db.execSQL("delete from "+ TABLE_SR);
		db.execSQL("delete from "+ TABLE_PC_HAS_USER);
		db.execSQL("delete from "+ TABLE_NODES);             //DELETE ALL Nodes
		close();
	}
	
	public void deleteAllUserNodes(){
		open();
		db.execSQL("delete from "+ TABLE_PC_HAS_USER);             //DELETE ALL Nodes from pc_has_user
		close();
	}
	
	public void deleteNode(String nodeID){ //DELETE node from all tables that contains it
		open();
		String[] UID = new String[1];
		UID[0]=String.valueOf(nodeID);
		db.execSQL("delete from "+ TABLE_SR+" where UID='"+nodeID+"'");
		Log.i("deleteStats", nodeID);
		db.execSQL("delete from "+ TABLE_PC_HAS_USER+" where UID='"+nodeID+"'");
		Log.i("deleteUsers", nodeID);
		db.execSQL("delete from "+ TABLE_NODES+" where UID='"+nodeID+"'");
		Log.i("deleteNode", nodeID);
		close();
	}
	
	public void deleteNodefromDeletion(String nodeID){ //DELETE ALL Nodes from pc_deletion
		open();
		String UID = String.valueOf(nodeID);   
		Log.i("sadasda", new String[]{UID}.toString());
		db.execSQL("delete from "+ TABLE_PC_DELETION+" where UID='"+nodeID+"'");
		close();
	}
	
	 public void deleteAllPatterns(){
		 open();                                                 //DELETE ALL Patterns
		 db.execSQL("delete from "+ TABLE_PATTERNS);
		 close();
     }
	 
	 public void deletePattern(String Pattern){
			open();
			db.execSQL("delete from "+ TABLE_PATTERNS+" where Pattern='"+Pattern+"'");
			//db.delete(TABLE_PATTERNS, "Pattern=?", new String[]{Pattern});		 //DELETE given Pattern
			close();
	}
	 
	 public void deleteAllReports(){
		 open();                                                 //DELETE ALL Reports
		 db.execSQL("delete from "+ TABLE_SR);
		 close();
     }
	
}