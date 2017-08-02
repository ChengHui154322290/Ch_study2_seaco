package com.tp.common.vo.supplier;

/**
 * {采购管理-采购模块} <br>
 * Create on : 2015年1月5日 下午5:44:28<br>
 * 
 * @author szy
 * @version 0.0.1
 */
public final class PurchaseConstant {

    /**
     * <code>CONFIRM</code> - {[订单][退货单]确认}.
     */
    public static final String BILL_CONFIRM = "1";

    /**
     * <code>UNCONFIRM</code> - {[订单][退货单]未确认}.
     */
    public static final String BILL_UNCONFIRM = "0";

    /**
     * <code>BILL_TYPE_COMMON</code> - {一般}.
     */
    public static final String BILL_TYPE_COMMON = "1";

    /**
     * <code>BILL_TYPE_EMERGENCY</code> - {紧急}.
     */
    public static final String BILL_TYPE_EMERGENCY = "0";

    /**
     * <code>RECEIVE_ALL</code> - {收货-全部}.
     */
    public static final String RECEIVE_ALL = "0";

    /**
     * <code>RECEIVE_PART</code> - {收货-部分}.
     */
    public static final String RECEIVE_PART = "0";

    /**
     * <code>ORDER_DONE</code> - {订单完成}.
     */
    public static final String ORDER_DONE = "OrderDone";


    private PurchaseConstant() {
    }

}
