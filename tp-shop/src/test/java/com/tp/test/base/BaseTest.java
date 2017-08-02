package com.tp.test.base;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
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
}
