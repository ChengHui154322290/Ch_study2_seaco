package com.tp.service.sch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sch.SearchShopDao;
import com.tp.model.sch.SearchShop;
import com.tp.service.BaseService;
import com.tp.service.sch.ISearchShopService;

@Service
public class SearchShopService extends BaseService<SearchShop> implements ISearchShopService {

	@Autowired
	private SearchShopDao searchShopDao;
	
	@Override
	public BaseDao<SearchShop> getDao() {
		return searchShopDao;
	}

}
