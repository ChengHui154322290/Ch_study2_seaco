package com.tp.test.user;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.tp.m.query.user.QueryUser;
import com.tp.m.query.user.QueryUserAuth;
import com.tp.m.util.JsonUtil;
import com.tp.test.base.BaseTest;

/**
 * 用户帐号安全相关单元测试
 * @author zhuss
 * @2016年1月3日 下午3:09:32
 */
public class SafetyTest extends BaseTest{
	
	// 帐户 - 绑定联合账户
	@Test
	public void bindunion() throws Exception {
		QueryUser user = new QueryUser();
		user.setToken("836fcd2e2309f4fcae98eb5afb3a123e");
		user.setTel("15800917996");
		String json = JsonUtil.convertObjToStr(user);
		mockMvc.perform(
				(post("/user/safety/bindtel.htm").accept(
						MediaType.APPLICATION_JSON).contentType(
						MediaType.APPLICATION_JSON).content(json.getBytes())))
				.andExpect(status().isOk()).andDo(print());
	}
	
	// 帐户 - 检查用户是否实名认证
	@Test
	public void checkAuth() throws Exception {
		QueryUser user = new QueryUser();
		user.setToken("xg");
		String json = JsonUtil.convertObjToStr(user);
		mockMvc.perform(
				(post("/user/safety/checkauth.htm").accept(
						MediaType.APPLICATION_JSON).contentType(
						MediaType.APPLICATION_JSON).content(json.getBytes())))
				.andExpect(status().isOk()).andDo(print());
	}
	
	// 帐户 - 实名认证
	@Test
	public void auth() throws Exception {
		QueryUserAuth user = new QueryUserAuth();
		user.setName("222");
		user.setCode("340223198610183554");
		user.setToken("xg");
		String json = JsonUtil.convertObjToStr(user);
		mockMvc.perform(
				(post("/user/safety/auth.htm").accept(
						MediaType.APPLICATION_JSON).contentType(
						MediaType.APPLICATION_JSON).content(json.getBytes())))
				.andExpect(status().isOk()).andDo(print());
	}
}
