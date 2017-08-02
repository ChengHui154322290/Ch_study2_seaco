package com.tp.test.wx;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tp.common.vo.wx.MessageConstant;
import com.tp.service.wx.IMenuInfoService;
import com.tp.service.wx.IMessageInfoService;
import com.tp.test.BaseTest;

public class MenuManagerTest extends BaseTest{
	
	@Autowired
	private IMenuInfoService menuInfoService;
	
	@Autowired
	private IMessageInfoService messageInfoService;

	@Test
	public void getMenu(){
		menuInfoService.getMenu();
		System.out.println("=====");
	}
	
	@Test
	public void getMessage(){
		System.out.println(messageInfoService.getMessage(MessageConstant.SCENE.SUBSCRIBE.getCode(),null));
	}
}
