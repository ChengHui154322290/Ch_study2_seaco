package com.tp.proxy.wms.logistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.wms.WaybillInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.wms.logistics.IWaybillInfoService;
/**
 * 运单报关及发运代理层
 * @author szy
 *
 */
@Service
public class WaybillInfoProxy extends BaseProxy<WaybillInfo>{

	@Autowired
	private IWaybillInfoService waybillInfoService;

	@Override
	public IBaseService<WaybillInfo> getService() {
		return waybillInfoService;
	}
}
