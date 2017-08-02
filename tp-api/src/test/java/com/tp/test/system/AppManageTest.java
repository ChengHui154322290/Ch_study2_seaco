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
public class AppManageTest extends BaseTest{
	
	@Test
	public void checknew() throws Exception {
		mockMvc.perform(
				(MockMvcRequestBuilders.get("/app/checknew.htm")
						.param("apptype", "ios")
						.param("appversion", "1.0.0")
						.accept(MediaType.APPLICATION_JSON)))
				.andExpect(status().isOk()).andDo(print());
	}
}
