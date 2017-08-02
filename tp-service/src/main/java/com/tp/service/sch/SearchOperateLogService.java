package com.tp.service.sch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sch.SearchOperateLogDao;
import com.tp.model.sch.SearchOperateLog;
import com.tp.service.BaseService;
import com.tp.service.sch.ISearchOperateLogService;

@Service
public class SearchOperateLogService extends BaseService<SearchOperateLog> implements ISearchOperateLogService {

	@Autowired
	private SearchOperateLogDao searchOperateLogDao;
	
	@Override
	public BaseDao<SearchOperateLog> getDao() {
		return searchOperateLogDao;
	}

}
