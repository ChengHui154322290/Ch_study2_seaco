package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.CustomsSeaport;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.ICustomsSeaportService;
/**
 * 通关参数 - 指运港代理层
 * @author szy
 *
 */
@Service
public class CustomsSeaportProxy extends BaseProxy<CustomsSeaport>{

	@Autowired
	private ICustomsSeaportService customsSeaportService;

	@Override
	public IBaseService<CustomsSeaport> getService() {
		return customsSeaportService;
	}
}
