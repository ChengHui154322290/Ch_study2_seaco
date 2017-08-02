package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.FlagshipInfoDao;
import com.tp.model.sup.FlagshipInfo;
import com.tp.service.BaseService;
import com.tp.service.sup.IFlagshipInfoService;

@Service
public class FlagshipInfoService extends BaseService<FlagshipInfo> implements IFlagshipInfoService {

	@Autowired
	private FlagshipInfoDao flagshipInfoDao;
	
	@Override
	public BaseDao<FlagshipInfo> getDao() {
		return flagshipInfoDao;
	}

}
