package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.CustomsUnitLink;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.ICustomsUnitLinkService;
/**
 * 对接海关数据计量单位信息-系统计量单位关联表代理层
 * @author szy
 *
 */
@Service
public class CustomsUnitLinkProxy extends BaseProxy<CustomsUnitLink>{

	@Autowired
	private ICustomsUnitLinkService customsUnitLinkService;

	@Override
	public IBaseService<CustomsUnitLink> getService() {
		return customsUnitLinkService;
	}
}
