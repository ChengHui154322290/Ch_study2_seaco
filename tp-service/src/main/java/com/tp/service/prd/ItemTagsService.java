package com.tp.service.prd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.prd.ItemTagsDao;
import com.tp.model.prd.ItemTags;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemTagsService;

@Service
public class ItemTagsService extends BaseService<ItemTags> implements IItemTagsService {

	@Autowired
	private ItemTagsDao itemTagsDao;
	
	@Override
	public BaseDao<ItemTags> getDao() {
		return itemTagsDao;
	}

}
