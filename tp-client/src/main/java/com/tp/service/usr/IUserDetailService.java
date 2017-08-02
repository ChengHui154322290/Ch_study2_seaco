package com.tp.service.usr;

import com.tp.model.usr.UserDetail;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 接口
  */
public interface IUserDetailService extends IBaseService<UserDetail>{

	UserDetail save(UserDetail userDetail);

	UserDetail findByUserId(Long userId);

}
