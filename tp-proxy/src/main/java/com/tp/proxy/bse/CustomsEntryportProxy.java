package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.CustomsEntryport;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.ICustomsEntryportService;
/**
 * 通关参数 - 口岸代码代理层
 * @author szy
 *
 */
@Service
public class CustomsEntryportProxy extends BaseProxy<CustomsEntryport>{

	@Autowired
	private ICustomsEntryportService customsEntryportService;

	@Override
	public IBaseService<CustomsEntryport> getService() {
		return customsEntryportService;
	}
}
