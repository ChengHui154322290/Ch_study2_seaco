package com.tp.test.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.tp.m.query.order.QueryLogistic;
import com.tp.m.util.JsonUtil;
import com.tp.test.base.BaseTest;

/**
 * 物流相关单元测试
 * @author zhuss
 * @2016年1月7日 下午4:51:32
 */
public class LogisticTest extends BaseTest{

	
	// 订单物流列表
	@Test
	public void list() throws Exception {
		QueryLogistic logistic = new QueryLogistic();
		logistic.setCode("1116011714904775");
		logistic.setToken("836fcd2e2309f4fcae98eb5afb3a123e");
		String json = JsonUtil.convertObjToStr(logistic);
		mockMvc.perform(
				(post("/logistic/list.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 物流公司列表
	@Test
	public void getlogcompany() throws Exception {
		QueryLogistic logistic = new QueryLogistic();
		String json = JsonUtil.convertObjToStr(logistic);
		mockMvc.perform((post("/logistic/companylist.htm").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
}
