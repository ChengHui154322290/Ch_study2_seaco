/**
 * ArrayOfAcceptAsnHeader.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package com.tp.model.stg.BML;

import org.apache.axis.description.TypeDesc;

public class ArrayOfAcceptAsnHeader  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6401358294707670558L;
	private AcceptAsnHeader[] acceptAsnHeader;

    public ArrayOfAcceptAsnHeader() {
    }

    public ArrayOfAcceptAsnHeader(
           AcceptAsnHeader[] acceptAsnHeader) {
           this.acceptAsnHeader = acceptAsnHeader;
    }


    /**
     * Gets the acceptAsnHeader value for this ArrayOfAcceptAsnHeader.
     * 
     * @return acceptAsnHeader
     */
    public AcceptAsnHeader[] getAcceptAsnHeader() {
        return acceptAsnHeader;
    }


    /**
     * Sets the acceptAsnHeader value for this ArrayOfAcceptAsnHeader.
     * 
     * @param acceptAsnHeader
     */
    public void setAcceptAsnHeader(AcceptAsnHeader[] acceptAsnHeader) {
        this.acceptAsnHeader = acceptAsnHeader;
    }

    public AcceptAsnHeader getAcceptAsnHeader(int i) {
        return this.acceptAsnHeader[i];
    }

    public void setAcceptAsnHeader(int i, AcceptAsnHeader _value) {
        this.acceptAsnHeader[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArrayOfAcceptAsnHeader)) return false;
        ArrayOfAcceptAsnHeader other = (ArrayOfAcceptAsnHeader) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.acceptAsnHeader==null && other.getAcceptAsnHeader()==null) || 
             (this.acceptAsnHeader!=null &&
              java.util.Arrays.equals(this.acceptAsnHeader, other.getAcceptAsnHeader())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getAcceptAsnHeader() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAcceptAsnHeader());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAcceptAsnHeader(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static TypeDesc typeDesc =
        new TypeDesc(ArrayOfAcceptAsnHeader.class, true);


    /**
     * Return type metadata object
     */
    public static TypeDesc getTypeDesc() {
        return typeDesc;
    }


}
