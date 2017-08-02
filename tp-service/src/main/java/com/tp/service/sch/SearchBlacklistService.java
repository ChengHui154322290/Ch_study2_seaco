package com.tp.service.sch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sch.SearchBlacklistDao;
import com.tp.model.sch.SearchBlacklist;
import com.tp.service.BaseService;
import com.tp.service.sch.ISearchBlacklistService;

@Service
public class SearchBlacklistService extends BaseService<SearchBlacklist> implements ISearchBlacklistService {

	@Autowired
	private SearchBlacklistDao searchBlacklistDao;
	
	@Override
	public BaseDao<SearchBlacklist> getDao() {
		return searchBlacklistDao;
	}

}
