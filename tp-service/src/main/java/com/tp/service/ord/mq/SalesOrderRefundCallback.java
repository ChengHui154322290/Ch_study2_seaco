package com.tp.service.ord.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.pay.PaymentRefundInfoConstant;
import com.tp.model.pay.RefundPayinfo;
import com.tp.mq.MqMessageCallBack;
import com.tp.service.ord.IRefundInfoService;

@Service
public class SalesOrderRefundCallback implements MqMessageCallBack {
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IRefundInfoService refundInfoService;
	
	@Override
	public boolean execute(Object o) {
		
		if (o instanceof RefundPayinfo) {
			RefundPayinfo refundPayinfo = (RefundPayinfo) o;
			log.info("order-{} refund {} succeed!", refundPayinfo.getBizCode(), refundPayinfo.getPayRefundId());
			try {
				if (PaymentRefundInfoConstant.REFUND_STATUS.REFUNDED.code.equals(refundPayinfo.getStatus()))
					refundInfoService.operateAfterRefund(refundPayinfo.getBizCode(), true);
				else if (PaymentRefundInfoConstant.REFUND_STATUS.FAIL_REFUND.code.equals(refundPayinfo.getStatus()))
					refundInfoService.operateAfterRefund(refundPayinfo.getBizCode(), false);
				return Boolean.TRUE;
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				return Boolean.FALSE;
			}
		} else {
			log.error("!!!!RefundInfo expected, but {} got!", o);
		}
		return Boolean.FALSE;
	}
}
