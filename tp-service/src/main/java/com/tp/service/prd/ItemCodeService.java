package com.tp.service.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.prd.ItemCodeDao;
import com.tp.model.prd.ItemCode;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemCodeService;

@Service
public class ItemCodeService extends BaseService<ItemCode> implements IItemCodeService {

	@Autowired
	private ItemCodeDao itemCodeDao;
	
	@Override
	public BaseDao<ItemCode> getDao() {
		return itemCodeDao;
	}

}
