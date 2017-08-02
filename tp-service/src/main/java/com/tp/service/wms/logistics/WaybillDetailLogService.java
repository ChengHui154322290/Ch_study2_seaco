package com.tp.service.wms.logistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.wms.logistics.WaybillDetailLogDao;
import com.tp.model.wms.WaybillDetailLog;
import com.tp.service.BaseService;
import com.tp.service.wms.logistics.IWaybillDetailLogService;

@Service
public class WaybillDetailLogService extends BaseService<WaybillDetailLog> implements IWaybillDetailLogService {

	@Autowired
	private WaybillDetailLogDao waybillDetailLogDao;
	
	@Override
	public BaseDao<WaybillDetailLog> getDao() {
		return waybillDetailLogDao;
	}

}
