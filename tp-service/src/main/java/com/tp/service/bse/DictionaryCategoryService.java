package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.DictionaryCategoryDao;
import com.tp.model.bse.DictionaryCategory;
import com.tp.service.BaseService;
import com.tp.service.bse.IDictionaryCategoryService;

@Service
public class DictionaryCategoryService extends BaseService<DictionaryCategory> implements IDictionaryCategoryService {

	@Autowired
	private DictionaryCategoryDao dictionaryCategoryDao;
	
	@Override
	public BaseDao<DictionaryCategory> getDao() {
		return dictionaryCategoryDao;
	}

}
