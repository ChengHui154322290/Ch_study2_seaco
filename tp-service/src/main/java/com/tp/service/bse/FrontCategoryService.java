package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.FrontCategoryDao;
import com.tp.model.bse.FrontCategory;
import com.tp.service.BaseService;
import com.tp.service.bse.IFrontCategoryService;

@Service
public class FrontCategoryService extends BaseService<FrontCategory> implements IFrontCategoryService {

	@Autowired
	private FrontCategoryDao frontCategoryDao;
	
	@Override
	public BaseDao<FrontCategory> getDao() {
		return frontCategoryDao;
	}

}
