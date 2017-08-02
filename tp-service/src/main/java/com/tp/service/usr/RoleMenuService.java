package com.tp.service.usr;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.usr.RoleMenuDao;
import com.tp.model.usr.RoleMenu;
import com.tp.service.BaseService;
import com.tp.service.usr.IRoleMenuService;

@Service
public class RoleMenuService extends BaseService<RoleMenu> implements IRoleMenuService {

	@Autowired
	private RoleMenuDao roleMenuDao;
	
	@Override
	public BaseDao<RoleMenu> getDao() {
		return roleMenuDao;
	}
	@Override
	public List<RoleMenu> selectByIds(List<Long> ids){
		if(null == ids || ids.isEmpty()) return null;
		return roleMenuDao.selectByIds(ids);
	}
	
	@Override
	public List<Long> selectSysMenuIds(RoleMenu roleMenuDO){
		return this.roleMenuDao.selectSysMenuIds(roleMenuDO);
	}
}
