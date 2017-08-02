package com.tp.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.app.SwitchInfoDao;
import com.tp.model.app.SwitchInfo;
import com.tp.service.BaseService;
import com.tp.service.app.ISwitchInfoService;

@Service
public class SwitchInfoService extends BaseService<SwitchInfo> implements ISwitchInfoService {

	@Autowired
	private SwitchInfoDao switchInfoDao;
	
	@Override
	public BaseDao<SwitchInfo> getDao() {
		return switchInfoDao;
	}

}
