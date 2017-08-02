package com.tp.test.search;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.tp.test.base.BaseTest;

public class SearchTest extends BaseTest{

	// 搜索导航
	@Test
	public void navigation() throws Exception {
		mockMvc.perform((MockMvcRequestBuilders.get("/search/navigation.htm")
				.accept(MediaType.APPLICATION_JSON)))
				.andExpect(status().isOk()).andDo(print());
	}
	
	// 搜索结果 - 筛选
	@Test
	public void condition() throws Exception {
		mockMvc.perform((MockMvcRequestBuilders.get("/search/condition.htm")
				.param("apptype", "wap")
				.param("key", "奶粉")
				.accept(MediaType.APPLICATION_JSON)))
				.andExpect(status().isOk()).andDo(print());
	}
	
	// 执行搜索
	@Test
	public void search() throws Exception {
		mockMvc.perform((MockMvcRequestBuilders.get("/search.htm")
				.param("apptype", "wap")
				.param("key", "奶粉")
				.param("country_id", "94")
				.param("brand_id", "87")
				.accept(MediaType.APPLICATION_JSON))).andExpect(status().isOk()).andDo(print());
	}
}
