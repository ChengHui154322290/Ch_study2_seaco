package com.tp.service.usr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.usr.UserRoleDao;
import com.tp.model.usr.UserRole;
import com.tp.service.BaseService;
import com.tp.service.usr.IUserRoleService;

@Service
public class UserRoleService extends BaseService<UserRole> implements IUserRoleService {

	@Autowired
	private UserRoleDao userRoleDao;
	
	@Override
	public BaseDao<UserRole> getDao() {
		return userRoleDao;
	}

}
