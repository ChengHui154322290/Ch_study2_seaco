package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.CustomsDistrictLink;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.ICustomsDistrictLinkService;
/**
 * 对接海关数据地区信息-系统地区信息关联表代理层
 * @author szy
 *
 */
@Service
public class CustomsDistrictLinkProxy extends BaseProxy<CustomsDistrictLink>{

	@Autowired
	private ICustomsDistrictLinkService customsDistrictLinkService;

	@Override
	public IBaseService<CustomsDistrictLink> getService() {
		return customsDistrictLinkService;
	}
}
