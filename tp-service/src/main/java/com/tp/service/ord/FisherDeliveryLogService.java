package com.tp.service.ord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.FisherDeliveryLogDao;
import com.tp.model.ord.FisherDeliveryLog;
import com.tp.service.BaseService;
import com.tp.service.ord.IFisherDeliveryLogService;

@Service
public class FisherDeliveryLogService extends BaseService<FisherDeliveryLog> implements IFisherDeliveryLogService {

	@Autowired
	private FisherDeliveryLogDao fisherDeliveryLogDao;
	
	@Override
	public BaseDao<FisherDeliveryLog> getDao() {
		return fisherDeliveryLogDao;
	}
	@Override
	public void addSendOrderLog(List<FisherDeliveryLog> fisherDeliveryLogList) {
		fisherDeliveryLogDao.batchInsert(fisherDeliveryLogList);
	}
}
