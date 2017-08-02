package com.tp.service.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.prd.ItemSkuBdFlagDao;
import com.tp.model.prd.ItemSkuBdFlag;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemSkuBdFlagService;

@Service
public class ItemSkuBdFlagService extends BaseService<ItemSkuBdFlag> implements IItemSkuBdFlagService {

	@Autowired
	private ItemSkuBdFlagDao itemSkuBdFlagDao;
	
	@Override
	public BaseDao<ItemSkuBdFlag> getDao() {
		return itemSkuBdFlagDao;
	}

}
