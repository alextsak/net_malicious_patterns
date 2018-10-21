package com.database.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
	
	private static String user;        //dbUsername
	private static String password;    //dbPassword
	private static String dbUrl;       //dbURL
	private static Connection conn;
	private static Connector instance =null;
	
	synchronized public static Connector getInstance(String username, String password, String url){
		if(instance == null){
			instance = new Connector(username, password, url);
		}
		return instance;
	}

	public static Connector getInstance(){
		if(instance != null){
			return instance;
		}
		return null;
	}
	
	private Connector(String username, String password, String url){
		Connector.user = username;
		Connector.password = password;
		Connector.dbUrl = url;
		try
		{
			DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
			Connector.conn = DriverManager.getConnection(Connector.dbUrl,Connector.user, Connector.password);
		}catch(SQLException e) {
			System.err.println("Can't connect");
			Connector.conn = null;
		}
	}
	
	public static Connection getConnection(){
		return Connector.conn;
	}
	
	public void closeConnection(){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
