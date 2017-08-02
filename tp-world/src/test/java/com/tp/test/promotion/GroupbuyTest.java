package com.tp.test.promotion;

import com.tp.m.query.promotion.QueryCoupon;
import com.tp.m.query.promotion.QueryGroupbuy;
import com.tp.m.util.JsonUtil;
import com.tp.test.base.BaseTest;

import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 详情
 * @author zhuss
 * @2016年1月4日 下午7:19:52
 */
public class GroupbuyTest extends BaseTest{

	// 详情
	@Test
	public void detail() throws Exception {
		QueryGroupbuy query = new QueryGroupbuy();
		query.setGbid("63");
		query.setGid("4");
		query.setToken("b987c1223f4dacac27deb4408d4569c5");
		String json = JsonUtil.convertObjToStr(query);
		mockMvc.perform(
				(post("/groupbuy/detail.htm")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}

	// 发起
	@Test
	public void launch() throws Exception {
		QueryGroupbuy query = new QueryGroupbuy();
		query.setGbid("65");
		//query.setToken("836fcd2e2309f4fcae98eb5afb3a123e");
		query.setToken("b987c1223f4dacac27deb4408d4569c5");
		//query.setToken("4a9488ec63f533bcf65722fac1f4b54f");
		String json = JsonUtil.convertObjToStr(query);
		mockMvc.perform(
				(post("/groupbuy/launch.htm")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json.getBytes())))
				.andDo(print());
	}
	// 参团
	@Test
	public void join() throws Exception {
		QueryGroupbuy query = new QueryGroupbuy();
		query.setGbid("65");
		query.setGid("57");
		//query.setToken("836fcd2e2309f4fcae98eb5afb3a123e");
		//query.setToken("4a9488ec63f533bcf65722fac1f4b54f");
		//query.setToken("b987c1223f4dacac27deb4408d4569c5");
		query.setToken("44bf9f15de989b86cd513cd65e205e86");
		String json = JsonUtil.convertObjToStr(query);
		mockMvc.perform(
				(post("/groupbuy/join.htm")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void my() throws Exception {
		QueryGroupbuy query = new QueryGroupbuy();
		query.setGbid("49");
		query.setToken("b987c1223f4dacac27deb4408d4569c5");
		String json = JsonUtil.convertObjToStr(query);
		mockMvc.perform(
				(post("/groupbuy/my.htm")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}

}
