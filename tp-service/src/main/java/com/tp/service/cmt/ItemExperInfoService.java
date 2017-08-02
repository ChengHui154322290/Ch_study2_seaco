package com.tp.service.cmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.cmt.ItemExperInfoDao;
import com.tp.model.cmt.ItemExperInfo;
import com.tp.service.BaseService;
import com.tp.service.cmt.IItemExperInfoService;

@Service
public class ItemExperInfoService extends BaseService<ItemExperInfo> implements IItemExperInfoService {

	@Autowired
	private ItemExperInfoDao itemExperInfoDao;
	
	@Override
	public BaseDao<ItemExperInfo> getDao() {
		return itemExperInfoDao;
	}

}
