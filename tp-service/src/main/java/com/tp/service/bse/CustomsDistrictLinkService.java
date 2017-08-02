package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.CustomsDistrictLinkDao;
import com.tp.model.bse.CustomsDistrictLink;
import com.tp.service.BaseService;
import com.tp.service.bse.ICustomsDistrictLinkService;

@Service
public class CustomsDistrictLinkService extends BaseService<CustomsDistrictLink> implements ICustomsDistrictLinkService {

	@Autowired
	private CustomsDistrictLinkDao customsDistrictLinkDao;
	
	@Override
	public BaseDao<CustomsDistrictLink> getDao() {
		return customsDistrictLinkDao;
	}

}
