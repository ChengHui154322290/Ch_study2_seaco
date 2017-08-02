package com.tp.service.usr;

import java.util.List;

import com.tp.model.usr.SysMenuLimit;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 权限表接口
  */
public interface ISysMenuLimitService extends IBaseService<SysMenuLimit>{

	List<SysMenuLimit> selectByIds(List<Long> ids);

}
