package com.tp.service.prd;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.DAOConstant;
import com.tp.dao.prd.ItemDescDao;
import com.tp.datasource.ContextHolder;
import com.tp.model.prd.ItemDesc;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemDescService;

@Service
public class ItemDescService extends BaseService<ItemDesc> implements IItemDescService {

	@Autowired
	private ItemDescDao itemDescDao;
	
	@Override
	public BaseDao<ItemDesc> getDao() {
		return itemDescDao;
	}

	@Override
	public ItemDesc selectByDetailIdFromMaster(Long detailId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		return super.queryUniqueByParams(params);
	}
}
