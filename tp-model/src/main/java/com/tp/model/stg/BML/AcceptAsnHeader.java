/**
 * AcceptAsnHeader.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package com.tp.model.stg.BML;

public class AcceptAsnHeader  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7192141729738127622L;
	private com.tp.model.stg.BML.ArrayOfAcceptAsnDetails asnDetails;
    private java.lang.String asnReference;
    private java.lang.String asnType;
    private java.lang.String creationTime;
    private java.lang.String currentPage;
    private java.lang.String customerID;
    private java.lang.String expectedArriveTime;
    private java.lang.String notes;
    private java.lang.String totalPage;
    private java.lang.String uniqueID;
    private java.lang.String wareHouseID;

    public AcceptAsnHeader() {
    }

    public AcceptAsnHeader(
           com.tp.model.stg.BML.ArrayOfAcceptAsnDetails asnDetails,
           java.lang.String asnReference,
           java.lang.String asnType,
           java.lang.String creationTime,
           java.lang.String currentPage,
           java.lang.String customerID,
           java.lang.String expectedArriveTime,
           java.lang.String notes,
           java.lang.String totalPage,
           java.lang.String uniqueID,
           java.lang.String wareHouseID) {
           this.asnDetails = asnDetails;
           this.asnReference = asnReference;
           this.asnType = asnType;
           this.creationTime = creationTime;
           this.currentPage = currentPage;
           this.customerID = customerID;
           this.expectedArriveTime = expectedArriveTime;
           this.notes = notes;
           this.totalPage = totalPage;
           this.uniqueID = uniqueID;
           this.wareHouseID = wareHouseID;
    }


    /**
     * Gets the asnDetails value for this AcceptAsnHeader.
     * 
     * @return asnDetails
     */
    public com.tp.model.stg.BML.ArrayOfAcceptAsnDetails getAsnDetails() {
        return asnDetails;
    }


    /**
     * Sets the asnDetails value for this AcceptAsnHeader.
     * 
     * @param asnDetails
     */
    public void setAsnDetails(com.tp.model.stg.BML.ArrayOfAcceptAsnDetails asnDetails) {
        this.asnDetails = asnDetails;
    }


    /**
     * Gets the asnReference value for this AcceptAsnHeader.
     * 
     * @return asnReference
     */
    public java.lang.String getAsnReference() {
        return asnReference;
    }


    /**
     * Sets the asnReference value for this AcceptAsnHeader.
     * 
     * @param asnReference
     */
    public void setAsnReference(java.lang.String asnReference) {
        this.asnReference = asnReference;
    }


    /**
     * Gets the asnType value for this AcceptAsnHeader.
     * 
     * @return asnType
     */
    public java.lang.String getAsnType() {
        return asnType;
    }


    /**
     * Sets the asnType value for this AcceptAsnHeader.
     * 
     * @param asnType
     */
    public void setAsnType(java.lang.String asnType) {
        this.asnType = asnType;
    }


    /**
     * Gets the creationTime value for this AcceptAsnHeader.
     * 
     * @return creationTime
     */
    public java.lang.String getCreationTime() {
        return creationTime;
    }


    /**
     * Sets the creationTime value for this AcceptAsnHeader.
     * 
     * @param creationTime
     */
    public void setCreationTime(java.lang.String creationTime) {
        this.creationTime = creationTime;
    }


    /**
     * Gets the currentPage value for this AcceptAsnHeader.
     * 
     * @return currentPage
     */
    public java.lang.String getCurrentPage() {
        return currentPage;
    }


    /**
     * Sets the currentPage value for this AcceptAsnHeader.
     * 
     * @param currentPage
     */
    public void setCurrentPage(java.lang.String currentPage) {
        this.currentPage = currentPage;
    }


    /**
     * Gets the customerID value for this AcceptAsnHeader.
     * 
     * @return customerID
     */
    public java.lang.String getCustomerID() {
        return customerID;
    }


    /**
     * Sets the customerID value for this AcceptAsnHeader.
     * 
     * @param customerID
     */
    public void setCustomerID(java.lang.String customerID) {
        this.customerID = customerID;
    }


    /**
     * Gets the expectedArriveTime value for this AcceptAsnHeader.
     * 
     * @return expectedArriveTime
     */
    public java.lang.String getExpectedArriveTime() {
        return expectedArriveTime;
    }


    /**
     * Sets the expectedArriveTime value for this AcceptAsnHeader.
     * 
     * @param expectedArriveTime
     */
    public void setExpectedArriveTime(java.lang.String expectedArriveTime) {
        this.expectedArriveTime = expectedArriveTime;
    }


    /**
     * Gets the notes value for this AcceptAsnHeader.
     * 
     * @return notes
     */
    public java.lang.String getNotes() {
        return notes;
    }


    /**
     * Sets the notes value for this AcceptAsnHeader.
     * 
     * @param notes
     */
    public void setNotes(java.lang.String notes) {
        this.notes = notes;
    }


    /**
     * Gets the totalPage value for this AcceptAsnHeader.
     * 
     * @return totalPage
     */
    public java.lang.String getTotalPage() {
        return totalPage;
    }


    /**
     * Sets the totalPage value for this AcceptAsnHeader.
     * 
     * @param totalPage
     */
    public void setTotalPage(java.lang.String totalPage) {
        this.totalPage = totalPage;
    }


    /**
     * Gets the uniqueID value for this AcceptAsnHeader.
     * 
     * @return uniqueID
     */
    public java.lang.String getUniqueID() {
        return uniqueID;
    }


    /**
     * Sets the uniqueID value for this AcceptAsnHeader.
     * 
     * @param uniqueID
     */
    public void setUniqueID(java.lang.String uniqueID) {
        this.uniqueID = uniqueID;
    }


    /**
     * Gets the wareHouseID value for this AcceptAsnHeader.
     * 
     * @return wareHouseID
     */
    public java.lang.String getWareHouseID() {
        return wareHouseID;
    }


    /**
     * Sets the wareHouseID value for this AcceptAsnHeader.
     * 
     * @param wareHouseID
     */
    public void setWareHouseID(java.lang.String wareHouseID) {
        this.wareHouseID = wareHouseID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AcceptAsnHeader)) return false;
        AcceptAsnHeader other = (AcceptAsnHeader) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.asnDetails==null && other.getAsnDetails()==null) || 
             (this.asnDetails!=null &&
              this.asnDetails.equals(other.getAsnDetails()))) &&
            ((this.asnReference==null && other.getAsnReference()==null) || 
             (this.asnReference!=null &&
              this.asnReference.equals(other.getAsnReference()))) &&
            ((this.asnType==null && other.getAsnType()==null) || 
             (this.asnType!=null &&
              this.asnType.equals(other.getAsnType()))) &&
            ((this.creationTime==null && other.getCreationTime()==null) || 
             (this.creationTime!=null &&
              this.creationTime.equals(other.getCreationTime()))) &&
            ((this.currentPage==null && other.getCurrentPage()==null) || 
             (this.currentPage!=null &&
              this.currentPage.equals(other.getCurrentPage()))) &&
            ((this.customerID==null && other.getCustomerID()==null) || 
             (this.customerID!=null &&
              this.customerID.equals(other.getCustomerID()))) &&
            ((this.expectedArriveTime==null && other.getExpectedArriveTime()==null) || 
             (this.expectedArriveTime!=null &&
              this.expectedArriveTime.equals(other.getExpectedArriveTime()))) &&
            ((this.notes==null && other.getNotes()==null) || 
             (this.notes!=null &&
              this.notes.equals(other.getNotes()))) &&
            ((this.totalPage==null && other.getTotalPage()==null) || 
             (this.totalPage!=null &&
              this.totalPage.equals(other.getTotalPage()))) &&
            ((this.uniqueID==null && other.getUniqueID()==null) || 
             (this.uniqueID!=null &&
              this.uniqueID.equals(other.getUniqueID()))) &&
            ((this.wareHouseID==null && other.getWareHouseID()==null) || 
             (this.wareHouseID!=null &&
              this.wareHouseID.equals(other.getWareHouseID())));
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
        if (getAsnDetails() != null) {
            _hashCode += getAsnDetails().hashCode();
        }
        if (getAsnReference() != null) {
            _hashCode += getAsnReference().hashCode();
        }
        if (getAsnType() != null) {
            _hashCode += getAsnType().hashCode();
        }
        if (getCreationTime() != null) {
            _hashCode += getCreationTime().hashCode();
        }
        if (getCurrentPage() != null) {
            _hashCode += getCurrentPage().hashCode();
        }
        if (getCustomerID() != null) {
            _hashCode += getCustomerID().hashCode();
        }
        if (getExpectedArriveTime() != null) {
            _hashCode += getExpectedArriveTime().hashCode();
        }
        if (getNotes() != null) {
            _hashCode += getNotes().hashCode();
        }
        if (getTotalPage() != null) {
            _hashCode += getTotalPage().hashCode();
        }
        if (getUniqueID() != null) {
            _hashCode += getUniqueID().hashCode();
        }
        if (getWareHouseID() != null) {
            _hashCode += getWareHouseID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AcceptAsnHeader.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://model.BML", "AcceptAsnHeader"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("asnDetails");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.BML", "asnDetails"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://model.BML", "ArrayOfAcceptAsnDetails"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("asnReference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.BML", "asnReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("asnType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.BML", "asnType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creationTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.BML", "creationTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("currentPage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.BML", "currentPage"));
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
        elemField.setFieldName("expectedArriveTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.BML", "expectedArriveTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.BML", "notes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalPage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.BML", "totalPage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uniqueID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.BML", "uniqueID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("wareHouseID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.BML", "wareHouseID"));
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
