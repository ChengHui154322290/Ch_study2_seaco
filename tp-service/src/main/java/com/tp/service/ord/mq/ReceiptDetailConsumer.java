package com.tp.service.ord.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.stg.OrderInvoiceDTO;
import com.tp.mq.MqMessageCallBack;
import com.tp.service.ord.local.IReceiptDetailLocalService;
import com.tp.util.JsonFormatUtils;

/**
 * 发票明细MQ消费
 * 
 * @author szy
 * @version 0.0.1
 */
@Service
public class ReceiptDetailConsumer implements MqMessageCallBack {
	
	private static final Logger log = LoggerFactory.getLogger(ReceiptDetailConsumer.class);
	
	@Autowired
	private IReceiptDetailLocalService receiptDetailLocalService;

	@Override
	public boolean execute(Object obj) {
		if (log.isDebugEnabled()) {
			log.debug("发票明细MQ消费开始：入参[{}]", JsonFormatUtils.format(obj));
		}
		
		if (validateParam(obj)) {
			try {
				receiptDetailLocalService.insertOrUpdateByOrderInvoiceDTO((OrderInvoiceDTO) obj);
			} catch (Exception e) {
				log.error("发票明细MQ消费错误：", e);
			}
		}
		return true;
	}

	// 入参校验
	private boolean validateParam(Object obj) {
		if (null == obj) {
			log.error("发票明细MQ消费：入参为空");
			return false;
		}
		
		if (!(obj instanceof OrderInvoiceDTO)) {
			log.error("发票明细MQ消费：入参类型错误");
			return false;
		}
		
		OrderInvoiceDTO invoice = (OrderInvoiceDTO) obj;
		
		if (log.isDebugEnabled()) {
			log.debug("发票明细MQ消费：参数[{}]", JsonFormatUtils.format(invoice));
		}
		
		if (null == invoice.getOrderNo()
				|| null == invoice.getInvoiceCode()
				|| null == invoice.getTitle()
				|| null == invoice.getAmount()
				) {
			log.error("发票明细MQ消费：入参数据有null属性");
			return false;
		}
		
		return true;
	}

}
