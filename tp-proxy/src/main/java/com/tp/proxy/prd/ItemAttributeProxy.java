package com.tp.proxy.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.prd.ItemAttribute;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemAttributeService;
/**
 * 商品的属性组代理层
 * @author szy
 *
 */
@Service
public class ItemAttributeProxy extends BaseProxy<ItemAttribute>{

	@Autowired
	private IItemAttributeService itemAttributeService;

	@Override
	public IBaseService<ItemAttribute> getService() {
		return itemAttributeService;
	}
}
