/**
 * ReceiveEncryptDeclareServiceImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.tp.service.ord.webservice.client.JKF;

public class ReceiveEncryptDeclareServiceImplServiceLocator extends org.apache.axis.client.Service implements com.tp.service.ord.webservice.client.JKF.ReceiveEncryptDeclareServiceImplService {

    public ReceiveEncryptDeclareServiceImplServiceLocator() {
    }


    public ReceiveEncryptDeclareServiceImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ReceiveEncryptDeclareServiceImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ReceiveEncryptDeclareServiceImplPort
    private java.lang.String ReceiveEncryptDeclareServiceImplPort_address = "http://122.224.230.4:18003/newyorkWS/ws/ReceiveEncryptDeclare";

    public java.lang.String getReceiveEncryptDeclareServiceImplPortAddress() {
        return ReceiveEncryptDeclareServiceImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ReceiveEncryptDeclareServiceImplPortWSDDServiceName = "ReceiveEncryptDeclareServiceImplPort";

    public java.lang.String getReceiveEncryptDeclareServiceImplPortWSDDServiceName() {
        return ReceiveEncryptDeclareServiceImplPortWSDDServiceName;
    }

    public void setReceiveEncryptDeclareServiceImplPortWSDDServiceName(java.lang.String name) {
        ReceiveEncryptDeclareServiceImplPortWSDDServiceName = name;
    }

    public com.tp.service.ord.webservice.client.JKF.ReceiveEncryptDeclareService getReceiveEncryptDeclareServiceImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ReceiveEncryptDeclareServiceImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getReceiveEncryptDeclareServiceImplPort(endpoint);
    }

    public com.tp.service.ord.webservice.client.JKF.ReceiveEncryptDeclareService getReceiveEncryptDeclareServiceImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
        	com.tp.service.ord.webservice.client.JKF.ReceiveEncryptDeclareServiceImplServiceSoapBindingStub _stub = new com.tp.service.ord.webservice.client.JKF.ReceiveEncryptDeclareServiceImplServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getReceiveEncryptDeclareServiceImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setReceiveEncryptDeclareServiceImplPortEndpointAddress(java.lang.String address) {
        ReceiveEncryptDeclareServiceImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.tp.service.ord.webservice.client.JKF.ReceiveEncryptDeclareService.class.isAssignableFrom(serviceEndpointInterface)) {
            	com.tp.service.ord.webservice.client.JKF.ReceiveEncryptDeclareServiceImplServiceSoapBindingStub _stub = new com.tp.service.ord.webservice.client.JKF.ReceiveEncryptDeclareServiceImplServiceSoapBindingStub(new java.net.URL(ReceiveEncryptDeclareServiceImplPort_address), this);
                _stub.setPortName(getReceiveEncryptDeclareServiceImplPortWSDDServiceName());
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
        if ("ReceiveEncryptDeclareServiceImplPort".equals(inputPortName)) {
            return getReceiveEncryptDeclareServiceImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://impl.ws.newyork.zjport.gov.cn/", "ReceiveEncryptDeclareServiceImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://impl.ws.newyork.zjport.gov.cn/", "ReceiveEncryptDeclareServiceImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ReceiveEncryptDeclareServiceImplPort".equals(portName)) {
            setReceiveEncryptDeclareServiceImplPortEndpointAddress(address);
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
