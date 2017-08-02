package com.tp.dto.wms.jdz;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ldr on 2016/6/23.
 */
public class JDZPurchaseOrderInfo implements Serializable {

    private static final long serialVersionUID = -7838864634855781755L;
    /**
     * 采购单号 必填
     */
    private String orderCode;
    /**
     * 合同号
     */
    private String contractCode;
    /**
     * 供应商编码
     */
    private String supplierCode;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 审核人编码 必填
     */
    private String operatorCode;
    /**
     * 审核人名称 必填
     */
    private String operator;
    /**
     * 审核日期 (yyyy-MM-dd hh:mm:ss) 必填
     */
    private String operateTime;
    /**
     * 仓库编码 默认 3316W6K001  必填
     */
    private String warehouseCode;
    /**
     * 仓库名称 默认 杭州通隆国际货运代理有限公司 必填
     */
    private String warehouseName;
    /**
     * 预计到货日期(yyyy-MM-dd hh:mm:ss) 必填
     */
    private String planTime;
    /**
     * 业务类型 1 生产采购 2 产品采购 3样品入库 4 残品入库 99 其他采购 必填
     */
    private String orderType;
    /**
     * 是否质检 0 否 1 是 默认 0
     */
    private String isCheck;
    /**
     * 是否裸装 0 否 1 是(全检) 默认0
     */
    private String isPackage;
    /**
     * 快递单号
     */
    private String expressCode;
    /**
     * 尺寸详情
     */
    private String sizeDetail;
    /**
     * 账册编码 (对接时告知商家，一个平台一个账号) 必填
     */
    private String manualId;
    /**
     * 毛重 必填
     */
    private Double grossWeight;
    /**
     * 净重 必填
     */
    private Double netWeight;
    /**
     * 件数 必填
     */
    private Integer amount;
    /**
     * 包装种类 有代码表 必填
     */
    private String wrapType;
    /**
     * 关区代码 必填
     */
    private  String customsCode;
    /**
     * 申报关区 必填
     */
    private String port;
    /**
     * 业务类别 默认K3 先进后报 必填
     */
    private String type;
    /**
     * 厂商编码(商检海关备案编码) 默认 CC01 必填
     */
    private String providerCode;
    /**
     * 备注
     */
    private String remark;
    /**
     * 货主名 必填 账册编号
     */
    private String goodsOwner;

    private List<JDZPurchaseOrderDetailInfo> items;


    public List<JDZPurchaseOrderDetailInfo> getItems() {
        return items;
    }

    public void setItems(List<JDZPurchaseOrderDetailInfo> items) {
        this.items = items;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getPlanTime() {
        return planTime;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public String getIsPackage() {
        return isPackage;
    }

    public void setIsPackage(String isPackage) {
        this.isPackage = isPackage;
    }

    public String getExpressCode() {
        return expressCode;
    }

    public void setExpressCode(String expressCode) {
        this.expressCode = expressCode;
    }

    public String getSizeDetail() {
        return sizeDetail;
    }

    public void setSizeDetail(String sizeDetail) {
        this.sizeDetail = sizeDetail;
    }

    public String getManualId() {
        return manualId;
    }

    public void setManualId(String manualId) {
        this.manualId = manualId;
    }

    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getWrapType() {
        return wrapType;
    }

    public void setWrapType(String wrapType) {
        this.wrapType = wrapType;
    }

    public String getCustomsCode() {
        return customsCode;
    }

    public void setCustomsCode(String customsCode) {
        this.customsCode = customsCode;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getGoodsOwner() {
        return goodsOwner;
    }

    public void setGoodsOwner(String goodsOwner) {
        this.goodsOwner = goodsOwner;
    }
}
