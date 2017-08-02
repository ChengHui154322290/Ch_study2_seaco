package com.tp.proxy.wms.logistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.wms.WaybillDetailLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.wms.logistics.IWaybillDetailLogService;
/**
 * 运单号详情日志代理层
 * @author szy
 *
 */
@Service
public class WaybillDetailLogProxy extends BaseProxy<WaybillDetailLog>{

	@Autowired
	private IWaybillDetailLogService waybillDetailLogService;

	@Override
	public IBaseService<WaybillDetailLog> getService() {
		return waybillDetailLogService;
	}
}
