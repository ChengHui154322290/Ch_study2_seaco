package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.FrontCategoryLogDao;
import com.tp.model.bse.FrontCategoryLog;
import com.tp.service.BaseService;
import com.tp.service.bse.IFrontCategoryLogService;

@Service
public class FrontCategoryLogService extends BaseService<FrontCategoryLog> implements IFrontCategoryLogService {

	@Autowired
	private FrontCategoryLogDao frontCategoryLogDao;
	
	@Override
	public BaseDao<FrontCategoryLog> getDao() {
		return frontCategoryLogDao;
	}

}
