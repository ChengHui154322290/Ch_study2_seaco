package com.tp.service.pay;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.NoHttpResponseException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.tp.common.vo.MqMessageConstant;
import com.tp.common.vo.PaymentConstant;
import com.tp.common.vo.OrderConstant.OrderType;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.common.vo.ord.OrderCodeType;
import com.tp.dao.pay.PaymentInfoDao;
import com.tp.dao.pay.PaymentLogDao;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.pay.AppPayData;
import com.tp.dto.pay.cbdata.CallbackResultDto;
import com.tp.dto.pay.cbdata.PayCallbackData;
import com.tp.dto.pay.cbdata.RefundCallbackData;
import com.tp.dto.pay.postdata.AliPayRefundPostData;
import com.tp.dto.pay.postdata.PayPostData;
import com.tp.exception.ServiceException;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.SubOrder;
import com.tp.model.pay.CustomsInfo;
import com.tp.model.pay.PaymentGateway;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.pay.PaymentLog;
import com.tp.model.pay.RefundPayinfo;
import com.tp.mq.RabbitMqProducer;
import com.tp.mq.exception.MqClientException;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.result.pay.RefundResult;
import com.tp.service.ord.IOrderCodeGeneratorService;
import com.tp.service.ord.IOrderRedeemItemService;
import com.tp.service.pay.IPayPlatformService;
import com.tp.service.pay.IPaymentGatewayService;
import com.tp.service.pay.IRefundPayinfoService;
import com.tp.util.BeanUtil;
import com.tp.util.DateUtil;
import com.tp.util.StringUtil;


public abstract class AbstractPayPlatformService implements IPayPlatformService {
	private Logger log = LoggerFactory.getLogger(AbstractPayPlatformService.class);

	@Autowired
	Properties settings;

	@Autowired
	PaymentLogDao paymentLogDao;

	@Autowired
	RabbitMqProducer rabbitMqProducer;

	@Autowired
	PaymentInfoDao paymentInfoDao;
	
	@Autowired
	IRefundPayinfoService refundPayinfoService;
	@Autowired
	IPaymentGatewayService paymentGatewayService;

	CloseableHttpClient httpClient = null;
	
	HttpRequestRetryHandler httpRequestRetryHandler = new PayHttpRequestRetryHandler();
	
	@Autowired
	JedisCacheUtil jedisCacheUtil;
	
	@Autowired
	IOrderRedeemItemService  orderRedeemItemService;

	@Autowired
	private IOrderCodeGeneratorService orderCodeGeneratorService;
	
	@PostConstruct
	public void init() {
		try {
			httpClient = HttpClients.custom().setRetryHandler(httpRequestRetryHandler).build();
		} catch (Exception e) {
			log.error("初始化证书失败", e);
		}
	}
	@PreDestroy
	public void destroy() {
		try {
			if (httpClient != null) {
				httpClient.close();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 判断是否是合法的调用
	 * 
	 * @param parameterMap
	 * @return
	 */
	abstract protected boolean verifyResponse(Map<String, String> parameterMap);

	abstract protected PayCallbackData getPayCallbackData(Map<String, String> parameterMap);

	abstract protected RefundCallbackData getRefundCallbackData(Map<String, String> parameterMap);

	/**
	 * 构建PC端支付请求参数对象
	 * 
	 * @param paymentInfo 支付信息
	 * @return
	 * @throws ServiceException 
	 */
	abstract protected PayPostData constructPostData(PaymentInfo paymentInfo) throws ServiceException;
	/**
	 * 构建支付请求参数对象
	 * 
	 * @param paymentInfo 支付信息
	 * @param forSdk 是否是App SDK支付
	 * @param plat 平台(mobile or btmobile)
	 * @return
	 * @throws ServiceException 
	 */
	abstract protected AppPayData constructAppPostData(PaymentInfo paymentInfo, boolean forSdk) throws ServiceException;
	/**
	 * 构建支付请求参数对象
	 * 
	 * @param paymentInfo 支付信息
	 * @param forSdk 是否是App SDK支付
	 * @param params 请求参数集
	 * @return
	 */
	protected AppPayData constructAppPostDataByParams(PaymentInfo paymentInfo, boolean forSdk, Map<String, Object> params){return null;};

	@Override
	public ResultInfo<Boolean> putPaymentInfoToCustoms(PaymentInfo paymentInfo, SubOrder subOrder, OrderConsignee consignee){
		return null;
	}
	@Override
	public ResultInfo<Boolean> putPaymentInfoToCustoms(PaymentInfo paymentInfo, SubOrder subOrder, OrderConsignee consignee, CustomsInfo customsInfo){
		return null;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public PayPostData getPostData(Long paymentInfoId) throws ServiceException {
		
		PaymentInfo paymentInfoObj = paymentInfoDao.queryById(paymentInfoId);

		PayPostData postData = constructPostData(paymentInfoObj);
		
		if (postData != null) {
			paymentInfoObj.setStatus(PaymentConstant.PAYMENT_STATUS.PAYING.code);

			updatePaymentInfo(paymentInfoObj, "PC网站：",PaymentConstant.PAY_ACTION_NAME.UPDATE, true, 1);
			
			log.info("payment-{} post info:{}", paymentInfoId, postData.getPaymentTradeNo());
		} else {
			log.info("post info is null, paymentInfoId={}", paymentInfoId);
		}
		return postData;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public AppPayData getAppPayData(Long paymentInfoId, boolean forSDK, String userId) throws ServiceException{
		
		PaymentInfo paymentInfoObj = paymentInfoDao.queryById(paymentInfoId);

		if(userId != null && !(paymentInfoObj.getCreateUser().equals(userId))){
			log.error("根据paymentId[{}] userId[{}]没有找到对应的支付信息", paymentInfoId, userId);
			throw new ServiceException("根据paymentId[" + paymentInfoId + "] userId[" + userId + "]没有找到对应的支付信息");
		}

		paymentInfoObj = processDSSPaymentInfo(paymentInfoObj);
		
		AppPayData postData = constructAppPostData(paymentInfoObj, forSDK);
		
		if (postData != null) {
			paymentInfoObj.setStatus(PaymentConstant.PAYMENT_STATUS.PAYING.code);
			if(paymentInfoObj.getAmount() <= 0) {
				paymentInfoObj.setStatus(PaymentConstant.PAYMENT_STATUS.PAYED.code);
			}

			String prefixContent = forSDK ? "SDK：" : "WAP：";
			Integer tradeType;
			if (forSDK) {
				tradeType =PaymentConstant.TRADE_TYPE.APP.code;
			} else {
				tradeType = PaymentConstant.TRADE_TYPE.WAP.code;
			}
			
			updatePaymentInfo(paymentInfoObj, prefixContent, PaymentConstant.PAY_ACTION_NAME.UPDATE, true, tradeType);
			log.info("payment-{} post info:{}", paymentInfoId, postData.getPaymentTradeNo());
		} else {
			log.info("post info is null, paymentInfoId={}", paymentInfoId);
		}
		return postData;
	}
	
	@Override
	public CallbackResultDto callback(Map<String, String> parameterMap, String gateway) throws ServiceException {
		PayCallbackData callbackData = getPayCallbackData(parameterMap);
		String paymentTradeNo = callbackData.getPaymentTradeNo();
		log.info("回调参数{}", parameterMap);
		log.info("处理流水号{}的回调", paymentTradeNo);
		PaymentInfo paymentInfoObj = null;
		
		CallbackResultDto result = null;
		
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			
			if(StringUtil.isNotBlank(paymentTradeNo)&& paymentTradeNo.startsWith("80")) {
				params.put("serial", paymentTradeNo);
			}
			else {
				params.put("paymentTradeNo", paymentTradeNo);
			}
			List<PaymentInfo> paymentInfoObjs = paymentInfoDao.queryByParamNotEmpty(params);
			if (CollectionUtils.isEmpty(paymentInfoObjs)){
				log.error("未找到支付信息, orderCode = {}", paymentTradeNo);
				throw new ServiceException("未找到支付信息");
			}
			paymentInfoObj = paymentInfoObjs.get(0);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new ServiceException("查询支付信息失败：订单号=" + paymentTradeNo, e);
		}
		
		result = proccessCallback(parameterMap, gateway, callbackData, paymentInfoObj);
		
		//团购券逻辑 ---start
		log.info("生成的兑换码信息开始------"+"OrderType.BUY_COUPONS.code:"+OrderType.BUY_COUPONS.code+"----paymentInfoObj.getOrderType().intValue():"+paymentInfoObj.getOrderType().intValue());
		
		log.info("OrderType.BUY_COUPONS.code==paymentInfoObj.getOrderType().intValue()-----:"+(OrderType.BUY_COUPONS.code==paymentInfoObj.getOrderType().intValue()));
		if(OrderType.BUY_COUPONS.code==paymentInfoObj.getOrderType().intValue()){//团购券入口
		
			int orderCode=paymentInfoObj.getBizCode().intValue();//订单编号
			if(PaymentConstant.PAYMENT_STATUS.PAYED.code.equals(paymentInfoObj.getStatus())){//支付成功
				String reddemCodes=orderRedeemItemService.generateAndSaveRedeemInfo( Long.valueOf(orderCode));
				log.info("生成的兑换码信息："+reddemCodes);
			}
		}
		
		
		//团购逻辑 ---end
		
		if(result.isNeedSendMQ()){
			
			if(PaymentConstant.BIZ_TYPE.MERGEORDER.code.intValue() == paymentInfoObj.getBizType().intValue()){
				PaymentInfo query = new PaymentInfo();
				query.setPrtPaymentId(paymentInfoObj.getPaymentId());
				List<PaymentInfo> paymentInfoList = paymentInfoDao.queryByObject(query);
				for(PaymentInfo subPaymentInfo : paymentInfoList){
					try {
						// 通知支付结果
						log.info("订单支付回调开始：orderCode={}", subPaymentInfo.getBizCode());
						if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfoObj.getBizType())){
							rabbitMqProducer.sendP2PMessage(MqMessageConstant.REGISTER_PROMOTER_SUCCESS, paymentInfoObj);
						}else{
							rabbitMqProducer.sendP2PMessage(PaymentConstant.PAYMENT_INFO_STATUS_QUEUE, paymentInfoObj);
						}
						log.info("订单支付回调完成：orderId={}, status={}",subPaymentInfo.getBizCode(), subPaymentInfo);
					} catch (MqClientException e) {
						log.error("biz code{} fail to send mq message!", subPaymentInfo.getBizCode());
					}
				}
			}
			else{
				try {
					// 通知支付结果
					log.info("订单支付回调开始：orderCode={}", paymentInfoObj.getBizCode());
					if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfoObj.getBizType())){
						rabbitMqProducer.sendP2PMessage(MqMessageConstant.REGISTER_PROMOTER_SUCCESS, paymentInfoObj);
					}else{
						rabbitMqProducer.sendP2PMessage(PaymentConstant.PAYMENT_INFO_STATUS_QUEUE, paymentInfoObj);
					}
					log.info("订单支付回调完成：orderCode={}, status={}",paymentInfoObj.getBizCode(), paymentInfoObj);
				} catch (MqClientException e) {
					log.error("biz code{} fail to send mq message!", paymentInfoObj.getBizCode());
				}
			}
			
			
			log.info("{}支付成功之后处理", paymentInfoObj.getBizCode());
//			operateAfterCallbackSuccess(paymentInfoObj);
		}
		
		return result;
	}

	/**
	 * @param paymentInfoObj
	 */
	public void operateAfterCallbackSuccess(PaymentInfo paymentInfoObj){}

	@Override
	public ResultInfo declareOrder(SubOrder subOrder, OrderConsignee consignee, CustomsInfo customsInfo) {
		throw new ServiceException("METHOD[declareOrder]ONLY_FOR_SUB_CLASS_TO_IMPL");
	}

	@Override
	public ResultInfo declareQuery(SubOrder subOrder, ClearanceChannelsEnum clearance) {
		throw new ServiceException("METHOD[declareQuery]ONLY_FOR_SUB_CLASS_TO_IMPL");
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public AppPayData getAppPayDataByParams(Long paymentInfoId, boolean forSDK, String userId, String gateway, Map<String,Object> params) {
		PaymentInfo paymentInfoDO = paymentInfoDao.queryById(paymentInfoId);
		
		paymentInfoDO.setGatewayCode(gateway);
		if(userId != null && !(paymentInfoDO.getCreateUser().equals(userId))){
			log.error("根据paymentId[{}] userId[{}]没有找到对应的支付信息", paymentInfoId, userId);
			throw new ServiceException("根据paymentId[" + paymentInfoId + "] userId[" + userId + "]没有找到对应的支付信息");
		}

		paymentInfoDO = processDSSPaymentInfo(paymentInfoDO);
		
		AppPayData postData = constructAppPostDataByParams(paymentInfoDO, forSDK, params);
		String appInvokeParamters = new Gson().toJson(postData);
		log.info("payment AppPayData is :{}", appInvokeParamters);
		if (postData != null) {
			paymentInfoDO.setStatus(PaymentConstant.PAYMENT_STATUS.PAYING.code);
			if(paymentInfoDO.getAmount() <= 0) {
				paymentInfoDO.setStatus(PaymentConstant.PAYMENT_STATUS.PAYED.code);
			}

			String prefixContent = forSDK ? "SDK：" : "WAP：";
			Integer tradeType;
			if (forSDK) {
				tradeType =  PaymentConstant.TRADE_TYPE.APP.code ;
			} else {
				tradeType = PaymentConstant.TRADE_TYPE.WAP.code;
			}
			updatePaymentInfo(paymentInfoDO, prefixContent + "App支付调用参数:" + appInvokeParamters, PaymentConstant.PAY_ACTION_NAME.UPDATE, true, tradeType);
			log.info("payment-{} post info:{}", paymentInfoId, postData.getPaymentTradeNo());
		} else {
			log.info("post info is null, paymentInfoId={}", paymentInfoId);
		}
		return postData;
	}

	private PaymentInfo processDSSPaymentInfo(PaymentInfo paymentInfoDO) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, -30);
		if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfoDO.getBizType())) {
			if(paymentInfoDO.getCreateTime().before(c.getTime())) {
				Date cur = new Date();
				PaymentInfo next = BeanUtil.clone(paymentInfoDO);
				String orderCode = orderCodeGeneratorService.generate(OrderCodeType.DSS_PAY).toString();
				next.setPaymentTradeNo(orderCode);
				next.setBizCode(Long.valueOf(orderCode));
				next.setSerial(orderCode);
				next.setCreateTime(cur);
				next.setUpdateTime(cur);
				next.setPaymentId(null);
				paymentInfoDao.insert(next);
				paymentInfoDO = next;
			}
		}
		return paymentInfoDO;
	}

	;
	
	@Override
	@Transactional
	public CallbackResultDto proccessCallback(Map<String, String> parameterMap, String gateway, PayCallbackData callbackData, PaymentInfo paymentInfo) throws ServiceException {
		String paymentTradeNo = callbackData.getPaymentTradeNo();
		
		try{
			
			if (paymentInfo == null) {
				log.error("支付平台[{}]回调的订单{}找不到相应的支付信息：{}。", callbackData.getPaymentGateway(), paymentTradeNo, parameterMap);
				CallbackResultDto crDto = new CallbackResultDto(false, null);
				String returnMsg = getReturnMsg(null, callbackData);
				crDto.setMessge(returnMsg);
				return crDto;
			}
	
			// 验证回调信息是否是合法的调用
			Map<String, String> parameters = new TreeMap<String, String>(parameterMap);
			parameters.remove(PaymentService.REQUEST_CONTENT_NAME);
			if (!verifyResponse(parameters)) {
				log.info("订单{}验证回调失败");
				savePaymentLog(paymentInfo, "非合法的支付回调调用:" + parameterMap);
				CallbackResultDto crDto = new CallbackResultDto(false, paymentInfo);
				crDto.setMessge("非合法的支付回调调用");
				return crDto;
			}
			log.info("订单{}验证回调通过", paymentInfo.getBizCode());
	
			CallbackResultDto crDto;
			Integer payStatus = paymentInfo.getStatus();
			log.info("支付状态 {}", payStatus);
			if (!PaymentConstant.PAYMENT_STATUS.PAYED.code.equals(payStatus)) {
				log.info("回调-订单{}处理...", paymentInfo.getPaymentId());
				if (callbackData.isSuccess()) {
					paymentInfo.setStatus(PaymentConstant.PAYMENT_STATUS.PAYED.code);
					paymentInfo.setPaymentTradeNo(paymentTradeNo);
					paymentInfo.setGatewayTradeNo(callbackData.getGatewayTradeNo());
					crDto = new CallbackResultDto(true, paymentInfo);
				} else {
					paymentInfo.setStatus(PaymentConstant.PAYMENT_STATUS.FAIL_PAY.code);
					paymentInfo.setCallbackInfo(callbackData.getCallbackInfo());
	
					crDto = new CallbackResultDto(false, paymentInfo);
					
					if(PaymentConstant.PAYMENT_STATUS.FAIL_PAY.code.equals(payStatus)){
						crDto.setNeedSendMQ(false);
						return crDto;
					}
				}
				
				if("alipayDirect".equals(gateway) && parameterMap.containsKey("currency")){
					gateway = "alipayInternational";
				}
				Map<String, Object> gatewayParams = new HashMap<String,Object>();
				gatewayParams.put("gatewayCode", gateway);
				PaymentGateway paymentGatewayDO = paymentGatewayService.queryUniqueByParams(gatewayParams);
				Date callBackTime = callbackData.getCallBackTime();
				paymentInfo.setCallbackTime(callBackTime == null ? new Date() : callBackTime);
				paymentInfo.setGatewayId(paymentGatewayDO.getGatewayId());
				paymentInfo.setUpdateUser(callbackData.getCreateUserID());
				
				int updateRow = 0;
				try{
					updateRow = updatePaymentInfoByStatus(paymentInfo,  "支付回调结果:"+new Gson().toJson(parameterMap), PaymentConstant.PAY_ACTION_NAME.CALLBACK, false, null);
				}catch(Exception e){
					throw new ServiceException(e);
				}
				
				if(updateRow == 1 && callbackData.isSuccess()){
					log.info("更新支付信息成功,等待发送mq消息");
					crDto.setNeedSendMQ(true);
				}
				else{
					log.info("支付更新行数为{},orderId={}，忽略此次回调", updateRow, paymentInfo);
					// 并发重复回调引起的情况， 标记不发送mq消息。
					crDto.setNeedSendMQ(false);
				}
	
			} else {
				
				// 重复回调了
				log.info("订单支付重复回调：orderId={}, status= {}", paymentInfo.getBizCode(), payStatus);
				crDto = new CallbackResultDto(true, paymentInfo);
			}
			String returnMsg = getReturnMsg(paymentInfo, callbackData);
			crDto.setMessge(returnMsg);
			
			return crDto;
		}
		catch(Exception e){
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 
	 * 
	 * @param paymentInfoDO
	 * @param content
	 * @param actionName
	 * @param updateStatusOnly 是否只更新支付状态
	 * @param tradeType 交易类型1：PC，2：WAP_mobile：3：WAP_btmobile 4:APP_mobile 5:APP_btmobile)
	 * @return
	 * @throws ServiceException
	 */
	private int updatePaymentInfoByStatus(PaymentInfo paymentInfoDO,
			String content,
			PaymentConstant.PAY_ACTION_NAME actionName,
			boolean updateStatusOnly, Integer tradeType)
			throws ServiceException {
		try {
			Date now = new Date();
			paymentInfoDO.setUpdateTime(now);
			if (tradeType != null)
				paymentInfoDO.setTradeType(tradeType);
			int row = 0;

			if (updateStatusOnly) {
				PaymentInfo upd = new PaymentInfo();
				upd.setPaymentId(paymentInfoDO.getPaymentId());
				upd.setStatus(paymentInfoDO.getStatus());
				upd.setUpdateTime(now);
				upd.setTradeType(tradeType);
				upd.setGatewayId(paymentInfoDO.getGatewayId());
				row = paymentInfoDao.updatePaymentByPayed(upd);
			} else {
				row = paymentInfoDao.updatePaymentByPayed(paymentInfoDO);
			}
			// 更新合并订单下的子订单
			if (PaymentConstant.BIZ_TYPE.MERGEORDER.code.intValue() == paymentInfoDO.getBizType().intValue()) {
            	PaymentInfo query = new PaymentInfo();
				query.setPrtPaymentId(paymentInfoDO.getPaymentId());
				List<PaymentInfo> paymentInfoList = paymentInfoDao.queryByObject(query);
                for (int i = 0; i < paymentInfoList.size(); i++) {
                    PaymentInfo subPaymentInfo = new PaymentInfo();
                    subPaymentInfo.setGatewayTradeNo(paymentInfoDO.getGatewayTradeNo());
                    subPaymentInfo.setGatewayId(paymentInfoDO.getGatewayId());
                    subPaymentInfo.setUpdateTime(now);
                    subPaymentInfo.setStatus(paymentInfoDO.getStatus());
                    subPaymentInfo.setTradeType(paymentInfoDO.getTradeType());
                    subPaymentInfo.setPaymentId(paymentInfoList.get(i).getPaymentId());
                    subPaymentInfo.setCallbackTime(paymentInfoDO.getCallbackTime());
                    subPaymentInfo.setCallbackInfo(paymentInfoDO.getCallbackInfo());
                    if (PaymentConstant.PAYMENT_STATUS.PAYED.code.intValue() == paymentInfoDO.getStatus().intValue())
                        subPaymentInfo.setPaymentTradeNo(paymentInfoDO.getPaymentTradeNo());
                    paymentInfoDao.updateNotNullById(subPaymentInfo);
                }
            }
			if(row == 1){
				PaymentLog paymentLogDO = new PaymentLog(
						paymentInfoDO.getPaymentId(),
						PaymentConstant.OBJECT_TYPE.PAYMENT.code,
						actionName.cnName, content,
						paymentInfoDO.getActionIp(), now,
						paymentInfoDO.getCreateUser());
				paymentLogDO.setPartTable(DateUtil.format(now, "yy"));
				paymentLogDao.insert(paymentLogDO);
			}
			return row;
		} catch (Exception e) {
			log.error("保存paymentInfo[paymentGatewayNo="+paymentInfoDO.getPaymentTradeNo()+" ]信息出错", e);
			throw new ServiceException(e);
		}
	}
	/**
	 * 
	 * 
	 * @param paymentInfoObj
	 * @param prefixContent
	 * @param actionName
	 * @param updateStatusOnly 是否只更新支付状态
	 * @param tradeType 交易类型1：PC，2：WAP_mobile：3：WAP_btmobile 4:APP_mobile 5:APP_btmobile)
	 * @return
	 * @throws ServiceException
	 */
	private int updatePaymentInfo(PaymentInfo paymentInfoObj,
			String prefixContent,
			PaymentConstant.PAY_ACTION_NAME actionName,
			boolean updateStatusOnly, Integer tradeType) throws ServiceException{
		try {
			Date now = new Date();
			paymentInfoObj.setUpdateTime(now);
			if (tradeType != null)
				paymentInfoObj.setTradeType(tradeType);
			int row = 0;
			if (updateStatusOnly) {
				PaymentInfo upd = new PaymentInfo();
				upd.setPaymentId(paymentInfoObj.getPaymentId());
				upd.setStatus(paymentInfoObj.getStatus());
				upd.setUpdateTime(now);
				upd.setTradeType(tradeType);
				row = paymentInfoDao.updateNotNullById(upd);
			} else {
				row = paymentInfoDao.updateNotNullById(paymentInfoObj);
			}

			PaymentLog paymentLogObj = new PaymentLog(
					paymentInfoObj.getPaymentId(),
					PaymentConstant.OBJECT_TYPE.PAYMENT.code,
					actionName.cnName, new StringBuffer(prefixContent)
							.append("根据")
							.append(PaymentConstant.BIZ_TYPE.getCnName(paymentInfoObj.getBizType()))
							.append(paymentInfoObj.getBizCode())
							.append(actionName.cnName).toString(),
					paymentInfoObj.getActionIp(), now,
					paymentInfoObj.getCreateUser());
			paymentLogObj.setPartTable(DateUtil.format(now, "yy"));
			paymentLogDao.insert(paymentLogObj);

			return row;
		} catch (Exception e) {
			log.error("保存paymentInfo[paymentGatewayNo="+paymentInfoObj.getPaymentTradeNo()+" ]信息出错", e);
			throw new ServiceException(e);
		}
	}

	private void savePaymentLog(PaymentInfo paymentInfoObj, String logContent) throws ServiceException {

		PaymentLog paymentLogObj = new PaymentLog(
				paymentInfoObj.getPaymentId(),
				PaymentConstant.OBJECT_TYPE.PAYMENT.code,
				PaymentConstant.PAY_ACTION_NAME.CALLBACK.cnName, logContent,
				paymentInfoObj.getActionIp(), new Date(),
				paymentInfoObj.getCreateUser());
		paymentLogObj.setPartTable(DateUtil.format(new Date(), "yy"));
		try {
			paymentLogDao.insert(paymentLogObj);
		} catch (Exception e) {
			log.error("", e);
			throw new ServiceException(e);
		}
	}

	/**
	 * 如果支付平台回调需要包含一些信息，通过此接口返回
	 * 
	 * @param paymentInfoObj
	 * @param callbackData
	 * @return
	 */
	protected String getReturnMsg(PaymentInfo paymentInfoObj, PayCallbackData callbackData) {
		return null;
	}

	@Override
	@Transactional
	public String refund(String gateway, Long refundNo) throws ServiceException {
		RefundPayinfo refundPayinfoObj = null;
		Map<String, Object> params = new HashMap<>();
		try {
			params.clear();
			params.put("bizCode", refundNo);
			refundPayinfoObj = refundPayinfoService.queryUniqueByParams(params);
			if (null != refundPayinfoObj) {
				PaymentInfo paymentInfo = paymentInfoDao.queryById(refundPayinfoObj.getPaymentId());
				refundPayinfoObj.setPaymentInfo(paymentInfo);
			} else {
				throw new ServiceException("找不到退款信息：refundNo=" + refundNo);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e);
		}

		
		if(PaymentConstant.REFUND_STATUS.REFUNDED.code.equals(refundPayinfoObj.getStatus())){
			log.info("退款单{}已经退款成功,忽略本次操作.", refundPayinfoObj.getBizCode());
			return "success";
		}
		
		RefundResult result = doRefund(refundPayinfoObj);
		if (result == null) {
			// NOTE 如果用户发起退款需要先在财务审批一道，就没有这种情况了(不会走到这里了)
			log.info("用户发起需确认的退款：refundId={}, orderId={}", refundPayinfoObj.getPayRefundId(), refundPayinfoObj.getBizCode());

			String logContent = "用户提交退款申请：" + refundPayinfoObj.getBizCode();
			saveRefundLog(refundPayinfoObj, logContent);

			// 回写退款状态
			refundPayinfoObj.setStatus(PaymentConstant.REFUND_STATUS.TO_CONFIRM.code);
			updateRefundPayinfo(refundPayinfoObj);

			return "success";
		} else if (!result.isSuccess()) {
			log.warn("refund no{} refund fail", refundPayinfoObj.getBizCode());
			if(!result.hasCallback())
				proccessRefundCallback(refundPayinfoObj, false, result.getCallBackTime(), result.getCreateUserID(), result.getMessage(), result.getCallbackInfo());
			return "fail";
		}
		else{
			// 调用接口成功后，如果支付平台支付异步回调则以回调结果为准 否则以同步结果为准直接更改退款状态
			if(!result.hasCallback())
				proccessRefundCallback(refundPayinfoObj, true, result.getCallBackTime(), result.getCreateUserID(), result.getMessage(), result.getCallbackInfo());
			return "success";
		}
	}

	@Override
	@Transactional
	public List<RefundPayinfo> refunds(String gateway, List<String> bizCodes) throws ServiceException {
		return null;
	}

	private void saveRefundLog(RefundPayinfo refundPayinfoObj, String logContent) throws ServiceException {
		PaymentLog paymentLogObj = new PaymentLog(refundPayinfoObj.getPayRefundId(), PaymentConstant.OBJECT_TYPE.REFUND.code,
				PaymentConstant.PAY_ACTION_NAME.REFUND.cnName, logContent, "", new Date(), refundPayinfoObj.getCreateUser());
		paymentLogObj.setPartTable(DateUtil.format(new Date(), "yy"));
		try {
			paymentLogDao.insert(paymentLogObj);
		} catch (Exception e) {
			log.error("", e);
			throw new ServiceException(e);
		}
	}

	abstract protected RefundResult doRefund(RefundPayinfo refundPayinfoObj) throws ServiceException;

	@Override
	@Transactional
	public String refundCallback(Map<String, String> params) throws ServiceException {
		RefundCallbackData callbackData = getRefundCallbackData(params);
		log.info("is alipay refund:{}", callbackData.isAlipayRefund());
		if (callbackData.isAlipayRefund()) {
			List<List<String>> resultDetail = callbackData.getAlipayRefundResultDetail();
			if (CollectionUtils.isEmpty(resultDetail)) {
				log.error("阿里退款回调参数错误");
				return "fail";
			} else {
				log.info("result_details length:" + resultDetail.size());
			}
			for (List<String> details : resultDetail) {
				String refundNo = callbackData.getRefundNo();
				Boolean isSuccess = "SUCCESS".equalsIgnoreCase(details.get(2));
				String callbackInfo = details.get(2);
				if(refundNo != null){
					proccessRefundCallback(refundNo, isSuccess, callbackData.getCallBackTime(), callbackData.getCreateUserID(), callbackInfo,callbackInfo, true);
				}
				else{
					log.error("退款获取refundNo失败，参数{}",params);
				}
			}
			return "success";
		}
		if(callbackData.getRefundNo() != null){
			return proccessRefundCallback(callbackData.getRefundNo(),
					callbackData.isSuccess(), callbackData.getCallBackTime(),
					callbackData.getCreateUserID(), callbackData.getMessage(), callbackData.getCallbackInfo(),false);
		}
		else{
			log.error("退款获取refundNo失败，参数{}",params);
			return "success";
		}
	}

	private String proccessRefundCallback(RefundPayinfo refundInfoDO, Boolean isSuccess, Date notifyTime, String modifyUser, String callbackInfo, String logContent) {
		// 如果数据状态是已付款则直接返回，防止重复操作
		if (PaymentConstant.REFUND_STATUS.REFUNDED.code.equals(refundInfoDO.getStatus())) {
			return "success";
		}
		refundInfoDO.setUpdateTime(new Date());
		if (isSuccess) {
			refundInfoDO.setStatus(PaymentConstant.REFUND_STATUS.REFUNDED.code);
		} else {
			refundInfoDO.setStatus(PaymentConstant.REFUND_STATUS.FAIL_REFUND.code);
		}
		refundInfoDO.setNotified(PaymentConstant.NOTIFY_STATUS.NOTIFIED.code);
		refundInfoDO.setNotifyTime(notifyTime);
		refundInfoDO.setUpdateUser(modifyUser);
		refundInfoDO.setCallbackInfo(callbackInfo);
		try {
			updateRefundPayinfo(refundInfoDO, logContent);
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}

		try {
			// 通知支付结果
			log.info("订单退款通知开始：orderId={}, status={}", refundInfoDO.getBizCode(), refundInfoDO.getStatus());
			rabbitMqProducer.sendP2PMessage(PaymentConstant.REFUND_INFO_STATUS_QUEUE, refundInfoDO);
			log.info("订单退款通知完成：orderId={}, status={}", refundInfoDO.getBizCode(), refundInfoDO.getStatus());
		} catch (MqClientException e) {
			log.error("refund-{} of payment {} fail to send mq message!", refundInfoDO.getPayRefundId(), refundInfoDO.getPaymentId());
		}
		if(isSuccess)
			return "success";
		else
			return "fail";
	}
	private String proccessRefundCallback(String queryString, Boolean isSuccess, Date notifyTime, String modifyUser, String callbackInfo,String logContent, Boolean isAlipay) {
		RefundPayinfo refundInfoDO = null;
		try {
			if (isAlipay) {
				RefundPayinfo refundQuery = new RefundPayinfo();
				refundQuery.setBizCode(Long.valueOf(queryString));
				List<RefundPayinfo> refundReust = refundPayinfoService.queryByObject(refundQuery);
				refundInfoDO = refundReust.get(0);
			} else {
				RefundPayinfo refundQuery = new RefundPayinfo();
				refundQuery.setBizCode(Long.valueOf(queryString));
				List<RefundPayinfo> refundReust = refundPayinfoService.queryByObject(refundQuery);
				refundInfoDO = refundReust.get(0);
			}
			
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
		// 如果数据状态是已付款则直接返回，防止重复操作
		if (PaymentConstant.REFUND_STATUS.REFUNDED.code.equals(refundInfoDO.getStatus()) && isSuccess) {
			return "success";
		}
		refundInfoDO.setUpdateTime(new Date());
		if (isSuccess) {
			refundInfoDO.setStatus(PaymentConstant.REFUND_STATUS.REFUNDED.code);
		} else {
			refundInfoDO.setStatus(PaymentConstant.REFUND_STATUS.FAIL_REFUND.code);
		}
		refundInfoDO.setNotified(PaymentConstant.NOTIFY_STATUS.NOTIFIED.code);
		refundInfoDO.setNotifyTime(notifyTime);
		refundInfoDO.setUpdateUser(modifyUser);
		refundInfoDO.setCallbackInfo(callbackInfo);
		int updateNum = 0;
		try {
			updateNum = updateRefundPayinfoUnrefunded(refundInfoDO, logContent);
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		if(updateNum>0){
			try {
				// 通知支付结果
				log.info("订单退款回调开始：orderId={}, status={}", refundInfoDO.getBizCode(), refundInfoDO.getStatus());
				rabbitMqProducer.sendP2PMessage(PaymentConstant.REFUND_INFO_STATUS_QUEUE, refundInfoDO);
				log.info("订单退款回调完成：orderId={}, status={}", refundInfoDO.getBizCode(), refundInfoDO.getStatus());
			} catch (MqClientException e) {
				log.error("refund-{} of payment {} fail to send mq message!", refundInfoDO.getPayRefundId(), refundInfoDO.getPaymentId());
			}
		}
		return "success";
	}
	private int updateRefundPayinfo(RefundPayinfo refundPayinfoDO, String logContent) throws ServiceException {
		try {
			refundPayinfoDO.setUpdateTime(new Date());

			int row = refundPayinfoService.updateNotNullById(refundPayinfoDO);

			PaymentLog paymentLogDO = new PaymentLog(
					refundPayinfoDO.getPaymentId(),
					PaymentConstant.OBJECT_TYPE.REFUND.code,
					PaymentConstant.PAY_ACTION_NAME.REFUND_UPDATE.cnName,
					new StringBuffer("根据")
							.append(PaymentConstant.BIZ_TYPE.getCnName(refundPayinfoDO.getBizType()))
							.append(refundPayinfoDO.getBizCode())
							.append("更新退款信息").append(logContent).toString(), "alipay", new Date(),
					refundPayinfoDO.getCreateUser());
			paymentLogDO.setPartTable(DateUtil.format(new Date(), "yy"));
			paymentLogDao.insert(paymentLogDO);
			return row;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e);
		}
	}
	private int updateRefundPayinfoUnrefunded(RefundPayinfo refundPayinfoDO, String logContent) throws ServiceException {
		try {
			refundPayinfoDO.setUpdateTime(new Date());

			int row = refundPayinfoService.updateDynamicByUnrefunded(refundPayinfoDO);

			PaymentLog paymentLogDO = new PaymentLog(
					refundPayinfoDO.getPaymentId(),
					PaymentConstant.OBJECT_TYPE.REFUND.code,
					PaymentConstant.PAY_ACTION_NAME.REFUND_UPDATE.cnName,
					new StringBuffer("根据")
							.append(PaymentConstant.BIZ_TYPE.getCnName(refundPayinfoDO.getBizType()))
							.append(refundPayinfoDO.getBizCode())
							.append("更新退款信息").append(logContent).toString(), "alipay", new Date(),
					refundPayinfoDO.getCreateUser());
			paymentLogDO.setPartTable(DateUtil.format(new Date(), "yy"));
			paymentLogDao.insert(paymentLogDO);
			return row;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e);
		}
	}
	private int updateRefundPayinfo(RefundPayinfo refundPayinfoObj) throws ServiceException {
		try {
			refundPayinfoObj.setUpdateTime(new Date());

			int row = refundPayinfoService.updateNotNullById(refundPayinfoObj);

			PaymentLog paymentLogObj = new PaymentLog(
					refundPayinfoObj.getPaymentId(),
					PaymentConstant.OBJECT_TYPE.REFUND.code,
					PaymentConstant.PAY_ACTION_NAME.REFUND_UPDATE.cnName,
					new StringBuffer("根据")
							.append(PaymentConstant.BIZ_TYPE.getCnName(refundPayinfoObj.getBizType()))
							.append(refundPayinfoObj.getBizCode())
							.append("更新退款信息").toString(), "alipay", new Date(),
					refundPayinfoObj.getCreateUser());
			paymentLogObj.setPartTable(DateUtil.format(new Date(), "yy"));
			paymentLogDao.insert(paymentLogObj);
			return row;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e);
		}
	}
	public AliPayRefundPostData getAlipayRefundPostdata() {
		return new AliPayRefundPostData(settings);
	}

	protected Map<String, String> convertXml2Map(String xml) {
		Map<String, String> map = new HashMap<String, String>();
		Pattern p = Pattern.compile("<(\\w+)><!\\[CDATA\\[([^]]*)\\]\\]></");
		Matcher m = p.matcher(xml);
		while (m.find()) {
			map.put(m.group(1), m.group(2));
		}
		p = Pattern.compile("<(\\w+)>([^<]*)</");
		m = p.matcher(xml);
		while (m.find()) {
			map.put(m.group(1), m.group(2));
		}

		return map;
	}
	
	public PaymentInfo getParentPaymentInfo(Long paymentId) {
		if(paymentId == null) return null;
		if(paymentId <= 0) return null;
		PaymentInfo parent = paymentInfoDao.queryById(paymentId);
		if(parent == null) return null;
		if(!PaymentConstant.PAYMENT_STATUS.PAYED.code.equals(parent.getStatus())) {
			return null;
		}
		return parent;
	}
	class PayHttpRequestRetryHandler implements HttpRequestRetryHandler {  
        public boolean retryRequest(IOException exception,  
                int executionCount, HttpContext context) {  
            if (executionCount >= 3) {// 如果已经重试了5次，就放弃  
                return false;  
            }  
            if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试  
                return true;  
            }  
            if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常  
                return false;  
            }  
            if (exception instanceof InterruptedIOException) {// 超时  
                return false;  
            }  
            if (exception instanceof UnknownHostException) {// 目标服务器不可达  
                return false;  
            }  
            if (exception instanceof ConnectTimeoutException) {// 连接被拒绝  
                return false;  
            }  
            if (exception instanceof SSLException) {// SSL握手异常  
                return false;  
            }  
 
            HttpClientContext clientContext = HttpClientContext  
                    .adapt(context);  
            HttpRequest request = clientContext.getRequest();  
               // 如果请求是幂等的，就再次尝试  
            if (!(request instanceof HttpEntityEnclosingRequest)) {  
                return true;  
            }  
            return false;  
        }  
    };  
}
