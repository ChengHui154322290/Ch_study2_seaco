package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.DistrictZipAreaCode;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.IDistrictZipAreaCodeService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class DistrictZipAreaCodeProxy extends BaseProxy<DistrictZipAreaCode>{

	@Autowired
	private IDistrictZipAreaCodeService districtZipAreaCodeService;

	@Override
	public IBaseService<DistrictZipAreaCode> getService() {
		return districtZipAreaCodeService;
	}
}
