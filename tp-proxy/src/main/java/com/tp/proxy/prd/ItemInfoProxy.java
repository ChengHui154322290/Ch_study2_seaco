package com.tp.proxy.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.prd.ItemInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemInfoService;
/**
 * 商品基础信息代理层
 * @author szy
 *
 */
@Service
public class ItemInfoProxy extends BaseProxy<ItemInfo>{

	@Autowired
	private IItemInfoService itemInfoService;

	@Override
	public IBaseService<ItemInfo> getService() {
		return itemInfoService;
	}
}
