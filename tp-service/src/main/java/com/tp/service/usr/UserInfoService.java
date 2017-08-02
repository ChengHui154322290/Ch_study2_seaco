package com.tp.service.usr;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.PageInfo;
import com.tp.dao.usr.UserDetailDao;
import com.tp.dao.usr.UserInfoDao;
import com.tp.dao.usr.UserRoleDao;
import com.tp.model.usr.UserDetail;
import com.tp.model.usr.UserInfo;
import com.tp.model.usr.UserRole;
import com.tp.service.BaseService;
import com.tp.service.usr.IUserInfoService;

@Service
public class UserInfoService extends BaseService<UserInfo> implements IUserInfoService {

	@Autowired
	private UserInfoDao userInfoDao;
	@Autowired
	private UserDetailDao userDetailDao;
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Override
	public BaseDao<UserInfo> getDao() {
		return userInfoDao;
	}
	@Override
	public UserInfo selectById(Long id) {
		UserInfo userInfo = userInfoDao.queryById(id);
		if(null!=userInfo){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("userId", userInfo.getId());
			List<UserDetail> userDetailList = userDetailDao.queryByParam(params);
			if(null != userDetailList && !userDetailList.isEmpty()){
				userInfo.setUserDetail(userDetailList.get(0));
			}
		}
		return userInfo;
	}

	
	@Override
	public Long save(UserInfo userInfo) {
		userInfo.setUpdateTime(new Date());
		UserRole userRole = new UserRole();
		userRole.setUserId(userInfo.getId());
		userRole.setRoleId(userInfo.getRoleId());
		if(null != userInfo.getId()){//修改
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("userId", userInfo.getId());
			userRoleDao.deleteByParam(param);
			userRoleDao.insert(userRole);
			userInfoDao.updateNotNullById(userInfo);
			return userInfo.getId();
		}else{//新增
			userInfo.setCreateTime(new Date());
			userInfoDao.insert(userInfo);
			userRole.setUserId(userInfo.getId());
			userRoleDao.insert(userRole);
			return userInfo.getId();
		}
	}
	
	@Override
	public UserInfo findByLoginName(String loginName){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("loginName", loginName);
		List<UserInfo> list = userInfoDao.queryByParam(params);
		if(null == list || list.isEmpty()) return null;
		UserInfo userInfo = list.get(0);
		
		params.clear();
		params.put("userId", userInfo.getId());
		List<UserDetail> userDetailList = userDetailDao.queryByParam(params);
		if(null != userDetailList && !userDetailList.isEmpty()){
			userInfo.setUserDetail(userDetailList.get(0));
		}
		return userInfo;
	}
	
	
	@Override
	public PageInfo<UserInfo> findListPageInfoFazzy(UserInfo userInfo) {
		if (userInfo != null) {
			Integer totalCount = userInfoDao.selectDynamicPageInfoQueryFazzyCount(userInfo);
			List<UserInfo> resultList = userInfoDao.selectDynamicPageInfoQueryFazzy(userInfo);
			PageInfo<UserInfo> page = new PageInfo<UserInfo>();
			page.setPage(userInfo.getStartPage());
			page.setSize(userInfo.getPageSize());
			page.setRecords(totalCount);
			page.setRows(resultList);
			return page;
		}
		return new PageInfo<UserInfo>();
	}
	
	@Override
	public PageInfo<UserInfo> findListPageInfoByParamsForFazzy(UserInfo userInfo,Integer page,Integer size){
		if (userInfo != null) {
			userInfo.setStartPage(page);
			userInfo.setPageSize(size);
			return this.findListPageInfoFazzy(userInfo);
		}
		
		return new PageInfo<UserInfo>();
	}
	
	
	@Override
	public List<UserInfo> findByDepartmentIds(List<Long> deptIds){
		return userInfoDao.selectByDepartmentIds(deptIds);
	}
	
	@Override
	public List<UserInfo> selectByIds(List<Long> ids){
		return userInfoDao.selectByIds(ids);
	}

}
