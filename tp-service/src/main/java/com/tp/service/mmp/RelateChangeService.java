package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.RelateChangeDao;
import com.tp.model.mmp.RelateChange;
import com.tp.service.BaseService;
import com.tp.service.mmp.IRelateChangeService;

@Service
public class RelateChangeService extends BaseService<RelateChange> implements IRelateChangeService {

	@Autowired
	private RelateChangeDao relateChangeDao;

	@Override
	public BaseDao<RelateChange> getDao() {
		return relateChangeDao;
	}

}
