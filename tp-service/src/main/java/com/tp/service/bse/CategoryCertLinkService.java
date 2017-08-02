package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.CategoryCertLinkDao;
import com.tp.model.bse.CategoryCertLink;
import com.tp.service.BaseService;
import com.tp.service.bse.ICategoryCertLinkService;

@Service
public class CategoryCertLinkService extends BaseService<CategoryCertLink> implements ICategoryCertLinkService {

	@Autowired
	private CategoryCertLinkDao categoryCertLinkDao;
	
	@Override
	public BaseDao<CategoryCertLink> getDao() {
		return categoryCertLinkDao;
	}

}
