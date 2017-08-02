package com.tp.service.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.prd.ItemInfoDao;
import com.tp.model.prd.ItemInfo;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemInfoService;

@Service
public class ItemInfoService extends BaseService<ItemInfo> implements IItemInfoService {

	@Autowired
	private ItemInfoDao itemInfoDao;
	
	@Override
	public BaseDao<ItemInfo> getDao() {
		return itemInfoDao;
	}

}
