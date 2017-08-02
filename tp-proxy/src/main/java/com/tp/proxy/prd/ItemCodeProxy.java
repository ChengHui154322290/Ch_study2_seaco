package com.tp.proxy.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.prd.ItemCode;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemCodeService;
/**
 * 商品编码生成表代理层
 * @author szy
 *
 */
@Service
public class ItemCodeProxy extends BaseProxy<ItemCode>{

	@Autowired
	private IItemCodeService itemCodeService;

	@Override
	public IBaseService<ItemCode> getService() {
		return itemCodeService;
	}
}
