package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.FrontCategoryLinkDao;
import com.tp.model.bse.FrontCategoryLink;
import com.tp.service.BaseService;
import com.tp.service.bse.IFrontCategoryLinkService;

@Service
public class FrontCategoryLinkService extends BaseService<FrontCategoryLink> implements IFrontCategoryLinkService {

	@Autowired
	private FrontCategoryLinkDao frontCategoryLinkDao;
	
	@Override
	public BaseDao<FrontCategoryLink> getDao() {
		return frontCategoryLinkDao;
	}

}
