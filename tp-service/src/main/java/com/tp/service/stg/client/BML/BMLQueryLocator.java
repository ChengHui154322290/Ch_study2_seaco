/**
 * BMLQueryLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package com.tp.service.stg.client.BML;

public class BMLQueryLocator extends org.apache.axis.client.Service implements BMLQuery {

    public BMLQueryLocator() {
    }


    public BMLQueryLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public BMLQueryLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for BMLQueryHttpPort
    private java.lang.String BMLQueryHttpPort_address = "http://58.210.118.230:9012/order/BMLservices/BMLQuery";

    public java.lang.String getBMLQueryHttpPortAddress() {
        return BMLQueryHttpPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String BMLQueryHttpPortWSDDServiceName = "BMLQueryHttpPort";

    public java.lang.String getBMLQueryHttpPortWSDDServiceName() {
        return BMLQueryHttpPortWSDDServiceName;
    }

    public void setBMLQueryHttpPortWSDDServiceName(java.lang.String name) {
        BMLQueryHttpPortWSDDServiceName = name;
    }

    public com.tp.service.stg.client.BML.BMLQueryPortType getBMLQueryHttpPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BMLQueryHttpPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBMLQueryHttpPort(endpoint);
    }

    public com.tp.service.stg.client.BML.BMLQueryPortType getBMLQueryHttpPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
        	com.tp.service.stg.client.BML.BMLQueryHttpBindingStub _stub = new com.tp.service.stg.client.BML.BMLQueryHttpBindingStub(portAddress, this);
            _stub.setPortName(getBMLQueryHttpPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBMLQueryHttpPortEndpointAddress(java.lang.String address) {
        BMLQueryHttpPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.tp.service.stg.client.BML.BMLQueryPortType.class.isAssignableFrom(serviceEndpointInterface)) {
            	com.tp.service.stg.client.BML.BMLQueryHttpBindingStub _stub = new com.tp.service.stg.client.BML.BMLQueryHttpBindingStub(new java.net.URL(BMLQueryHttpPort_address), this);
                _stub.setPortName(getBMLQueryHttpPortWSDDServiceName());
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
        if ("BMLQueryHttpPort".equals(inputPortName)) {
            return getBMLQueryHttpPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://BML", "BMLQuery");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://BML", "BMLQueryHttpPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("BMLQueryHttpPort".equals(portName)) {
            setBMLQueryHttpPortEndpointAddress(address);
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
