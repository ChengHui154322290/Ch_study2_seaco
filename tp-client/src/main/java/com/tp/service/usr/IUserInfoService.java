package com.tp.service.usr;

import java.util.List;

import com.tp.common.vo.PageInfo;
import com.tp.model.usr.UserInfo;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 接口
  */
public interface IUserInfoService extends IBaseService<UserInfo>{

	UserInfo selectById(Long id);

	Long save(UserInfo userInfo);

	UserInfo findByLoginName(String loginName);

	PageInfo<UserInfo> findListPageInfoFazzy(UserInfo userInfo);

	PageInfo<UserInfo> findListPageInfoByParamsForFazzy(UserInfo userInfo,
			Integer page, Integer size);

	List<UserInfo> findByDepartmentIds(List<Long> deptIds);

	List<UserInfo> selectByIds(List<Long> ids);

}
