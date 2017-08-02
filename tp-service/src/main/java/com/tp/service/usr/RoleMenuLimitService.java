package com.tp.service.usr;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.usr.RoleMenuLimitDao;
import com.tp.model.usr.RoleMenuLimit;
import com.tp.service.BaseService;
import com.tp.service.usr.IRoleMenuLimitService;

@Service
public class RoleMenuLimitService extends BaseService<RoleMenuLimit> implements IRoleMenuLimitService {

	@Autowired
	private RoleMenuLimitDao roleMenuLimitDao;
	
	@Override
	public BaseDao<RoleMenuLimit> getDao() {
		return roleMenuLimitDao;
	}
	@Override
	public List<RoleMenuLimit> selectByIds(List<Long> ids){
		if(null == ids || ids.isEmpty()) return null;
		return roleMenuLimitDao.selectByIds(ids);
	}
	
	@Override
	public List<Long> selectSysMenuLimitIds(RoleMenuLimit roleMenuLimit){
		return this.roleMenuLimitDao.selectSysMenuLimitIds(roleMenuLimit);
	}
}
