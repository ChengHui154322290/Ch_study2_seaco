package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.CustomsDistrictInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.ICustomsDistrictInfoService;
/**
 * 对接海关数据地区信息表代理层
 * @author szy
 *
 */
@Service
public class CustomsDistrictInfoProxy extends BaseProxy<CustomsDistrictInfo>{

	@Autowired
	private ICustomsDistrictInfoService customsDistrictInfoService;

	@Override
	public IBaseService<CustomsDistrictInfo> getService() {
		return customsDistrictInfoService;
	}
}
