package com.tp.proxy.fin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.fin.SettleRefundItemInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.fin.ISettleRefundItemInfoService;
/**
 * 退款、补偿结算项表代理层
 * @author szy
 *
 */
@Service
public class SettleRefundItemInfoProxy extends BaseProxy<SettleRefundItemInfo>{

	@Autowired
	private ISettleRefundItemInfoService settleRefundItemInfoService;

	@Override
	public IBaseService<SettleRefundItemInfo> getService() {
		return settleRefundItemInfoService;
	}
}
