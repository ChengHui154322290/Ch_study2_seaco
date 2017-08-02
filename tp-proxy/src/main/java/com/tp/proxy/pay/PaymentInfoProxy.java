package com.tp.proxy.pay;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.pay.PayPaymentSimpleDTO;
import com.tp.model.pay.PaymentGateway;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.pay.RefundPayinfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.pay.IPaymentGatewayService;
import com.tp.service.pay.IPaymentInfoService;
import com.tp.service.pay.IPaymentService;
import com.tp.service.pay.IRefundPayinfoService;
/**
 * 支付信息表代理层
 * @author szy
 *
 */
@Service
public class PaymentInfoProxy extends BaseProxy<PaymentInfo>{

	@Autowired
	private IPaymentInfoService paymentInfoService;
	@Autowired
	private IPaymentGatewayService paymentGatewayService;
	@Autowired
	private IPaymentService paymentService;
	@Autowired
	private IRefundPayinfoService refundPayinfoService;

	@Override
	public IBaseService<PaymentInfo> getService() {
		return paymentInfoService;
	}

	public List<PaymentGateway> queryPaymentGatewayList() {
		List<PaymentGateway> paymentGatewayList = paymentGatewayService.selectAllOrderbyParentId();
		return paymentGatewayList;
	}

	public RefundPayinfo queryRefundPayinfoByRefundNo(Long refundNo) {
		RefundPayinfo refundPayinfo = refundPayinfoService.selectByRefundNo(refundNo);
		return refundPayinfo;
	}

	public PaymentInfo queryPaymentInfoByOrderNo(Long orderNo) {
		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setBizCode(orderNo);
		paymentInfo = paymentInfoService.queryPaymentInfoByBizCode(orderNo);
		return paymentInfo;
	}

	public ResultInfo<Boolean> refund(String gateway, Long refundNo) {
		try{
			paymentService.refund(gateway, refundNo);
			return new ResultInfo<Boolean>(Boolean.TRUE);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,gateway,refundNo);
			return new ResultInfo<>(failInfo);
		}
	}

	public ResultInfo<List<PaymentInfo>> batchInsert(List<PayPaymentSimpleDTO> paymentInfoList) {
		try{
			return new ResultInfo<List<PaymentInfo>>(paymentInfoService.insertPaymentInfoList(paymentInfoList));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,paymentInfoList);
			return new ResultInfo<List<PaymentInfo>>(failInfo);
		}
	}
}
