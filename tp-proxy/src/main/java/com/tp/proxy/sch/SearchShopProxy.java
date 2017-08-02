package com.tp.proxy.sch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sch.SearchShop;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sch.ISearchShopService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class SearchShopProxy extends BaseProxy<SearchShop>{

	@Autowired
	private ISearchShopService searchShopService;

	@Override
	public IBaseService<SearchShop> getService() {
		return searchShopService;
	}
}
