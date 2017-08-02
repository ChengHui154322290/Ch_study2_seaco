package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.DistrictZipAreaCodeDao;
import com.tp.model.bse.DistrictZipAreaCode;
import com.tp.service.BaseService;
import com.tp.service.bse.IDistrictZipAreaCodeService;

@Service
public class DistrictZipAreaCodeService extends BaseService<DistrictZipAreaCode> implements IDistrictZipAreaCodeService {

	@Autowired
	private DistrictZipAreaCodeDao districtZipAreaCodeDao;
	
	@Override
	public BaseDao<DistrictZipAreaCode> getDao() {
		return districtZipAreaCodeDao;
	}

}
