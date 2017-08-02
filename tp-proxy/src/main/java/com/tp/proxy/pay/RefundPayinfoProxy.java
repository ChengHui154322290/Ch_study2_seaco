package com.tp.proxy.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.pay.RefundPayinfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.pay.IRefundPayinfoService;
/**
 * 退款支付明细表代理层
 * @author szy
 *
 */
@Service
public class RefundPayinfoProxy extends BaseProxy<RefundPayinfo>{

	@Autowired
	private IRefundPayinfoService refundPayinfoService;

	@Override
	public IBaseService<RefundPayinfo> getService() {
		return refundPayinfoService;
	}
}
