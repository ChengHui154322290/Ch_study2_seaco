package com.tp.proxy.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.prd.ItemSkuArt;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemSkuArtService;
/**
 * 商品通关信息表代理层
 * @author szy
 *
 */
@Service
public class ItemSkuArtProxy extends BaseProxy<ItemSkuArt>{

	@Autowired
	private IItemSkuArtService itemSkuArtService;

	@Override
	public IBaseService<ItemSkuArt> getService() {
		return itemSkuArtService;
	}
}
