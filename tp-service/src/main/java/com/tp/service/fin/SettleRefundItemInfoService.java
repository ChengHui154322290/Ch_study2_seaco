package com.tp.service.fin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.fin.SettleRefundItemInfoDao;
import com.tp.model.fin.SettleRefundItemInfo;
import com.tp.service.BaseService;
import com.tp.service.fin.ISettleRefundItemInfoService;

@Service
public class SettleRefundItemInfoService extends BaseService<SettleRefundItemInfo> implements ISettleRefundItemInfoService {

	@Autowired
	private SettleRefundItemInfoDao settleRefundItemInfoDao;
	
	@Override
	public BaseDao<SettleRefundItemInfo> getDao() {
		return settleRefundItemInfoDao;
	}

}
