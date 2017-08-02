package com.tp.proxy.pay;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.pay.PayPaymentSimpleDTO;
import com.tp.dto.pay.cbdata.CallbackResultDto;
import com.tp.dto.pay.postdata.AliPayRefundPostData;
import com.tp.dto.pay.postdata.PayPostData;
import com.tp.dto.pay.query.TradeStatusQuery;
import com.tp.dto.pay.servicestatus.ServiceStatusDto;
import com.tp.exception.ServiceException;
import com.tp.model.pay.PaymentGateway;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.pay.RefundPayinfo;
import com.tp.result.pay.TradeStatusResult;
import com.tp.service.pay.IAppPaymentService;
import com.tp.service.pay.IPaymentChannelGatewayService;
import com.tp.service.pay.IPaymentGatewayService;
import com.tp.service.pay.IPaymentService;
import com.tp.service.pay.IRefundPayinfoService;

@Service(value = "payServiceProxy")
public class PayServiceProxy {
	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private IPaymentService paymentService;
	@Autowired
	private IAppPaymentService appPaymentService;
	@Autowired
	private IPaymentGatewayService paymentGatewayService;
	@Autowired
	private IRefundPayinfoService refundPayinfoService;
	@Autowired
	private IPaymentChannelGatewayService paymentChannelGatewayService;

	public boolean validateOrder(PayPaymentSimpleDTO payQuery) {

		return true;
	}

	public PaymentInfo checkPayment(Long paymentId) {
		return paymentService.checkPayment(paymentId);
	}

	public TradeStatusResult queryTradeStatus(TradeStatusQuery tradeStatusQuery) {
		return paymentService.queryTradeStatus(tradeStatusQuery);
	}

	public PayPostData getPostData(String gateway, Long paymentInfoId) throws ServiceException {
		return paymentService.getPostData(gateway, paymentInfoId);
	}

	public CallbackResultDto callback(String gateway,
			Map<String, String> parameterMap, boolean isAsyn) throws ServiceException {
		return paymentService.callback(gateway, parameterMap);
	}

	public String refundCallback(String gateway, Map<String, String> params) throws ServiceException {
		return paymentService.refundCallback(gateway, params);
	}

	public String refund(String gateway, Long refundNo) throws ServiceException {
		return paymentService.refund(gateway, refundNo);
	}

	public PaymentInfo queryPaymentInfoById(Long paymentId) {
		PaymentInfo paymentInfoDO = paymentService.selectById(paymentId);
		if (null != paymentInfoDO) {
			PaymentGateway paymentGateway = paymentGatewayService.queryById(paymentInfoDO.getGatewayId());
			if(paymentGateway != null)
				paymentInfoDO.setGatewayCode(paymentGateway.getGatewayCode());
		}
		return paymentInfoDO;
	}

	public List<PaymentInfo> queryPaymentInfoByIds(List<Long> paymentIds) {
		List<PaymentInfo> paymentInfoDOs = paymentService.selectByIds(paymentIds);
		if (null != paymentInfoDOs) {

			for (PaymentInfo pido : paymentInfoDOs) {
				Long gatewayId = pido.getGatewayId();
				Long orderType = pido.getOrderType();
				Long channelId = pido.getChannelId();

				List<PaymentGateway> availableGateways = paymentGatewayService.queryPaymentGateWayLists(orderType, channelId);
				PaymentGateway paymentGateway = null;
				boolean foundGateway = false;
				for (PaymentGateway gw : availableGateways) {
					if (gw.getGatewayId().equals(gatewayId)) {
						paymentGateway = gw;
						foundGateway = true;
						break;
					}
					if (paymentGateway == null) {
						paymentGateway = gw;
					}
				}
				if (paymentGateway != null) {
					if (!foundGateway) {
						pido.setGatewayId(paymentGateway.getGatewayId());
					}
					pido.setGatewayCode(paymentGateway.getGatewayCode());
					pido.setGatewayName(paymentGateway.getGatewayName());
				}
				pido.setAvailableGateways(availableGateways);
			}
		}
		return paymentInfoDOs;
	}

	public List<RefundPayinfo> selectRecentRefund2Confirm(
			List<Long> refundNos) {
		List<RefundPayinfo> payinfoDOs = refundPayinfoService
				.selectByRefundNos(refundNos);
		return payinfoDOs;
	}

	public PaymentInfo try2Pay(Long paymentId, Long gatewayId) {
		PaymentInfo paymentInfoDO = paymentService.try2Pay(paymentId,
				gatewayId);
		return paymentInfoDO;
	}

	// public OrderPayDto getOrderPayDto(Long paymentInfoId){
	// OrderPayDto orderPayDto =
	// appPaymentService.getOrderPayDto(paymentInfoId);
	// return orderPayDto;
	// }

	public List<PaymentGateway> listAllOrderbyParentId() {
		return paymentGatewayService.selectAllOrderbyParentId();
	}

	public PaymentGateway selectGateway(Long gatewayId) {
		return paymentGatewayService.queryById(gatewayId);
	}

	public AliPayRefundPostData getAlipayRefundPostdata() {
		return paymentService.getAlipayRefundPostdata();
	}

	// public void callbackBizSystem(String bizCode,Integer bizType){
	// //先直连，等消息完成后再使用MQ
	//
	// orderService.operateAfterSuccessPay(bizCode);
	// }

	public ServiceStatusDto checkServiceStatus() {
		return paymentService.checkServiceStatus();
	}

	public File getRefundFile(List<Long> refundNos) {

		return refundPayinfoService.getRefundFile(refundNos);
	}
	public String getPropertiesValue(String key){
		return paymentService.getPropertiesValue(key);
	}

	public PaymentInfo queryPaymentInfoByBizCode(String bizCode) {
		return paymentService.queryPaymentInfoByBizCode(bizCode);
	}
}
