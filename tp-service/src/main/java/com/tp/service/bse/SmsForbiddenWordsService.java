package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.SmsForbiddenWordsDao;
import com.tp.model.bse.SmsForbiddenWords;
import com.tp.service.BaseService;
import com.tp.service.bse.ISmsForbiddenWordsService;

@Service
public class SmsForbiddenWordsService extends BaseService<SmsForbiddenWords> implements ISmsForbiddenWordsService {

	@Autowired
	private SmsForbiddenWordsDao smsForbiddenWordsDao;
	
	@Override
	public BaseDao<SmsForbiddenWords> getDao() {
		return smsForbiddenWordsDao;
	}

}
