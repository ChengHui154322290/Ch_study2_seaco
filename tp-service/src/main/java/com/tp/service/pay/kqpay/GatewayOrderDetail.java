/**
 * GatewayOrderDetail.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.tp.service.pay.kqpay;

public class GatewayOrderDetail  implements java.io.Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3299227059401877297L;

	private java.lang.String dealId;

    private java.lang.String dealTime;

    private long fee;

    private long orderAmount;

    private java.lang.String orderId;

    private java.lang.String orderTime;

    private long payAmount;

    private java.lang.String payResult;

    private java.lang.String payType;

    private java.lang.String signInfo;

    public GatewayOrderDetail() {
    }

    public GatewayOrderDetail(
           java.lang.String dealId,
           java.lang.String dealTime,
           long fee,
           long orderAmount,
           java.lang.String orderId,
           java.lang.String orderTime,
           long payAmount,
           java.lang.String payResult,
           java.lang.String payType,
           java.lang.String signInfo) {
           this.dealId = dealId;
           this.dealTime = dealTime;
           this.fee = fee;
           this.orderAmount = orderAmount;
           this.orderId = orderId;
           this.orderTime = orderTime;
           this.payAmount = payAmount;
           this.payResult = payResult;
           this.payType = payType;
           this.signInfo = signInfo;
    }

    @Override
	public String toString() {
		return "dealId=" + dealId + ", dealTime=" + dealTime + ", fee=" + fee + ", orderAmount=" + orderAmount + ", orderId=" + orderId
				+ ", orderTime=" + orderTime + ", payAmount=" + payAmount + ", payResult=" + payResult + ", payType=" + payType + ", signInfo=" + signInfo;
	}

	/**
     * Gets the dealId value for this GatewayOrderDetail.
     * 
     * @return dealId
     */
    public java.lang.String getDealId() {
        return dealId;
    }


    /**
     * Sets the dealId value for this GatewayOrderDetail.
     * 
     * @param dealId
     */
    public void setDealId(java.lang.String dealId) {
        this.dealId = dealId;
    }


    /**
     * Gets the dealTime value for this GatewayOrderDetail.
     * 
     * @return dealTime
     */
    public java.lang.String getDealTime() {
        return dealTime;
    }


    /**
     * Sets the dealTime value for this GatewayOrderDetail.
     * 
     * @param dealTime
     */
    public void setDealTime(java.lang.String dealTime) {
        this.dealTime = dealTime;
    }


    /**
     * Gets the fee value for this GatewayOrderDetail.
     * 
     * @return fee
     */
    public long getFee() {
        return fee;
    }


    /**
     * Sets the fee value for this GatewayOrderDetail.
     * 
     * @param fee
     */
    public void setFee(long fee) {
        this.fee = fee;
    }


    /**
     * Gets the orderAmount value for this GatewayOrderDetail.
     * 
     * @return orderAmount
     */
    public long getOrderAmount() {
        return orderAmount;
    }


    /**
     * Sets the orderAmount value for this GatewayOrderDetail.
     * 
     * @param orderAmount
     */
    public void setOrderAmount(long orderAmount) {
        this.orderAmount = orderAmount;
    }


    /**
     * Gets the orderId value for this GatewayOrderDetail.
     * 
     * @return orderId
     */
    public java.lang.String getOrderId() {
        return orderId;
    }


    /**
     * Sets the orderId value for this GatewayOrderDetail.
     * 
     * @param orderId
     */
    public void setOrderId(java.lang.String orderId) {
        this.orderId = orderId;
    }


    /**
     * Gets the orderTime value for this GatewayOrderDetail.
     * 
     * @return orderTime
     */
    public java.lang.String getOrderTime() {
        return orderTime;
    }


    /**
     * Sets the orderTime value for this GatewayOrderDetail.
     * 
     * @param orderTime
     */
    public void setOrderTime(java.lang.String orderTime) {
        this.orderTime = orderTime;
    }


    /**
     * Gets the payAmount value for this GatewayOrderDetail.
     * 
     * @return payAmount
     */
    public long getPayAmount() {
        return payAmount;
    }


    /**
     * Sets the payAmount value for this GatewayOrderDetail.
     * 
     * @param payAmount
     */
    public void setPayAmount(long payAmount) {
        this.payAmount = payAmount;
    }


    /**
     * Gets the payResult value for this GatewayOrderDetail.
     * 
     * @return payResult
     */
    public java.lang.String getPayResult() {
        return payResult;
    }


    /**
     * Sets the payResult value for this GatewayOrderDetail.
     * 
     * @param payResult
     */
    public void setPayResult(java.lang.String payResult) {
        this.payResult = payResult;
    }


    /**
     * Gets the payType value for this GatewayOrderDetail.
     * 
     * @return payType
     */
    public java.lang.String getPayType() {
        return payType;
    }


    /**
     * Sets the payType value for this GatewayOrderDetail.
     * 
     * @param payType
     */
    public void setPayType(java.lang.String payType) {
        this.payType = payType;
    }


    /**
     * Gets the signInfo value for this GatewayOrderDetail.
     * 
     * @return signInfo
     */
    public java.lang.String getSignInfo() {
        return signInfo;
    }


    /**
     * Sets the signInfo value for this GatewayOrderDetail.
     * 
     * @param signInfo
     */
    public void setSignInfo(java.lang.String signInfo) {
        this.signInfo = signInfo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GatewayOrderDetail)) return false;
        GatewayOrderDetail other = (GatewayOrderDetail) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dealId==null && other.getDealId()==null) || 
             (this.dealId!=null &&
              this.dealId.equals(other.getDealId()))) &&
            ((this.dealTime==null && other.getDealTime()==null) || 
             (this.dealTime!=null &&
              this.dealTime.equals(other.getDealTime()))) &&
            this.fee == other.getFee() &&
            this.orderAmount == other.getOrderAmount() &&
            ((this.orderId==null && other.getOrderId()==null) || 
             (this.orderId!=null &&
              this.orderId.equals(other.getOrderId()))) &&
            ((this.orderTime==null && other.getOrderTime()==null) || 
             (this.orderTime!=null &&
              this.orderTime.equals(other.getOrderTime()))) &&
            this.payAmount == other.getPayAmount() &&
            ((this.payResult==null && other.getPayResult()==null) || 
             (this.payResult!=null &&
              this.payResult.equals(other.getPayResult()))) &&
            ((this.payType==null && other.getPayType()==null) || 
             (this.payType!=null &&
              this.payType.equals(other.getPayType()))) &&
            ((this.signInfo==null && other.getSignInfo()==null) || 
             (this.signInfo!=null &&
              this.signInfo.equals(other.getSignInfo())));
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
        if (getDealId() != null) {
            _hashCode += getDealId().hashCode();
        }
        if (getDealTime() != null) {
            _hashCode += getDealTime().hashCode();
        }
        _hashCode += new Long(getFee()).hashCode();
        _hashCode += new Long(getOrderAmount()).hashCode();
        if (getOrderId() != null) {
            _hashCode += getOrderId().hashCode();
        }
        if (getOrderTime() != null) {
            _hashCode += getOrderTime().hashCode();
        }
        _hashCode += new Long(getPayAmount()).hashCode();
        if (getPayResult() != null) {
            _hashCode += getPayResult().hashCode();
        }
        if (getPayType() != null) {
            _hashCode += getPayType().hashCode();
        }
        if (getSignInfo() != null) {
            _hashCode += getSignInfo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GatewayOrderDetail.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gatewayquery.dto.domain.seashell.bill99.com", "GatewayOrderDetail"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dealId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dealId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dealTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dealTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fee");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fee"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orderAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orderId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orderTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("payAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "payAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("payResult");
        elemField.setXmlName(new javax.xml.namespace.QName("", "payResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("payType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "payType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "signInfo"));
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
