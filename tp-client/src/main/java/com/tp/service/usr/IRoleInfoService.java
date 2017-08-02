package com.tp.service.usr;

import com.tp.model.usr.RoleInfo;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 接口
  */
public interface IRoleInfoService extends IBaseService<RoleInfo>{

	Long save(RoleInfo roleInfo);

}
