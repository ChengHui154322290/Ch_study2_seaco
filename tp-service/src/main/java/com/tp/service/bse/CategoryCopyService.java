package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.CategoryCopyDao;
import com.tp.model.bse.CategoryCopy;
import com.tp.service.BaseService;
import com.tp.service.bse.ICategoryCopyService;

@Service
public class CategoryCopyService extends BaseService<CategoryCopy> implements ICategoryCopyService {

	@Autowired
	private CategoryCopyDao categoryCopyDao;
	
	@Override
	public BaseDao<CategoryCopy> getDao() {
		return categoryCopyDao;
	}

}
