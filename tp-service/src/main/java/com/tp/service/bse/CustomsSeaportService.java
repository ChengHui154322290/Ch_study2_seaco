package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.CustomsSeaportDao;
import com.tp.model.bse.CustomsSeaport;
import com.tp.service.BaseService;
import com.tp.service.bse.ICustomsSeaportService;

@Service
public class CustomsSeaportService extends BaseService<CustomsSeaport> implements ICustomsSeaportService {

	@Autowired
	private CustomsSeaportDao customsSeaportDao;
	
	@Override
	public BaseDao<CustomsSeaport> getDao() {
		return customsSeaportDao;
	}

}
