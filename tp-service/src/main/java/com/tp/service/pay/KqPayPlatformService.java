package com.tp.service.pay;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.tp.common.vo.PaymentConstant;
import com.tp.dto.pay.AppPayData;
import com.tp.dto.pay.cbdata.PayCallbackData;
import com.tp.dto.pay.cbdata.RefundCallbackData;
import com.tp.dto.pay.postdata.KqPayPostData;
import com.tp.dto.pay.postdata.PayPostData;
import com.tp.exception.ServiceException;
import com.tp.model.pay.CustomsInfo;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.pay.RefundPayinfo;
import com.tp.result.pay.RefundResult;
import com.tp.result.pay.TradeStatusResult;
import com.tp.service.pay.ICustomsInfoService;
import com.tp.service.pay.IPaymentInfoService;
import com.tp.service.pay.cbdata.KqPayCallbackData;
import com.tp.service.pay.cbdata.KqRefundResult;
import com.tp.service.pay.kqpay.GatewayOrderDetail;
import com.tp.service.pay.kqpay.GatewayOrderQueryRequest;
import com.tp.service.pay.kqpay.GatewayOrderQueryResponse;
import com.tp.service.pay.kqpay.GatewayOrderQueryServiceLocator;
import com.tp.service.pay.kqpay.GatewayRefundQuery;
import com.tp.service.pay.kqpay.GatewayRefundQueryRequest;
import com.tp.service.pay.kqpay.GatewayRefundQueryResponse;
import com.tp.service.pay.kqpay.GatewayRefundQueryResultDto;
import com.tp.service.pay.kqpay.GatewayRefundQueryServiceLocator;
import com.tp.service.pay.util.KqPaySecurityUtil;
import com.tp.util.DateUtil;
import com.tp.util.MD5;


@Service("kqPayPlatformService")
public class KqPayPlatformService extends AbstractPayPlatformService {
	protected Logger log = LoggerFactory.getLogger(AbstractPayPlatformService.class);
	//@Autowired
	Properties settings;
	
	@Autowired
	IPaymentInfoService  paymentInfoService;
	
	@Autowired
	ICustomsInfoService customsInfoService;
	
	
	@PostConstruct
	public void init() {
		super.init();
	}
	@PreDestroy
	public void destroy(){
		super.destroy();
	}
	
	@Override
	protected AppPayData constructAppPostData(PaymentInfo paymentInfo, boolean forSdk) throws ServiceException {
		//手机端：快钱没有SDK，所以也是通过https请求的方式
		return constructPostData(paymentInfo, true);
	}
	
	@Override
	protected PayPostData constructPostData(PaymentInfo paymentInfo) throws ServiceException {
		return constructPostData(paymentInfo, false);
	}
	
	private KqPayPostData constructPostData(PaymentInfo paymentInfo, boolean forApp) throws ServiceException {
		KqPayPostData kqPostData = new KqPayPostData();
		
		//人民币网关账号，该账号为11位人民币网关商户编号+01,该参数必填。
		String merchantAcctId;
		if (forApp) {
			merchantAcctId = settings.getProperty("kqPay.merchantAcctId");
		} else {
			merchantAcctId = settings.getProperty("kqPay.merchantAcctId");
		}
		//编码方式，1代表 UTF-8; 2 代表 GBK; 3代表 GB2312 默认为1,该参数必填。
		String inputCharset = "1";
		//接收支付结果的页面地址，该参数一般置为空即可。
		String pageUrl;
		if (forApp) {
			pageUrl = "";//settings.getProperty("kqPay.redirectUrl")+paymentInfo.getPaymentId();
		} else {
			pageUrl = settings.getProperty("kqPay.redirectUrl")+paymentInfo.getPaymentId();
		}
		//服务器接收支付结果的后台地址，该参数务必填写，不能为空。
//		String bgUrl = "http://222.72.249.242:9080/RMBPORT/receive.jsp";
		String bgUrl = settings.getProperty("kqPay.notifyUrl");
		//扩展字段1，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
		String ext1 = "";
		if("4".equals(paymentInfo.getOrderType()) || "5".equals(paymentInfo.getOrderType())){
			bgUrl = settings.getProperty("kqPay.seaOrderNotifyUrl");
			
			Map<String, Object> params = new HashMap<>();
			params.put("channelsId", paymentInfo.getChannelId());
			List<CustomsInfo> customsInfoObjs = customsInfoService.queryByParamNotEmpty(params);
			if(!CollectionUtils.isEmpty(customsInfoObjs)){
				ext1 = customsInfoObjs.get(0).getCustomsCode() + "," + paymentInfo.getRealName() + "," + paymentInfo.getIdentityType() + "," + paymentInfo.getIdentityCode() + ";" +
						paymentInfo.getBizCode() + "," + paymentInfo.getAmount() + "," + paymentInfo.getTaxFee() + "," + paymentInfo.getFreight();
			}
			else{
				log.error("ChannelId:{} 找不到对应的海关信息", paymentInfo.getChannelId());
				throw new ServiceException("ChannelId:" + paymentInfo.getChannelId() + "找不到对应的海关信息");
			}
		}
		

		//网关版本，固定值：v2.0,该参数必填。
		String version =  forApp? "mobile1.0" : "v2.0";
		String mobileGateway=forApp? "phone":null;
		//语言种类，1代表中文显示，2代表英文显示。默认为1,该参数必填。
		String language =  "1";
		//签名类型,该值为4，代表PKI加密方式,该参数必填。
		String signType =  "4";
		//支付人姓名,可以为空。
		String payerName= ""; 
//		//支付人联系类型，1 代表电子邮件方式；2 代表手机联系方式。可以为空。
//		String payerContactType =  "";//"1";
//		//支付人联系方式，与payerContactType设置对应，payerContactType为1，则填写邮箱地址；payerContactType为2，则填写手机号码。可以为空。
//		String payerContact =  "";//"2532987@qq.com";
		String createUserId = paymentInfo.getCreateUser();
		//指定付款人 1 代表通过商户方 ID 挃定付款人
		String payerIdType=null;
		//付款人标识
		String payerId=null;
		if (createUserId!=null) {
			//指定付款人 1 代表通过商户方 ID 挃定付款人
			payerIdType="1";
			//付款人标识
			payerId=MD5.sign(createUserId, "DonotMake99BillKnown", "UTF-8");
		}

		//商户订单号，以下采用时间来定义订单号，商户可以根据自己订单号的定义规则来定义该值，不能为空。
		String orderId = String.valueOf(paymentInfo.getBizCode());
		//订单金额，金额以“分”为单位，商户测试以1分测试即可，切勿以大金额测试。该参数必填。
		String orderAmount = String.valueOf(Math.round(paymentInfo.getAmount()*100));;
		//订单提交时间，格式：yyyyMMddHHmmss，如：20071117020101，不能为空。
		String orderTime = DateUtil.format(paymentInfo.getCreateTime(), "yyyyMMddHHmmss");
		//商品名称，可以为空。
		String productName= settings.getProperty("kqPay.subject"); 
		//商品数量，可以为空。
		String productNum = "";
		//商品代码，可以为空。
		String productId = "";
		//商品描述，可以为空。
		String productDesc = productName;
		
		//扩展自段2，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
		String ext2 = "";
		//支付方式，一般为00，代表所有的支付方式。如果是银行直连商户，该值为10，必填。
		String payType = "00";
		//银行代码，如果payType为00，该值可以为空；如果payType为10，该值必须填写，具体请参考银行列表。
		String bankId = "";
		//同一订单禁止重复提交标志，实物购物车填1，虚拟产品用0。1代表只能提交一次，0代表在支付不成功情况下可以再提交。可为空。
		String redoFlag = "";
		//快钱合作伙伴的帐户号，即商户编号，可为空。
		String pid = "";
		
		String actionUrl;
		if (forApp) {
			actionUrl = settings.getProperty("kqPay.appActionUrl");
		} else {
			actionUrl = settings.getProperty("kqPay.actionUrl");
		}
		kqPostData.setActionUrl(actionUrl);
		kqPostData.setInputCharset(inputCharset);
		kqPostData.setPageUrl(pageUrl);
		kqPostData.setBgUrl(bgUrl);
		kqPostData.setVersion(version);
		kqPostData.setMobileGateway(mobileGateway);
		kqPostData.setLanguage(language);
		kqPostData.setSignType(signType);
		kqPostData.setMerchantAcctId(merchantAcctId);
		kqPostData.setPayerName(payerName);
		kqPostData.setPayerIdType(payerIdType);
		kqPostData.setPayerId(payerId);
		kqPostData.setOrderId(orderId);
		kqPostData.setOrderAmount(orderAmount);
		kqPostData.setOrderTime(orderTime);
		kqPostData.setProductName(productName);
		kqPostData.setProductNum(productNum);
		kqPostData.setProductId(productId);
		kqPostData.setProductDesc(productDesc);
		kqPostData.setExt1(ext1);
		kqPostData.setExt2(ext2);
		kqPostData.setPayType(payType);
		kqPostData.setBankId(bankId);
		kqPostData.setRedoFlag(redoFlag);
		kqPostData.setPid(pid);
		
		String password = settings.getProperty("kqPay.rsaPassword");
		String signMsg = KqPaySecurityUtil.signMsg(kqPostData.getMsg2Sign(), password);
		kqPostData.setSignMsg(signMsg);

		return kqPostData;
	}
	
	@Override
	protected String getReturnMsg(PaymentInfo paymentInfoObj, PayCallbackData callbackData) {
		
		if (paymentInfoObj!=null) {
			return "<result>1</result>\n<redirecturl>" + settings.getProperty("kqPay.urlSwitch") + "</redirecturl>";
		}
		
		return null;
	}
	
	@Override
	protected boolean verifyResponse(Map<String, String> parameterMap) {
		KqPayCallbackData callbackData = new KqPayCallbackData(parameterMap);
		boolean validResp = KqPaySecurityUtil.verifyMsg(callbackData.getMsg2Sign(), parameterMap.get("signMsg"));
		
		if (validResp) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected PayCallbackData getPayCallbackData(Map<String, String> parameterMap) {
		
		return new KqPayCallbackData(parameterMap);
	}

	
	@Override
	protected RefundResult doRefund(RefundPayinfo refundPayinfoObj) {
		String refundUrl = settings.getProperty("kqPay.refundUrl");
		//商户编号，线上的话改成你们自己的商户编号的，发到商户的注册快钱账户邮箱的
		String merchant_id = settings.getProperty("kqPay.refundMerchantAcctId");
		//退款接口版本号 目前固定为此值
		String version = "bill_drawback_api_1";
		//操作类型
		String command_type = "001";
		//原商户订单号
		PaymentInfo paymentInfoObj = refundPayinfoObj.getPaymentInfo();
		if (paymentInfoObj==null) {
			paymentInfoObj = paymentInfoDao.queryById(refundPayinfoObj.getPaymentId());
		}
		String orderid = String.valueOf(paymentInfoObj.getBizCode());
		//退款金额，整数或小数，小数位为2位   以人民币元为单位
		String amount = String.format("%.2f", refundPayinfoObj.getAmount());
		//退款提交时间 数字串，一共14位 格式为：年[4 位]月[2 位]日[2 位]时[2 位]分[2 位]秒[2位]
		String postdate = DateUtil.format(new Date(), "yyyyMMddHHmmss");
		//退款流水号  字符串
		final String txOrder = String.valueOf(refundPayinfoObj.getBizCode());
		//加密所需的key值，线上的话发到商户快钱账户邮箱里
		String merchant_key= settings.getProperty("kqPay.refundKey");
		//生成加密签名串
		StringBuilder signParams = new StringBuilder();
		signParams.append("merchant_id").append('=').append(merchant_id);
		if (version != null && !version.equals("")) {
//			signParams.append('&');
			signParams.append("version").append('=').append(version);
		}
		if (command_type != null && !command_type.equals("")) {
//			signParams.append('&');
			signParams.append("command_type").append('=').append(command_type);
		}
		if (orderid != null && !orderid.equals("")) {
//			signParams.append('&');
			signParams.append("orderid").append('=').append(orderid);
		}
		if (amount != null && !amount.equals("")) {
//			signParams.append('&');
			signParams.append("amount").append('=').append(amount);
		}
		if (postdate != null && !postdate.equals("")) {
//			signParams.append('&');
			signParams.append("postdate").append('=').append(postdate);
		}
		if (txOrder != null && !txOrder.equals("")) {
//			signParams.append('&');
			signParams.append("txOrder").append('=').append(txOrder);
		}
		if (merchant_key != null && !merchant_key.equals("")) {
//			signParams.append('&');
			signParams.append("merchant_key").append('=').append(merchant_key);
		}
//		queryFormData.put("merchant_key", merchant_key);
		
		String signMsg = null;
		try {
			signMsg = DigestUtils.md5Hex(signParams.toString().getBytes("UTF-8")).toUpperCase();
		} catch (Exception e) {
			log.error("", e);;
		}
		Map<String, String> queryFormData = new HashMap<String, String>();
		queryFormData.put("merchant_id", merchant_id);
		queryFormData.put("version", version);
		queryFormData.put("command_type", command_type);
		queryFormData.put("txOrder", txOrder);
		queryFormData.put("amount", amount);
		queryFormData.put("postdate", postdate);
		queryFormData.put("orderid", orderid);
		queryFormData.put("mac", signMsg);
		
		log.info("refund-{} params:{}", refundPayinfoObj.getPayRefundId(), queryFormData);
		Map<String, String> refundResult = submitPostUrl(queryFormData, refundUrl);
		log.info("refund-{} result:{}", refundPayinfoObj.getPayRefundId(), refundResult);
		refundResult.put("txOrder", txOrder);
		return new KqRefundResult(refundResult);
	}
	
	@Override
	protected RefundCallbackData getRefundCallbackData(Map<String, String> parameterMap) {
		//快钱没有/不需要退钱的通知接口
		return null;
	}

	
	@Override
	public TradeStatusResult queryPayStatus(PaymentInfo paymentInfoObj) {
		String queryUrl = settings.getProperty("kqPay.queryUrl");
//		System.out.println(requestBackUrl);
		Map<String, String> queryFormData = new HashMap<String, String>();
		//人民币账号
		//本参数用来指定接收款项的快钱用户的人民币账号 
		String merchantAcctId = settings.getProperty("kqPay.merchantAcctId");
		//客户编号所对应的密钥。。在账户邮箱中获取
		String key =  settings.getProperty("kqPay.queryKey");
		//字符集  固定值：1 1代表UTF-8 
		String inputCharset ="1";
		//查询接口版本   固定值：v2.0注意为小写字母
		String version = "v2.0";
		//签名类型   固定值：1  1代表MD5加密签名方式
		String signType ="1";
		//查询方式   固定选择值：0、1  
		//0按商户订单号单笔查询（返回该订单信息）
		//1按交易结束时间批量查询（只返回成功订单）
		String queryType ="0";
		//查询模式   固定值：1  1代表简单查询（返回基本订单信息）
		String queryMode ="1";
		//交易开始时间  数字串，一共14位
		//格式为：年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]，例如：20071117020101
		String startTime ="";//"20120319150000" ;
		//交易结束时间  数字串，一共14位
		//格式为：年[4位]月[2位]日[2位]时[2位]分[2位]秒[2位]，例如：20071117020101
		String endTime ="";//"20120320142624";
		//请求记录集页码  在查询结果数据总量很大时，快钱会将支付结果分多次返回。本参数表示商户需要得到的记录集页码。
		//默认为1，表示第1页。
		String requestPage ="";
		//商户订单号  只允许使用字母、数字、- 、_,并以字母或数字开头,需要查询的某比订单
		String orderId =  String.valueOf(paymentInfoObj.getBizCode());
		//组合字符串。。必须按照此顺序组串
		
		StringBuilder signParams = new StringBuilder();
		signParams.append("inputCharset").append('=').append(inputCharset);
		if (version != null && !version.equals("")) {
			signParams.append('&').append("version").append('=').append(version);
		}
		if (signType != null && !signType.equals("")) {
			signParams.append('&').append("signType").append('=').append(signType);
		}
		if (merchantAcctId != null && !merchantAcctId.equals("")) {
			signParams.append('&').append("merchantAcctId").append('=').append(merchantAcctId);
		}
		if (queryType != null && !queryType.equals("")) {
			signParams.append('&').append("queryType").append('=').append(queryType);
		}
		if (queryMode != null && !queryMode.equals("")) {
			signParams.append('&').append("queryMode").append('=').append(queryMode);
		}
		if (startTime != null && !startTime.equals("")) {
			signParams.append('&').append("startTime").append('=').append(startTime);
		}
		if (endTime != null && !endTime.equals("")) {
			signParams.append('&').append("endTime").append('=').append(endTime);
		}
		if (requestPage != null && !requestPage.equals("")) {
			signParams.append('&').append("requestPage").append('=').append(requestPage);
		}
		if (orderId != null && !orderId.equals("")) {
			signParams.append('&').append("orderId").append('=').append(orderId);
		}
		if (key != null && !key.equals("")) {
			signParams.append('&').append("key").append('=').append(key);
		}
		
		String signMsg = null;
		try {
			signMsg = DigestUtils.md5Hex(signParams.toString().getBytes("UTF-8")).toUpperCase();
			queryFormData.put("signMsg",signMsg);
		} catch (Exception e) {
			log.error("", e);;
		}
		
		TradeStatusResult result = new TradeStatusResult();
		result.setTradeNo(orderId);
		result.setStatus(paymentInfoObj.getStatus());
		result.setSuccess(false);
		result.setCanceled(Integer.valueOf(1).equals(paymentInfoObj.getCanceled()));
		GatewayOrderQueryRequest queryRequest = new GatewayOrderQueryRequest();
		queryRequest.setInputCharset(inputCharset);
		queryRequest.setVersion(version);
		queryRequest.setSignType(Integer.parseInt(signType));
		queryRequest.setMerchantAcctId(merchantAcctId);
		queryRequest.setQueryType(Integer.parseInt(queryType));
		queryRequest.setQueryMode(Integer.parseInt(queryMode));
		queryRequest.setOrderId(orderId);
		queryRequest.setStartTime(startTime);
		queryRequest.setEndTime(endTime);
		queryRequest.setRequestPage(requestPage);
		queryRequest.setSignMsg(signMsg);
		
		GatewayOrderQueryServiceLocator locator = new GatewayOrderQueryServiceLocator();
		try {
			GatewayOrderQueryResponse queryResponse = locator.getgatewayOrderQuery(new URL(queryUrl)).gatewayOrderQuery(queryRequest);
			String errCode = queryResponse.getErrCode();
			log.info("payment-{} query response code:{}", paymentInfoObj.getPaymentId(), errCode);
			GatewayOrderDetail[] orders = queryResponse.getOrders();
			if (orders!=null&&orders.length>0) {
				GatewayOrderDetail orderDetail= orders[0];
				String payResult = orderDetail.getPayResult();
				if ("10".equals(payResult)) {
					result.setSuccess(true);
					result.setStatus(PaymentConstant.PAYMENT_STATUS.PAYED.code);
					paymentInfoObj.setStatus(PaymentConstant.PAYMENT_STATUS.PAYED.code);
				}
				
				log.info("payment info: {}", orderDetail);
			}
		} catch (Exception e) {
			log.error("", e);
		}
		
		return result;
	}

	@Override
	public TradeStatusResult queryRefundStatus(RefundPayinfo refundPayinfoObj) {
		//版本号：现在固定版本为v2.0
		String version = "v2.0";
		//签名类型：固定为1，代表MD5
		String signType = "1";
		//人民币账号：
		String acctId = settings.getProperty("kqPay.merchantAcctId");
		//退款申请开始时间
		String startDate = DateUtil.format(refundPayinfoObj.getCreateTime(), "yyyyMMdd");
		//退款申请结束时间
		String endDate = startDate;
		//退款完成开始时间
		String lastupdateStartDate = "";
		//退款完成结束时间
		String lastupdateEndDate = "";
		//客户方的批次号
		String customerBatchId = "";	
		//退款订单号
		String orderId = String.valueOf(refundPayinfoObj.getBizCode());
		//请求记录集页码, 取1即可
		String requestPage = "1";
		//原商家订单号,
		String rOrderId = "";
		//退款交易号
		String seqId = "";	
		if (StringUtils.isBlank(orderId)) {
			PaymentInfo paymentInfo = refundPayinfoObj.getPaymentInfo();
			if (paymentInfo==null) {
				paymentInfo=paymentInfoDao.queryById(refundPayinfoObj.getPaymentId());
			}
			rOrderId=String.valueOf(paymentInfo.getBizCode());
		}
		//交易状态：0进行中|1成功|2失败
		String status = "";	
		String key = settings.getProperty("kqPay.queryKey");
		StringBuilder params2Sign =new StringBuilder();
		params2Sign.append("version=").append(version);
		if(signType != ""){
			params2Sign.append("&signType=").append(signType);
		}
		if(acctId != ""){
			params2Sign.append("&merchantAcctId=").append(acctId);
		}			
		if(startDate != ""){
			params2Sign.append("&startDate=").append(startDate);
		}			
		if(endDate != ""){
			params2Sign.append("&endDate=").append(endDate);
		}			
		//if(lastupdateStartDate != ""){
			//params2Sign.append("&lastupdateStartDate=").append(lastupdateStartDate);
		//}			
		//if(lastupdateEndDate != ""){
			//params2Sign.append("&lastupdateEndDate=").append(lastupdateEndDate);
		//}	
		if(customerBatchId != ""){
			params2Sign.append("&customerBatchId=").append(customerBatchId);
		}	
		if(orderId != ""){
			params2Sign.append("&orderId=").append(orderId);
		}	
		if(requestPage != ""){
			params2Sign.append("&requestPage=").append(requestPage);
		}
		if(rOrderId != ""){
			params2Sign.append("&rOrderId=").append(rOrderId);
		}
		if(seqId != ""){
			params2Sign.append("&seqId=").append(seqId);
		}	
		if(status!= ""){
			params2Sign.append("&status=").append(status);
		}													
		if(key != ""){
			params2Sign.append("&key=").append(key);
		}
		String signMsg = null;
		try {
			signMsg = DigestUtils.md5Hex(params2Sign.toString().getBytes("UTF-8")).toUpperCase();
		} catch (Exception e) {
			log.error("", e);;
		}

		TradeStatusResult tradeStatusResult = new TradeStatusResult();
		Integer s = refundPayinfoObj.getStatus();
		if (s==null) {
			tradeStatusResult.setStatus(PaymentConstant.REFUND_STATUS.NOT_REFUNDING.code);
		} else {
			tradeStatusResult.setStatus(s);
		}
		try {
			String refundQueryUrl = settings.getProperty("kqPay.refundQueryUrl");
			GatewayRefundQueryServiceLocator gl = new GatewayRefundQueryServiceLocator();
			GatewayRefundQuery  grQuery = gl.getgatewayRefundQuery(new URL(refundQueryUrl));
			GatewayRefundQueryRequest dto = new GatewayRefundQueryRequest();
			dto.setVersion(version);
			dto.setMerchantAcctId(acctId);
			dto.setSignType(signType);
			dto.setRequestPage(requestPage);
			dto.setStartDate(startDate);
			dto.setEndDate(endDate);
			dto.setLastupdateStartDate(lastupdateStartDate);
			dto.setLastupdateEndDate(lastupdateEndDate);
			dto.setROrderId(rOrderId);
			dto.setCustomerBatchId(customerBatchId);
			dto.setOrderId(orderId);
			dto.setSeqId(seqId);
			dto.setStatus(status);
			dto.setSignMsg(signMsg);
			GatewayRefundQueryResponse result = grQuery.query(dto);
			
			log.error(""+result.getErrCode());
			GatewayRefundQueryResultDto[] results = result.getResults();
			if (results!=null&&results.length>0) {
				GatewayRefundQueryResultDto refundResult = results[0];
				log.info(refundResult.toString());
				
				tradeStatusResult.setTradeNo(refundResult.getOrderId());
				tradeStatusResult.setErrorMsg(refundResult.getFailReason());
				if ("1".equals(refundResult.getStatus())) {
					tradeStatusResult.setStatus(PaymentConstant.REFUND_STATUS.REFUNDED.code);
				}
				tradeStatusResult.setSuccess(PaymentConstant.REFUND_STATUS.REFUNDED.code.equals(tradeStatusResult.getStatus()));
			}
			log.error("result={}", Arrays.toString(results));
		} catch (Exception e) {
			log.error("", e);
		}
		
		return tradeStatusResult;
	}

	
	/**
	 * 数据提交 提交到后台
	 * 
	 * @param submitFromData
	 * @param requestUrl 
	 * @return
	 */
	private Map<String, String> submitPostUrl(Map<String, String> submitFromData,String requestUrl){
		log.info("submit request to url:{}\nparams:{}", requestUrl, submitFromData);
		Map<String, String> result = new HashMap<String, String>();
		try {
            List <NameValuePair> params = new ArrayList<NameValuePair>();  
            Set<Entry<String, String>> entrySet = submitFromData.entrySet();
			for (Entry<String, String> entry : entrySet) {
				String value = entry.getValue();
				if (StringUtils.isBlank(value)) {
					continue;
				}
				String key = entry.getKey();
				params.add(new BasicNameValuePair(key, value));  
			}
              
			HttpPost httpPost = new HttpPost(requestUrl);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));  
			CloseableHttpResponse response = httpClient.execute(httpPost);
			
			try {
				HttpEntity entity = response.getEntity();
				
				StatusLine statusLine = response.getStatusLine();
				if (entity != null) {
					long contentLength = entity.getContentLength();
					log.debug("Response content length: {}",  contentLength);
					InputStream inputStream = entity.getContent();
					
					ByteArrayOutputStream outstream = new ByteArrayOutputStream(
							contentLength > 0 ? (int) contentLength : 4096);
					byte[] buffer = new byte[4096];
					int len;
					while ((len = inputStream.read(buffer)) > 0) {
						outstream.write(buffer, 0, len);
					}
					outstream.close();
					
					String responseBody = outstream.toString("UTF-8");
					Map<String, String> map = convertXml2Map(responseBody);
					if (map!=null) {
						result.putAll(map);
					}
					
					log.info("status={}, response:{}", statusLine.getStatusCode(), responseBody);
				} else {
					log.error("no http entity, status={}", statusLine.getStatusCode());
				}
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}
		} catch(Exception e) {
			log.error("", e);
		}
		return result;
	}
}
