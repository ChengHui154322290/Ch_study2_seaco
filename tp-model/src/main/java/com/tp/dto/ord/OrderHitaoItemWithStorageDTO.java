/**
 *
 */
package com.tp.dto.ord;

import java.util.List;

/**
 * 海淘订单商品，根据仓库拆分
 *
 * @author szy
 */
public class OrderHitaoItemWithStorageDTO implements BaseDTO {
    private static final long serialVersionUID = -2645511775715446304L;

    /** 仓库ID */
    private Long warehouseId;

    /** 仓库名称 */
    private String warehouseName;

    /** 仓库类型 */
    private Integer storageType;

    /** 海淘渠道 */
    private Long seaChannelId;
    /** 海淘渠道编码 */
    private String seaChannelCode;
    /** 海淘渠道名称 */
    private String seaChannelName;

    /** 订购商品行信息 */
    private List<OrderItemLineDTO> orderItemLineList;

    /** 购买的商品总数量 */
    private Integer itemTotalQuantity;

    /** 购买的商品总金额 */
    private Double totalPrice;

    /** 商品总优惠金额 */
    private Double totalDiscount;

    /** 购买的商品总税费 */
    private Double totalTaxfee;

    /** 真实使用的税费，该税费会计进总价中 */
    private Double actualUsedTaxfee;

    /** 购买的商品总运费 */
    private Double totalFreight;

    /** 购买的商品应付总金额（总金额-优惠金额+税费+运费） */
    private Double totalPayPrice;

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(final Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(final String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public Integer getStorageType() {
        return storageType;
    }

    public void setStorageType(final Integer storageType) {
        this.storageType = storageType;
    }

    public String getSeaChannelCode() {
        return seaChannelCode;
    }

    public void setSeaChannelCode(final String seaChannelCode) {
        this.seaChannelCode = seaChannelCode;
    }

    public String getSeaChannelName() {
        return seaChannelName;
    }

    public void setSeaChannelName(final String seaChannelName) {
        this.seaChannelName = seaChannelName;
    }

    public List<OrderItemLineDTO> getOrderItemLineList() {
        return orderItemLineList;
    }

    public void setOrderItemLineList(final List<OrderItemLineDTO> orderItemLineList) {
        this.orderItemLineList = orderItemLineList;
    }

    public Integer getItemTotalQuantity() {
        return itemTotalQuantity;
    }

    public void setItemTotalQuantity(final Integer itemTotalQuantity) {
        this.itemTotalQuantity = itemTotalQuantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(final Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(final Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public Double getTotalTaxfee() {
        return totalTaxfee;
    }

    public void setTotalTaxfee(final Double totalTaxfee) {
        this.totalTaxfee = totalTaxfee;
    }

    public Double getActualUsedTaxfee() {
        return actualUsedTaxfee;
    }

    public void setActualUsedTaxfee(final Double actualUsedTaxfee) {
        this.actualUsedTaxfee = actualUsedTaxfee;
    }

    public Double getTotalFreight() {
        return totalFreight;
    }

    public void setTotalFreight(final Double totalFreight) {
        this.totalFreight = totalFreight;
    }

    public Double getTotalPayPrice() {
        return totalPayPrice;
    }

    public void setTotalPayPrice(final Double totalPayPrice) {
        this.totalPayPrice = totalPayPrice;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Long getSeaChannelId() {
        return seaChannelId;
    }

    public void setSeaChannelId(final Long seaChannelId) {
        this.seaChannelId = seaChannelId;
    }

}
