package com.tp.test.promotion;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.tp.m.query.promotion.QueryCoupon;
import com.tp.m.util.JsonUtil;
import com.tp.model.mmp.Coupon;
import com.tp.test.base.BaseTest;
/**
 * 优惠券相关单元测试
 * @author zhuss
 * @2016年1月4日 下午7:19:52
 */
public class CouponTest extends BaseTest{

	// 兑换优惠券
	@Test
	public void exchange() throws Exception {
		QueryCoupon coupon = new QueryCoupon();
		coupon.setCcode("2e156a730a455eba");
		coupon.setToken("836fcd2e2309f4fcae98eb5afb3a123e");
		String json = JsonUtil.convertObjToStr(coupon);
		mockMvc.perform(
				(post("/coupon/exchange.htm")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 领取优惠券
	@Test
	public void receive() throws Exception {
		QueryCoupon coupon = new QueryCoupon();
		coupon.setCcode("944D419049563876");
		coupon.setCaptcha("468700");
		coupon.setTel("15800917996");
		//coupon.setToken("836fcd2e2309f4fcae98eb5afb3a123e");
		String json = JsonUtil.convertObjToStr(coupon);
		mockMvc.perform(
				(post("/coupon/receive.htm")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 领取多张优惠券
	@Test
	public void receivemany() throws Exception {
		QueryCoupon coupon = new QueryCoupon();
		List<String> l = new ArrayList<>();
		l.add("DF74CBA47CA9E07D");
		//l.add("224DAE8E947AA8AC");
		coupon.setCcodes(l);
		coupon.setCaptcha("468700");
		coupon.setTel("13588877105");
		// coupon.setToken("836fcd2e2309f4fcae98eb5afb3a123e");
		String json = JsonUtil.convertObjToStr(coupon);
		mockMvc.perform(
				(post("/coupon/receivemany.htm").accept(
						MediaType.APPLICATION_JSON).contentType(
						MediaType.APPLICATION_JSON).content(json.getBytes())))
				.andExpect(status().isOk()).andDo(print());
	}
	
	// 优惠券列表
	@Test
	public void couponList() throws Exception {
		QueryCoupon coupon = new QueryCoupon();
		coupon.setType("3");
		//coupon.setStatus("0");
		coupon.setApptype("wap");
		coupon.setUuid("7123b57c-935f-4c92-9fca-5d9397e2cd08");
		coupon.setToken("836fcd2e2309f4fcae98eb5afb3a123e");
		String json = JsonUtil.convertObjToStr(coupon);
		mockMvc.perform(
				(post("/coupon/list.htm")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 首次操作获取优惠券
	@Test
	public void receiveOnly() throws Exception {
		QueryCoupon coupon = new QueryCoupon();
		coupon.setSendtype("2");;
		coupon.setToken("f0ca62c07acfe06b8066f6b64974cd80");
		String json = JsonUtil.convertObjToStr(coupon);
		mockMvc.perform((post("/coupon/receiveonly.htm").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	// 首次操作获取优惠券
		@Test
		public void receiveCenterlist() throws Exception {
			Coupon coupon = new Coupon();
			coupon.setMobile("13588877105");
			String json = JsonUtil.convertObjToStr(coupon);
			mockMvc.perform((post("/coupon/receiveCenterlist.htm").accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON).content(json.getBytes()))).andExpect(status().isOk())
					.andDo(print());
		}
	
		// 领取优惠券
		@Test
		public void receiveCoupon() throws Exception {
			QueryCoupon coupon = new QueryCoupon();
			coupon.setCcode("F206F54849C4AE68");;
			coupon.setTel("13588877105");
			//coupon.setToken("836fcd2e2309f4fcae98eb5afb3a123e");
			String json = JsonUtil.convertObjToStr(coupon);
			mockMvc.perform(
					(post("/coupon/receiveCoupon.htm")
							.accept(MediaType.APPLICATION_JSON)
							.contentType(MediaType.APPLICATION_JSON)
							.content(json.getBytes()))).andExpect(status().isOk())
					.andDo(print());
		}
}
