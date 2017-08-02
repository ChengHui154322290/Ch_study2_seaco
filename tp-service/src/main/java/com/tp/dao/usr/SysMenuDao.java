package com.tp.dao.usr;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.usr.SysMenu;

public interface SysMenuDao extends BaseDao<SysMenu> {

	List<SysMenu> findListByParentIds(List<SysMenu> list);

	List<SysMenu> findListByIds(List<Long> list);

	List<SysMenu> findParentMenu();

	List<SysMenu> selectByIds(List<Long> ids);

	List<SysMenu> selectDynamicForUrlIsNull(SysMenu sysMenu);
}
