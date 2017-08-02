package com.tp.test.wx;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tp.service.wx.IUserManagerService;
import com.tp.test.BaseTest;

public class UserManagerTest extends BaseTest{
	
	@Autowired
	private IUserManagerService userManagerService;

	@Test
	public void queryUserList(){
		List<String> list = userManagerService.queryUserList();
		System.out.println(list.size());
	}
	
	public static void main(String[] args) {
		List<String> a = new ArrayList<>();
		a.add("111");
		a.add("222");
		a.add("333");
		a.add("444");
		List<String> b = new ArrayList<>();
		b.add("999");
		b.add("222");
		b.add("333");
		b.add("888");
		a.retainAll(b);
		for(String str : a){
			System.out.println(str);
		}
	}
}
