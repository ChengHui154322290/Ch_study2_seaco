package com.tp.service.prd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.prd.ItemDetailSpecDao;
import com.tp.datasource.ContextHolder;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemDetailSpecService;
import com.tp.util.StringUtil;

@Service
public class ItemDetailSpecService extends BaseService<ItemDetailSpec> implements IItemDetailSpecService {

	@Autowired
	private ItemDetailSpecDao itemDetailSpecDao;
	
	@Override
	public BaseDao<ItemDetailSpec> getDao() {
		return itemDetailSpecDao;
	}
	@Override
	public List<ItemDetailSpec> selectDetailSpecListByDetailIds(List<Long> ids){
		List<ItemDetailSpec>  list = new ArrayList<ItemDetailSpec>();
		if(CollectionUtils.isNotEmpty(ids)){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "detail_id in ("+StringUtil.join(ids, Constant.SPLIT_SIGN.COMMA)+")");
			list = itemDetailSpecDao.queryByParam(params);
		}
		return list;
	}


	@Override
	public List<ItemDetailSpec> selectDetailSpecListByDetailIdsFromMaster(List<Long> detialIds)  {
		List<ItemDetailSpec>  list = new ArrayList<ItemDetailSpec>();
		if(CollectionUtils.isEmpty(detialIds)){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(DAOConstant.DATA_SOURCE_KEY, ContextHolder.DATA_SOURCE_KEY.MASTER_SALE_ORDER_DATA_SOURCE);
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "detail_id in ("+StringUtil.join(detialIds, Constant.SPLIT_SIGN.COMMA)+")");
			list = itemDetailSpecDao.queryByParam(params);
		}
		return list;
	}
}
