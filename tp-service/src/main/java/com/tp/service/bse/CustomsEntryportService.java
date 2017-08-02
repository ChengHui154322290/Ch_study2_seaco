package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.CustomsEntryportDao;
import com.tp.model.bse.CustomsEntryport;
import com.tp.service.BaseService;
import com.tp.service.bse.ICustomsEntryportService;

@Service
public class CustomsEntryportService extends BaseService<CustomsEntryport> implements ICustomsEntryportService {

	@Autowired
	private CustomsEntryportDao customsEntryportDao;
	
	@Override
	public BaseDao<CustomsEntryport> getDao() {
		return customsEntryportDao;
	}

}
