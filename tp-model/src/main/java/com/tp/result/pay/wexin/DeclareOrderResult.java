package com.tp.result.pay.wexin;


/**
 * Created by ldr on 8/24/2016.
 */
public class DeclareOrderResult  extends WeixinResult {

    private static final long serialVersionUID = -73398876346111942L;
    /**
     * 状态码
     UNDECLARED -- 未申报
     SUBMITTED -- 申报已提交（订单已经送海关，商户重新申报，并且海关还有修改接口，那么记录的状态会是这个）
     PROCESSING -- 申报中
     SUCCESS -- 申报成功
     FAIL-- 申报失败
     EXCEPT --海关接口异常
     */
    private String state;

    /**
     * 微信支付订单号
     */
    private String transaction_id;

    /**
     * 商户订单号
     */
    private String out_trade_no;

    /**
     * 商户子订单号
     */
    private String sub_order_no;

    /**
     * 微信子订单号
     */
    private String sub_order_id;

    /**
     * 最后更新时间
     */
    private String modify_time;


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getSub_order_no() {
        return sub_order_no;
    }

    public void setSub_order_no(String sub_order_no) {
        this.sub_order_no = sub_order_no;
    }

    public String getSub_order_id() {
        return sub_order_id;
    }

    public void setSub_order_id(String sub_order_id) {
        this.sub_order_id = sub_order_id;
    }

    public String getModify_time() {
        return modify_time;
    }

    public void setModify_time(String modify_time) {
        this.modify_time = modify_time;
    }
}
