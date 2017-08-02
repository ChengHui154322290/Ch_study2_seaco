package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.CategorySpecGroupLinkDao;
import com.tp.model.bse.CategorySpecGroupLink;
import com.tp.service.BaseService;
import com.tp.service.bse.ICategorySpecGroupLinkService;

@Service
public class CategorySpecGroupLinkService extends BaseService<CategorySpecGroupLink> implements ICategorySpecGroupLinkService {

	@Autowired
	private CategorySpecGroupLinkDao categorySpecGroupLinkDao;
	
	@Override
	public BaseDao<CategorySpecGroupLink> getDao() {
		return categorySpecGroupLinkDao;
	}

}
