package com.tp.service.prd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.DAOConstant;
import com.tp.dao.prd.ItemAttributeDao;
import com.tp.datasource.ContextHolder;
import com.tp.model.prd.ItemAttribute;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemAttributeService;

@Service
public class ItemAttributeService extends BaseService<ItemAttribute> implements IItemAttributeService {

	@Autowired
	private ItemAttributeDao itemAttributeDao;
	
	@Override
	public BaseDao<ItemAttribute> getDao() {
		return itemAttributeDao;
	}

	@Override
	public List<ItemAttribute> selectAttrListByDetailIdFromMaster(Long detailId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("detailId", detailId);
		params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
		return super.queryByParam(params);
	}

}
