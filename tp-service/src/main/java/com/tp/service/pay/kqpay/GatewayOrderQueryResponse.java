/**
 * GatewayOrderQueryResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.tp.service.pay.kqpay;

public class GatewayOrderQueryResponse  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7815790016757239342L;

	private java.lang.String currentPage;

    private java.lang.String errCode;

    private java.lang.String merchantAcctId;

    private GatewayOrderDetail[] orders;

    private java.lang.String pageCount;

    private java.lang.String pageSize;

    private java.lang.String recordCount;

    private java.lang.String signMsg;

    private int signType;

    private java.lang.String version;

    public GatewayOrderQueryResponse() {
    }

    public GatewayOrderQueryResponse(
           java.lang.String currentPage,
           java.lang.String errCode,
           java.lang.String merchantAcctId,
           GatewayOrderDetail[] orders,
           java.lang.String pageCount,
           java.lang.String pageSize,
           java.lang.String recordCount,
           java.lang.String signMsg,
           int signType,
           java.lang.String version) {
           this.currentPage = currentPage;
           this.errCode = errCode;
           this.merchantAcctId = merchantAcctId;
           this.orders = orders;
           this.pageCount = pageCount;
           this.pageSize = pageSize;
           this.recordCount = recordCount;
           this.signMsg = signMsg;
           this.signType = signType;
           this.version = version;
    }


    /**
     * Gets the currentPage value for this GatewayOrderQueryResponse.
     * 
     * @return currentPage
     */
    public java.lang.String getCurrentPage() {
        return currentPage;
    }


    /**
     * Sets the currentPage value for this GatewayOrderQueryResponse.
     * 
     * @param currentPage
     */
    public void setCurrentPage(java.lang.String currentPage) {
        this.currentPage = currentPage;
    }


    /**
     * Gets the errCode value for this GatewayOrderQueryResponse.
     * 
     * @return errCode
     */
    public java.lang.String getErrCode() {
        return errCode;
    }


    /**
     * Sets the errCode value for this GatewayOrderQueryResponse.
     * 
     * @param errCode
     */
    public void setErrCode(java.lang.String errCode) {
        this.errCode = errCode;
    }


    /**
     * Gets the merchantAcctId value for this GatewayOrderQueryResponse.
     * 
     * @return merchantAcctId
     */
    public java.lang.String getMerchantAcctId() {
        return merchantAcctId;
    }


    /**
     * Sets the merchantAcctId value for this GatewayOrderQueryResponse.
     * 
     * @param merchantAcctId
     */
    public void setMerchantAcctId(java.lang.String merchantAcctId) {
        this.merchantAcctId = merchantAcctId;
    }


    /**
     * Gets the orders value for this GatewayOrderQueryResponse.
     * 
     * @return orders
     */
    public GatewayOrderDetail[] getOrders() {
        return orders;
    }


    /**
     * Sets the orders value for this GatewayOrderQueryResponse.
     * 
     * @param orders
     */
    public void setOrders(GatewayOrderDetail[] orders) {
        this.orders = orders;
    }


    /**
     * Gets the pageCount value for this GatewayOrderQueryResponse.
     * 
     * @return pageCount
     */
    public java.lang.String getPageCount() {
        return pageCount;
    }


    /**
     * Sets the pageCount value for this GatewayOrderQueryResponse.
     * 
     * @param pageCount
     */
    public void setPageCount(java.lang.String pageCount) {
        this.pageCount = pageCount;
    }


    /**
     * Gets the pageSize value for this GatewayOrderQueryResponse.
     * 
     * @return pageSize
     */
    public java.lang.String getPageSize() {
        return pageSize;
    }


    /**
     * Sets the pageSize value for this GatewayOrderQueryResponse.
     * 
     * @param pageSize
     */
    public void setPageSize(java.lang.String pageSize) {
        this.pageSize = pageSize;
    }


    /**
     * Gets the recordCount value for this GatewayOrderQueryResponse.
     * 
     * @return recordCount
     */
    public java.lang.String getRecordCount() {
        return recordCount;
    }


    /**
     * Sets the recordCount value for this GatewayOrderQueryResponse.
     * 
     * @param recordCount
     */
    public void setRecordCount(java.lang.String recordCount) {
        this.recordCount = recordCount;
    }


    /**
     * Gets the signMsg value for this GatewayOrderQueryResponse.
     * 
     * @return signMsg
     */
    public java.lang.String getSignMsg() {
        return signMsg;
    }


    /**
     * Sets the signMsg value for this GatewayOrderQueryResponse.
     * 
     * @param signMsg
     */
    public void setSignMsg(java.lang.String signMsg) {
        this.signMsg = signMsg;
    }


    /**
     * Gets the signType value for this GatewayOrderQueryResponse.
     * 
     * @return signType
     */
    public int getSignType() {
        return signType;
    }


    /**
     * Sets the signType value for this GatewayOrderQueryResponse.
     * 
     * @param signType
     */
    public void setSignType(int signType) {
        this.signType = signType;
    }


    /**
     * Gets the version value for this GatewayOrderQueryResponse.
     * 
     * @return version
     */
    public java.lang.String getVersion() {
        return version;
    }


    /**
     * Sets the version value for this GatewayOrderQueryResponse.
     * 
     * @param version
     */
    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GatewayOrderQueryResponse)) return false;
        GatewayOrderQueryResponse other = (GatewayOrderQueryResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.currentPage==null && other.getCurrentPage()==null) || 
             (this.currentPage!=null &&
              this.currentPage.equals(other.getCurrentPage()))) &&
            ((this.errCode==null && other.getErrCode()==null) || 
             (this.errCode!=null &&
              this.errCode.equals(other.getErrCode()))) &&
            ((this.merchantAcctId==null && other.getMerchantAcctId()==null) || 
             (this.merchantAcctId!=null &&
              this.merchantAcctId.equals(other.getMerchantAcctId()))) &&
            ((this.orders==null && other.getOrders()==null) || 
             (this.orders!=null &&
              java.util.Arrays.equals(this.orders, other.getOrders()))) &&
            ((this.pageCount==null && other.getPageCount()==null) || 
             (this.pageCount!=null &&
              this.pageCount.equals(other.getPageCount()))) &&
            ((this.pageSize==null && other.getPageSize()==null) || 
             (this.pageSize!=null &&
              this.pageSize.equals(other.getPageSize()))) &&
            ((this.recordCount==null && other.getRecordCount()==null) || 
             (this.recordCount!=null &&
              this.recordCount.equals(other.getRecordCount()))) &&
            ((this.signMsg==null && other.getSignMsg()==null) || 
             (this.signMsg!=null &&
              this.signMsg.equals(other.getSignMsg()))) &&
            this.signType == other.getSignType() &&
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion())));
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
        if (getCurrentPage() != null) {
            _hashCode += getCurrentPage().hashCode();
        }
        if (getErrCode() != null) {
            _hashCode += getErrCode().hashCode();
        }
        if (getMerchantAcctId() != null) {
            _hashCode += getMerchantAcctId().hashCode();
        }
        if (getOrders() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOrders());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOrders(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPageCount() != null) {
            _hashCode += getPageCount().hashCode();
        }
        if (getPageSize() != null) {
            _hashCode += getPageSize().hashCode();
        }
        if (getRecordCount() != null) {
            _hashCode += getRecordCount().hashCode();
        }
        if (getSignMsg() != null) {
            _hashCode += getSignMsg().hashCode();
        }
        _hashCode += getSignType();
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GatewayOrderQueryResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gatewayquery.dto.domain.seashell.bill99.com", "GatewayOrderQueryResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("currentPage");
        elemField.setXmlName(new javax.xml.namespace.QName("", "currentPage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "errCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("merchantAcctId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "merchantAcctId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orders");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orders"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gatewayquery.dto.domain.seashell.bill99.com", "GatewayOrderDetail"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pageCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pageCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pageSize");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pageSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recordCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "recordCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signMsg");
        elemField.setXmlName(new javax.xml.namespace.QName("", "signMsg"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "signType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("", "version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
