package com.tp.test.user;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.tp.m.query.user.QueryAddress;
import com.tp.m.query.user.QueryUser;
import com.tp.m.util.JsonUtil;
import com.tp.test.base.BaseTest;

/**
 * 用户收货地址相关单元测试
 * @author zhuss
 * @2016年1月3日 下午3:09:32
 */
public class AddressTest extends BaseTest{
	
	// 收货地址列表
	@Test
	public void getAddressList() throws Exception {
		QueryUser user = new QueryUser();
		user.setToken("836fcd2e2309f4fcae98eb5afb3a123e");
		String json = JsonUtil.convertObjToStr(user);
		mockMvc.perform(
				(post("/address/list.htm").accept(
						MediaType.APPLICATION_JSON).contentType(
						MediaType.APPLICATION_JSON).content(json.getBytes())))
				.andExpect(status().isOk()).andDo(print());
	}
	
	// 用户默认收货地址
	@Test
	public void getDefaultAddress() throws Exception {
		QueryUser user = new QueryUser();
		user.setToken("18780b76b43309ba184f82c6db38950e");
		String json = JsonUtil.convertObjToStr(user);
		mockMvc.perform(
				(post("/address/default.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 新增收货地址
	@Test
	public void addAddress() throws Exception {
		QueryAddress address = new QueryAddress();
		address.setToken("836fcd2e2309f4fcae98eb5afb3a123e");
		address.setName("山哥1111");
		address.setTel("15021308552");
		address.setProvid("11302");
		address.setProvname("上海");
		address.setCityid("11303");
		address.setCityname("市辖区");
		address.setDistrictid("11430");
		address.setDistrictname("浦东新区");
		address.setStreetid("11452");
		address.setStreetname("张江镇");
		address.setIsdefault("1");
		address.setInfo("中科路2500弄23号602室");
		address.setIdentityCard("340223198610183554");
		String json = JsonUtil.convertObjToStr(address);
		mockMvc.perform(
				(post("/address/add.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 编辑收货地址
	@Test
	public void editAddress() throws Exception {
		QueryAddress address = new QueryAddress();
		address.setToken("836fcd2e2309f4fcae98eb5afb3a123e");
		address.setAid("77");
		address.setName("山哥");
		/*address.setTel("15800917991");
		address.setProvid("11302");
		address.setProvname("上海");
		address.setCityid("11303");
		address.setCityname("市辖区");
		address.setDistrictid("11376");
		address.setDistrictname("杨浦区");
		address.setStreetid("11387");
		address.setStreetname("新江湾城街道");
		address.setInfo("淞沪路433号6号楼7层");
		address.setIsdefault("1");*/
		address.setIdentityCard("340223198610183554");
		String json = JsonUtil.convertObjToStr(address);
		mockMvc.perform(
				(post("/address/edit.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
	
	// 删除收货地址
	@Test
	public void delAddress() throws Exception {
		QueryAddress address = new QueryAddress();
		address.setToken("d5cf60e2df9d0d2db7aee4a734eb81ac");
		address.setAid("942");
		String json = JsonUtil.convertObjToStr(address);
		mockMvc.perform(
				(post("/address/del.htm").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(json
						.getBytes()))).andExpect(status().isOk())
				.andDo(print());
	}
}
