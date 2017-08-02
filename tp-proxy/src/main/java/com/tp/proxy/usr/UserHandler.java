package com.tp.proxy.usr;

import javax.annotation.PostConstruct;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

import com.tp.model.usr.UserInfo;

/**
 * 
 * @author szy
 *
 */
@Component
public class UserHandler {
	
	public static UserHandler handler;
	
	@PostConstruct
	public void init(){
		handler = this;
	}
	
    /**
     * 获取操作员信息
     * @return
     */
	public static UserInfo getUser(){
		UserInfo user = (UserInfo)SecurityUtils.getSubject().getPrincipal();
		return user;
    }
	
}
