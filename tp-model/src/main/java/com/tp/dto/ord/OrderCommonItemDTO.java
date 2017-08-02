package com.tp.dto.ord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 订购普通商品实体类
 *
 * @author szy
 */
public class OrderCommonItemDTO implements Serializable {
    private static final long serialVersionUID = 1915653022326706804L;
    /** 购买商品行列表 */
    private List<OrderItemLineDTO> orderItemLineDTOList;
    /** 使用优惠券ID(包括满减券和红包) */
    private List<Long> couponIdList;

    /** 优惠券对应商品SKUCODE */
    private Map<Long, List<String>> cuidSkuMap;

    /** 优惠券信息 */
    List<SimpleCouponInfoDTO> couponInfoList;

    /** 满减分组信息 */
    private Map<SimpleFullDiscountDTO, List<SimpleOrderItemDTO>> fullDiscountGroupMap;
    /** 用户ID */
    private Long memberId;
    /** 商品总数量 */
    private Integer quantity;

    /** 商品总价,无优惠处理 */
    private Double itemTotalSum;
    /** 优惠总金额 */
    private Double totalDiscount;
    /** 应付运费 */
    private Double originalFreight;
    /** 实付运费 */
    private Double realPayFreight;
    /** 实付金额 */
    private Double realPaySum;
    /** 扣除满减的总额 */
    private Double realTotalPayPrice;
    /** 满减总额 */
    private Double fullDiscountTotal;

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
        return orderItemLineDTOList;
    }

    public List<OrderItemLineDTO> getOrderItemLineDTOList() {
        return orderItemLineDTOList;
    }

    public void setOrderItemLineDTOList(final List<OrderItemLineDTO> orderItemLineDTOList) {
        this.orderItemLineDTOList = orderItemLineDTOList;
    }

    public List<Long> getCouponIdList() {
        return couponIdList;
    }

    public void setCouponIdList(final List<Long> couponIdList) {
        this.couponIdList = couponIdList;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(final Long memberId) {
        this.memberId = memberId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    public Double getItemTotalSum() {
        return itemTotalSum;
    }

    public void setItemTotalSum(final Double itemTotalSum) {
        this.itemTotalSum = itemTotalSum;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(final Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public Double getOriginalFreight() {
        return originalFreight;
    }

    public void setOriginalFreight(final Double originalFreight) {
        this.originalFreight = originalFreight;
    }

    public Double getRealPayFreight() {
        return realPayFreight;
    }

    public void setRealPayFreight(final Double realPayFreight) {
        this.realPayFreight = realPayFreight;
    }

    public Double getRealPaySum() {
        return realPaySum;
    }

    public void setRealPaySum(final Double realPaySum) {
        this.realPaySum = realPaySum;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Map<SimpleFullDiscountDTO, List<SimpleOrderItemDTO>> getFullDiscountGroupMap() {
        return fullDiscountGroupMap;
    }

    public void setFullDiscountGroupMap(final Map<SimpleFullDiscountDTO, List<SimpleOrderItemDTO>> fullDiscountGroupMap) {
        this.fullDiscountGroupMap = fullDiscountGroupMap;
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

    public Double getFullDiscountTotal() {
        return fullDiscountTotal;
    }

    public void setFullDiscountTotal(final Double fullDiscountTotal) {
        this.fullDiscountTotal = fullDiscountTotal;
    }

    public Double getRealTotalPayPrice() {
        return realTotalPayPrice;
    }

    public void setRealTotalPayPrice(final Double realTotalPayPrice) {
        this.realTotalPayPrice = realTotalPayPrice;
    }

}
