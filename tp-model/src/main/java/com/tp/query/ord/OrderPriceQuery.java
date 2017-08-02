/**
 * NewHeight.com Inc.
 * Copyright (c) 2008-2010 All Rights Reserved.
 */
package com.tp.query.ord;

import java.io.Serializable;

/**
 * <pre>
 * 计算价格入参实体
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class OrderPriceQuery implements Serializable {
    private static final long serialVersionUID = 1980588048067073099L;

    /** 用户ID */
    private Long userId;

    /**
     * Getter method for property <tt>userId</tt>.
     * 
     * @return property value of userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Setter method for property <tt>userId</tt>.
     * 
     * @param userId value to be assigned to property userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    
}
