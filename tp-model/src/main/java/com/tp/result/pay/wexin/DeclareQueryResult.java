package com.tp.result.pay.wexin;

/**
 * Created by ldr on 8/24/2016.
 */
public class DeclareQueryResult extends WeixinResult {
    private static final long serialVersionUID = -3206069366284265293L;
    /**
     * 微信支付订单号
     */
    private String  transaction_id ;
    /**
     * 笔数
     */
    private int count  ;
    /**
     * 商户子订单号
     */
    private String sub_order_no_0  ;
    /**
     * 微信子订单号
     */
    private String sub_order_id_0  ;
    /**
     * 商户海关备案号
     */
    private String  mch_customs_no_0 ;
    /**
     * 海关
     */
    private String  customs_0  ;
    /**
     * 证件类型
     */
    private String cert_type_0   ;
    /**
     * 证件号码
     */
    private String  cert_id_0 ;
    /**
     * 姓名
     */
    private String  name_0 ;
    /**
     * 币种
     */
    private String fee_type_0  ;
    /**
     * 应付金额
     */
    private String order_fee_0  ;
    /**
     * 关税
     */
    private String duty_0  ;
    /**
     * 物流费
     */
    private String transport_fee_0  ;
    /**
     * 商品价格
     */
    private String product_fee_0  ;
    /**
     * 状态码
     UNDECLARED -- 未申报
     SUBMITTED -- 申报已提交（订单已经送海关，商户重新申报，并且海关还有修改接口，那么记录的状态会是这个）
     PROCESSING -- 申报中
     SUCCESS -- 申报成功
     FAIL -- 申报失败
     EXCEPT --海关接口异常
     */
    private String  state_0 ;
    /**
     * 申报结果说明
     */
    private String  explanation_0 ;
    /**
     * 最后更新时间
     */
    private String modify_time_0  ;


    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSub_order_no_0() {
        return sub_order_no_0;
    }

    public void setSub_order_no_0(String sub_order_no_0) {
        this.sub_order_no_0 = sub_order_no_0;
    }

    public String getSub_order_id_0() {
        return sub_order_id_0;
    }

    public void setSub_order_id_0(String sub_order_id_0) {
        this.sub_order_id_0 = sub_order_id_0;
    }

    public String getMch_customs_no_0() {
        return mch_customs_no_0;
    }

    public void setMch_customs_no_0(String mch_customs_no_0) {
        this.mch_customs_no_0 = mch_customs_no_0;
    }

    public String getCustoms_0() {
        return customs_0;
    }

    public void setCustoms_0(String customs_0) {
        this.customs_0 = customs_0;
    }

    public String getCert_type_0() {
        return cert_type_0;
    }

    public void setCert_type_0(String cert_type_0) {
        this.cert_type_0 = cert_type_0;
    }

    public String getCert_id_0() {
        return cert_id_0;
    }

    public void setCert_id_0(String cert_id_0) {
        this.cert_id_0 = cert_id_0;
    }

    public String getName_0() {
        return name_0;
    }

    public void setName_0(String name_0) {
        this.name_0 = name_0;
    }

    public String getFee_type_0() {
        return fee_type_0;
    }

    public void setFee_type_0(String fee_type_0) {
        this.fee_type_0 = fee_type_0;
    }

    public String getOrder_fee_0() {
        return order_fee_0;
    }

    public void setOrder_fee_0(String order_fee_0) {
        this.order_fee_0 = order_fee_0;
    }

    public String getDuty_0() {
        return duty_0;
    }

    public void setDuty_0(String duty_0) {
        this.duty_0 = duty_0;
    }

    public String getTransport_fee_0() {
        return transport_fee_0;
    }

    public void setTransport_fee_0(String transport_fee_0) {
        this.transport_fee_0 = transport_fee_0;
    }

    public String getProduct_fee_0() {
        return product_fee_0;
    }

    public void setProduct_fee_0(String product_fee_0) {
        this.product_fee_0 = product_fee_0;
    }

    public String getState_0() {
        return state_0;
    }

    public void setState_0(String state_0) {
        this.state_0 = state_0;
    }

    public String getExplanation_0() {
        return explanation_0;
    }

    public void setExplanation_0(String explanation_0) {
        this.explanation_0 = explanation_0;
    }

    public String getModify_time_0() {
        return modify_time_0;
    }

    public void setModify_time_0(String modify_time_0) {
        this.modify_time_0 = modify_time_0;
    }
}
