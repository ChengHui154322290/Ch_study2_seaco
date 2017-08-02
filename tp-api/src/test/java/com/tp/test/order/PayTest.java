package com.tp.test.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.tp.m.query.pay.QueryPay;
import com.tp.m.util.JsonUtil;
import com.tp.test.base.BaseTest;

/**
 * 支付相关单元测试
 * @author zhuss
 * @2016年1月7日 下午4:51:32
 */
public class PayTest extends BaseTest{

	
	// 获取支付方式列表
	@Test
	public void paywayList() throws Exception {
		mockMvc.perform(
				(MockMvcRequestBuilders.get("/pay/paywaylist.htm")
						.param("ordertype", "4")
						.param("channelid", "8")
						.param("apptype", "ios")
						.param("appversion", "1.1.0")
						.accept(MediaType.APPLICATION_JSON)))
				.andExpect(status().isOk()).andDo(print());
	}
		
		
	// 立即支付
	@Test
	public void payment() throws Exception {
		QueryPay pay = new QueryPay();
		pay.setOrdercode("1100042150000065");
		pay.setApptype("app");
		pay.setPayway("mergeAlipay");
		//pay.setOpenid("oNU-xs25i026V9ZKhU4r1aEWQ95Q");
		pay.setToken("39bc4aa8ccef0054583f4031f1049f89");
		String json = JsonUtil.convertObjToStr(pay);
		mockMvc.perform(
				(post("/pay/payorder.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 查看支付结果
	@Test
	public void paymentResult() throws Exception {
		QueryPay pay = new QueryPay();
		//pay.setOrdercode("1015040212113944");
		pay.setPayid("1114");
		pay.setToken("d5cf60e2df9d0d2db7aee4a734eb81ac");
		String json = JsonUtil.convertObjToStr(pay);
		mockMvc.perform(
				(post("/pay/payresult.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 查看支付结果
	@Test
	public void callback() throws Exception{
		mockMvc.perform(
				(MockMvcRequestBuilders.get("/paycb/weixin.htm")
						.param("MTMM_REQUEST_CONTENT", "<xml><appid><![CDATA[wx6f7f5f0bab32e7d3]]></appid><attach><![CDATA[xggj]]></attach><bank_type><![CDATA[CFT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><device_info><![CDATA[000001]]></device_info><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[1305134101]]></mch_id><nonce_str><![CDATA[0.4942111169516926]]></nonce_str><openid><![CDATA[oNU-xs1EPqo3G1LeG2teycJd4VJU]]></openid><out_trade_no><![CDATA[1116011706568594]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[A1FE8F7A96BE8333A8A781320D3855B8]]></sign><time_end><![CDATA[20160117231302]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1002411000201601172757000360]]></transaction_id></xml>null")
						.accept(MediaType.APPLICATION_JSON)))
				.andExpect(status().isOk()).andDo(print());
	}
}
