package com.tp.proxy.usr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.model.usr.RoleInfo;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.usr.IRoleInfoService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class RoleInfoProxy extends BaseProxy<RoleInfo>{

	@Autowired
	private IRoleInfoService roleInfoService;

	@Override
	public IBaseService<RoleInfo> getService() {
		return roleInfoService;
	}
	public PageInfo<RoleInfo> getAll(Integer pageNo,Integer pageSize){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", Constant.DEFAULTED.YES);
		return roleInfoService.queryPageByParam(params, new PageInfo<RoleInfo>(pageNo,pageSize));
	}
	
	public List<RoleInfo> getAll(){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", Constant.DEFAULTED.YES);
		return roleInfoService.queryByParam(params);
	}
	
	public Long save(RoleInfo role){
		UserInfo user = UserHandler.getUser();
		if(null  == role.getId()){
			role.setCreateUser(user.getUserName());
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("name", role.getName());
			List<RoleInfo> roleList =  roleInfoService.queryByParam(params);
			if(null != roleList && !roleList.isEmpty())return role.getId();
		}
		role.setStatus(Constant.DEFAULTED.YES);
		role.setUpdateUser(user.getUserName());
		return roleInfoService.save(role);
	}
	
	
	public RoleInfo findById(Long id){
		return roleInfoService.queryById(id);
	}
}
