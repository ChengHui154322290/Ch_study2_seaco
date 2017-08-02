package com.tp.test.wx;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.tp.m.util.JsonUtil;
import com.tp.test.base.BaseTest;

/**
 * 微信 - 认证单元测试
 * @author zhuss
 * @2016年1月16日 下午4:46:11
 */
public class OauthTest extends BaseTest{

	// 获取CODE
	@Test
	public void getCode() throws Exception {
		mockMvc.perform(
				(MockMvcRequestBuilders.get("/wx/oauth/getcode.htm").accept(MediaType.APPLICATION_JSON)))
				.andExpect(status().isOk()).andDo(print());
	}
	
	// 通过config接口注入权限验证配置
	@Test
	public void config() throws Exception {
		mockMvc.perform((MockMvcRequestBuilders.get("/wx/oauth/config.htm")
				.param("url", "http://m.51seaco.com/item.htm?tid=165233&sku=02060500170101")
				.accept(MediaType.APPLICATION_JSON)))
				.andExpect(status().isOk()).andDo(print());
	}
	@Test
	public void corePost() throws Exception {
         Map<String,String>  param=new HashMap<String,String>();
        String paramxml="<xml>" ;
        paramxml=paramxml+"<ToUserName><![CDATA[123213123]]></ToUserName>" ;
        paramxml=paramxml+"<FromUserName><![CDATA[1234567893122]]></FromUserName>" ;
        paramxml=paramxml+"<CreateTime>"+System.currentTimeMillis()+"</CreateTime>" ;
        paramxml=paramxml+"<MsgType><![CDATA[event]]></MsgType>" ;
        paramxml=paramxml+" <Event><![CDATA[scan]]></Event>" ;
        paramxml=paramxml+" <EventKey><![CDATA[qpu_13588877105_02]]></EventKey>" ;
        paramxml=paramxml+" </xml>" ;
		mockMvc.perform(
				(post("/core").accept(
						MediaType.APPLICATION_ATOM_XML_VALUE).contentType(
						MediaType.APPLICATION_ATOM_XML).content(paramxml.getBytes())))
				.andExpect(status().isOk()).andDo(print());
	}
}
