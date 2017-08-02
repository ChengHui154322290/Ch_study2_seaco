package com.tp.proxy.usr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.usr.UserDetail;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.usr.IUserDetailService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class UserDetailProxy extends BaseProxy<UserDetail>{

	@Autowired
	private IUserDetailService userDetailService;

	@Override
	public IBaseService<UserDetail> getService() {
		return userDetailService;
	}
}
