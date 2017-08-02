package com.tp.proxy.usr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.usr.UserRole;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.usr.IUserRoleService;
/**
 * 用户角色关系表代理层
 * @author szy
 *
 */
@Service
public class UserRoleProxy extends BaseProxy<UserRole>{

	@Autowired
	private IUserRoleService userRoleService;

	@Override
	public IBaseService<UserRole> getService() {
		return userRoleService;
	}
	public void save(Long roleId,Long userId){
		UserRole uRoleInfo = new UserRole();
		uRoleInfo.setUserId(userId);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		List<UserRole> list = userRoleService.queryByParam(params);
		
		if(null != list && !list.isEmpty()) {
			UserRole oUr =  list.get(0);
			userRoleService.deleteById(oUr.getId());
		}
		uRoleInfo.setRoleId(roleId);
		userRoleService.insert(uRoleInfo);
		
	}
	
	
	public Long getRoleIdByUserId(Long userId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		List<UserRole> list = userRoleService.queryByParam(params);
		return null != list && !list.isEmpty()?list.get(0).getRoleId():null;
	}
}
