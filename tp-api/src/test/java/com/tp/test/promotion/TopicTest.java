package com.tp.test.promotion;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.tp.test.base.BaseTest;
/**
 * 专题
 * @author zhuss
 * @2016年1月4日 下午7:19:52
 */
public class TopicTest extends BaseTest{

	// 专题详情
	@Test
	public void getTopicDetail() throws Exception {
		mockMvc.perform(
				(MockMvcRequestBuilders.get("/topic/detail.htm")
						.param("tid", "570")
						.accept(MediaType.APPLICATION_JSON)))
				.andExpect(status().isOk()).andDo(print());
	}
	 
	// 专题商品列表
	@Test
	public void getTopicItemList() throws Exception {
		mockMvc.perform(
				(MockMvcRequestBuilders.get("/topic/products.htm").param("tid",
						"568").accept(MediaType.APPLICATION_JSON)))
				.andExpect(status().isOk()).andDo(print());
	}
}
