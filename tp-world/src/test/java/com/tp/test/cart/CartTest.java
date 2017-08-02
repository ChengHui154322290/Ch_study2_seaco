package com.tp.test.cart;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.Test;
import org.springframework.http.MediaType;

import com.tp.m.query.cart.QueryCart;
import com.tp.m.util.JsonUtil;
import com.tp.test.base.BaseTest;

public class CartTest extends BaseTest{

	// 购物车 - 添加商品
	@Test
	public void add() throws Exception {
		QueryCart cart = new QueryCart();
		cart.setApptype("wap");
		cart.setRegcode("65");
		cart.setSku("07010100010101");
		cart.setTid("165351");
		cart.setCount("1");
		cart.setToken("836fcd2e2309f4fcae98eb5afb3a123e");
		String json = JsonUtil.convertObjToStr(cart);
		mockMvc.perform(
				(post("/cart/add.htm").accept(
						MediaType.APPLICATION_JSON).contentType(
						MediaType.APPLICATION_JSON).content(json.getBytes())))
				.andExpect(status().isOk()).andDo(print());
	}
	
	// 购物车 - 角标数量
	@Test
	public void supCount() throws Exception {
		QueryCart cart = new QueryCart();
		cart.setToken("836fcd2e2309f4fcae98eb5afb3a123e");
		String json = JsonUtil.convertObjToStr(cart);
		mockMvc.perform(
				(post("/cart/supcount.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
		
	// 购物车 - 加载
	@Test
	public void load() throws Exception {
		QueryCart cart = new QueryCart();
		cart.setApptype("ios");
		cart.setAppversion("1.2.0");
		cart.setRegcode("250");
		cart.setToken("39bc4aa8ccef0054583f4031f1049f89");
		String json = JsonUtil.convertObjToStr(cart);
		mockMvc.perform(
				(post("/cart/load.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
		
	// 购物车 - 操作
	@Test
	public void operation() throws Exception {
		QueryCart cart = new QueryCart();
		cart.setApptype("wap");
		cart.setRegcode("65");
		cart.setType("7");
		cart.setToken("836fcd2e2309f4fcae98eb5afb3a123e");
		/*List<ParamProductTO> products = new ArrayList<>();
		products.add(new ParamProductTO("165222","01010100070101","1"));
		cart.setProducts(products);*/
		String json = JsonUtil.convertObjToStr(cart);
		mockMvc.perform(
				(post("/cart/operation.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
}
