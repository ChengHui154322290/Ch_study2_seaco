package com.tp.service.mem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mem.MemberTradeInfoDao;
import com.tp.model.mem.MemberTradeInfo;
import com.tp.service.BaseService;
import com.tp.service.mem.IMemberTradeInfoService;

@Service
public class MemberTradeInfoService extends BaseService<MemberTradeInfo> implements IMemberTradeInfoService {

	@Autowired
	private MemberTradeInfoDao memberTradeInfoDao;
	
	@Override
	public BaseDao<MemberTradeInfo> getDao() {
		return memberTradeInfoDao;
	}

}
