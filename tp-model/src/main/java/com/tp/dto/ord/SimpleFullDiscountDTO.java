package com.tp.dto.ord;

import java.io.Serializable;

/**
 * 简单满减规则对象DTO
 *
 * @author szy
 */
public class SimpleFullDiscountDTO implements Serializable {

    private static final long serialVersionUID = 1561485261865324736L;

    private int orderTag = 1;

    /** 满减规则编号 */
    private String fullDiscountCode;

    /** 满减规则名称 */
    private String fullDiscountName;

    /** 满减面值 */
    private Double fullDiscountFace;

    /** 费用承担方 */
    private Long sourceType;

    /**
     * supplierId
     */
    private Long supplierId;

    /** 满减使用金额 */
    private Double fullDiscountUse = 0.00;

    public String getFullDiscountCode() {
        return fullDiscountCode;
    }

    public void setFullDiscountCode(final String fullDiscountCode) {
        this.fullDiscountCode = fullDiscountCode;
    }

    public String getFullDiscountName() {
        return fullDiscountName;
    }

    public void setFullDiscountName(final String fullDiscountName) {
        this.fullDiscountName = fullDiscountName;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Double getFullDiscountFace() {
        return fullDiscountFace;
    }

    public void setFullDiscountFace(final Double fullDiscountFace) {
        this.fullDiscountFace = fullDiscountFace;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fullDiscountCode == null) ? 0 : fullDiscountCode.hashCode() + orderTag);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof SimpleFullDiscountDTO) {
            SimpleFullDiscountDTO dto = (SimpleFullDiscountDTO) obj;
            if (fullDiscountCode == null) {
                return dto.fullDiscountCode == null;
            }
            return fullDiscountCode.equals(dto.fullDiscountCode) && orderTag == dto.orderTag;
        } else {
            return false;
        }
    }

    public Double getFullDiscountUse() {
        return fullDiscountUse;
    }

    public void setFullDiscountUse(final Double fullDiscountUse) {
        this.fullDiscountUse = fullDiscountUse;
    }

    public int getOrderTag() {
        return orderTag;
    }

    public void setOrderTag(final int orderTag) {
        this.orderTag = orderTag;
    }

    public Long getSourceType() {
        return sourceType;
    }

    public void setSourceType(final Long sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(final Long supplierId) {
        this.supplierId = supplierId;
    }

}
