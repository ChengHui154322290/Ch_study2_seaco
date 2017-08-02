package com.tp.proxy.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.prd.ItemTags;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemTagsService;
/**
 * 商品标签信息表，仅做新增和删除，不做修改代理层
 * @author szy
 *
 */
@Service
public class ItemTagsProxy extends BaseProxy<ItemTags>{

	@Autowired
	private IItemTagsService itemTagsService;

	@Override
	public IBaseService<ItemTags> getService() {
		return itemTagsService;
	}
}
