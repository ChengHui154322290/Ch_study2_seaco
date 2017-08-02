package com.tp.service.fin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.fin.SettleInfoDao;
import com.tp.model.fin.SettleInfo;
import com.tp.service.BaseService;
import com.tp.service.fin.ISettleInfoService;

@Service
public class SettleInfoService extends BaseService<SettleInfo> implements ISettleInfoService {

	@Autowired
	private SettleInfoDao settleInfoDao;
	
	@Override
	public BaseDao<SettleInfo> getDao() {
		return settleInfoDao;
	}

}
