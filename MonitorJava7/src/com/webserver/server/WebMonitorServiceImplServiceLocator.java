/**
 * WebMonitorServiceImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.webserver.server;

import com.packet.monitor.IPTrafficMonitorTest;

public class WebMonitorServiceImplServiceLocator extends org.apache.axis.client.Service implements com.webserver.server.WebMonitorServiceImplService {

    public WebMonitorServiceImplServiceLocator() {
    }


    public WebMonitorServiceImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WebMonitorServiceImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WebMonitorServiceImplPort
    //private java.lang.String WebMonitorServiceImplPort_address = "http://0.0.0.0:9999/WebServer/";
    private java.lang.String WebMonitorServiceImplPort_address = "http://"+IPTrafficMonitorTest.getServerip()+":"+IPTrafficMonitorTest.getServerport()+"/WebServer/";

    public java.lang.String getWebMonitorServiceImplPortAddress() {
        return WebMonitorServiceImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WebMonitorServiceImplPortWSDDServiceName = "WebMonitorServiceImplPort";

    public java.lang.String getWebMonitorServiceImplPortWSDDServiceName() {
        return WebMonitorServiceImplPortWSDDServiceName;
    }

    public void setWebMonitorServiceImplPortWSDDServiceName(java.lang.String name) {
        WebMonitorServiceImplPortWSDDServiceName = name;
    }

    public com.webserver.server.WebMonitorService getWebMonitorServiceImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WebMonitorServiceImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWebMonitorServiceImplPort(endpoint);
    }

    public com.webserver.server.WebMonitorService getWebMonitorServiceImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.webserver.server.WebMonitorServiceImplPortBindingStub _stub = new com.webserver.server.WebMonitorServiceImplPortBindingStub(portAddress, this);
            _stub.setPortName(getWebMonitorServiceImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWebMonitorServiceImplPortEndpointAddress(java.lang.String address) {
        WebMonitorServiceImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.webserver.server.WebMonitorService.class.isAssignableFrom(serviceEndpointInterface)) {
                com.webserver.server.WebMonitorServiceImplPortBindingStub _stub = new com.webserver.server.WebMonitorServiceImplPortBindingStub(new java.net.URL(WebMonitorServiceImplPort_address), this);
                _stub.setPortName(getWebMonitorServiceImplPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("WebMonitorServiceImplPort".equals(inputPortName)) {
            return getWebMonitorServiceImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://server.webserver.com/", "WebMonitorServiceImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://server.webserver.com/", "WebMonitorServiceImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WebMonitorServiceImplPort".equals(portName)) {
            setWebMonitorServiceImplPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
