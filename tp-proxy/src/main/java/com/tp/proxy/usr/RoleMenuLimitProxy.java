package com.tp.proxy.usr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.usr.RoleMenuLimit;
import com.tp.model.usr.SysMenuLimit;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.usr.IRoleMenuLimitService;
import com.tp.service.usr.IRoleMenuService;
import com.tp.service.usr.ISysMenuLimitService;
import com.tp.service.usr.ISysMenuService;
import com.tp.util.StringUtil;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class RoleMenuLimitProxy extends BaseProxy<RoleMenuLimit>{

	@Autowired
	private IRoleMenuLimitService roleMenuLimitService;
	@Autowired
	private IRoleMenuService roleMenuService;
	@Autowired
	private ISysMenuService sysMenuService;
	@Autowired
	private ISysMenuLimitService sysMenuLimitService;
	
	@Override
	public IBaseService<RoleMenuLimit> getService() {
		return roleMenuLimitService;
	}

	public void setRoleMenuLimit(Long roleId, String sysMenuLimitIds) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("roleId", roleId);
		roleMenuLimitService.deleteByParam(params);
		if (StringUtil.isNullOrEmpty(sysMenuLimitIds)) return;
		List<Long> menuLimitIds = new ArrayList<Long>();
		for (String s : sysMenuLimitIds.split(",")) {
			menuLimitIds.add(Long.parseLong(s));
		}
		List<SysMenuLimit> menuLimitList = this.sysMenuLimitService.selectByIds(menuLimitIds);
		if(null == menuLimitList|| menuLimitList.isEmpty()) return;
		for (SysMenuLimit menu : menuLimitList) {
			roleMenuLimitService.insert(new RoleMenuLimit(roleId, menu.getId()));
		}
	}
	
	
	public String getRoleMenuLimits(Long roleId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("roleId", roleId);
		String returnValue = "";
		List<RoleMenuLimit> list = this.roleMenuLimitService.queryByParam(params);
		if (null == list || list.isEmpty())
			return returnValue;
		for (RoleMenuLimit rm : list) {
			returnValue += "," + rm.getSysMenuLimitId();
		}

		return returnValue;
	}
	
	public List<Long> selectSysMenuLimitIds(Long roleId){
		RoleMenuLimit rDo = new RoleMenuLimit();
		rDo.setRoleId(roleId);
		return this.roleMenuLimitService.selectSysMenuLimitIds(rDo);
		
	}
}
