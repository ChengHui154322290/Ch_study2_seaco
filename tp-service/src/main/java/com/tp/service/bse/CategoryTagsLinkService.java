package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.CategoryTagsLinkDao;
import com.tp.model.bse.CategoryTagsLink;
import com.tp.service.BaseService;
import com.tp.service.bse.ICategoryTagsLinkService;

@Service
public class CategoryTagsLinkService extends BaseService<CategoryTagsLink> implements ICategoryTagsLinkService {

	@Autowired
	private CategoryTagsLinkDao categoryTagsLinkDao;
	
	@Override
	public BaseDao<CategoryTagsLink> getDao() {
		return categoryTagsLinkDao;
	}

}
