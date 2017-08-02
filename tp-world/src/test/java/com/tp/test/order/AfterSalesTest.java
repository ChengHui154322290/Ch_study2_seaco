package com.tp.test.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.tp.m.query.order.QueryAfterSales;
import com.tp.m.util.JsonUtil;
import com.tp.test.base.BaseTest;

/**
 * 售后相关单元测试
 * @author zhuss
 * @2016年1月7日 下午4:51:32
 */
public class AfterSalesTest extends BaseTest{

	// 申请售后
	@Test
	public void apply() throws Exception {
		QueryAfterSales afterSales = new QueryAfterSales();
		afterSales.setToken("18780b76b43309ba184f82c6db38950e");
		afterSales.setApptype("ios");
		afterSales.setLineid("12714");
		afterSales.setLinkname("王八蛋");
		afterSales.setLinktel("13377777777");
		afterSales.setOrdercode("1116012579483391");
		afterSales.setReturncount("1");
		afterSales.setReturnreason("2");
		afterSales.setReturninfo("太差了.");
		afterSales.setImageone("http://m.51seaco.com/afterSales/apply_one.jpg");
		afterSales.setImagefive("http://m.51seaco.com/afterSales/apply_five.jpg");
		afterSales.setImagefour("http://m.51seaco.com/afterSales/apply_four.jpg");
		String json = JsonUtil.convertObjToStr(afterSales);
		mockMvc.perform(
				(post("/aftersales/apply.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 申请列表及详情
	@Test
	public void list2Detail() throws Exception {
		QueryAfterSales afterSales = new QueryAfterSales();
		afterSales.setToken("18780b76b43309ba184f82c6db38950e");
		String json = JsonUtil.convertObjToStr(afterSales);
		mockMvc.perform((post("/aftersales/list2detail.htm").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 提交物流单号
	@Test
	public void submitLogistic() throws Exception {
		QueryAfterSales afterSales = new QueryAfterSales();
		afterSales.setToken("18780b76b43309ba184f82c6db38950e");
		afterSales.setAsid("562");
		afterSales.setLogisticcode("123456789");
		afterSales.setCompany("顺丰");
		afterSales.setCompanycode("sf");
		String json = JsonUtil.convertObjToStr(afterSales);
		mockMvc.perform((post("/aftersales/submitlogistic.htm").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 修改售后
	@Test
	public void update() throws Exception {
		QueryAfterSales afterSales = new QueryAfterSales();
		afterSales.setToken("18780b76b43309ba184f82c6db38950e");
		afterSales.setAsid("558");
		//afterSales.setReturninfo("测试修改");
		//afterSales.setReturnreason("3");
		afterSales.setLinkname("李东日");
		afterSales.setLinktel("13665655656");
		afterSales.setReturncount("2");
		String json = JsonUtil.convertObjToStr(afterSales);
		mockMvc.perform((post("/aftersales/update.htm").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	//取消售后
	@Test
	public void cancel() throws Exception {
		QueryAfterSales afterSales = new QueryAfterSales();
		afterSales.setToken("9aa7aeba69f61ca1221256e603713571");
		afterSales.setAsid("568");
		String json = JsonUtil.convertObjToStr(afterSales);
		mockMvc.perform((post("/aftersales/cancel.htm").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
}
