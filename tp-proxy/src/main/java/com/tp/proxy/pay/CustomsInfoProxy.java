package com.tp.proxy.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.pay.CustomsInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.pay.ICustomsInfoService;
/**
 * 海关信息代理层
 * @author szy
 *
 */
@Service
public class CustomsInfoProxy extends BaseProxy<CustomsInfo>{

	@Autowired
	private ICustomsInfoService customsInfoService;

	@Override
	public IBaseService<CustomsInfo> getService() {
		return customsInfoService;
	}
}
