package com.tp.test.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.tp.m.query.order.QueryOrder;
import com.tp.m.util.JsonUtil;
import com.tp.test.base.BaseTest;

/**
 * 订单相关单元测试
 * @author zhuss
 * @2016年1月7日 下午4:51:32
 */
public class OrderTest extends BaseTest{

	// 提交订单页面的信息
	@Test
	public void getSubmitInfo() throws Exception {
		QueryOrder order = new QueryOrder();
		//order.setUuid("0fe3db76-b5ba-4fc0-b9cc-3bcd963eb8a8");
		//order.setCid("2563685");
		order.setToken("f0ca62c07acfe06b8066f6b64974cd80");
		order.setApptype("ios");
		List<String> cidlist = new ArrayList<String>();
		cidlist.add("11111");
		order.setCidlist(cidlist);
		String json = JsonUtil.convertObjToStr(order);
		System.out.println(json);
		mockMvc.perform(
				(post("/order/submitinfo.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 提交订单
	@Test
	public void submit() throws Exception {
		QueryOrder order = new QueryOrder();
		order.setAid("945");
		order.setApptype("wap");
		order.setToken("836fcd2e2309f4fcae98eb5afb3a123e");
		String json = JsonUtil.convertObjToStr(order);
		mockMvc.perform(
				(post("/order/submit.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 订单列表
	@Test
	public void list() throws Exception {
		QueryOrder order = new QueryOrder();
		order.setType("1");
		order.setCurpage("1");
		order.setToken("18780b76b43309ba184f82c6db38950e");
		String json = JsonUtil.convertObjToStr(order);
		mockMvc.perform(
				(post("/order/list.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 订单详情
	@Test
	public void detail() throws Exception {
		QueryOrder order = new QueryOrder();
		order.setOrdercode("1116012648567922");
		order.setToken("18780b76b43309ba184f82c6db38950e");
		String json = JsonUtil.convertObjToStr(order);
		mockMvc.perform(
				(post("/order/detail.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 取消删除
	@Test
	public void calordel() throws Exception {
		QueryOrder order = new QueryOrder();
		order.setType("1");
		order.setOrdercode("1116011717470158");
		order.setToken("d5cf60e2df9d0d2db7aee4a734eb81ac");
		String json = JsonUtil.convertObjToStr(order);
		mockMvc.perform(
				(post("/order/calordel.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 确认订单
	@Test
	public void confirm() throws Exception {
		QueryOrder order = new QueryOrder();
		order.setOrdercode("1");
		order.setToken("d5cf60e2df9d0d2db7aee4a734eb81ac");
		String json = JsonUtil.convertObjToStr(order);
		mockMvc.perform(
				(post("/order/confirm.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 立即购买
	@Test
	public void buyNow() throws Exception {
		QueryOrder order = new QueryOrder();
		order.setApptype("wap");
		order.setTid("165233");
		order.setSku("26030100150101");
		order.setCount("2");
		order.setRegcode("250");
		order.setToken("836fcd2e2309f4fcae98eb5afb3a123e");
		String json = JsonUtil.convertObjToStr(order);
		mockMvc.perform(
				(post("/order/buynow.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
}
