package com.tp.proxy.fin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.fin.SettleOrderItem;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.fin.ISettleOrderItemService;
/**
 * 结算订单商品项表代理层
 * @author szy
 *
 */
@Service
public class SettleOrderItemProxy extends BaseProxy<SettleOrderItem>{

	@Autowired
	private ISettleOrderItemService settleOrderItemService;

	@Override
	public IBaseService<SettleOrderItem> getService() {
		return settleOrderItemService;
	}
}
