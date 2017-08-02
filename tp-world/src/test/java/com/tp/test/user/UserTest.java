package com.tp.test.user;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.tp.m.query.user.QueryUser;
import com.tp.m.util.JsonUtil;
import com.tp.test.base.BaseTest;

/**
 * 帐户相关单元测试
 * @author zhuss
 * @2016年1月3日 下午3:09:32
 */
public class UserTest extends BaseTest{

	// 帐户 - 登录
	@Test
	public void logon() throws Exception {
		QueryUser user = new QueryUser();
		user.setApptype("wx");
		//user.setUniontype("1");
		//user.setUnionval("oNU-xs1EPqo3G1LeG2teycJd4VJU");
		user.setLoginname("15800917996");
		user.setPwd("999999");
		//user.setToken("39bc4aa8ccef0054583f4031f1049f89");
		String json = JsonUtil.convertObjToStr(user);
		mockMvc.perform(
				(post("/user/logon.htm")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 帐户 - 获取验证码
	@Test
	public void getCaptcha() throws Exception {
		QueryUser user = new QueryUser();
		user.setTel("13761373570");
		user.setType("4");
		String json = JsonUtil.convertObjToStr(user);
		mockMvc.perform(
				(post("/user/getcaptcha.htm").accept(
						MediaType.APPLICATION_JSON).contentType(
						MediaType.APPLICATION_JSON).content(json.getBytes())))
				.andExpect(status().isOk()).andDo(print());
	}
	
	// 帐户 - 注册
	@Test
	public void regist() throws Exception {
		QueryUser user = new QueryUser();
		user.setLoginname("15800917996");
		user.setPwd("123456");
		user.setCaptcha("405384");
		user.setApptype("wap");
		// user.setToken("d5cf60e2df9d0d2db7aee4a734eb81ac");
		String json = JsonUtil.convertObjToStr(user);
		mockMvc.perform(
				(post("/user/regist.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 帐户 - 修改密码
	@Test
	public void modifypwd() throws Exception {
		QueryUser user = new QueryUser();
		user.setTel("15800917996");
		user.setPwd("111111");
		user.setCaptcha("312221");
	    user.setToken("d5cf60e2df9d0d2db7aee4a734eb81ac");
		String json = JsonUtil.convertObjToStr(user);
		mockMvc.perform(
				(post("/user/modifypwd.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 帐户 - 退出
	@Test
	public void logout() throws Exception {
		QueryUser user = new QueryUser();
		user.setToken("836fcd2e2309f4fcae98eb5afb3a123e");
		user.setUniontype("1");
		user.setUnionval("oNU-xs1EPqo3G1LeG2teycJd4VJU");
		String json = JsonUtil.convertObjToStr(user);
		mockMvc.perform(
				(post("/user/logout.htm").accept(
						MediaType.APPLICATION_JSON).contentType(
						MediaType.APPLICATION_JSON).content(json.getBytes())))
				.andExpect(status().isOk()).andDo(print());
	}
	
	// 帐户 - 角标数量
	@Test
	public void supCount() throws Exception {
		QueryUser user = new QueryUser();
		user.setToken("d5cf60e2df9d0d2db7aee4a734eb81ac");
		String json = JsonUtil.convertObjToStr(user);
		mockMvc.perform(
				(post("/user/supcount.htm").accept(
						MediaType.APPLICATION_JSON).contentType(
						MediaType.APPLICATION_JSON).content(json.getBytes())))
				.andExpect(status().isOk()).andDo(print());
	}
}
