package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.CustomsUnitLinkDao;
import com.tp.model.bse.CustomsUnitLink;
import com.tp.service.BaseService;
import com.tp.service.bse.ICustomsUnitLinkService;

@Service
public class CustomsUnitLinkService extends BaseService<CustomsUnitLink> implements ICustomsUnitLinkService {

	@Autowired
	private CustomsUnitLinkDao customsUnitLinkDao;
	
	@Override
	public BaseDao<CustomsUnitLink> getDao() {
		return customsUnitLinkDao;
	}

}
