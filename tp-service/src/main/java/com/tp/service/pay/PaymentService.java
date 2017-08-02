package com.tp.service.pay;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.vo.Constant;
import com.tp.common.vo.MqMessageConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.PaymentConstant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.OrderConstant.OrderType;
import com.tp.common.vo.bse.ClearanceChannelsEnum;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.pay.AppPayData;
import com.tp.dto.pay.cbdata.CallbackResultDto;
import com.tp.dto.pay.postdata.AliPayRefundPostData;
import com.tp.dto.pay.postdata.PayPostData;
import com.tp.dto.pay.query.TradeStatusQuery;
import com.tp.dto.pay.servicestatus.ServiceStatusDto;
import com.tp.exception.ServiceException;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.SubOrder;
import com.tp.model.pay.CustomsInfo;
import com.tp.model.pay.PaymentChannelGateway;
import com.tp.model.pay.PaymentGateway;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.pay.PaymentLog;
import com.tp.model.pay.RefundPayinfo;
import com.tp.mq.RabbitMqProducer;
import com.tp.mq.exception.MqClientException;
import com.tp.result.pay.TradeStatusResult;
import com.tp.service.ord.IOrderRedeemItemService;
import com.tp.service.pay.ICustomsInfoService;
import com.tp.service.pay.IPayPlatformService;
import com.tp.service.pay.IPaymentChannelGatewayService;
import com.tp.service.pay.IPaymentGatewayService;
import com.tp.service.pay.IPaymentInfoService;
import com.tp.service.pay.IPaymentLogService;
import com.tp.service.pay.IPaymentService;
import com.tp.service.pay.IRefundPayinfoService;
import com.tp.util.DateUtil;


//@Service(value = "paymentService")
@Service
public class PaymentService implements IPaymentService {

	private final Logger logger = LoggerFactory.getLogger(PaymentService.class);

	@Autowired
	IPaymentLogService paymentLogService;
	
	@Autowired
	IPaymentGatewayService paymentGatewayService; 
	
	@Autowired
	IRefundPayinfoService refundPayinfoService;
	
	@Autowired
	IPaymentInfoService paymentInfoService;

	@Autowired
	RabbitMqProducer rabbitMqProducer;

	@Autowired
	IPaymentChannelGatewayService paymentChannelGatewayService;


	@Autowired
	Map<String, IPayPlatformService> payPlatformServices;// {{'alipay',
														// alipayPlatformService}}
	@Autowired
	IOrderRedeemItemService  orderRedeemItemService;
	
	@Autowired
	Properties settings;
	
	@Autowired
	private ICustomsInfoService customsInfoService;
	
	@Override
	public PaymentInfo checkPayment(Long paymentId){
		PaymentInfo paymentInfoObj = paymentInfoService.queryById(paymentId);
		if (null != paymentInfoObj) {
			Long gatewayId = paymentInfoObj.getGatewayId();
			if (gatewayId != null && gatewayId.intValue() != 0) {
				PaymentGateway paymentGateway = paymentGatewayService.queryById(gatewayId);
				if (paymentGateway != null) {
					paymentInfoObj.setGatewayCode(paymentGateway.getGatewayCode());
				}
			}
		}
		return paymentInfoObj;
	}

	@Override
	public PayPostData getPostData(String gateway, Long paymentInfoId) throws ServiceException {
		IPayPlatformService payPlatformService = payPlatformServices.get(gateway
				+ "IPayPlatformService");
		return payPlatformService.getPostData(paymentInfoId);
	}

	@Override
	public AppPayData getAppPayData(String gateway, Long paymentInfoId, boolean forSDK, String userId) throws ServiceException{
		IPayPlatformService payPlatformService = payPlatformServices.get(gateway + "PayPlatformService");
		return payPlatformService.getAppPayData(paymentInfoId, forSDK, userId);
	}

	@Override
	public CallbackResultDto callback(String gateway,
			Map<String, String> parameterMap) throws ServiceException {
		IPayPlatformService payPlatformService = payPlatformServices.get(gateway + "PayPlatformService");
		CallbackResultDto resultDto = payPlatformService.callback(parameterMap, gateway);
		return resultDto;
	}

	@Transactional
	@Override
	public PaymentInfo try2Pay(Long paymentId, Long gatewayId) {
		PaymentInfo paymentInfo = paymentInfoService.queryById(paymentId);
		if (paymentInfo != null) {
			Integer status = paymentInfo.getStatus();
			if (!PaymentConstant.PAYMENT_STATUS.PAYED.code.equals(status)) {
				Long origGatewayId = paymentInfo.getGatewayId();
				if (gatewayId != null) {
					try {
						if (!gatewayId.equals(origGatewayId)) {
							PaymentGateway gatewayObj = paymentGatewayService.queryById(gatewayId);
							if (gatewayObj != null) {
								paymentInfo.setGatewayId(gatewayId);
								paymentInfo.setGatewayCode(gatewayObj.getGatewayCode());
								paymentInfo.setGatewayName(gatewayObj.getGatewayName());
								paymentInfo.setStatus(PaymentConstant.PAYMENT_STATUS.PAYING.code);

								PaymentInfo upd = new PaymentInfo();
								upd.setPaymentId(paymentId);
								upd.setGatewayId(gatewayId);
								upd.setStatus(PaymentConstant.PAYMENT_STATUS.PAYING.code);
								upd.setUpdateTime(new Date());
								
								Integer updateDynamic = paymentInfoService.updateNotNullById(upd);
								logger.info("updateDynamic={}", updateDynamic);

								Long bizCode = paymentInfo.getBizCode();
								PaymentLog paymentLogObj = new PaymentLog(paymentId,PaymentConstant.OBJECT_TYPE.PAYMENT.code,
										"修改支付方式", "修改"
										+ PaymentConstant.BIZ_TYPE.getCnName(paymentInfo.getBizType())
										+ "编号" + bizCode + " 支付方式："
										+ origGatewayId + " => "
										+ gatewayId, "", new Date(),
										gatewayObj.getGatewayCode());
								paymentLogObj.setPartTable(String.valueOf(bizCode).substring(2,
										6));
								paymentLogService.insert(paymentLogObj);
							} else {
								logger.error("找不到相应的支付ID：{}", gatewayId);
							}
						} else {
							try {
								PaymentGateway gatewayObj = paymentGatewayService.queryById(origGatewayId);
								paymentInfo.setGatewayCode(gatewayObj.getGatewayCode());
								paymentInfo.setGatewayName(gatewayObj.getGatewayName());
							} catch (Exception e) {
								logger.error(e.getMessage(), e);
							}
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				} else {
					try {
						PaymentGateway gatewayObj = paymentGatewayService.queryById(origGatewayId);
						paymentInfo.setGatewayCode(gatewayObj.getGatewayCode());
						paymentInfo.setGatewayName(gatewayObj.getGatewayName());
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			} else {
				try {
					PaymentGateway gatewayObj = paymentGatewayService.queryById(paymentInfo.getGatewayId());
					paymentInfo.setGatewayCode(gatewayObj.getGatewayCode());
					paymentInfo.setGatewayName(gatewayObj.getGatewayName());
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return paymentInfo;
	}

	@Override
	public Integer updateCancelPaymentInfo(Long bizCode, Integer bizType, String cancelIp, String cancelUser){
		if (bizCode == null) {
			logger.error("参数不能为空，bizId={},  bizType={}", bizCode, bizType);
			return 0;
		}
		PaymentInfo paymentInfoObj = new PaymentInfo();
		paymentInfoObj.setBizCode(bizCode);
		//paymentInfoObj.setBizType(bizType);
		int result = 0;
		try {
			List<PaymentInfo> paymentInfoObjs = paymentInfoService.queryByObject(paymentInfoObj);
			if (CollectionUtils.isNotEmpty(paymentInfoObjs)) {
				result = paymentInfoService.updateCancelPayment(paymentInfoObj);
				PaymentLog paymentLogObj = new PaymentLog(paymentInfoObjs.get(0).getPaymentId(),
						PaymentConstant.OBJECT_TYPE.PAYMENT.code, "取消支付信息",
						"根据" + PaymentConstant.BIZ_TYPE.getCnName(bizType)
						+ "编号" + bizCode + " 取消支付消息", cancelIp,
						new Date(), cancelUser);
				paymentLogObj.setPartTable(String.valueOf(bizCode).substring(2, 6));
				paymentLogService.insert(paymentLogObj);
			}
			return result;
		} catch (Exception e) {
			logger.error("取消支付记录出错", e);
			return 0;
		}
	}

	public TradeStatusResult queryTradeStatus(TradeStatusQuery query) {
		Map<String, Object> params = new HashMap<>();
		params.put("bizCode", query.getBizCode());
		List<PaymentInfo> paymentInfoObjs = paymentInfoService.queryByParamNotEmpty(params); 
		PaymentInfo paymentInfoObj = CollectionUtils.isNotEmpty(paymentInfoObjs) ? paymentInfoObjs.get(0):null;
		if (null == paymentInfoObj || null == paymentInfoObj.getGatewayId()) {
			TradeStatusResult result = new TradeStatusResult();
			result.setSuccess(false);
			result.setErrorMsg("没有对应的支付网关信息,请确认订单是否已支付");
			return result;
		}

		TradeStatusResult result = new TradeStatusResult();
		result.setCanceled(Integer.valueOf(1).equals(paymentInfoObj.getCanceled()));
		result.setTradeNo(paymentInfoObj.getGatewayTradeNo());
		Integer status = paymentInfoObj.getStatus();
		boolean payed = PaymentConstant.PAYMENT_STATUS.PAYED.code.equals(status);
		boolean payFailed = PaymentConstant.PAYMENT_STATUS.FAIL_PAY.code.equals(status);
		if (payed || payFailed) {
			result.setStatus(status);
			result.setSuccess(payed);
		} else {
			PaymentGateway gatewayObj = paymentGatewayService.queryById(paymentInfoObj.getGatewayId());
			IPayPlatformService payPlatformService = payPlatformServices.get(gatewayObj.getGatewayCode() + "PayPlatformService");
			TradeStatusResult queryResult = payPlatformService.queryPayStatus(paymentInfoObj);
			result.setStatus(queryResult.getStatus());
			result.setSuccess(queryResult.isSuccess());
			result.setErrorMsg(queryResult.getErrorMsg());
		}
		return result;
	}

	@Override
	public String refundCallback(String gateway, Map<String, String> params) throws ServiceException{
		IPayPlatformService payPlatformService = payPlatformServices.get(gateway + "PayPlatformService");
		return payPlatformService.refundCallback(params);
	}
	@Override
	public String refund(String gateway, Long refundNo) throws ServiceException{
		IPayPlatformService payPlatformService = payPlatformServices.get(gateway + "PayPlatformService");
		return payPlatformService.refund(gateway, refundNo);
	}

	@Override
	public PaymentInfo selectById(Long paymentId) {
		return paymentInfoService.queryById(paymentId);
	}

	@Override
	public List<PaymentInfo> selectByIds(List<Long> paymentIds) {
		Map<String, Object> params = new HashMap<>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "payment_id in(" + StringUtils.join(paymentIds, Constant.SPLIT_SIGN.COMMA) + ")");
		return paymentInfoService.queryByParamNotEmpty(params);
	}

	@Override
	public List<RefundPayinfo> selectRecentRefund2Confirm() {
		Map<String, Object> params = new HashMap<>();
		params.put("gatewayId", 1L);
		params.put("status", PaymentConstant.REFUND_STATUS.REFUNDING.code);
		PageInfo<RefundPayinfo> pageInfo = refundPayinfoService.queryPageByParamNotEmpty(params, new PageInfo<RefundPayinfo>(1, 100));
		return pageInfo.getRows();
	}

	@Override
	public AliPayRefundPostData getAlipayRefundPostdata() {
		IPayPlatformService payPlatformService = payPlatformServices.get("alipayDirectPayPlatformService");
		return payPlatformService.getAlipayRefundPostdata();
	}

	@Transactional
	public void checkRefundStatus() {
		// checkRefundStatus(4L);
		checkRefundStatus(null);
	}

	@Override
	public void checkRefundStatus(Long gatewayId) {
		try {
			// 支付方式信息
			Map<Long, PaymentGateway> paymentGateways = new HashMap<Long, PaymentGateway>();

			Map<String, Object> params = new HashMap<>();		
			params.put("status", PaymentConstant.REFUND_STATUS.REFUNDING.code);
			params.put("gatewayId", gatewayId);
			Integer count = refundPayinfoService.queryByParamCount(params);
			logger.info("gateway-{} 存在{}条正在退款的信息", gatewayId, count);
			if (count > 0) {
				int startPage = 1;
				logger.info("准备检查第{}页的退款信息...", startPage);
				do {
					PageInfo<RefundPayinfo> pageInfo = refundPayinfoService.queryPageByParam(params, new PageInfo<RefundPayinfo>(startPage, 100));
					List<RefundPayinfo> refundPayinfos = pageInfo.getRows();
					if (refundPayinfos.isEmpty()) {
						break;
					}

					for (RefundPayinfo refundPayinfoObj : refundPayinfos) {
						Long gwId = refundPayinfoObj.getGatewayId();
						if (gwId.equals(1L)) {
							// 支付宝没有这种方式
							continue;
						}
						PaymentGateway gatewayObj = getPaymentGateway(paymentGateways, gwId);
						if (gatewayObj == null) {
							logger.error("＃＃＃＃＃＃＃＃取不到id为{}的支付方式！", gwId);
							continue;
						}

						IPayPlatformService payPlatformService = payPlatformServices.get(gatewayObj.getGatewayCode() + "PayPlatformService");
						TradeStatusResult queryRefundStatus = payPlatformService.queryRefundStatus(refundPayinfoObj);
						if (queryRefundStatus == null) {
							logger.info("退款-{}查询不到状态！", refundPayinfoObj.getPayRefundId());
							continue;
						}
						if (queryRefundStatus.isSuccess()) {
							refundPayinfoObj.setStatus(PaymentConstant.REFUND_STATUS.REFUNDED.code);
							refundPayinfoObj.setNotified(PaymentConstant.NOTIFY_STATUS.NOTIFIED.code);
							refundPayinfoObj.setNotifyTime(new Date());
							refundPayinfoObj.setUpdateTime(new Date());
							refundPayinfoObj.setUpdateUser(gatewayObj.getGatewayCode());


							int row = refundPayinfoService.updateNotNullById(refundPayinfoObj);
							logger.info("更新了{}条退款记录：id={}", row, refundPayinfoObj.getPayRefundId());

							PaymentLog paymentLogObj = new PaymentLog(
									refundPayinfoObj.getPaymentId(),
									PaymentConstant.OBJECT_TYPE.REFUND.code,
									PaymentConstant.PAY_ACTION_NAME.REFUND_UPDATE.cnName,
									new StringBuffer("退款")
											.append(refundPayinfoObj.getBizCode())
											.append("由系统更新为已成功").toString(),
									"alipay", new Date(), refundPayinfoObj.getCreateUser());
							paymentLogObj.setPartTable(DateUtil.format(new Date(), "yy"));
							paymentLogService.insert(paymentLogObj);

							try {
								// 通知退款结果
								logger.info("订单退款回调开始：orderId={}, status={}", refundPayinfoObj.getBizCode(), refundPayinfoObj.getStatus());
								rabbitMqProducer.sendP2PMessage(PaymentConstant.REFUND_INFO_STATUS_QUEUE, refundPayinfoObj);
								logger.info("订单退款回调完成：orderId={}, status={}", refundPayinfoObj.getBizCode(), refundPayinfoObj.getStatus());
							} catch (MqClientException e) {
								logger.error("refund-{} of payment {} fail to send mq message!", refundPayinfoObj.getPayRefundId(), refundPayinfoObj.getPaymentId());
							}
						}
					}

					startPage++;
				} while (true);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Transactional
	@Override
	public void checkPaymentStatus() {
		try {
			// 支付方式信息
			Map<Long, PaymentGateway> paymentGateways = new HashMap<Long, PaymentGateway>();

			// 只找最近15分钟的
			final int recentMinites = 15;
			Date updatedAfter = new Date(System.currentTimeMillis() - 1000L * 60 * recentMinites);
			logger.info("createdAfter={}", updatedAfter);
			List<PaymentInfo> recentPaymentInfos = paymentInfoService.selectRecentPaymentInfos(updatedAfter,PaymentConstant.PAYMENT_STATUS.PAYED.code);
			int size = recentPaymentInfos.size();
			logger.info("最近{}分钟有{}条支付信息还没有被回调", recentMinites, size);
			if (size > 0) {
				for (PaymentInfo paymentInfo : recentPaymentInfos) {
					Long gwId = paymentInfo.getGatewayId();
					PaymentGateway gatewayObj = getPaymentGateway(paymentGateways, gwId);
					if (gatewayObj == null) {
						logger.error("＃＃＃＃＃＃＃＃取不到id为{}的支付方式！", gwId);
						continue;
					}

					IPayPlatformService payPlatformService = payPlatformServices.get(gatewayObj.getGatewayCode() + "PayPlatformService");
					TradeStatusResult queryPaymentStatus = payPlatformService.queryPayStatus(paymentInfo);
					if (queryPaymentStatus == null) {
						logger.info("支付信息-{}查询不到状态！", paymentInfo.getPaymentId());
						continue;
					}
					if (queryPaymentStatus.isSuccess()) {
                        final Integer payStatus = PaymentConstant.PAYMENT_STATUS.PAYED.code;
                        paymentInfo.setStatus(payStatus);
                        Date now = new Date();
                        paymentInfo.setCallbackTime(now);
                        PaymentInfo upd = new PaymentInfo();
                        upd.setPaymentId(paymentInfo.getPaymentId());
                        upd.setStatus(payStatus);
                        upd.setUpdateTime(now);
                        paymentInfo.setUpdateTime(now);
                        upd.setCallbackTime(now);
                        paymentInfo.setCallbackTime(now);
                        upd.setCallbackInfo("auto query");
                        paymentInfo.setCallbackInfo("auto query");
                        if (StringUtils.isNotEmpty(queryPaymentStatus.getTradeNo())) {
                            upd.setGatewayTradeNo(queryPaymentStatus.getTradeNo());
                            paymentInfo.setGatewayTradeNo(queryPaymentStatus.getTradeNo());
                        }
                        int row = paymentInfoService.updatePaymentByPayed(upd);
                        if (PaymentConstant.BIZ_TYPE.MERGEORDER.code.intValue() == paymentInfo.getBizType().intValue()) {
                        	PaymentInfo query = new PaymentInfo();
            				query.setPrtPaymentId(upd.getPaymentId());
            				List<PaymentInfo> paymentInfoList = paymentInfoService.queryByObject(query);
                            for (int i = 0; i < paymentInfoList.size(); i++) {
                                PaymentInfo subPaymentInfo = new PaymentInfo();
                                subPaymentInfo.setGatewayTradeNo(upd.getGatewayTradeNo());
                                subPaymentInfo.setGatewayId(gwId);
                                subPaymentInfo.setUpdateTime(now);
                                subPaymentInfo.setStatus(upd.getStatus());
                                subPaymentInfo.setTradeType(paymentInfo.getTradeType());
                                subPaymentInfo.setPaymentId(paymentInfoList.get(i).getPaymentId());
                                subPaymentInfo.setCallbackTime(paymentInfo.getCallbackTime());
                                subPaymentInfo.setCallbackInfo(paymentInfo.getCallbackInfo());
                                if (PaymentConstant.PAYMENT_STATUS.PAYED.code.intValue() == paymentInfo
                                    .getStatus().intValue())
                                    subPaymentInfo.setPaymentTradeNo(paymentInfo.getPaymentTradeNo());
                                paymentInfoService.updateNotNullById(subPaymentInfo);
                            }
                        }
                        logger.info("{}条记录已更新：id={}", row, paymentInfo.getPaymentId());

                        PaymentLog paymentLogDO = new PaymentLog(paymentInfo.getPaymentId(),
                            PaymentConstant.OBJECT_TYPE.PAYMENT.code, "系统查到已支付", new StringBuffer("订单-")
                                .append(paymentInfo.getBizCode()).append("已支付").toString(), paymentInfo.getActionIp(),
                            now, paymentInfo.getCreateUser());
                        paymentLogDO.setPartTable(DateUtil.format(now, "YYMM"));
                        
                        
                      //团购券逻辑 ---start
                        logger.info("生成的兑换码信息开始------"+"OrderType.BUY_COUPONS.code:"+OrderType.BUY_COUPONS.code+"----paymentInfoObj.getOrderType().intValue():"+paymentInfo.getOrderType().intValue());
                		
                        logger.info("OrderType.BUY_COUPONS.code==paymentInfoObj.getOrderType().intValue()-----:"+(OrderType.BUY_COUPONS.code==paymentInfo.getOrderType().intValue()));
                		if(OrderType.BUY_COUPONS.code==paymentInfo.getOrderType().intValue()){//团购券入口
                			Long orderCode=paymentInfo.getBizCode();//订单编号
                			if(PaymentConstant.PAYMENT_STATUS.PAYED.code.equals(paymentInfo.getStatus())){//支付成功
                				String reddemCodes=orderRedeemItemService.generateAndSaveRedeemInfo( orderCode);
                				logger.info("生成的兑换码信息："+reddemCodes);
                			}
                		}
                		  //团购券逻辑 ---end
                        paymentLogService.insert(paymentLogDO);
                        if (row > 0) {

                            if (PaymentConstant.BIZ_TYPE.MERGEORDER.code.intValue() == paymentInfo.getBizType().intValue()) {
                            	PaymentInfo query = new PaymentInfo();
                				query.setPrtPaymentId(paymentInfo.getPaymentId());
                				List<PaymentInfo> paymentInfoList = paymentInfoService.queryByObject(query);
                                for (PaymentInfo subPaymentInfo : paymentInfoList) {
                                    try {
                                        // 通知支付结果
                                        logger.info("系统通知支付结果开始：orderId={}", subPaymentInfo.getBizCode());
                                        if(PaymentConstant.BIZ_TYPE.DSS.code.equals(subPaymentInfo.getBizType())){
                        					rabbitMqProducer.sendP2PMessage(MqMessageConstant.REGISTER_PROMOTER_SUCCESS, subPaymentInfo);
                        				}else{
                        					rabbitMqProducer.sendP2PMessage(PaymentConstant.PAYMENT_INFO_STATUS_QUEUE, subPaymentInfo);
                        				}
                                        logger.info("系统通知支付结果开始：orderId={}, status={}", subPaymentInfo.getBizCode(),
                                            subPaymentInfo);
                                    } catch (MqClientException e) {
                                        logger.error("biz code{} fail to send mq message!",
                                            subPaymentInfo.getBizCode());
                                    }
                                }
                            } else {
                                try {
                                    // 通知支付结果
                                    logger.info("系统通知支付结果开始：orderId={}", paymentInfo.getBizCode());
                                    if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfo.getBizType())){
                    					rabbitMqProducer.sendP2PMessage(MqMessageConstant.REGISTER_PROMOTER_SUCCESS, paymentInfo);
                    				}else{
                    					rabbitMqProducer.sendP2PMessage(PaymentConstant.PAYMENT_INFO_STATUS_QUEUE, paymentInfo);
                    				}
                                    logger.info("系统通知支付结果开始：orderId={}, status={}", paymentInfo.getBizCode(),
                                        paymentInfo);
                                } catch (MqClientException e) {
                                    logger.error("biz code{} fail to send mq message!", paymentInfo.getBizCode());
                                }
                            }

//                            payPlatformService.operateAfterCallbackSuccess(paymentInfo);
                        }
                    }
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private PaymentGateway getPaymentGateway(
			Map<Long, PaymentGateway> paymentGateways, Long gatewayId) {
		PaymentGateway gw = paymentGateways.get(gatewayId);
		if (gw == null) {
			try {
				gw = paymentGatewayService.queryById(gatewayId);
				paymentGateways.put(gatewayId, gw);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return gw;
	}

	@Override
	public ServiceStatusDto checkServiceStatus() {
		ServiceStatusDto status = new ServiceStatusDto();
		try {
			PaymentGateway gw = paymentGatewayService.queryById(1L);
			status.setMasterDbOk(gw != null);
		} catch (Exception e) {
			status.setMasterDbOk(false);
		}
		try {
			PaymentGateway gw = paymentGatewayService.queryById(1L);
			status.setSlaveDbOk(gw != null);
		} catch (Exception e) {
			status.setSlaveDbOk(false);
		}
		return status;
	}

	@Override
	public String getPropertiesValue(String key) {
		return settings.getProperty(key);
	}

	@Override
	public PaymentInfo queryPaymentInfoByBizCode(String bizCode) {
		Map<String, Object> params = new HashMap<>();
		params.put("bizCode", bizCode);
		List<PaymentInfo> paymentInfoObjs = paymentInfoService.queryByParamNotEmpty(params);
		if (CollectionUtils.isNotEmpty(paymentInfoObjs)) {
			return paymentInfoObjs.get(0);
		}
		return null;
	}

	@Override
	public PaymentInfo checkPaymentByGateway(Long paymentId, String gateway) {
        try {
            PaymentInfo paymentInfoDO = paymentInfoService.queryById(paymentId);
            if (null != paymentInfoDO) {
                PaymentGateway query = new PaymentGateway();
                query.setGatewayCode(gateway);
                List<PaymentGateway> paymentGateways = paymentGatewayService.queryByObject(query);
                PaymentGateway paymentGateway = null;
                if (CollectionUtils.isNotEmpty(paymentGateways)) {
                    paymentGateway = paymentGateways.get(0);
                }
                Long channelId = paymentInfoDO.getChannelId();
                Long orderType = paymentInfoDO.getOrderType();
                if (channelId == null || channelId.intValue() == 0) {
                    channelId = -1L;
                    orderType = -1L;
                }
                List<PaymentChannelGateway> paymentChannelGatewayDOs = paymentChannelGatewayService.queryChannelGateways(orderType, channelId);

                if (paymentGateway != null && CollectionUtils.isNotEmpty(paymentChannelGatewayDOs)) {
                    for (PaymentChannelGateway paymentChannelGatewayDO : paymentChannelGatewayDOs) {
                        if (paymentGateway.getGatewayId().intValue() == paymentChannelGatewayDO.getGatewayId()
                            .intValue()) {
                            paymentInfoDO.setGatewayCode(paymentGateway.getGatewayCode());
                        }
                    }
                }
            }

            return paymentInfoDO;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

	@Override
	public AppPayData getAppPayDataByParams(String gateway, Long paymentInfoId, boolean forSDK, String userId, Map<String, Object> params) {
		IPayPlatformService payPlatformService = payPlatformServices.get(gateway + "PayPlatformService");
        return payPlatformService.getAppPayDataByParams(paymentInfoId, forSDK, userId, gateway, params);
    }
	
	@Override
	public ResultInfo<PaymentInfo> putPaymentInfoToCustoms(SubOrder subOrder, OrderConsignee consignee) {
		String bizCode = subOrder.getOrderCode().toString();
		PaymentInfo paymentInfo = queryPaymentInfoByBizCode(bizCode);
		if (paymentInfo == null) {
			logger.error("[PUSH_PAYMENT_INFO][{}]支付信息不存在", bizCode);
			return new ResultInfo<>(new FailInfo("支付信息不存在"));
		}
		if (!PaymentConstant.PAYMENT_STATUS.PAYED.code.equals(paymentInfo.getStatus())) {
			logger.error("[PUSH_PAYMENT_INFO][{}]订单未支付", bizCode);
			return new ResultInfo<>(new FailInfo("订单未支付"));
		}
		if (paymentInfo.getPaymentCustomsType().intValue() == 1) { //已报关成功
			return new ResultInfo<>(paymentInfo);
		}
		PaymentGateway gatewayObj = paymentGatewayService.queryById(paymentInfo.getGatewayId());
		if (!"alipayInternational".equals(gatewayObj.getGatewayCode()) && 
				!"mergeAlipay".equals(gatewayObj.getGatewayCode()) &&
				!"weixinExternal".equals(gatewayObj.getGatewayCode())) {	
			logger.error("[PUSH_PAYMENT_INFO][{}]支付方式暂时不支持报关", bizCode);
			return new ResultInfo<>(new FailInfo("支付方式暂时不支持报关"));
		}
		ResultInfo<Boolean> putResult = null;
		paymentInfo.setGatewayCode(gatewayObj.getGatewayCode());
		if ("alipayInternational".equals(gatewayObj.getGatewayCode()) 
				|| "mergeAlipay".equals(gatewayObj.getGatewayCode())) {
			IPayPlatformService payPlatformService = payPlatformServices.get("mergeAlipayPayPlatformService");
			putResult = payPlatformService.putPaymentInfoToCustoms(paymentInfo, subOrder, consignee, getCustomsInfo(paymentInfo));
		}else if ("weixinExternal".equals(gatewayObj.getGatewayCode())) {
			IPayPlatformService payPlatformService = payPlatformServices.get("weixinExternalPayPlatformService");
			putResult = payPlatformService.putPaymentInfoToCustoms(paymentInfo, subOrder, consignee, getCustomsInfo(paymentInfo));
		}else{
			putResult = new ResultInfo<>(new FailInfo("报关失败"));
		}
		if (!putResult.isSuccess()) {
			logger.error("[PUSH_PAYMENT_INFO][{}]报关失败：{}", bizCode, putResult.getMsg().getDetailMessage());
			return new ResultInfo<>(putResult.getMsg());
		}
		return new ResultInfo<>(paymentInfoService.queryById(paymentInfo.getPaymentId()));
	}
	
	//获取报关参数
	private CustomsInfo getCustomsInfo(PaymentInfo paymentInfo){
		Long channelId = paymentInfo.getChannelId();
		boolean isSendCeb = Boolean.valueOf(getPropertiesValue("XG.CC.isSendCeb")); //推送总署
		if (isSendCeb && ClearanceChannelsEnum.HANGZHOU.id.equals(channelId)){ //杭州保税区全部报送海关总署
			channelId = ClearanceChannelsEnum.ZONGSHU.id;
		}
		return customsInfoService.getCustomsInfo(paymentInfo.getGatewayId(), channelId);
	}
}
