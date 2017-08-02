
/**
 * ImportOrderServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.3  Built on : May 30, 2016 (04:08:57 BST)
 */

package com.tp.service.wms.thirdparty.sto.ws;

/*
 *  ImportOrderServiceService java interface
 */

public interface ImportOrderServiceService {

	/**
	 * Auto generated method signature
	 * 
	 * @param importOrderByJsonString0
	 * 
	 */

	public com.tp.service.wms.thirdparty.sto.ws.ImportOrderByJsonStringResponseE importOrderByJsonString(

			com.tp.service.wms.thirdparty.sto.ws.ImportOrderByJsonStringE importOrderByJsonString0) throws java.rmi.RemoteException;

	/**
	 * Auto generated method signature for Asynchronous Invocations
	 * 
	 * @param importOrderByJsonString0
	 * 
	 */
	public void startimportOrderByJsonString(

			com.tp.service.wms.thirdparty.sto.ws.ImportOrderByJsonStringE importOrderByJsonString0,

			final com.tp.service.wms.thirdparty.sto.ws.ImportOrderServiceServiceCallbackHandler callback)

					throws java.rmi.RemoteException;

	//
}
