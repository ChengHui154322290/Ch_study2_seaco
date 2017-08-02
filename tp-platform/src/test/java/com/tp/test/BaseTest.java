package com.tp.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.io.ByteArrayInputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.web.context.WebApplicationContext;
/**
 * 
 * @Description 单元测试基础类
 * @author zhuss
 * @date 2014年1月5日下午6:20:38
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "/spring/spring-web.xml","/spring/spring-beans.xml" })
public class BaseTest {

	@Autowired  
    private WebApplicationContext wac; 
	
    public MockMvc mockMvc;  
  
    @Before  
    public void setup() {  
        //import 导入static的类
    	this.mockMvc = webAppContextSetup(this.wac).build();
    }
    
    @Test
    public void test() throws Exception{
//    	System.out.println(mockMvc.perform(
//				(MockMvcRequestBuilders.get("/order/orderInPage.htm")
//						.param("appkey", "111111111")
//						.content("{\"supplierId\":121}")))
//				.andExpect(status().isOk()).andDo(print()));
    	HttpClient client = new HttpClient();
    	PostMethod method = new PostMethod("http://localhost/ROOT/order/orderInPage.htm?appkey=lei&version=1.0&timestamp=20160615101919&sign=79ea670ca74cc91eb268827fa6f98ee9");
    	method.setRequestHeader("Content-Type", "text/html");
    	method.setRequestBody(new ByteArrayInputStream("{\"supplierId\":161}".getBytes("utf-8")));
    	
    	int executeMethod = client.executeMethod(method);
    	System.out.println(new String(method.getResponseBody(),"utf-8"));
    	
    }
    
}
