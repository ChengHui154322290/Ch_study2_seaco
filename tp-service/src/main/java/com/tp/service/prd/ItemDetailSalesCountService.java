package com.tp.service.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.prd.ItemDetailSalesCountDao;
import com.tp.model.prd.ItemDetailSalesCount;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemDetailSalesCountService;

@Service
public class ItemDetailSalesCountService extends BaseService<ItemDetailSalesCount> implements IItemDetailSalesCountService {

	@Autowired
	private ItemDetailSalesCountDao itemDetailSalesCountDao;
	
	@Override
	public BaseDao<ItemDetailSalesCount> getDao() {
		return itemDetailSalesCountDao;
	}

}
