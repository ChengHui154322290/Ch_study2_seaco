package com.tp.dao.usr;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.usr.RoleMenu;

public interface RoleMenuDao extends BaseDao<RoleMenu> {

	List<RoleMenu> selectByIds(List<Long> ids);

	List<Long> selectSysMenuIds(RoleMenu roleMenuDO);

}
