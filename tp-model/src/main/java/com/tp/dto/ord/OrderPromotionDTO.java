/**
 * NewHeight.com Inc.
 * Copyright (c) 2008-2010 All Rights Reserved.
 */
package com.tp.dto.ord;

import java.io.Serializable;

/**
 * <pre>
 * 订单促销信息
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class OrderPromotionDTO implements Serializable {
    private static final long serialVersionUID = 2420890512769276510L;
    
    /** 主键 */
    private Long id;

    /** 类型（整单优惠、赠品、包邮等等） */
    private Integer type;

    /** 促销ID */
    private Long promotionId;

    /** 优惠金额 */
    private String discount;

    /**
     * Getter method for property <tt>id</tt>.
     * 
     * @return property value of id
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter method for property <tt>id</tt>.
     * 
     * @param id value to be assigned to property id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter method for property <tt>type</tt>.
     * 
     * @return property value of type
     */
    public Integer getType() {
        return type;
    }

    /**
     * Setter method for property <tt>type</tt>.
     * 
     * @param type value to be assigned to property type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * Getter method for property <tt>promotionId</tt>.
     * 
     * @return property value of promotionId
     */
    public Long getPromotionId() {
        return promotionId;
    }

    /**
     * Setter method for property <tt>promotionId</tt>.
     * 
     * @param promotionId value to be assigned to property promotionId
     */
    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    /**
     * Getter method for property <tt>discount</tt>.
     * 
     * @return property value of discount
     */
    public String getDiscount() {
        return discount;
    }

    /**
     * Setter method for property <tt>discount</tt>.
     * 
     * @param discount value to be assigned to property discount
     */
    public void setDiscount(String discount) {
        this.discount = discount;
    }

}
