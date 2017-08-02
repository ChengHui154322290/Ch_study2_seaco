/**
 * BMLQueryPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package com.tp.service.stg.client.BML;

public interface BMLQueryPortType extends java.rmi.Remote {
    public java.lang.String acceptEDIResult(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String queryShipmentStatus(java.lang.String in0) throws java.rmi.RemoteException;
    public java.lang.String invoiceFromBZ(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String noticeOfArrivalQueryToday(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.String urgentOrderBySoNo(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String snFeeBack(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.String stockQueryLocBySku(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String noticeOfArrivalQueryById(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String searchOrderStatus(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String cancelOrderBySku(java.lang.String in0, java.lang.String in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException;
    public java.lang.String getCarrierInfo(java.lang.String in0) throws java.rmi.RemoteException;
    public java.lang.String cancelAsnRX(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String orderCollectionToday(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.String customerToWms(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String test(com.tp.model.stg.BML.ArrayOfAcceptAsnHeader in0) throws java.rmi.RemoteException, java.sql.SQLException;
    public java.lang.String expressChargeQueryByMonth(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String orderDetailQueryToday(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.String deliveryFeeBack(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.String queryInventory(java.lang.String in0) throws java.rmi.RemoteException;
    public java.lang.String soToWmsbyPage(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String ansToWmsbyPage(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String noticeOfArrivalAssign(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String cancelShipment(java.lang.String in0) throws java.rmi.RemoteException;
    public java.lang.String orderCollectionYesterday(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.String createShipment(java.lang.String in0) throws java.rmi.RemoteException;
    public java.lang.String createItem(java.lang.String in0) throws java.rmi.RemoteException;
    public java.lang.String cancelReceipt(java.lang.String in0) throws java.rmi.RemoteException;
    public java.lang.String orderDetailQueryById(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String shipmentInfoQueryByDate(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String invoiceFeeBack(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.String stockQueryBySku(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String ansToWms(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String noticeOfArrivalQueryYesterday(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.String orderDetailQueryYesterday(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.String shipmentInfoQueryByOrderId(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String createReceipt(java.lang.String in0) throws java.rmi.RemoteException;
    public java.lang.String soToWms(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String singleSkuToWms(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String orderCostQueryByMonth(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String soFeeBackCheck(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String soFeeBack(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.String cancelOrderRX(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String billToWms(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String cancelOrderBySoNo(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String stockQuery(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.String invoiceToWms(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
}
