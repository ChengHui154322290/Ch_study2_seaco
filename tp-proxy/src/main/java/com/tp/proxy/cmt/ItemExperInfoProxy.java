package com.tp.proxy.cmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.cmt.ItemExperInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cmt.IItemExperInfoService;
/**
 * 体验商品信息代理层
 * @author szy
 *
 */
@Service
public class ItemExperInfoProxy extends BaseProxy<ItemExperInfo>{

	@Autowired
	private IItemExperInfoService itemExperInfoService;

	@Override
	public IBaseService<ItemExperInfo> getService() {
		return itemExperInfoService;
	}
}
