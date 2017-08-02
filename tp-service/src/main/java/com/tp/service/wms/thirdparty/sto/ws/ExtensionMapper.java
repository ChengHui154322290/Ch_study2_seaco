
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.3  Built on : May 30, 2016 (04:09:26 BST)
 */

package com.tp.service.wms.thirdparty.sto.ws;

/**
 * ExtensionMapper class
 */
@SuppressWarnings({ "unchecked", "unused" })

public class ExtensionMapper {

	public static java.lang.Object getTypeObject(java.lang.String namespaceURI, java.lang.String typeName,
			javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {

		if ("http://service.what21.com/".equals(namespaceURI) && "importOrderByJsonStringResponse".equals(typeName)) {

			return com.tp.service.wms.thirdparty.sto.ws.ImportOrderByJsonStringResponse.Factory.parse(reader);

		}

		if ("http://service.what21.com/".equals(namespaceURI) && "importOrderByJsonString".equals(typeName)) {

			return com.tp.service.wms.thirdparty.sto.ws.ImportOrderByJsonString.Factory.parse(reader);

		}

		throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
	}

}
