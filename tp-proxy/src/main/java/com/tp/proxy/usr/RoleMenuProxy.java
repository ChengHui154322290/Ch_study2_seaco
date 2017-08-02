package com.tp.proxy.usr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.usr.RoleMenu;
import com.tp.model.usr.SysMenu;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.usr.IRoleMenuService;
import com.tp.service.usr.ISysMenuService;
import com.tp.util.StringUtil;
/**
 * 角色 菜单关系表代理层
 * @author szy
 *
 */
@Service
public class RoleMenuProxy extends BaseProxy<RoleMenu>{

	@Autowired
	private IRoleMenuService roleMenuService;
	@Autowired
	private ISysMenuService sysMenuService;

	@Override
	public IBaseService<RoleMenu> getService() {
		return roleMenuService;
	}

	public String getRoleMenus(Long roleId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("roleId", roleId);
		String returnValue = "";
		List<RoleMenu> list = this.roleMenuService.queryByParam(params);
		if (null == list || list.isEmpty())
			return returnValue;
		for (RoleMenu rm : list) {
			returnValue += "," + rm.getSysMenuId();
		}

		return returnValue;
	}

	public void setRoleMenu(Long roleId, String sysMenuIds) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("roleId", roleId);
		roleMenuService.deleteByParam(params);
		if (StringUtil.isNullOrEmpty(sysMenuIds)) return;
		List<Long> menuIds = new ArrayList<Long>();
		Map<Long,Long> map = new HashMap<Long, Long>();
		
		for (String s : sysMenuIds.split(",")) {
			map.put(Long.parseLong(s), Long.parseLong(s));
		}
		for (Map.Entry<Long,Long> entry : map.entrySet()) {
			   menuIds.add(entry.getValue());
		}
		List<SysMenu> menuList = this.sysMenuService.selectByIds(menuIds);
		if(null == menuList|| menuList.isEmpty()) return;
		for (SysMenu menu : menuList) {
			this.roleMenuService.insert(new RoleMenu(roleId, menu.getId()));
		}
		
	}
	
	
	public List<Long> selectSysMenuIds(Long roleId){
		RoleMenu rDo = new RoleMenu();
		rDo.setRoleId(roleId);
		return this.roleMenuService.selectSysMenuIds(rDo);
		
	}
}
