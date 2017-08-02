package com.tp.dto.ord.remote;


import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.tp.common.vo.Constant;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.OrderConstant.OrderType;
import com.tp.common.vo.ord.SubOrderConstant;
import com.tp.model.ord.SubOrder;

/**
 * 子订单DTO
 *
 * @author 项硕
 * @version 2015年1月11日 下午5:14:18
 */
public class SubOrderDTO extends SubOrder {

    /**
     * <code>AUDIT_STATUS_1</code> - {审核通过}.
     */
    private static final String AUDIT_STATUS_1 = "1";

    private static final long serialVersionUID = 7070872784217813262L;

    private String payWayStr;

    /**
     * 是否可发货
     *
     * @return
     */
    public boolean canDelivery() {
        return OrderConstant.ORDER_STATUS.DELIVERY.code.equals(getOrderStatus());
    }

    public String getStatusStr() {
        return OrderConstant.ORDER_STATUS.getCnName(getOrderStatus());
    }

    public String getTypeStr() {
        if (OrderType.DOMESTIC.code.equals(getType()) || OrderType.BONDEDAREA.code.equals(getType()) || OrderType.OVERSEASMAIL.code.equals(getType())) { // 海淘订单
            return OrderType.getCnName(getType()) + " - " + getSeaChannelName();
        }
        return OrderType.getCnName(getType());
    }

    public String getDeletedStr() {
        return getDeleted().equals(SubOrderConstant.DELETED_FALSE)? "未刪除" : "已刪除";
    }

    public Double getPayTotal() {
        return new BigDecimal(getTotal()).add(new BigDecimal(getFreight())).doubleValue();
    }

    public String getZhOrderStatus() {
        return OrderConstant.ORDER_STATUS.getCnName(getOrderStatus());
    }

    public String getZhType() {
        return OrderConstant.OrderType.getCnName(getType());
    }

    public String getSeaChannelStr() {
        return getSeaChannelName();
    }


    public String getPayTypeStr() {
        if (null == getPayType()) {
            return StringUtils.EMPTY;
        }
        return getPayType() == null ? null : getPayType().toString();
    }

    public String getPayWayStr() {
        return this.payWayStr;
    }

    public void setPayWayStr(final String payWayStr) {
        this.payWayStr = payWayStr;
    }

    public String getSourceStr() {
        return Constant.PLATFORM_TYPE.getCnName(getSource());
    }
}
