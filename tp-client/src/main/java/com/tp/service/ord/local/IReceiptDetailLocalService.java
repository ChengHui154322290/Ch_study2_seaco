package com.tp.service.ord.local;

import com.tp.dto.stg.OrderInvoiceDTO;
import com.tp.model.ord.ReceiptDetail;



/**
 * 发票明细本地服务
 * 
 * @author szy
 * @version 0.0.1
 */
public interface IReceiptDetailLocalService {

	/**
	 * 新增或更新发票明细
	 * 
	 * @param invoice
	 * @return
	 */
	ReceiptDetail insertOrUpdateByOrderInvoiceDTO(OrderInvoiceDTO invoice);
}
