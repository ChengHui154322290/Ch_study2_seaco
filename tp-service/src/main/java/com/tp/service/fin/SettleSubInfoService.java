package com.tp.service.fin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.fin.SettleSubInfoDao;
import com.tp.model.fin.SettleSubInfo;
import com.tp.service.BaseService;
import com.tp.service.fin.ISettleSubInfoService;

@Service
public class SettleSubInfoService extends BaseService<SettleSubInfo> implements ISettleSubInfoService {

	@Autowired
	private SettleSubInfoDao settleSubInfoDao;
	
	@Override
	public BaseDao<SettleSubInfo> getDao() {
		return settleSubInfoDao;
	}

}
