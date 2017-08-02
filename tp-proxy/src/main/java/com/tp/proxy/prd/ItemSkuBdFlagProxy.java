package com.tp.proxy.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.prd.ItemSkuBdFlag;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemSkuBdFlagService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class ItemSkuBdFlagProxy extends BaseProxy<ItemSkuBdFlag>{

	@Autowired
	private IItemSkuBdFlagService itemSkuBdFlagService;

	@Override
	public IBaseService<ItemSkuBdFlag> getService() {
		return itemSkuBdFlagService;
	}
}
