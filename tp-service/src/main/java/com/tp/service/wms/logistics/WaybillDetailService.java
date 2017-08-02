package com.tp.service.wms.logistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.wms.logistics.WaybillDetailDao;
import com.tp.model.wms.WaybillDetail;
import com.tp.service.BaseService;
import com.tp.service.wms.logistics.IWaybillDetailService;

@Service
public class WaybillDetailService extends BaseService<WaybillDetail> implements IWaybillDetailService {
	
	@Autowired
	private WaybillDetailDao waybillDetailDao;
	
	
	@Override
	public BaseDao<WaybillDetail> getDao() {
		return waybillDetailDao;
	}
}
