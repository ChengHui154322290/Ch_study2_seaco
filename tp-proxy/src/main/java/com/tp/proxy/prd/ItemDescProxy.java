package com.tp.proxy.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.prd.ItemDesc;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemDescService;
/**
 * 商品介绍代理层
 * @author szy
 *
 */
@Service
public class ItemDescProxy extends BaseProxy<ItemDesc>{

	@Autowired
	private IItemDescService itemDescService;

	@Override
	public IBaseService<ItemDesc> getService() {
		return itemDescService;
	}
}
