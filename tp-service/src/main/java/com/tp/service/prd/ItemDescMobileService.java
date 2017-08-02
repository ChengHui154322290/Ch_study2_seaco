package com.tp.service.prd;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.DAOConstant;
import com.tp.dao.prd.ItemDescMobileDao;
import com.tp.datasource.ContextHolder;
import com.tp.model.prd.ItemDescMobile;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemDescMobileService;

@Service
public class ItemDescMobileService extends BaseService<ItemDescMobile> implements IItemDescMobileService {

	@Autowired
	private ItemDescMobileDao itemDescMobileDao;
	
	@Override
	public BaseDao<ItemDescMobile> getDao() {
		return itemDescMobileDao;
	}

	@Override
	public ItemDescMobile selectByDetailFromMaster(Long detailId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		return super.queryUniqueByParams(params);
	}

}
