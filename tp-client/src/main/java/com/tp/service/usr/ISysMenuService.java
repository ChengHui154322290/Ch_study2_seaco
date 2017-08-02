package com.tp.service.usr;

import java.util.List;

import com.tp.model.usr.SysMenu;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 菜单表接口
  */
public interface ISysMenuService extends IBaseService<SysMenu>{

	SysMenu save(SysMenu sysMenu);

	List<SysMenu> findListByParentIds(List<SysMenu> list);

	List<SysMenu> findListByIds(List<Long> list);

	List<SysMenu> findParentMenu();

	List<SysMenu> selectByIds(List<Long> ids);

	List<SysMenu> queryByParamForUrlIsNull(SysMenu sysMenu);

}
