package com.tp.test.dss;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.Serializable;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.DssConstant;
import com.tp.m.query.dss.QueryPromoterTopic;
import com.tp.m.query.home.QueryIndex;
import com.tp.m.query.order.QueryAfterSales;
import com.tp.m.query.promoter.QueryPromoter;
import com.tp.m.util.JsonUtil;
import com.tp.model.BaseDO;
import com.tp.model.dss.PromoterInfo;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.test.base.BaseTest;

/**
 * 售后相关单元测试
 * @author zhs
 * @2016年4月18日 
 */
public class DSSTest extends BaseTest{


//	 public static void main(String args[]) { 
//		 System.out.println("@@@####sssss\n");
//	}

	@Autowired
	PromoterInfoProxy promoterProxy;

	@Test
	public void withdraw() throws Exception {
		//token : "3fa5077f40f74cc66b97a02a159e2506"
		QueryPromoter promoter = new QueryPromoter();
		promoter.setToken("3fa5077f40f74cc66b97a02a159e2506");
		promoter.setType("1");
		promoter.setPromoterName("张兴");
		promoter.setCredentialType(DssConstant.CARD_TYPE.IDENTITY_CARD.code);
		promoter.setCredentialCode("330303198501160618");
		promoter.setBankName("招商银行");
		promoter.setBankAccount("6226090250072276");
		promoter.setCurWithdraw(60000.00);				
		String json = JsonUtil.convertObjToStr(promoter);
		mockMvc.perform(
				(post("/promoter/withdraw.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}

	
	@Test
	public void withdrawdetail() throws Exception {
		QueryPromoter promoter = new QueryPromoter();
		promoter.setToken("3fa5077f40f74cc66b97a02a159e2506");
		promoter.setType("1");
		String json = JsonUtil.convertObjToStr(promoter);
		mockMvc.perform(
				(post("/promoter/withdrawdetail.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}


	@Test
	public void regiesterPromoter() throws Exception {
		PromoterInfo promoter = new PromoterInfo();
		promoter.setMobile("15800381241");
		promoter.setCaptcha("132487");
		promoter.setGender(1);
		promoter.setNickName("1985");
		promoter.setPassWord("12345qwert");
		promoter.setUserAgreed(true);
		
		String json = JsonUtil.convertObjToStr(promoter);
		mockMvc.perform(
				(post("/dss/regist.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
		
	}
	
	//updatepromoter
	@Test
	public void updatepromoter() throws Exception {
		QueryPromoter promoter = new QueryPromoter();
		promoter.setToken("3fa5077f40f74cc66b97a02a159e2506");
		promoter.setType("1");
//		promoter.setPromoterName("张兴");
//		promoter.setCredentialType(DssConstant.CARD_TYPE.IDENTITY_CARD.code);
//		promoter.setCredentialCode("330303198501160618");
//		promoter.setBankName("招商银行");
		promoter.setBankAccount("6226090250078888");
		promoter.setAlipay("zhanghongsheng@163.com");
		promoter.setQq("123456");
		promoter.setEmail("zhanghongsheng@163.com");
				
		String json = JsonUtil.convertObjToStr(promoter);
		mockMvc.perform(
				(post("/promoter/updatepromoter.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());		
	}
	
	
	
	@Test
	public void getinfo() throws Exception {
		QueryPromoter promoter = new QueryPromoter();
		promoter.setToken("3fa5077f40f74cc66b97a02a159e2506");
				
		String json = JsonUtil.convertObjToStr(promoter);
		mockMvc.perform(
				(post("/dss/getinfo.htm").accept(MediaType.APPLICATION_JSON)
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
	
	
	@Test
	public void testgettopics() throws Exception {
		QueryPromoterTopic query = new QueryPromoterTopic();
		query.setOem("AM");
		query.setOsversion("1.1.0");
		query.setScreenwidth("500");
		query.setScreenheight("867");
		query.setApptype("wap");
		query.setAppversion("1.0.1");
		query.setNettype("unknown");
		query.setRegcode("250");
		query.setProvcode("264");
		query.setPartner("xigou");		
		query.setToken("fa25e8e2b3151de41be192137f4bc946");
		query.setType("1");
//		query.setShop("15800381241");
		String json = JsonUtil.convertObjToStr(query);
		mockMvc.perform((post("/promoter/getopics.htm").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	@Test
	public void testgettopicitems() throws Exception {
		QueryPromoterTopic query = new QueryPromoterTopic();
		query.setOem("AM");
		query.setOsversion("1.1.0");
		query.setScreenwidth("500");
		query.setScreenheight("867");
		query.setApptype("wap");
		query.setAppversion("1.0.1");
		query.setNettype("unknown");
		query.setRegcode("250");
		query.setProvcode("264");
		query.setPartner("xigou");		
		query.setToken("fa25e8e2b3151de41be192137f4bc946");
		query.setType("1");
		query.setTopicid(117L);
		query.setCurpage("1");
		String json = JsonUtil.convertObjToStr(query);
		mockMvc.perform((post("/promoter/getopicitems.htm").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void testsetshelves() throws Exception {
		QueryPromoterTopic query = new QueryPromoterTopic();
		query.setOem("AM");
		query.setOsversion("1.1.0");
		query.setScreenwidth("500");
		query.setScreenheight("867");
		query.setApptype("wap");
		query.setAppversion("1.0.1");
		query.setNettype("unknown");
		query.setRegcode("250");
		query.setProvcode("264");
		query.setPartner("xigou");		
		query.setToken("fa25e8e2b3151de41be192137f4bc946");
		query.setType("1");
		query.setTopicid(117L);
		query.setSku("06030100100101");
		query.setOnshelf(0);
		query.setShelftype(1);
		
		String json = JsonUtil.convertObjToStr(query);
		mockMvc.perform((post("/promoter/setshelves.htm").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}

	
	// http://localhost/index/gettodaynew.htm?appversion=1.1.1&curpage=2&oem=AM&osversion=1.1.0&screenwidth=441&screenheight=786&apptype=wap&appversion=1.0.1&nettype=unknown&regcode=250&provcode=264&partner=xigou

	@Test
	public void testgettodaynew() throws Exception {

		mockMvc.perform(
				(MockMvcRequestBuilders.get("/index/gettodaynew.htm")
						.param("appversion", "1.1.1")
						.param("curpage", "1")
						.param("oem", "AM")
						.param("osversion", "1.1.0")
						.param("screenwidth", "441")
						.param("screenheight", "786")
						.param("apptype", "wap")
						.param("nettype", "unknown")
						.param("regcode", "250")
						.param("provcode", "264")
						.param("partner", "xigou")
						.param("token", "fa25e8e2b3151de41be192137f4bc946")
						.accept(MediaType.APPLICATION_JSON)))
				.andExpect(status().isOk()).andDo(print());	
	}
	
	

	@Test
	public void testproducts() throws Exception {
		
		// {"sort":"0","tid":"115"}		
		mockMvc.perform(
				(MockMvcRequestBuilders.get("/topic/products.htm")
						.param("appversion", "1.1.1")
						.param("curpage", "2")
						.param("oem", "AM")
						.param("osversion", "1.1.0")
						.param("screenwidth", "441")
						.param("screenheight", "786")
						.param("apptype", "wap")
						.param("nettype", "unknown")
						.param("regcode", "250")
						.param("provcode", "264")						
						.param("partner", "xigou")
						.param("sort", "0")
						.param("tid", "112")						
						.param("token", "fa25e8e2b3151de41be192137f4bc946")
						.accept(MediaType.APPLICATION_JSON)))
				.andExpect(status().isOk()).andDo(print());	
	}

	

	@Test
	public void test123() throws Exception {
		boolean bExist = promoterProxy.existPromoter(null);
		System.out.println("11111" + bExist);
	}
	
	
	@Test
	public void testShare() throws Exception {
		QueryPromoter query = new QueryPromoter();
		query.setUserMobile("13588877105");
		String json = JsonUtil.convertObjToStr(query);
		mockMvc.perform((post("/promoter/shareShop.htm").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
   
	@Test
	public void testCheckInviteCode() throws Exception {
		QueryPromoter query = new QueryPromoter();
		query.setInviteCode("bkqbzN");;
		String json = JsonUtil.convertObjToStr(query);
		mockMvc.perform((post("/promoter/getInviteCode.htm").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	@Test
	public void testSaveInviteUserInfo() throws Exception {
		QueryPromoter query = new QueryPromoter();
		query.setInviteCode("bkqbzN");
		query.setUserMobile("13588877105");
		query.setCredentialCode("330324198210216257");
		query.setPromoterName("周国丰");
		query.setBankName("工商银行");
		query.setBankAccount("622222229645646574545");
		query.setAlipay("13588877105");
		query.setPassword("123456");
		String json = JsonUtil.convertObjToStr(query);
		mockMvc.perform((post("/promoter/saveInviteUserInfo.htm").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
}



