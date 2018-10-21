package com.webserver.server;

public class WebMonitorServiceProxy implements com.webserver.server.WebMonitorService {
  private String _endpoint = null;
  private com.webserver.server.WebMonitorService webMonitorService = null;
  
  public WebMonitorServiceProxy() {
    _initWebMonitorServiceProxy();
  }
  
  public WebMonitorServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initWebMonitorServiceProxy();
  }
  
  private void _initWebMonitorServiceProxy() {
    try {
      webMonitorService = (new com.webserver.server.WebMonitorServiceImplServiceLocator()).getWebMonitorServiceImplPort();
      if (webMonitorService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)webMonitorService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)webMonitorService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (webMonitorService != null)
      ((javax.xml.rpc.Stub)webMonitorService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.webserver.server.WebMonitorService getWebMonitorService() {
    if (webMonitorService == null)
      _initWebMonitorServiceProxy();
    return webMonitorService;
  }
  
  public java.lang.String retrieveMaliciousPatterns(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException{
    if (webMonitorService == null)
      _initWebMonitorServiceProxy();
    return webMonitorService.retrieveMaliciousPatterns(arg0, arg1);
  }
  
  public void insertMaliciousPatterns(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3) throws java.rmi.RemoteException{
    if (webMonitorService == null)
      _initWebMonitorServiceProxy();
    webMonitorService.insertMaliciousPatterns(arg0, arg1, arg2, arg3);
  }
  
  public int login(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException{
    if (webMonitorService == null)
      _initWebMonitorServiceProxy();
    return webMonitorService.login(arg0, arg1);
  }
  
  public boolean logout(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException{
    if (webMonitorService == null)
      _initWebMonitorServiceProxy();
    return webMonitorService.logout(arg0, arg1);
  }
  
  public com.webserver.server.MaliciousPatterns maliciousPatternsRequest(java.lang.String arg0) throws java.rmi.RemoteException{
    if (webMonitorService == null)
      _initWebMonitorServiceProxy();
    return webMonitorService.maliciousPatternsRequest(arg0);
  }
  
  public boolean registerUser(java.lang.String arg0, java.lang.String arg1, com.webserver.server.AvailableNodes arg2) throws java.rmi.RemoteException{
    if (webMonitorService == null)
      _initWebMonitorServiceProxy();
    return webMonitorService.registerUser(arg0, arg1, arg2);
  }
  
  public boolean delete(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) throws java.rmi.RemoteException{
    if (webMonitorService == null)
      _initWebMonitorServiceProxy();
    return webMonitorService.delete(arg0, arg1, arg2);
  }
  
  public void maliciousPatternsStatisticalReport(java.lang.String arg0, com.webserver.server.StatisticalReports arg1) throws java.rmi.RemoteException{
    if (webMonitorService == null)
      _initWebMonitorServiceProxy();
    webMonitorService.maliciousPatternsStatisticalReport(arg0, arg1);
  }
  
  public boolean unregister(java.lang.String arg0) throws java.rmi.RemoteException{
    if (webMonitorService == null)
      _initWebMonitorServiceProxy();
    return webMonitorService.unregister(arg0);
  }
  
  public com.webserver.server.StatisticalReports[] retrieveStatistics(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException{
    if (webMonitorService == null)
      _initWebMonitorServiceProxy();
    return webMonitorService.retrieveStatistics(arg0, arg1);
  }
  
  public boolean registerPC(java.lang.String arg0) throws java.rmi.RemoteException{
    if (webMonitorService == null)
      _initWebMonitorServiceProxy();
    return webMonitorService.registerPC(arg0);
  }
  
  
}