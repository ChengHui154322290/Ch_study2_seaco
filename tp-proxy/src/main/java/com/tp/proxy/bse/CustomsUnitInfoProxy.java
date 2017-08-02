package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.CustomsUnitInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.ICustomsUnitInfoService;
/**
 * 对接海关数据计量单位信息表代理层
 * @author szy
 *
 */
@Service
public class CustomsUnitInfoProxy extends BaseProxy<CustomsUnitInfo>{

	@Autowired
	private ICustomsUnitInfoService customsUnitInfoService;

	@Override
	public IBaseService<CustomsUnitInfo> getService() {
		return customsUnitInfoService;
	}
}
