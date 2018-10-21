package com.packetMem.shared;


public class PropertiesMemory {

	private static PropertiesMemory memory;
	private String WebServerIp;
	private String guiUser;
	private String guiPass;
	private String WebServerPort;
	private  String dbusername;
	private  String dbpassword;
	private  String dburl;
	private  Object lock1;
	
	public String getGuiUser() {
		return guiUser;
	}
	public void setGuiUser(String guiUser) {
		this.guiUser = guiUser;
	}
	public String getGuiPass() {
		return guiPass;
	}
	public void setGuiPass(String guiPass) {
		this.guiPass = guiPass;
	}
	public String getWebServerIp() {
		return WebServerIp;
	}
	public void setWebServerIp(String webServerIp) {
		WebServerIp = webServerIp;
	}
	public String getWebServerPort() {
		return WebServerPort;
	}
	public void setWebServerPort(String webServerPort) {
		WebServerPort = webServerPort;
	}
	
	private PropertiesMemory(){
		lock1 = new Object();
		
	}
	public static PropertiesMemory getInstance() {
		if (memory == null) {
			synchronized (PropertiesMemory.class) {
				memory = new PropertiesMemory();
			}
		}
		return memory;
	}
	
	
	public String getDbusername() {
		return dbusername;
	}
	public void setDbusername(String dbusername) {
		this.dbusername = dbusername;
	}
	public String getDbpassword() {
		return dbpassword;
	}
	public void setDbpassword(String dbpassword) {
		this.dbpassword = dbpassword;
	}
	public String getDburl() {
		return dburl;
	}
	public void setDburl(String dburl) {
		this.dburl = dburl;
	}
	
	public Object getLock1() {
		return lock1;
	}
	public void setLock1(Object lock1) {
		this.lock1 = lock1;
	}
	
}