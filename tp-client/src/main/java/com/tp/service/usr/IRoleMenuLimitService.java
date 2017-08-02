package com.tp.service.usr;

import java.util.List;

import com.tp.model.usr.RoleMenuLimit;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 接口
  */
public interface IRoleMenuLimitService extends IBaseService<RoleMenuLimit>{

	List<RoleMenuLimit> selectByIds(List<Long> ids);

	List<Long> selectSysMenuLimitIds(RoleMenuLimit roleMenuLimit);

}
