package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.CategoryAttributeLinkDao;
import com.tp.model.bse.CategoryAttributeLink;
import com.tp.service.BaseService;
import com.tp.service.bse.ICategoryAttributeLinkService;

@Service
public class CategoryAttributeLinkService extends BaseService<CategoryAttributeLink> implements ICategoryAttributeLinkService {

	@Autowired
	private CategoryAttributeLinkDao categoryAttributeLinkDao;
	
	@Override
	public BaseDao<CategoryAttributeLink> getDao() {
		return categoryAttributeLinkDao;
	}

}
