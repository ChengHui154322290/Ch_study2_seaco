package com.tp.service.usr;

import java.util.List;

import com.tp.model.usr.RoleMenu;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 角色 菜单关系表接口
  */
public interface IRoleMenuService extends IBaseService<RoleMenu>{

	List<RoleMenu> selectByIds(List<Long> ids);

	List<Long> selectSysMenuIds(RoleMenu roleMenu);

}
