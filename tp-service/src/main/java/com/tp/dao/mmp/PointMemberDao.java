package com.tp.dao.mmp;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.mmp.PointMember;

public interface PointMemberDao extends BaseDao<PointMember> {

	PointMember queryByMemberId(@Param("memberId") Long memberId); 
	
	Integer updateTotalPointByMemberId(PointMember pointMember);
}
