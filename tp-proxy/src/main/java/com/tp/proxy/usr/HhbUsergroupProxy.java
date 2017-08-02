package com.tp.proxy.usr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mem.MemberInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mem.IHhbUsergroupService;

@Service
public class HhbUsergroupProxy extends BaseProxy<MemberInfo>{

	@Autowired
	private IHhbUsergroupService hhbUsergroupService;
	
	@Override
	public IBaseService<MemberInfo> getService() {
		// TODO Auto-generated method stub
		return hhbUsergroupService;
	}

	public String getAppLoginToken(MemberInfo memberInfo){
		return hhbUsergroupService.getAppLoginToken(memberInfo);
	}
	public Long hhbRegister(MemberInfo memberInfo,String address,String contact){
		return hhbUsergroupService.hhbRegister(memberInfo,address,contact);
	}
}
