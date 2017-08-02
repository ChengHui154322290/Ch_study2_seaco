package com.tp.service.mem;

import com.tp.model.mem.MemberInfo;
import com.tp.service.IBaseService;

public interface IHhbUsergroupService extends IBaseService<MemberInfo>{

	String getAppLoginToken(MemberInfo memberInfo);
	
	Long hhbRegister(MemberInfo memberInfo,String address,String contact);
}
