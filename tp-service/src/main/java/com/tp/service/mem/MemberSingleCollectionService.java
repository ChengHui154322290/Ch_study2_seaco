package com.tp.service.mem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mem.MemberSingleCollectionDao;
import com.tp.model.mem.MemberSingleCollection;
import com.tp.service.BaseService;
import com.tp.service.mem.IMemberSingleCollectionService;

@Service
public class MemberSingleCollectionService extends BaseService<MemberSingleCollection> implements IMemberSingleCollectionService {

	@Autowired
	private MemberSingleCollectionDao memberSingleCollectionDao;
	
	@Override
	public BaseDao<MemberSingleCollection> getDao() {
		return memberSingleCollectionDao;
	}

}
