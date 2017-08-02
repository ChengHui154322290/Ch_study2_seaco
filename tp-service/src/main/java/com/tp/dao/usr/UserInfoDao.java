package com.tp.dao.usr;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.usr.UserInfo;

public interface UserInfoDao extends BaseDao<UserInfo> {

	List<UserInfo> selectByDepartmentIds(List<Long> deptIds);

	List<UserInfo> selectByIds(List<Long> ids);

	Integer selectDynamicPageInfoQueryFazzyCount(UserInfo userInfo);

	List<UserInfo> selectDynamicPageInfoQueryFazzy(UserInfo userInfo);

}
