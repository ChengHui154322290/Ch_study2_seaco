package com.tp.test.mkt;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.tp.test.base.BaseTest;

public class ChannelPromoteTest extends BaseTest{

	@Test
	public void save() throws Exception {
		mockMvc.perform((MockMvcRequestBuilders.get("/mkt/channel/save.htm")
				.param("channel", "MS")
				.param("uuid", "xxxxxxdwewewewwewewewewwwwewe")
				.accept(MediaType.APPLICATION_JSON))).andExpect(status().isOk())
				.andDo(print());
	}
}
