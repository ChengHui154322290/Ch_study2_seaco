package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.RelateDao;
import com.tp.model.mmp.Relate;
import com.tp.service.BaseService;
import com.tp.service.mmp.IRelateService;

@Service
public class RelateService extends BaseService<Relate> implements IRelateService {

	@Autowired
	private RelateDao relateDao;
	
	@Override
	public BaseDao<Relate> getDao() {
		return relateDao;
	}

}
