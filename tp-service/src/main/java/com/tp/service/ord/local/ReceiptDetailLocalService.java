package com.tp.service.ord.local;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tp.common.vo.ord.OrderErrorCodes;
import com.tp.dto.stg.OrderInvoiceDTO;
import com.tp.exception.OrderServiceException;
import com.tp.model.ord.ReceiptDetail;
import com.tp.model.ord.SubOrder;
import com.tp.service.ord.IReceiptDetailService;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.local.IReceiptDetailLocalService;

/**
 * 发票明细本地服务
 * 
 * @author szy
 * @version 0.0.1
 */
@Service("receiptDetailLocalService")
public class ReceiptDetailLocalService implements IReceiptDetailLocalService {

	private static final Logger log = LoggerFactory.getLogger(ReceiptDetailLocalService.class);

	@Autowired
	private IReceiptDetailService receiptDetailService;
	@Autowired
	private ISubOrderService subOrderService;

	@Override
	@Transactional
	public ReceiptDetail insertOrUpdateByOrderInvoiceDTO(OrderInvoiceDTO invoice) {
		Assert.notNull(invoice);
		
		ReceiptDetail receipt = convert(invoice);
		if (isExistent(receipt)) {	// 是否已存在该订单的发票
			receiptDetailService.updateBySubOrderCode(receipt);
			return receipt;
		} else {
			return receiptDetailService.insert(receipt);
		}
	}

	// 是否已存在该订单的发票
	private boolean isExistent(ReceiptDetail receipt) {
		ReceiptDetail receiptList = receiptDetailService.selectListBySubOrderCode(receipt.getParentOrderCode());
		return null!=receiptList;
	}

	// OrderInvoiceDTO 转 ReceiptDetail
	private ReceiptDetail convert(OrderInvoiceDTO orderInvoiceDTO) {
		ReceiptDetail receiptDetail = new ReceiptDetail();
		
		SubOrder sub = subOrderService.selectOneByCode(orderInvoiceDTO.getOrderNo());
		if (null == sub) {
			log.error("订单编号[{}]不存在", orderInvoiceDTO.getOrderNo());
			throw new OrderServiceException(OrderErrorCodes.INVALID_ORDER_CODE);
		}

		receiptDetail.setAmount(orderInvoiceDTO.getAmount());
		receiptDetail.setCreateTime(new Date());
		receiptDetail.setParentOrderCode(sub.getParentOrderCode());
		receiptDetail.setReceiptCode(orderInvoiceDTO.getInvoiceCode());
		receiptDetail.setReceiptNo(orderInvoiceDTO.getInvoiceNo());
		receiptDetail.setReceiptTime(orderInvoiceDTO.getInvoiceTime());
		receiptDetail.setTitle(orderInvoiceDTO.getTitle());
		receiptDetail.setType(orderInvoiceDTO.getInvoiceType());
		
		return receiptDetail;
	}
}
