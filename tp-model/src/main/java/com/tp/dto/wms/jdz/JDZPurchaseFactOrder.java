package com.tp.dto.wms.jdz;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ldr on 2016/7/7.
 */
public class JDZPurchaseFactOrder implements Serializable {

    private static final long serialVersionUID = 7824343132518459529L;

    private  String orderCode;

    private String warehouseCode;

    private String auditor;

    private String auditTime;

    private String providerCode;

    private String goodsOwner;

    private String remark;

    private List<JDZPurchaseFactOrderDetail>  orderInDetails;

    public List<JDZPurchaseFactOrderDetail> getOrderInDetails() {
        return orderInDetails;
    }

    public void setOrderInDetails(List<JDZPurchaseFactOrderDetail> orderInDetails) {
        this.orderInDetails = orderInDetails;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public String getGoodsOwner() {
        return goodsOwner;
    }

    public void setGoodsOwner(String goodsOwner) {
        this.goodsOwner = goodsOwner;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
