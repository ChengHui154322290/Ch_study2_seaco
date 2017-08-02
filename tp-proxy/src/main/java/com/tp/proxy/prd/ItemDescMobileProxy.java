package com.tp.proxy.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.prd.ItemDescMobile;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemDescMobileService;
/**
 * 商品介绍手机版代理层
 * @author szy
 *
 */
@Service
public class ItemDescMobileProxy extends BaseProxy<ItemDescMobile>{

	@Autowired
	private IItemDescMobileService itemDescMobileService;

	@Override
	public IBaseService<ItemDescMobile> getService() {
		return itemDescMobileService;
	}
}
