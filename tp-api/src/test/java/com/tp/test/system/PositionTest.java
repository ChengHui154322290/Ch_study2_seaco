package com.tp.test.system;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.tp.test.base.BaseTest;

/**
 * 位置单元测试
 * @author Administrator
 *
 */
public class PositionTest extends BaseTest{

	@Test
	public void getprovlist() throws Exception {
		mockMvc.perform(
				(MockMvcRequestBuilders.get("/position/getprovlist.htm")
						.accept(MediaType.APPLICATION_JSON)))
				.andExpect(status().isOk()).andDo(print());
	}
	
	@Test
	public void getarealist() throws Exception {
		mockMvc.perform(
				(MockMvcRequestBuilders.get("/position/getarealist.htm")
						.param("id", "42687")
						.accept(MediaType.APPLICATION_JSON)))
				.andExpect(status().isOk()).andDo(print());
	}
}
