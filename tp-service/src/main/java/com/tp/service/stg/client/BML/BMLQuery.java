/**
 * BMLQuery.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package com.tp.service.stg.client.BML;

import javax.xml.rpc.ServiceException;

public interface BMLQuery extends javax.xml.rpc.Service {
    public java.lang.String getBMLQueryHttpPortAddress();

    public BMLQueryPortType getBMLQueryHttpPort() throws ServiceException;

    public BMLQueryPortType getBMLQueryHttpPort(java.net.URL portAddress) throws ServiceException;
}
