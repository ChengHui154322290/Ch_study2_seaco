package com.tp.service.mem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mem.WhiteInfoDao;
import com.tp.model.mem.WhiteInfo;
import com.tp.service.BaseService;
import com.tp.service.mem.IWhiteInfoService;

@Service
public class WhiteInfoService extends BaseService<WhiteInfo> implements IWhiteInfoService {

	@Autowired
	private WhiteInfoDao whiteInfoDao;
	
	@Override
	public BaseDao<WhiteInfo> getDao() {
		return whiteInfoDao;
	}

}
