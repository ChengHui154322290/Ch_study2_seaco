package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.ReceiptDetail;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IReceiptDetailService;
/**
 * 发票明细表代理层
 * @author szy
 *
 */
@Service
public class ReceiptDetailProxy extends BaseProxy<ReceiptDetail>{

	@Autowired
	private IReceiptDetailService receiptDetailService;

	@Override
	public IBaseService<ReceiptDetail> getService() {
		return receiptDetailService;
	}
}
