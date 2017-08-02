package com.tp.dao.mem;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.mem.MemberInfo;

public interface MemberInfoDao extends BaseDao<MemberInfo> {

	List<MemberInfo> selectByIds(List<Long> ids);

	MemberInfo selectLastUser();

	/**
	 * 根据会员ID更新所关联的推广员是谁
	 * @param memberId
	 * @param promoterId
	 * @return
	 */
	Integer updatePromoterIdByMemberId(MemberInfo memberInfo);

}
