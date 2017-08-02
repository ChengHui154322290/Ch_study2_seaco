package com.tp.test.wx;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.tp.test.base.BaseTest;

/**
 * 微信 - 认证单元测试
 * @author zhuss
 * @2016年1月16日 下午4:46:11
 */
public class OauthTest extends BaseTest{

	// 获取CODE
	@Test
	public void getCode() throws Exception {
		mockMvc.perform(
				(MockMvcRequestBuilders.get("/wx/oauth/getcode.htm").accept(MediaType.APPLICATION_JSON)))
				.andExpect(status().isOk()).andDo(print());
	}
	
	// 通过config接口注入权限验证配置
	@Test
	public void config() throws Exception {
		mockMvc.perform((MockMvcRequestBuilders.get("/wx/oauth/config.htm")
				.param("url", "http://m.51seaco.com/item.htm?tid=165233&sku=02060500170101")
				.accept(MediaType.APPLICATION_JSON)))
				.andExpect(status().isOk()).andDo(print());
	}
}
