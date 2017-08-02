/**
 *
 */
package com.tp.dto.ord;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 订购海淘商品实体
 *
 * @author szy
 */
public class OrderHitaoItemDTO implements Serializable {
    private static final long serialVersionUID = 6606631559055144425L;
    /** 会员ID */
    private Long memberId;
    /** 使用优惠券ID(包括满减券和红包) */
    private List<Long> couponIdList;
    /** 优惠券对应商品SKUCODE */
    private Map<Long, List<String>> cuidSkuMap;
    /** 优惠券信息 */
    List<SimpleCouponInfoDTO> couponInfoList;
    /** 满减分组信息 */
    private Map<SimpleFullDiscountDTO, List<SimpleOrderItemDTO>> fullDiscountGroupMap;
    /** 海淘订单商品，根据供应商拆分 */
    private List<OrderHitaoItemWithSupplierDTO> orderHitaoItemWithSupplierList;
    /** 优惠券对应 子单 商品SKUCODE */
    private Map<Long, Map<String, List<String>>> cuidSuborderSkuMap;

    /** 商品金额总价 */
    private Double totalPrice;

    /** 商品总优惠金额 */
    private Double totalDiscount;

    /** 商品支付价格 */
    private Double payPrice;

    /** 购买的商品总税费 */
    private Double totalTaxfee;

    /** 原始运费 */
    private Double originalFreight;

    /** 购买的商品总运费 */
    private Double totalFreight;

    /** 整单商品总数 */
    private Integer quantity;

    /** 商品吊牌总价总价 */
    private Double totalOrignalSum;

    /** 活动未使用优惠券应付金额 */
    private Double realSum;

    /**
     * 获取满减编号列表
     *
     * @return
     */
    public List<String> getFullDiscountCodeList() {
        List<String> list = new ArrayList<>();
        if (MapUtils.isNotEmpty(this.fullDiscountGroupMap)) {
            for (SimpleFullDiscountDTO simpleFullDiscount : fullDiscountGroupMap.keySet()) {
                if (simpleFullDiscount != null && StringUtils.isNotBlank(simpleFullDiscount.getFullDiscountCode())) {
                    list.add(simpleFullDiscount.getFullDiscountCode());
                }
            }
        }
        return list;
    }


    public List<OrderItemLineDTO> lineList() {
        List<OrderItemLineDTO> lineList = new ArrayList<OrderItemLineDTO>();
        if (CollectionUtils.isNotEmpty(orderHitaoItemWithSupplierList)) {
            for (OrderHitaoItemWithSupplierDTO itemSupplier : orderHitaoItemWithSupplierList) {
                if (CollectionUtils.isNotEmpty(itemSupplier.getOrderHitaoItemWithStorageList())) {
                    for (OrderHitaoItemWithStorageDTO itemStorage : itemSupplier.getOrderHitaoItemWithStorageList()) {
                        if (CollectionUtils.isNotEmpty(itemStorage.getOrderItemLineList())) {
                            lineList.addAll(itemStorage.getOrderItemLineList());
                        }
                    }
                }
            }
        }
        return lineList;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(final Long memberId) {
        this.memberId = memberId;
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

    public Double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(final Double payPrice) {
        this.payPrice = payPrice;
    }

    public Double getTotalTaxfee() {
        return totalTaxfee;
    }

    public void setTotalTaxfee(final Double totalTaxfee) {
        this.totalTaxfee = totalTaxfee;
    }

    public Double getTotalFreight() {
        return totalFreight;
    }

    public void setTotalFreight(final Double totalFreight) {
        this.totalFreight = totalFreight;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalOrignalSum() {
        return totalOrignalSum;
    }

    public void setTotalOrignalSum(final Double totalOrignalSum) {
        this.totalOrignalSum = totalOrignalSum;
    }

    public Double getRealSum() {
        return realSum;
    }

    public void setRealSum(final Double realSum) {
        this.realSum = realSum;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public List<OrderHitaoItemWithSupplierDTO> getOrderHitaoItemWithSupplierList() {
        return orderHitaoItemWithSupplierList;
    }

    public void setOrderHitaoItemWithSupplierList(final List<OrderHitaoItemWithSupplierDTO> orderHitaoItemWithSupplierList) {
        this.orderHitaoItemWithSupplierList = orderHitaoItemWithSupplierList;
    }

    public Map<SimpleFullDiscountDTO, List<SimpleOrderItemDTO>> getFullDiscountGroupMap() {
        return fullDiscountGroupMap;
    }

    public void setFullDiscountGroupMap(final Map<SimpleFullDiscountDTO, List<SimpleOrderItemDTO>> fullDiscountGroupMap) {
        this.fullDiscountGroupMap = fullDiscountGroupMap;
    }

    public List<Long> getCouponIdList() {
        return couponIdList;
    }

    public void setCouponIdList(final List<Long> couponIdList) {
        this.couponIdList = couponIdList;
    }

    public Map<Long, List<String>> getCuidSkuMap() {
        return cuidSkuMap;
    }

    public void setCuidSkuMap(final Map<Long, List<String>> cuidSkuMap) {
        this.cuidSkuMap = cuidSkuMap;
    }

    public List<SimpleCouponInfoDTO> getCouponInfoList() {
        return couponInfoList;
    }

    public void setCouponInfoList(final List<SimpleCouponInfoDTO> couponInfoList) {
        this.couponInfoList = couponInfoList;
    }

    public Double getOriginalFreight() {
        return originalFreight;
    }

    public void setOriginalFreight(final Double originalFreight) {
        this.originalFreight = originalFreight;
    }

    public Map<Long, Map<String, List<String>>> getCuidSuborderSkuMap() {
        return cuidSuborderSkuMap;
    }

    public void setCuidSuborderSkuMap(final Map<Long, Map<String, List<String>>> cuidSuborderSkuMap) {
        this.cuidSuborderSkuMap = cuidSuborderSkuMap;
    }

}
