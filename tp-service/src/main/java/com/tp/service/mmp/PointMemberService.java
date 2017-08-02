package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.PointMemberDao;
import com.tp.model.mmp.PointMember;
import com.tp.service.BaseService;
import com.tp.service.mmp.IPointMemberService;

@Service
public class PointMemberService extends BaseService<PointMember> implements IPointMemberService {

	@Autowired
	private PointMemberDao pointMemberDao;
	
	@Override
	public BaseDao<PointMember> getDao() {
		return pointMemberDao;
	}

	public PointMember queryPointMemberByMemberId(Long memberId){
		return pointMemberDao.queryByMemberId(memberId);
	}
}
