package com.tp.service.ord;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tp.common.vo.ord.OrderCodeType;
import com.tp.service.ord.IOrderCodeGeneratorService;
import com.tp.test.BaseTest;

public class OrderCodeGeneratorServiceTest extends BaseTest{

	@Autowired
	private IOrderCodeGeneratorService orderCodeGeneratorService;
	@Test
	public void testGenerate() {
		for(int i=0;i<100;i++){
			if(System.nanoTime()%2==0){
				System.out.println(orderCodeGeneratorService.generate(OrderCodeType.PARENT));
			}else{
				System.out.println(orderCodeGeneratorService.generate(OrderCodeType.SON));
			}
		}
	}

}
