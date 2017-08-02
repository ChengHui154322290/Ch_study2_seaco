package com.tp.dto.pay.appdata;


import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.tp.dto.pay.AppPayData;
import com.tp.dto.pay.postdata.PayPostData;
import com.tp.model.pay.PaymentInfo;

public class AliInternationalMergeAppSdkPayData implements PayPostData, AppPayData, Serializable {
    private static final long serialVersionUID = -1042765309810230140L;
    private Logger log = LoggerFactory.getLogger(AliInternationalMergeAppSdkPayData.class);
    private String actionUrl;

    private String service;
    private String partner;
    private String inputCharset;
    private String signType;
    private String notifyUrl;

    private String outTradeNo;
    private String subject;
    private String paymentType;
    private String sellerId;
    private String rmbFee;
    private String forexBiz;
    private String currency;
    private String body;

    private String privateKey;
    // 分账信息
    private String split_fund_info = "";

    private String productCode;

    public AliInternationalMergeAppSdkPayData(Properties paymentConfig, PaymentInfo dto) {
        this.service = "mobile.securitypay.pay";
        this.partner = paymentConfig.getProperty("ALIPAY_MERGEALIPAY_PARTNER");
        this.inputCharset = paymentConfig.getProperty("ALIPAY_INPUT_CHARSET");
        this.signType = "RSA";
        this.notifyUrl = paymentConfig.getProperty("ALIPAY_MERGEALIPAY_NOTIFY_URL");

        this.outTradeNo = dto.getPaymentTradeNo();
        this.subject = paymentConfig.getProperty("ALIPAY_SUBJECT");
        this.paymentType = "1";
        this.sellerId = paymentConfig.getProperty("ALIPAY_MERGEALIPAY_PARTNER");
        this.rmbFee = String.format("%.2f", dto.getAmount());
        this.forexBiz = "FP";
        this.currency = "USD";
        this.body = "西客妈妈订单";
        this.privateKey = paymentConfig.getProperty("ALIPAY_MERGEALIPAY_RSA_PRIVATE_KEY");
        this.productCode = "NEW_WAP_OVERSEAS_SELLER";
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getForexBiz() {
        return forexBiz;
    }

    public void setForexBiz(String forexBiz) {
        this.forexBiz = forexBiz;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getInputCharset() {
        return inputCharset;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRmbFee() {
        return rmbFee;
    }

    public void setRmbFee(String rmbFee) {
        this.rmbFee = rmbFee;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSplit_fund_info() {
        return split_fund_info;
    }

    public void setSplit_fund_info(String split_fund_info) {
        this.split_fund_info = split_fund_info;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getInfoData() {
        StringBuilder orderInfo = new StringBuilder();

        orderInfo.append("partner=\"").append(partner).append("\"");

        // 签约卖家支付宝账号
        orderInfo.append("&seller_id=\"").append(sellerId).append("\"");

        // 商户网站唯一订单号
        orderInfo.append("&out_trade_no=\"").append(getOutTradeNo()).append("\"");

        // 商品名称
        orderInfo.append("&subject=\"").append(subject).append("\"");

        // 商品详情
        orderInfo.append("&body=\"").append(body).append("\"");

        // 商品金额
        orderInfo.append("&rmb_fee=\"").append(rmbFee).append("\"");

        orderInfo.append("&forex_biz=\"").append(forexBiz).append("\"");

        orderInfo.append("&currency=\"").append(currency).append("\"");

        // 服务器异步通知页面路径
        orderInfo.append("&notify_url=\"").append(notifyUrl).append("\"");

        // 服务接口名称， 固定值
        orderInfo.append("&service=\"").append(service).append("\"");

        // 支付类型， 固定值
        orderInfo.append("&payment_type=\"").append(paymentType).append("\"");

        // 参数编码， 固定值
        orderInfo.append("&_input_charset=\"").append(inputCharset).append("\"");

        orderInfo.append("&split_fund_info=\"").append(split_fund_info).append("\"");

        orderInfo.append("&product_code=\"").append(productCode).append("\"");

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo.append("&it_b_pay=\"30m\"");

        return orderInfo.toString();
    }

    @Override
    public String getSignature() {
        String prestr = getInfoData();
        try {
            return URLEncoder.encode(com.tp.util.RSA.sign(prestr, privateKey, this.getInputCharset()), "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("", e);
        }
        return null;
    }

	/* (non-Javadoc)
	 * @see com.tp.dto.pay.postdata.PayPostData#getPaymentTradeNo()
	 */
	@Override
	public String getPaymentTradeNo() {
		return outTradeNo;
	}
}
