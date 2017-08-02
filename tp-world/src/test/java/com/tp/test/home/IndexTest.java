package com.tp.test.home;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.tp.test.base.BaseTest;

public class IndexTest extends BaseTest{

	//首页 - 广告位
	@Test
	public void getBanners() throws Exception {
		mockMvc.perform(
				(MockMvcRequestBuilders.get("/index/getbanners.htm")
						.accept(MediaType.APPLICATION_JSON)))
				.andExpect(status().isOk()).andDo(print());
	}
	
	//首页 - 今日上新
	@Test
	public void getTodayNew() throws Exception {
		mockMvc.perform(
				(MockMvcRequestBuilders.get("/index/gettodaynew.htm")
						.param("appversion", "1.1.1")
						.param("apptype", "ios")
						.param("curpage", "1")
						.accept(MediaType.APPLICATION_JSON)))
				.andExpect(status().isOk()).andDo(print());
	}
	
	// 首页 - 模块管理
	@Test
	public void queryModule() throws Exception {
		mockMvc.perform((MockMvcRequestBuilders.get("/index/module.htm")
				.accept(MediaType.APPLICATION_JSON)))
				.andExpect(status().isOk()).andDo(print());
	}
}
