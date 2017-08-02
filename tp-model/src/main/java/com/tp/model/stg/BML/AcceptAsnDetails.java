/**
 * AcceptAsnDetails.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package com.tp.model.stg.BML;

public class AcceptAsnDetails  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3675030028725320307L;
	private java.lang.String asnReference;
    private java.lang.String customerID;
    private java.lang.String expectedQty;
    private java.lang.String price;
    private java.lang.String remark;
    private java.lang.String sku;
    private java.lang.String skuDescribe;

    public AcceptAsnDetails() {
    }

    public AcceptAsnDetails(
           java.lang.String asnReference,
           java.lang.String customerID,
           java.lang.String expectedQty,
           java.lang.String price,
           java.lang.String remark,
           java.lang.String sku,
           java.lang.String skuDescribe) {
           this.asnReference = asnReference;
           this.customerID = customerID;
           this.expectedQty = expectedQty;
           this.price = price;
           this.remark = remark;
           this.sku = sku;
           this.skuDescribe = skuDescribe;
    }


    /**
     * Gets the asnReference value for this AcceptAsnDetails.
     * 
     * @return asnReference
     */
    public java.lang.String getAsnReference() {
        return asnReference;
    }


    /**
     * Sets the asnReference value for this AcceptAsnDetails.
     * 
     * @param asnReference
     */
    public void setAsnReference(java.lang.String asnReference) {
        this.asnReference = asnReference;
    }


    /**
     * Gets the customerID value for this AcceptAsnDetails.
     * 
     * @return customerID
     */
    public java.lang.String getCustomerID() {
        return customerID;
    }


    /**
     * Sets the customerID value for this AcceptAsnDetails.
     * 
     * @param customerID
     */
    public void setCustomerID(java.lang.String customerID) {
        this.customerID = customerID;
    }


    /**
     * Gets the expectedQty value for this AcceptAsnDetails.
     * 
     * @return expectedQty
     */
    public java.lang.String getExpectedQty() {
        return expectedQty;
    }


    /**
     * Sets the expectedQty value for this AcceptAsnDetails.
     * 
     * @param expectedQty
     */
    public void setExpectedQty(java.lang.String expectedQty) {
        this.expectedQty = expectedQty;
    }


    /**
     * Gets the price value for this AcceptAsnDetails.
     * 
     * @return price
     */
    public java.lang.String getPrice() {
        return price;
    }


    /**
     * Sets the price value for this AcceptAsnDetails.
     * 
     * @param price
     */
    public void setPrice(java.lang.String price) {
        this.price = price;
    }


    /**
     * Gets the remark value for this AcceptAsnDetails.
     * 
     * @return remark
     */
    public java.lang.String getRemark() {
        return remark;
    }


    /**
     * Sets the remark value for this AcceptAsnDetails.
     * 
     * @param remark
     */
    public void setRemark(java.lang.String remark) {
        this.remark = remark;
    }


    /**
     * Gets the sku value for this AcceptAsnDetails.
     * 
     * @return sku
     */
    public java.lang.String getSku() {
        return sku;
    }


    /**
     * Sets the sku value for this AcceptAsnDetails.
     * 
     * @param sku
     */
    public void setSku(java.lang.String sku) {
        this.sku = sku;
    }


    /**
     * Gets the skuDescribe value for this AcceptAsnDetails.
     * 
     * @return skuDescribe
     */
    public java.lang.String getSkuDescribe() {
        return skuDescribe;
    }


    /**
     * Sets the skuDescribe value for this AcceptAsnDetails.
     * 
     * @param skuDescribe
     */
    public void setSkuDescribe(java.lang.String skuDescribe) {
        this.skuDescribe = skuDescribe;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AcceptAsnDetails)) return false;
        AcceptAsnDetails other = (AcceptAsnDetails) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.asnReference==null && other.getAsnReference()==null) || 
             (this.asnReference!=null &&
              this.asnReference.equals(other.getAsnReference()))) &&
            ((this.customerID==null && other.getCustomerID()==null) || 
             (this.customerID!=null &&
              this.customerID.equals(other.getCustomerID()))) &&
            ((this.expectedQty==null && other.getExpectedQty()==null) || 
             (this.expectedQty!=null &&
              this.expectedQty.equals(other.getExpectedQty()))) &&
            ((this.price==null && other.getPrice()==null) || 
             (this.price!=null &&
              this.price.equals(other.getPrice()))) &&
            ((this.remark==null && other.getRemark()==null) || 
             (this.remark!=null &&
              this.remark.equals(other.getRemark()))) &&
            ((this.sku==null && other.getSku()==null) || 
             (this.sku!=null &&
              this.sku.equals(other.getSku()))) &&
            ((this.skuDescribe==null && other.getSkuDescribe()==null) || 
             (this.skuDescribe!=null &&
              this.skuDescribe.equals(other.getSkuDescribe())));
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
        if (getAsnReference() != null) {
            _hashCode += getAsnReference().hashCode();
        }
        if (getCustomerID() != null) {
            _hashCode += getCustomerID().hashCode();
        }
        if (getExpectedQty() != null) {
            _hashCode += getExpectedQty().hashCode();
        }
        if (getPrice() != null) {
            _hashCode += getPrice().hashCode();
        }
        if (getRemark() != null) {
            _hashCode += getRemark().hashCode();
        }
        if (getSku() != null) {
            _hashCode += getSku().hashCode();
        }
        if (getSkuDescribe() != null) {
            _hashCode += getSkuDescribe().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AcceptAsnDetails.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://model.BML", "AcceptAsnDetails"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("asnReference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.BML", "asnReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("customerID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.BML", "customerID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expectedQty");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.BML", "expectedQty"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("price");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.BML", "price"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("remark");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.BML", "remark"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sku");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.BML", "sku"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("skuDescribe");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.BML", "skuDescribe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
