package com.tp.service.mmp.groupbuy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.GroupbuyJoinDao;
import com.tp.model.mmp.GroupbuyJoin;
import com.tp.service.BaseService;
import com.tp.service.mmp.groupbuy.IGroupbuyJoinService;

@Service
public class GroupbuyJoinService extends BaseService<GroupbuyJoin> implements IGroupbuyJoinService {

	@Autowired
	private GroupbuyJoinDao groupbuyJoinDao;
	
	@Override
	public BaseDao<GroupbuyJoin> getDao() {
		return groupbuyJoinDao;
	}

}
