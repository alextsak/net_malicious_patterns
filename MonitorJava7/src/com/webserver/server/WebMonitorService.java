/**
 * WebMonitorService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.webserver.server;

public interface WebMonitorService extends java.rmi.Remote {
    public java.lang.String retrieveMaliciousPatterns(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException;
    public void insertMaliciousPatterns(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3) throws java.rmi.RemoteException;
    public int login(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException;
    public boolean logout(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException;
    public com.webserver.server.MaliciousPatterns maliciousPatternsRequest(java.lang.String arg0) throws java.rmi.RemoteException;
    public boolean registerUser(java.lang.String arg0, java.lang.String arg1, com.webserver.server.AvailableNodes arg2) throws java.rmi.RemoteException;
    public boolean delete(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2) throws java.rmi.RemoteException;
    public void maliciousPatternsStatisticalReport(java.lang.String arg0, com.webserver.server.StatisticalReports arg1) throws java.rmi.RemoteException;
    public boolean unregister(java.lang.String arg0) throws java.rmi.RemoteException;
    public com.webserver.server.StatisticalReports[] retrieveStatistics(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException;
    public boolean registerPC(java.lang.String arg0) throws java.rmi.RemoteException;
}
