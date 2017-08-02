package com.tp.dto.pay.postdata;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.tp.common.vo.PaymentConstant;
import com.tp.dto.pay.AppPayData;
import com.tp.model.pay.CustomsInfo;
import com.tp.model.pay.PaymentInfo;
import com.tp.util.AlipayUtil;

/**
 * 支付宝国际支付实体
 * 
 * @author zhouhui
 * @version $Id: AliPayInternationalPostData.java, v 0.1 2015年4月15日 下午2:13:29 zhouhui Exp $
 */
public class AliPayInternationalCustomsData implements Serializable, PayPostData, AppPayData {

    // serialVersionUID
    private static final long serialVersionUID = -5851584272079923007L;

    // 服务名称
    private String service = "alipay.acquire.customs";
    // 境外商户id
    private String partner;
    // 字符集
    private String inputCharset = "utf-8";
    // 签名机制
    private String sign;
    // 签名方式
    private String signType = "MD5";

    private String key = "";

    // 报关流水号
    private String out_request_no;
    // 支付宝交易号
    private String trade_no;
    // 商户海关备案编号
    private String merchant_customs_code;
    // 报关金额
    private String amount;
    // 海关编号
    private String customs_place;
    // 商户海关备案名称
    private String merchant_customs_name;

    private Map<String, String> sParam = new HashMap<String, String>();

    public Map<String, String> getsParam() {
        return sParam;
    }

    public void setsParam(Map<String, String> sParam) {
        this.sParam = sParam;
    }

    public AliPayInternationalCustomsData() {

    }

    public AliPayInternationalCustomsData(Properties paymentConfig, PaymentInfo dto) {
        this.partner = paymentConfig.getProperty("ALIPAY_MERGEALIPAY_PARTNER");
        this.key = paymentConfig.getProperty("ALIPAY_MERGEALIPAY_KEY");
        this.inputCharset = paymentConfig.getProperty("ALIPAY_INPUT_CHARSET");
        this.signType = paymentConfig.getProperty("ALIPAY_SIGN_TYPE");
        this.out_request_no = dto.getBizCode().toString();
        this.trade_no = dto.getGatewayTradeNo();
        this.amount = String.format("%.2f", dto.getAmount());
//        this.merchant_customs_name = PaymentConstant.CUMSTOM_PAYPLAT.getMerName(dto.getChannelId(),
//            dto.getGatewayCode());
//        this.merchant_customs_code = PaymentConstant.CUMSTOM_PAYPLAT.getMerCode(dto.getChannelId(),
//            dto.getGatewayCode());
//        this.customs_place = PaymentConstant.CUMSTOM_PAYPLAT.getCustomCode(dto.getChannelId(),
//            dto.getGatewayCode());
    }

    public AliPayInternationalCustomsData(Properties paymentConfig, PaymentInfo dto, CustomsInfo customsInfo) {
        this.partner = paymentConfig.getProperty("ALIPAY_MERGEALIPAY_PARTNER");
        this.key = paymentConfig.getProperty("ALIPAY_MERGEALIPAY_KEY");
        this.inputCharset = paymentConfig.getProperty("ALIPAY_INPUT_CHARSET");
        this.signType = paymentConfig.getProperty("ALIPAY_SIGN_TYPE");
        this.out_request_no = dto.getBizCode().toString();
        this.trade_no = dto.getGatewayTradeNo();
        this.amount = String.format("%.2f", dto.getAmount());
        this.merchant_customs_name = customsInfo.getMerName();
        this.merchant_customs_code = customsInfo.getMerCode();
        this.customs_place = customsInfo.getCustomsCode();
    }
    
    @Override
    public String getSignature() {
        sParam.put("service", getService());
        sParam.put("partner", getPartner());
        sParam.put("_input_charset", getInputCharset());
        sParam.put("sign_type", getSignType());
        sParam.put("out_request_no", getOut_request_no());
        sParam.put("trade_no", getTrade_no());
        sParam.put("merchant_customs_code", getMerchant_customs_code());
        sParam.put("amount", getAmount());
        sParam.put("customs_place", getCustoms_place());
        sParam.put("merchant_customs_name", getMerchant_customs_name());
        return AlipayUtil.buildRequestMysign(sParam, getKey());
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getInputCharset() {
        return inputCharset;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOut_request_no() {
        return out_request_no;
    }

    public void setOut_request_no(String out_request_no) {
        this.out_request_no = out_request_no;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getMerchant_customs_code() {
        return merchant_customs_code;
    }

    public void setMerchant_customs_code(String merchant_customs_code) {
        this.merchant_customs_code = merchant_customs_code;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCustoms_place() {
        return customs_place;
    }

    public void setCustoms_place(String customs_place) {
        this.customs_place = customs_place;
    }

    public String getMerchant_customs_name() {
        return merchant_customs_name;
    }

    public void setMerchant_customs_name(String merchant_customs_name) {
        this.merchant_customs_name = merchant_customs_name;
    }

    @Override
    public String getPaymentTradeNo() {
        // TODO Auto-generated method stub
        return null;
    }
}
