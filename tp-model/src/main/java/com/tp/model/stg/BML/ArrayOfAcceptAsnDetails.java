/**
 * ArrayOfAcceptAsnDetails.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package com.tp.model.stg.BML;

public class ArrayOfAcceptAsnDetails  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2043005498441178655L;
	private com.tp.model.stg.BML.AcceptAsnDetails[] acceptAsnDetails;

    public ArrayOfAcceptAsnDetails() {
    }

    public ArrayOfAcceptAsnDetails(
           com.tp.model.stg.BML.AcceptAsnDetails[] acceptAsnDetails) {
           this.acceptAsnDetails = acceptAsnDetails;
    }


    /**
     * Gets the acceptAsnDetails value for this ArrayOfAcceptAsnDetails.
     * 
     * @return acceptAsnDetails
     */
    public com.tp.model.stg.BML.AcceptAsnDetails[] getAcceptAsnDetails() {
        return acceptAsnDetails;
    }


    /**
     * Sets the acceptAsnDetails value for this ArrayOfAcceptAsnDetails.
     * 
     * @param acceptAsnDetails
     */
    public void setAcceptAsnDetails(com.tp.model.stg.BML.AcceptAsnDetails[] acceptAsnDetails) {
        this.acceptAsnDetails = acceptAsnDetails;
    }

    public com.tp.model.stg.BML.AcceptAsnDetails getAcceptAsnDetails(int i) {
        return this.acceptAsnDetails[i];
    }

    public void setAcceptAsnDetails(int i, com.tp.model.stg.BML.AcceptAsnDetails _value) {
        this.acceptAsnDetails[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArrayOfAcceptAsnDetails)) return false;
        ArrayOfAcceptAsnDetails other = (ArrayOfAcceptAsnDetails) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.acceptAsnDetails==null && other.getAcceptAsnDetails()==null) || 
             (this.acceptAsnDetails!=null &&
              java.util.Arrays.equals(this.acceptAsnDetails, other.getAcceptAsnDetails())));
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
        if (getAcceptAsnDetails() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAcceptAsnDetails());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAcceptAsnDetails(), i);
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
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ArrayOfAcceptAsnDetails.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://model.BML", "ArrayOfAcceptAsnDetails"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("acceptAsnDetails");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.BML", "AcceptAsnDetails"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://model.BML", "AcceptAsnDetails"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
