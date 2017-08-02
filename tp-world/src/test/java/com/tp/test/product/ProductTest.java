package com.tp.test.product;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.tp.proxy.prd.ItemSkuProxy;
import com.tp.service.prd.IItemSkuService;
import com.tp.test.base.BaseTest;
/**
 * 商品
 * @author zhuss
 * @2016年1月4日 下午7:19:52
 */
public class ProductTest extends BaseTest{
	
	@Autowired
	private IItemSkuService itemSkuService;

	// 商品详情
	@Test
	public void getProductDetail() throws Exception {
		mockMvc.perform(
				(MockMvcRequestBuilders.get("/product/detail.htm")
						.param("tid", "568")
						.param("sku", "01120200010101")                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
						.accept(MediaType.APPLICATION_JSON)))
				.andExpect(status().isOk()).andDo(print());
	}
	
	@Test
	public void queryItemListBySkus(){
		List<String> skus = new ArrayList<>();
		skus.add("01010100010101");
		skus.add("01010100020101");
		itemSkuService.queryItemListBySkus(skus);
	}
}
