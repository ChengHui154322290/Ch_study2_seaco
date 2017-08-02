package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.OrderPromotion;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IOrderPromotionService;
/**
 * 订单促销明细表代理层
 * @author szy
 *
 */
@Service
public class OrderPromotionProxy extends BaseProxy<OrderPromotion>{

	@Autowired
	private IOrderPromotionService orderPromotionService;

	@Override
	public IBaseService<OrderPromotion> getService() {
		return orderPromotionService;
	}
	
}
